package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces;

import ch.bfh.unicrypt.math.random.RandomOracle;

public interface NonInteractiveChallengeGenerator
	   extends ChallengeGenerator {

	public RandomOracle getRandomOracle();

}
