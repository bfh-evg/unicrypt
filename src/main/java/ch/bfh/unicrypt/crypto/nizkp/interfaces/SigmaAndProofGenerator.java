package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import ch.bfh.unicrypt.math.element.interfaces.Tuple;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.function.interfaces.ProductFunction;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;

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
