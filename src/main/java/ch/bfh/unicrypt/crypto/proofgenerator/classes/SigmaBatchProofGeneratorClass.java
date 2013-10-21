package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import java.util.Random;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.ProductProofGeneratorAbstract;
import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.SigmaBatchProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.SigmaProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.HashFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

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
  public Tuple generate(final Element secretInput, final Element publicInput, final Element otherInput, final Random random) {
    return this.sigmaProofGenerator.generate(secretInput, publicInput, otherInput, random);
  }

  @Override
  public boolean verify(final Tuple proof, final Element publicInput, final Element otherInput) {
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
  public Tuple getResponse(final Tuple proof) {
    return (Tuple) this.sigmaProofGenerator.getResponse(proof);
  }

  @Override
  public ProductGroup getCommitmentSpace() {
    return (ProductGroup) this.sigmaProofGenerator.getCommitmentSpace();
  }

  @Override
  public Tuple getCommitment(final Tuple proof) {
    return (Tuple) this.sigmaProofGenerator.getCommitment(proof);
  }

  @Override
  public HashFunction getHashFunction() {
    return this.sigmaProofGenerator.getHashFunction();
  }

}
