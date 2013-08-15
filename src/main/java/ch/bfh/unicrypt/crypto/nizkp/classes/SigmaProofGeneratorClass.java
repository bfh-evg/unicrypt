package ch.bfh.unicrypt.crypto.nizkp.classes;

import java.util.Random;

import ch.bfh.unicrypt.crypto.nizkp.abstracts.ProofGeneratorAbstract;
import ch.bfh.unicrypt.crypto.nizkp.interfaces.SigmaProofGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunction;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunction.ConcatParameter;
import ch.bfh.unicrypt.math.function.classes.HashFunction;
import ch.bfh.unicrypt.math.function.interfaces.ConcatenateFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.function.interfaces.HashFunction;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import ch.bfh.unicrypt.math.utility.mapper.classes.CharsetXRadixYMapperClass;
import ch.bfh.unicrypt.math.utility.mapper.interfaces.Mapper;

public class SigmaProofGeneratorClass extends ProofGeneratorAbstract implements SigmaProofGenerator {

  public static final HashFunction.HashAlgorithm DEFAULT_HASH_ALGORITHM = HashFunction.HashAlgorithm.SHA256;
  public static final ConcatenateFunction.ConcatParameter DEFAULT_CONCAT_ALGORITHM = ConcatParameter.Plain;
  public static final Mapper DEFAULT_MAPPER = new CharsetXRadixYMapperClass(CharsetXRadixYMapperClass.DEFAULT_CHARSET, CharsetXRadixYMapperClass.DEFAULT_RADIX);

  private final Function function;
  private final HashFunction hashFunction;
  private final ZPlusMod challengeSpace;
  private final ProductGroup proofSpace;
  private final ConcatParameter concatParameter;
  private final Mapper mapper;

  public SigmaProofGeneratorClass(final Function function) {
    this(function, SigmaProofGeneratorClass.DEFAULT_HASH_ALGORITHM, SigmaProofGeneratorClass.DEFAULT_CONCAT_ALGORITHM, SigmaProofGeneratorClass.DEFAULT_MAPPER);
  }

  public SigmaProofGeneratorClass(final Function function,
      final HashFunction.HashAlgorithm hashAlgorithm,
      final ConcatenateFunction.ConcatParameter concatParameter,
      final Mapper mapper) {
    if ((function == null) || (hashAlgorithm == null) || (concatParameter == null) || (mapper == null)) {
      throw new IllegalArgumentException();
    }
    this.function = function;
    this.challengeSpace = this.getDomain().getMinOrderGroup();
    this.hashFunction = new HashFunction(hashAlgorithm, this.challengeSpace);
    this.concatParameter = concatParameter;
    this.mapper = mapper;
    this.proofSpace = new ProductGroup(this.getCommitmentSpace(), this.getResponseSpace());

  }

  @Override
  public TupleElement generate(final Element secretInput, final Element publicInput, final Element otherInput, final Random random) {
    if ((secretInput == null) || !this.getResponseSpace().contains(secretInput) || (publicInput == null)
        || !this.getCommitmentSpace().contains(publicInput)) {
      throw new IllegalArgumentException();
    }
    final Element randomElement = this.createRandomElement(random);
    final Element commitment = this.createCommitment(randomElement);
    final AdditiveElement challenge = this.createChallenge(commitment, publicInput, otherInput);
    final Element response = this.createResponse(randomElement, challenge, secretInput);
    return this.createProof(commitment, response);
  }

  public Element createRandomElement(final Random random) {
    return this.getResponseSpace().getRandomElement(random);
  }

  public Element createCommitment(final Element randomElement) {
    return this.getProofFunction().apply(randomElement);
  }

  public AdditiveElement createChallenge(final Element commitment, final Element publicInput, final Element otherInput) {
    AdditiveElement hashInput;
    if (otherInput == null) {
      final ProductGroup pg = new ProductGroup(publicInput.getSet(), commitment.getSet());
      final ConcatenateFunction concatFunction = new ConcatenateFunction(pg, this.concatParameter, this.mapper);
      hashInput = concatFunction.apply(publicInput, commitment);
    } else {
      final ProductGroup pg = new ProductGroup(publicInput.getSet(), commitment.getSet(), otherInput.getSet());
      final ConcatenateFunction concatFunction = new ConcatenateFunction(pg, this.concatParameter, this.mapper);
      hashInput = concatFunction.apply(publicInput, commitment, otherInput);
    }
    return this.getHashFunction().apply(hashInput);
  }

  @SuppressWarnings("static-method")
  public Element createResponse(final Element randomElement, final AdditiveElement challenge, final Element secretInput) {
    return randomElement.apply(secretInput.selfApply(challenge));
  }

  public TupleElement createProof(final Element commitment, final Element response) {
    return this.getProofSpace().getElement(commitment, response);
  }

  @Override
  public boolean verify(final TupleElement proof, final Element publicInput, final Element otherInput) {
    if ((proof == null) || !this.getProofSpace().contains(proof) || (publicInput == null) || !this.getCommitmentSpace().contains(publicInput)) {
      throw new IllegalArgumentException();
    }
    AdditiveElement hashInput;
    if (otherInput == null) {
      final ProductGroup pg = new ProductGroup(publicInput.getSet(), this.getCommitment(proof).getGroup());
      final ConcatenateFunction concatFunction = new ConcatenateFunction(pg, this.concatParameter, this.mapper);
      hashInput = concatFunction.apply(publicInput, this.getCommitment(proof));
    } else {
      final ProductGroup pg = new ProductGroup(publicInput.getSet(), this.getCommitment(proof).getGroup(), otherInput.getSet());
      final ConcatenateFunction concatFunction = new ConcatenateFunction(pg, this.concatParameter, this.mapper);
      hashInput = concatFunction.apply(publicInput, this.getCommitment(proof), otherInput);
    }
    final AdditiveElement challenge = this.getHashFunction().apply(hashInput);
    final Element left = this.getProofFunction().apply(this.getResponse(proof));
    final Element right = this.getCommitment(proof).apply(publicInput.selfApply(challenge));
    return left.equals(right);
  }

  @Override
  public Function getProofFunction() {
    return this.function;
  }

  @Override
  public ProductGroup getProofSpace() {
    return this.proofSpace;
  }

  @Override
  public Group getCommitmentSpace() {
    return this.getCoDomain();
  }

  @Override
  public Group getResponseSpace() {
    return this.getDomain();
  }

  @Override
  public Element getCommitment(final TupleElement element) {
    if (element == null) {
      throw new IllegalArgumentException();
    }
    return element.getElementAt(0);
  }

  @Override
  public Element getResponse(final TupleElement element) {
    if (element == null) {
      throw new IllegalArgumentException();
    }
    return element.getElementAt(1);
  }

  @Override
  public HashFunction getHashFunction() {
    return this.hashFunction;
  }

}
