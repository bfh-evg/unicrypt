package ch.bfh.unicrypt.crypto.nizkp.classes;

import java.util.Random;

import ch.bfh.unicrypt.crypto.nizkp.abstracts.ProductProofGeneratorAbstract;
import ch.bfh.unicrypt.crypto.nizkp.interfaces.SigmaBatchProofGenerator;
import ch.bfh.unicrypt.crypto.nizkp.interfaces.SigmaProofGenerator;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunction.ConcatParameter;
import ch.bfh.unicrypt.math.function.classes.HashFunction.HashAlgorithm;
import ch.bfh.unicrypt.math.function.classes.PowerFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.function.interfaces.HashFunction;
import ch.bfh.unicrypt.math.function.interfaces.PowerFunction;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;
import ch.bfh.unicrypt.math.utility.mapper.interfaces.Mapper;

public class SigmaBatchProofGeneratorClass extends ProductProofGeneratorAbstract implements SigmaBatchProofGenerator {

  private final SigmaProofGenerator sigmaProofGenerator;

  public SigmaBatchProofGeneratorClass(final Function function, final int arity) {
    this(function, arity, SigmaProofGeneratorClass.DEFAULT_HASH_ALGORITHM, SigmaProofGeneratorClass.DEFAULT_CONCAT_ALGORITHM,
        SigmaProofGeneratorClass.DEFAULT_MAPPER);
  }

  public SigmaBatchProofGeneratorClass(final Function function,
      final int arity,
      final HashAlgorithm hashAlgorithm,
      final ConcatParameter concatParameter,
      final Mapper mapper) {
    final Function proofFunction = new PowerFunction(function, arity);
    this.sigmaProofGenerator = new SigmaProofGeneratorClass(proofFunction, hashAlgorithm, concatParameter, mapper);
  }

  @Override
  public TupleElement generate(final Element secretInput, final Element publicInput, final Element otherInput, final Random random) {
    return this.sigmaProofGenerator.generate(secretInput, publicInput, otherInput, random);
  }

  @Override
  public boolean verify(final TupleElement proof, final Element publicInput, final Element otherInput) {
    return this.sigmaProofGenerator.verify(proof, publicInput, otherInput);
  }

  @Override
  public ProductGroup getProofSpace() {
    return this.sigmaProofGenerator.getProofSpace();
  }

  @Override
  public PowerFunction getProofFunction() {
    return (PowerFunction) this.sigmaProofGenerator.getProofFunction();
  }

  @Override
  public ProductGroup getResponseSpace() {
    return (ProductGroup) this.sigmaProofGenerator.getResponseSpace();
  }

  @Override
  public TupleElement getResponse(final TupleElement proof) {
    return (TupleElement) this.sigmaProofGenerator.getResponse(proof);
  }

  @Override
  public ProductGroup getCommitmentSpace() {
    return (ProductGroup) this.sigmaProofGenerator.getCommitmentSpace();
  }

  @Override
  public TupleElement getCommitment(final TupleElement proof) {
    return (TupleElement) this.sigmaProofGenerator.getCommitment(proof);
  }

  @Override
  public HashFunction getHashFunction() {
    return this.sigmaProofGenerator.getHashFunction();
  }

}
