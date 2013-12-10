package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces;

import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public interface ElementChallengeGenerator
	   extends ChallengeGenerator {

	@Override
	public Tuple generate(Element input);

}
