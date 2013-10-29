package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.ProductProofGeneratorAbstract;
import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.AndProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.SigmaProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.HashFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class AndSigmaProofGeneratorClass extends ProductProofGeneratorAbstract implements AndProofGenerator {

  private final SigmaProofGenerator sigmaProofGenerator;
  private final Function[] functions;

  public AndSigmaProofGeneratorClass(final List<Function> functions) {
    this(functions, SigmaProofGenerator.DEFAULT_HASH_ALGORITHM, SigmaProofGenerator.DEFAULT_CONCAT_ALGORITHM, SigmaProofGenerator.DEFAULT_MAPPER);
  }

  public AndSigmaProofGeneratorClass(final List<Function> functions,
          final HashAlgorithm hashAlgorithm,
          final ConcatParameter concatParameter,
          final Mapper mapper) {
    this(functions.toArray(new Function[functions.size()]), hashAlgorithm, concatParameter, mapper);
  }

  public AndSigmaProofGeneratorClass(final Function... functions) {
    this(functions, SigmaProofGenerator.DEFAULT_HASH_ALGORITHM, SigmaProofGenerator.DEFAULT_CONCAT_ALGORITHM, SigmaProofGenerator.DEFAULT_MAPPER);
  }

  public AndSigmaProofGeneratorClass(final Function[] functions, final HashAlgorithm hashAlgorithm, final ConcatParameter concatParameter, final Mapper mapper) {
    final Function function = new ProductFunction(functions);
    this.sigmaProofGenerator = new SigmaProofGenerator(function, hashAlgorithm, concatParameter, mapper);
    this.functions = functions;
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
  public ProductFunction getProofFunction() {
    return (ProductFunction) this.sigmaProofGenerator.getProofFunction();
  }

  @Override
  public Function[] getFunctions() {
    return this.functions;
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
