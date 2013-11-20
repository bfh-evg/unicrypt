package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.util.Random;

public interface ProofGenerator {

  public Element generate(Element privateInput, Element publicInput);

  public Element generate(Element privateInput, Element publicInput, Element proverID);

  public Element generate(Element privateInput, Element publicInput, Random random);

  public Element generate(Element privateInput, Element publicInput, Element proverID, Random random);

  public BooleanElement verify(Element proof, Element publicInput);

  public BooleanElement verify(Element proof, Element publicInput, Element proverID);

  public Set getPrivateInputSpace();

  public Set getPublicInputSpace();

  public Set getProofSpace();

}
