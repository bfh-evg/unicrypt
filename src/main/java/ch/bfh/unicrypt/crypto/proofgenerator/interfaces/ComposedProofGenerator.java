package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface ComposedProofGenerator extends ProofGenerator {

  public Function[] getProofFunctions();

  @Override
  public ProductFunction getProofFunction();

  @Override
  public ProductSet getPublicInputSpace();

}
