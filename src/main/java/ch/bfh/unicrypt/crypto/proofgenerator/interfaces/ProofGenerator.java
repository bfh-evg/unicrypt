package ch.bfh.unicrypt.crypto.proofgenerator.interfaces;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public interface ProofGenerator {

	public Element generate(Element privateInput, Element publicInput);

	public Element generate(Element privateInput, Element publicInput, RandomGenerator randomGenerator);

	public BooleanElement verify(Element proof, Element publicInput);

	public Set getPrivateInputSpace();

	public Set getPublicInputSpace();

	public Set getProofSpace();

}
