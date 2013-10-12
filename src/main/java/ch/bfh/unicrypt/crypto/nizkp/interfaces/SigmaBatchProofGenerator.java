package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import ch.bfh.unicrypt.math.function.classes.ProductFunction;

public interface SigmaBatchProofGenerator extends ProductProofGenerator, SigmaProofGenerator {

  @Override
  public ProductFunction getProofFunction();

}
