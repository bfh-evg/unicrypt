package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface SigmaAndProofGenerator extends ProductProofGenerator, SigmaProofGenerator {

  public Function[] getFunctions();

  @Override
  public ProductFunction getProofFunction();

  @Override
  public ProductGroup getCommitmentSpace();

  @Override
  public ProductGroup getResponseSpace();

  @Override
  public Tuple getCommitment(Tuple proof);

  @Override
  public Tuple getResponse(Tuple proof);
}
