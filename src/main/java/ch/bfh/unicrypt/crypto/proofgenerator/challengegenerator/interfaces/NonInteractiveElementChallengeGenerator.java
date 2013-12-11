package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces;

import ch.bfh.unicrypt.math.helper.HashMethod;
import ch.bfh.unicrypt.math.random.RandomOracle;

public interface NonInteractiveElementChallengeGenerator
	   extends ElementChallengeGenerator {

	public RandomOracle getRandomOracle();

	public HashMethod getHashMethod();

}
