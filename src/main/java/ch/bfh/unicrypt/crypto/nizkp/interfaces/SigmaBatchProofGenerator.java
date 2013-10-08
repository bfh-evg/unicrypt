package ch.bfh.unicrypt.crypto.nizkp.interfaces;


public interface SigmaBatchProofGenerator extends ProductProofGenerator, SigmaProofGenerator {

  @Override
  public PowerFunction getProofFunction();

}
