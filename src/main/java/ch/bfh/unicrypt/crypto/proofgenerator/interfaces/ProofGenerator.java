package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public interface ProofGenerator {

  public Element generate(Element privateInput, Element publicInput);

  public Element generate(Element privateInput, Element publicInput, Element otherInput);

  public Element generate(Element privateInput, Element publicInput, Random random);

  public Element generate(Element privateInput, Element publicInput, Element otherInput, Random random);

  public BooleanElement verify(Element proof, Element publicInput);

  public BooleanElement verify(Element proof, Element publicInput, Element otherInput);

  public Set getPrivateInputSpace();

  public Set getPublicInputSpace();

  public Set getOtherInputSpace();

  public Set getProofSpace();

}
