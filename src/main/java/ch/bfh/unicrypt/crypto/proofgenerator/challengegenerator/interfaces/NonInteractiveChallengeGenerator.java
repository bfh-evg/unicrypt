package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;

public interface NonInteractiveChallengeGenerator
			 extends ChallengeGenerator {

	public RandomOracle getRandomOracle();

}
