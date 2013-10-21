package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.function.classes.ProductFunction;

public interface SigmaBatchProofGenerator extends ProductProofGenerator, SigmaProofGenerator {

  @Override
  public ProductFunction getProofFunction();

}
