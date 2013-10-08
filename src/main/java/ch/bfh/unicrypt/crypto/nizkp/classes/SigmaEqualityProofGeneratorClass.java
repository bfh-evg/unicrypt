package ch.bfh.unicrypt.crypto.nizkp.classes;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.crypto.nizkp.abstracts.ProductCoDomainProofGeneratorAbstract;
import ch.bfh.unicrypt.crypto.nizkp.interfaces.SigmaEqualityProofGenerator;
import ch.bfh.unicrypt.crypto.nizkp.interfaces.SigmaProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.HashFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class SigmaEqualityProofGeneratorClass extends ProductCoDomainProofGeneratorAbstract implements SigmaEqualityProofGenerator {

  private final SigmaProofGenerator sigmaProofGenerator;
  private final Function[] functions;

  public SigmaEqualityProofGeneratorClass(final List<Function> functions) {
    this(functions, SigmaProofGeneratorClass.DEFAULT_HASH_ALGORITHM, SigmaProofGeneratorClass.DEFAULT_CONCAT_ALGORITHM, SigmaProofGeneratorClass.DEFAULT_MAPPER);
  }

  public SigmaEqualityProofGeneratorClass(final List<Function> functions,
      final HashAlgorithm hashAlgorithm,
      final ConcatParameter concatParameter,
      final Mapper mapper) {
    this(functions.toArray(new Function[functions.size()]), hashAlgorithm, concatParameter, mapper);
  }

  public SigmaEqualityProofGeneratorClass(final Function... functions) {
    this(functions, SigmaProofGeneratorClass.DEFAULT_HASH_ALGORITHM, SigmaProofGeneratorClass.DEFAULT_CONCAT_ALGORITHM, SigmaProofGeneratorClass.DEFAULT_MAPPER);
  }

  public SigmaEqualityProofGeneratorClass(final Function[] functions,
      final HashAlgorithm hashAlgorithm,
      final ConcatParameter concatParameter,
      final Mapper mapper) {
    final Function function = new CompositeFunction(new MultiIdentityFunction(SigmaEqualityProofGeneratorClass.getDomain(functions), functions.length),
        new ProductFunction(functions));
    this.sigmaProofGenerator = new SigmaProofGeneratorClass(function, hashAlgorithm, concatParameter, mapper);
    this.functions = functions;
  }

  private static Group getDomain(final Function[] functions) {
    if ((functions == null) || (functions.length < 1)) {
      throw new IllegalArgumentException();
    }
    return functions[0].getDomain();
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
  public Function getProofFunction() {
    return this.sigmaProofGenerator.getProofFunction();
  }

  @Override
  public Function[] getFunctions() {
    return this.functions;
  }

  @Override
  public Group getResponseSpace() {
    return this.sigmaProofGenerator.getResponseSpace();
  }

  @Override
  public Element getResponse(final Tuple proof) {
    return this.sigmaProofGenerator.getResponse(proof);
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
