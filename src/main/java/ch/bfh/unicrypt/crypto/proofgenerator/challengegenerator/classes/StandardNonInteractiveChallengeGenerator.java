package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts.AbstractNonInteractiveChallengeGenerator;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public class StandardNonInteractiveChallengeGenerator
	   extends AbstractNonInteractiveChallengeGenerator<Set, Element, Set, Element> {

	protected StandardNonInteractiveChallengeGenerator(final Set inputSpace, final Set challengeSpace, final RandomOracle randomOracle) {
		super(inputSpace, challengeSpace, randomOracle);
	}

	public static StandardNonInteractiveChallengeGenerator getInstance(final Set inputSpace, final Set challengeSet, final int arity) {
		return StandardNonInteractiveChallengeGenerator.getInstance(inputSpace, challengeSet, arity, (RandomOracle) null);
	}

	public static StandardNonInteractiveChallengeGenerator getInstance(final Set inputSpace, final Set challengeSet, final int arity, final RandomOracle randomOracle) {
		if (challengeSet == null || arity < 1) {
			throw new IllegalArgumentException();
		}
		return StandardNonInteractiveChallengeGenerator.getInstance(inputSpace, ProductSet.getInstance(challengeSet, arity), randomOracle);
	}

	public static StandardNonInteractiveChallengeGenerator getInstance(final Set inputSpace, final Set challengeSpace) {
		return StandardNonInteractiveChallengeGenerator.getInstance(inputSpace, challengeSpace, (RandomOracle) null);
	}

	public static StandardNonInteractiveChallengeGenerator getInstance(final Set inputSpace, final Set challengeSpace, RandomOracle randomOracle) {
		if (inputSpace == null || challengeSpace == null) {
			throw new IllegalArgumentException();
		}
		if (randomOracle == null) {
			randomOracle = PseudoRandomOracle.DEFAULT;
		}
		return new StandardNonInteractiveChallengeGenerator(inputSpace, challengeSpace, randomOracle);
	}

}
