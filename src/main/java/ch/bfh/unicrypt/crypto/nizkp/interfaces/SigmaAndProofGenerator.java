package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
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
  public TupleElement getCommitment(TupleElement proof);

  @Override
  public TupleElement getResponse(TupleElement proof);
}
