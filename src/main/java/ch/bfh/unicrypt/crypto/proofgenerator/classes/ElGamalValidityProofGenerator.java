package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.util.Random;

// TODO Implement!
public class ElGamalValidityProofGenerator
	   extends AbstractProofGenerator<Set, Set, Set, Element> {

	@Override
	protected Element abstractGenerate(Element secretInput, Element publicInput, Element otherInput, Random random) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected BooleanElement abstractVerify(Element proof, Element publicInput, Element otherInput) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected Set abstractGetPrivateInputSpace() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected Set abstractGetPublicInputSpace() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected Set abstractGetProofSpace() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
//   /**
//     * Create a validity proof of an encrypted text
//     * @param randomization randomization used in the encryption
//     * @param publicKey public key used in the encryption
//     * @param index position of the plaintext that was encrypted in the array/list possiblePlainTexts
//     * @param random random generator
//     * @param possiblePlaintexts list of admitted plain texts
//     * @return the proof Tuple
//     */
//    public Element createValididtyProof(Element randomization,
//            Element publicKey, int index, final Random random,
//            Element... possiblePlaintexts) {
//        if(possiblePlaintexts.length < 2){
//            throw new IllegalArgumentException("possiblePlaintexts must have at least a length of 2!");
//        } else if(index<0 || index >= possiblePlaintexts.length){
//            throw new IllegalArgumentException("Index must be greater than 0 and smaller than the length of the possiblePlaintexts list!");
//        }
//
//        // perform the precomputation steps
//        ArrayList<Element> precomputedMessages = new ArrayList<Element>();
//        for (int j = 0; j < possiblePlaintexts.length; j++) {
//            precomputedMessages.add(encrypt(publicKey,
//                    possiblePlaintexts[index].apply(possiblePlaintexts[j]
//                    .invert()), randomization));
//        }
//        final Element publicProofInput = (ProductGroup
//                .getElement(precomputedMessages));
//
//        // create the homomorphic one way function for the proof
//        final Function f = getIdentityEncryptionFunction().partiallyApply(
//                publicKey, 0);
//
//        // the function for each relation in the proof is always the same, so we need to create a list of functions
//        Function[] functions = new Function[possiblePlaintexts.length];
//        for (int j = 0; j < possiblePlaintexts.length; j++) {
//            functions[j] = f;
//        }
//
//        final PreimageOrProofGeneratorClass proofGen = new PreimageOrProofGeneratorClass(functions);
//
//        // generate the OR proof with the prepared input and return it
//        return proofGen.generate(randomization, index, publicProofInput, null,
//                random);
//    }
//
//    /**
//     * Verifies a validity proof
//     * @param validityProof the proof tuple
//     * @param ciphertext the cipher text we want to prove the validity
//     * @param publicKey public key used to generate the cipher text
//     * @param possiblePlaintexts list of admitted plain texts
//     * @return true if proof is correct, false otherwise
//     */
//    public boolean verifyValidityProof(Element validityProof, Element ciphertext, Element publicKey, Element... possiblePlaintexts) {
//
//        if(possiblePlaintexts.length < 2){
//            throw new IllegalArgumentException("possiblePlaintexts must have at least a length of 2!");
//        } else if (validityProof.getArity()!= 3){
//            throw new IllegalArgumentException("proof must have an arity of 3 !");
//        }
//
//        Element a = ((Element) ciphertext).getAt(0);
//        Element b = ((Element) ciphertext).getAt(1);
//
//
//        // perform the precomputation steps
//        ArrayList<Element> precomputedMessages = new ArrayList<Element>();
//        for (int j = 0; j < possiblePlaintexts.length; j++) {
//            precomputedMessages.add(ProductGroup.getElement(a, b.apply(possiblePlaintexts[j].invert())));
//        }
//        final Element publicProofInput = (ProductGroup
//                .getElement(precomputedMessages));
//
//        // create the homomorphic one way function used in the proof
//        final Function f = getIdentityEncryptionFunction().partiallyApply(
//                publicKey, 0);
//
//        // the function for each relation in the proof is always the same, so we need to create a list of functions
//        Function[] functions = new Function[validityProof.getAt(2).getArity()];
//        for (int j = 0; j < validityProof.getAt(2).getArity(); j++) {
//            functions[j] = f;
//        }
//
//        final PreimageOrProofGeneratorClass proofGen = new PreimageOrProofGeneratorClass(functions);
//
//        return proofGen.verify(validityProof, publicProofInput);
//    }

