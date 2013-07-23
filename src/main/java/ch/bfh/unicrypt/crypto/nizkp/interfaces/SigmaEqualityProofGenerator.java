package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;

public interface SigmaEqualityProofGenerator extends ProductCoDomainProofGenerator, SigmaProofGenerator {

  public Function[] getFunctions();

  @Override
  public ProductGroup getCommitmentSpace();

  @Override
  public TupleElement getCommitment(TupleElement proof);

}
