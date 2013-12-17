package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts.AbstractNonInteractiveMultiChallengeGenerator;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public class NonInteractiveMultiChallengeGenerator
			 extends AbstractNonInteractiveMultiChallengeGenerator<Set, Element, ProductSet> {

	protected NonInteractiveMultiChallengeGenerator(final Set inputSpace, final ProductSet challengeSpace, final RandomOracle randomOracle, Element proverID) {
		super(inputSpace, challengeSpace, randomOracle, proverID);
	}

	public static NonInteractiveMultiChallengeGenerator getInstance(final Set inputSpace, final Set challengeSet, int arity) {
		return NonInteractiveMultiChallengeGenerator.getInstance(inputSpace, challengeSet, arity, (RandomOracle) null, (Element) null);
	}

	public static NonInteractiveMultiChallengeGenerator getInstance(final Set inputSpace, final Set challengeSet, int arity, final RandomOracle randomOracle) {
		return NonInteractiveMultiChallengeGenerator.getInstance(inputSpace, challengeSet, arity, randomOracle, (Element) null);
	}

	public static NonInteractiveMultiChallengeGenerator getInstance(final Set inputSpace, final Set challengeSet, int arity, Element proverID) {
		return NonInteractiveMultiChallengeGenerator.getInstance(inputSpace, challengeSet, arity, (RandomOracle) null, proverID);
	}

	public static NonInteractiveMultiChallengeGenerator getInstance(final Set inputSpace, final Set challengeSet, int arity, final RandomOracle randomOracle, Element proverID) {
		if (challengeSet == null || arity < 1) {
			throw new IllegalArgumentException();
		}
		return NonInteractiveMultiChallengeGenerator.getInstance(inputSpace, ProductSet.getInstance(challengeSet, arity), randomOracle, proverID);
	}

	public static NonInteractiveMultiChallengeGenerator getInstance(final Set inputSpace, final ProductSet challengeSpace) {
		return NonInteractiveMultiChallengeGenerator.getInstance(inputSpace, challengeSpace, (RandomOracle) null, (Element) null);
	}

	public static NonInteractiveMultiChallengeGenerator getInstance(final Set inputSpace, final ProductSet challengeSpace, Element proverID) {
		return NonInteractiveMultiChallengeGenerator.getInstance(inputSpace, challengeSpace, (RandomOracle) null, proverID);
	}

	public static NonInteractiveMultiChallengeGenerator getInstance(final Set inputSpace, final ProductSet challengeSpace, final RandomOracle randomOracle) {
		return NonInteractiveMultiChallengeGenerator.getInstance(inputSpace, challengeSpace, randomOracle, (Element) null);
	}

	public static NonInteractiveMultiChallengeGenerator getInstance(final Set inputSpace, final ProductSet challengeSpace, RandomOracle randomOracle, Element proverID) {
		if (inputSpace == null || challengeSpace == null) {
			throw new IllegalArgumentException();
		}
		if (randomOracle == null) {
			randomOracle = PseudoRandomOracle.DEFAULT;
		}
		return new NonInteractiveMultiChallengeGenerator(inputSpace, challengeSpace, randomOracle, proverID);
	}

}
