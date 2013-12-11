package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts.AbstractNonInteractiveElementChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.HashMethod;
import ch.bfh.unicrypt.math.random.RandomOracle;

public class StandardNonInteractiveElementChallengeGenerator
	   extends AbstractNonInteractiveElementChallengeGenerator {

	protected StandardNonInteractiveElementChallengeGenerator(final Set inputSpace,
		   final Set outputSet, final int size, final RandomOracle randomOracle, final HashMethod hashMethod) {
		super(inputSpace, outputSet, size, randomOracle, hashMethod);
	}

	public static StandardNonInteractiveElementChallengeGenerator getInstance(final Set inputSpace, final Set outputSet, final int size) {
		return StandardNonInteractiveElementChallengeGenerator.getInstance(inputSpace, outputSet, size, RandomOracle.DEFAULT);
	}

	public static StandardNonInteractiveElementChallengeGenerator getInstance(final Set inputSpace, final Set outputSet, final int size, final RandomOracle randomOracle) {
		return StandardNonInteractiveElementChallengeGenerator.getInstance(inputSpace, outputSet, size, randomOracle, HashMethod.DEFAULT);
	}

	public static StandardNonInteractiveElementChallengeGenerator getInstance(final Set inputSpace, final Set outputSet, final int size, final HashMethod hashMethod) {
		return StandardNonInteractiveElementChallengeGenerator.getInstance(inputSpace, outputSet, size, RandomOracle.DEFAULT, hashMethod);
	}

	public static StandardNonInteractiveElementChallengeGenerator getInstance(final Set inputSpace,
		   final Set outputSet, final int size, final RandomOracle randomOracle, final HashMethod hashMethod) {
		if (inputSpace == null || outputSet == null || size < 1 || randomOracle == null || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new StandardNonInteractiveElementChallengeGenerator(inputSpace, outputSet, size, randomOracle, hashMethod);
	}

}
