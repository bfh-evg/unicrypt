package ch.bfh.unicrypt.crypto.encryption.old;

import java.util.ArrayList;
import java.util.Random;

import ch.bfh.unicrypt.crypto.encryption.abstracts.AbstractEncryptionScheme;
import ch.bfh.unicrypt.crypto.keygen.old.DDHGroupKeyPairGeneratorClass;
import ch.bfh.unicrypt.crypto.nizkp.classes.SigmaOrProofGeneratorClass;
import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlusMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.DDHGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class ElGamalEncryptionSchemeOld extends AbstractEncryptionScheme {

    private final ZPlusMod zModPlus;
    private final DDHGroup ddhGroup;
    private final Element generator; // of ddhGroup
    private final DDHGroupDistributedKeyPairGenerator keyGenerator;
    private final Function encryptionFunction;
    private final Function decryptionFunction;
    private final Function identityEncryptionFunction;

    public ElGamalEncryptionSchemeOld(final DDHGroup ddhGroup) {
        this(ddhGroup, ddhGroup.getDefaultGenerator());
    }

    public ElGamalEncryptionSchemeOld(final DDHGroup ddhGroup, final Element generator) {
        if ((ddhGroup == null) || (generator == null) || !ddhGroup.contains(generator)) {
            throw new IllegalArgumentException();
        }
        this.zModPlus = ddhGroup.getZModOrder();
        this.ddhGroup = ddhGroup;
        this.generator = generator;

        this.keyGenerator = new DDHGroupKeyPairGeneratorClass(ddhGroup, generator);
        this.encryptionFunction = this.createEncryptionFunction();
        this.decryptionFunction = this.createDecryptionFunction();
        this.identityEncryptionFunction = this.createIdentityEncryptionFunction();
    }

    @Override
    public Element encrypt(final Element publicKey, final Element plaintext) {
        return this.encrypt(publicKey, plaintext, (Random) null);
    }

    @Override
    public Element encrypt(final Element publicKey, final Element plaintext, final Random random) {
        final Element randomization = this.getRandomizationSpace().getRandomElement(random);
        return this.encrypt(publicKey, plaintext, randomization);
    }

    @Override
    public Element encrypt(final Element publicKey, final Element plaintext, final Element randomization) {
        return (Element) this.getEncryptionFunction().apply(publicKey, plaintext, randomization);
    }

    @Override
    public Element reEncrypt(final Element publicKey, final Element ciphertext) {
        return this.reEncrypt(publicKey, ciphertext, (Random) null);
    }

    @Override
    public Element reEncrypt(final Element publicKey, final Element ciphertext, final Random random) {
        final Element randomization = this.getRandomizationSpace().getRandomElement(random);
        return this.reEncrypt(publicKey, ciphertext, randomization);
    }

    @Override
    public Element reEncrypt(final Element publicKey, final Element ciphertext, final Element randomization) {
        if (!getCiphertextSpace().contains(ciphertext)) {
            throw new IllegalArgumentException();
        }
        return (Element) ciphertext.apply(this.getIdentityEncryptionFunction().apply(publicKey, randomization));
    }

    @Override
    public Element decrypt(final Element privateKey, final Element ciphertext) {
        return (Element) this.getDecryptionFunction().apply(privateKey, ciphertext);
    }

    // @Override
    @Override
    public Element partialDecrypt(final Element privateKey, final Element ciphertext) {
        return (Element) this.getPartialDecryptionFunction().apply(privateKey, ((Element) ciphertext).getAt(0));
    }

    // @Override
    @Override
    public Element combinePartialDecryptions(final Element ciphertext, final Element... partialDecryptions) {
        final Element element = ((Element) ciphertext).getAt(1);
        return this.ddhGroup.apply(partialDecryptions).apply(element);
    }

    @Override
    public DDHGroupDistributedKeyPairGenerator getKeyGenerator() {
        return this.keyGenerator;
    }

    @Override
    public Function getEncryptionFunction() {
        return this.encryptionFunction;
    }

    @Override
    public Function getEncryptionFunctionLeft() {
        return ((CompoundFunction) this.getEncryptionFunction()).getFunctionAt(1, 0, 1);
    }

    @Override
    public Function getEncryptionFunctionRight() {
        return ((CompoundFunction) this.getEncryptionFunction()).getFunctionAt(1, 1);
    }

    @Override
    public Function getIdentityEncryptionFunction() {
        return this.identityEncryptionFunction;
    }

    @Override
    public Function getDecryptionFunction() {
        return this.decryptionFunction;
    }

    @Override
    public Function getPartialDecryptionFunction() {
        return ((CompoundFunction) this.getDecryptionFunction()).getFunctionAt(1, 0, 2);
    }

    @Override
    public DDHGroup getPlaintextSpace() {
        return this.ddhGroup;
    }

    @Override
    public ZPlusMod getRandomizationSpace() {
        return this.zModPlus;
    }

    @Override
    public Group getCiphertextSpace() {
        return this.getEncryptionFunction().getCoDomain();
    }
    
    /**
     * Create a validity proof of an encrypted text
     * @param randomization randomization used in the encryption
     * @param publicKey public key used in the encryption
     * @param index position of the plaintext that was encrypted in the array/list possiblePlainTexts
     * @param random random generator
     * @param possiblePlaintexts list of admitted plain texts
     * @return the proof Tuple
     */
    public Element createValididtyProof(Element randomization,
            Element publicKey, int index, final Random random,
            Element... possiblePlaintexts) {
        if(possiblePlaintexts.length < 2){
            throw new IllegalArgumentException("possiblePlaintexts must have at least a length of 2!");
        } else if(index<0 || index >= possiblePlaintexts.length){
            throw new IllegalArgumentException("Index must be greater than 0 and smaller than the length of the possiblePlaintexts list!");
        }

        // perform the precomputation steps
        ArrayList<Element> precomputedMessages = new ArrayList<Element>();
        for (int j = 0; j < possiblePlaintexts.length; j++) {
            precomputedMessages.add(encrypt(publicKey,
                    possiblePlaintexts[index].apply(possiblePlaintexts[j]
                    .invert()), randomization));
        }
        final Element publicProofInput = (ProductGroup
                .getElement(precomputedMessages));

        // create the homomorphic one way function for the proof
        final Function f = getIdentityEncryptionFunction().partiallyApply(
                publicKey, 0);
        
        // the function for each relation in the proof is always the same, so we need to create a list of functions
        Function[] functions = new Function[possiblePlaintexts.length];
        for (int j = 0; j < possiblePlaintexts.length; j++) {
            functions[j] = f;
        }

        final SigmaOrProofGeneratorClass proofGen = new SigmaOrProofGeneratorClass(functions);

        // generate the OR proof with the prepared input and return it
        return proofGen.generate(randomization, index, publicProofInput, null,
                random);
    }

    /**
     * Verifies a validity proof
     * @param validityProof the proof tuple
     * @param ciphertext the cipher text we want to prove the validity
     * @param publicKey public key used to generate the cipher text
     * @param possiblePlaintexts list of admitted plain texts 
     * @return true if proof is correct, false otherwise
     */
    public boolean verifyValidityProof(Element validityProof, Element ciphertext, Element publicKey, Element... possiblePlaintexts) {

        if(possiblePlaintexts.length < 2){
            throw new IllegalArgumentException("possiblePlaintexts must have at least a length of 2!");
        } else if (validityProof.getArity()!= 3){
            throw new IllegalArgumentException("proof must have an arity of 3 !");
        }
        
        Element a = ((Element) ciphertext).getAt(0);
        Element b = ((Element) ciphertext).getAt(1);


        // perform the precomputation steps
        ArrayList<Element> precomputedMessages = new ArrayList<Element>();
        for (int j = 0; j < possiblePlaintexts.length; j++) {
            precomputedMessages.add(ProductGroup.getElement(a, b.apply(possiblePlaintexts[j].invert())));
        }
        final Element publicProofInput = (ProductGroup
                .getElement(precomputedMessages));

        // create the homomorphic one way function used in the proof
        final Function f = getIdentityEncryptionFunction().partiallyApply(
                publicKey, 0);
        
        // the function for each relation in the proof is always the same, so we need to create a list of functions
        Function[] functions = new Function[validityProof.getAt(2).getArity()];
        for (int j = 0; j < validityProof.getAt(2).getArity(); j++) {
            functions[j] = f;
        }

        final SigmaOrProofGeneratorClass proofGen = new SigmaOrProofGeneratorClass(functions);

        return proofGen.verify(validityProof, publicProofInput);
    }

    private Function createEncryptionFunction() {
        final ProductGroup encryptionDomain = new ProductGroup(this.ddhGroup, this.ddhGroup, this.zModPlus);
        //@formatter:off
        return new CompositeFunction(
            new MultiIdentityFunction(encryptionDomain, 2),
            new ProductFunction(
                new CompositeFunction(
                   new SelectionFunction(encryptionDomain, 2),
                   this.createEncryptionFunctionLeft()),
                this.createEncryptionFunctionRight(encryptionDomain)));
        //@formatter:on
    }

    private Function createEncryptionFunctionLeft() {
      return new SelfApplyFunction(this.ddhGroup, this.zModPlus).partiallyApply(this.generator, 0);
    }
    
    private Function createEncryptionFunctionRight(final ProductGroup encryptionDomain) {
      //@formatter:off
      return new CompositeFunction(
          new MultiIdentityFunction(encryptionDomain, 2),
          new ProductFunction(
              new CompositeFunction(
                  new MultiIdentityFunction(encryptionDomain, 2),
                  new ProductFunction(
                      new SelectionFunction(encryptionDomain, 0),
                      new SelectionFunction(encryptionDomain, 2)),
                  new SelfApplyFunction(this.ddhGroup, this.zModPlus)),
              new SelectionFunction(encryptionDomain, 1)),
          new ApplyFunction(this.ddhGroup));
      //@formatter:on
    }

    private Function createIdentityEncryptionFunction() {
      return this.getEncryptionFunction().partiallyApply(this.ddhGroup.getIdentityElement(), 1);
    }

    private Function createDecryptionFunction() {
      final ProductGroup decryptionDomain = new ProductGroup(this.zModPlus, new PowerGroup(this.ddhGroup, 2));
      //@formatter:off
      return new CompositeFunction(
          new MultiIdentityFunction(decryptionDomain, 2),
          new ProductFunction(
              new CompositeFunction(
                  new MultiIdentityFunction(decryptionDomain, 2),
                  new ProductFunction(
                      new SelectionFunction(decryptionDomain, 0),
                      new SelectionFunction(decryptionDomain, 1, 0)),
                  this.createPartialDecryptionFunction()),
              new SelectionFunction(decryptionDomain, 1, 1)),
          new ApplyFunction(this.ddhGroup));
      //@formatter:on
    }

    private Function createPartialDecryptionFunction() {
      final ProductGroup partialDecryptionDomain = new ProductGroup(this.zModPlus, this.ddhGroup);
      //@formatter:off
      return new CompositeFunction(
          new MultiIdentityFunction(partialDecryptionDomain, 2),
          new ProductFunction(
              new SelectionFunction(partialDecryptionDomain, 1),
              new CompositeFunction(
                  new SelectionFunction(partialDecryptionDomain, 0),
                  new InvertFunction(this.zModPlus))),
                  new SelfApplyFunction(this.ddhGroup, this.zModPlus));
      //@formatter:on
    }
}
