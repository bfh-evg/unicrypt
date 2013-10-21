package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface SigmaEqualityProofGenerator extends ProductCoDomainProofGenerator, SigmaProofGenerator {

  public Function[] getFunctions();

  @Override
  public ProductGroup getCommitmentSpace();

  @Override
  public Tuple getCommitment(Tuple proof);

}
