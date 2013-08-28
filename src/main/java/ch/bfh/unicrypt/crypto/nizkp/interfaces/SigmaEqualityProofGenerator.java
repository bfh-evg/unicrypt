package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import ch.bfh.unicrypt.math.product.interfaces.Tuple;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;

public interface SigmaEqualityProofGenerator extends ProductCoDomainProofGenerator, SigmaProofGenerator {

  public Function[] getFunctions();

  @Override
  public ProductGroup getCommitmentSpace();

  @Override
  public Tuple getCommitment(Tuple proof);

}
