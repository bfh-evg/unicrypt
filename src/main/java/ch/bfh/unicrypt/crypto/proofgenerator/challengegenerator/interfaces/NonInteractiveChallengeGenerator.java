package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public interface NonInteractiveChallengeGenerator
			 extends ChallengeGenerator {

	public RandomOracle getRandomOracle();

	// May return null!
	public Element getProverID();

}
