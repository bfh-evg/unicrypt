package ch.bfh.unicrypt.crypto.nizkp.interfaces;

import ch.bfh.unicrypt.math.function.interfaces.PowerFunction;

public interface SigmaBatchProofGenerator extends ProductProofGenerator, SigmaProofGenerator {

  @Override
  public PowerFunction getProofFunction();

}
