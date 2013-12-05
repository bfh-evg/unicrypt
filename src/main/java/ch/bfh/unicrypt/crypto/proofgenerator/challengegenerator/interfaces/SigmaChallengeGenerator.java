package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public interface SigmaChallengeGenerator
	   extends ChallengeGenerator {

	public Set getPublicInputSpace();

	public Set getCommitmentSpace();

	public ZModElement generate(Element publicInput, Element commitment);

	@Override
	public ZModElement generate(Element input);

}
