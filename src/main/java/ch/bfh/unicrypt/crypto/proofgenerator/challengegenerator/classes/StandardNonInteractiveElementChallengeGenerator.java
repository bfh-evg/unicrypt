package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts.AbstractNonInteractiveElementChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.random.RandomOracle;

public class StandardNonInteractiveElementChallengeGenerator
	   extends AbstractNonInteractiveElementChallengeGenerator {

	protected StandardNonInteractiveElementChallengeGenerator(final Set outputSet, final int size, final RandomOracle randomOracle) {
		super(FiniteByteArraySet.getInstance(64), outputSet, size, randomOracle);
	}

	public static StandardNonInteractiveElementChallengeGenerator getInstance(final Set outputSet, final int size) {
		return StandardNonInteractiveElementChallengeGenerator.getInstance(outputSet, size, (RandomOracle) null);
	}

	public static StandardNonInteractiveElementChallengeGenerator getInstance(final Set outputSet, final int size, RandomOracle randomOracle) {
		if (outputSet == null || size < 1) {
			throw new IllegalArgumentException();
		}
		if (randomOracle == null) {
			randomOracle = RandomOracle.DEFAULT;
		}
		return new StandardNonInteractiveElementChallengeGenerator(outputSet, size, randomOracle);
	}

}
