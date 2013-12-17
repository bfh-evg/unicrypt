package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts.AbstractNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class NonInteractiveSigmaChallengeGenerator
			 extends AbstractNonInteractiveSigmaChallengeGenerator {

	protected NonInteractiveSigmaChallengeGenerator(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, RandomOracle randomOracle, Element proverID) {
		super(publicInputSpace, commitmentSpace, challengeSpace, randomOracle, proverID);
	}

	public static NonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace) {
		return NonInteractiveSigmaChallengeGenerator.getInstance(publicInputSpace, commitmentSpace, challengeSpace, (RandomOracle) null, (Element) null);
	}

	public static NonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, Element proverID) {
		return NonInteractiveSigmaChallengeGenerator.getInstance(publicInputSpace, commitmentSpace, challengeSpace, (RandomOracle) null, (Element) proverID);
	}

	public static NonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, RandomOracle randomOracle) {
		return NonInteractiveSigmaChallengeGenerator.getInstance(publicInputSpace, commitmentSpace, challengeSpace, randomOracle, (Element) null);
	}

	public static NonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, RandomOracle randomOracle, Element proverID) {
		if (publicInputSpace == null || commitmentSpace == null || challengeSpace == null) {
			throw new IllegalArgumentException();
		}
		if (randomOracle == null) {
			randomOracle = PseudoRandomOracle.DEFAULT;
		}
		return new NonInteractiveSigmaChallengeGenerator(publicInputSpace, commitmentSpace, challengeSpace, randomOracle, proverID);
	}

	public static NonInteractiveSigmaChallengeGenerator getInstance(Function function) {
		return NonInteractiveSigmaChallengeGenerator.getInstance(function, (RandomOracle) null, (Element) null);
	}

	public static NonInteractiveSigmaChallengeGenerator getInstance(Function function, RandomOracle randomOracle) {
		return NonInteractiveSigmaChallengeGenerator.getInstance(function, randomOracle, (Element) null);
	}

	public static NonInteractiveSigmaChallengeGenerator getInstance(Function function, Element proverID) {
		return NonInteractiveSigmaChallengeGenerator.getInstance(function, (RandomOracle) null, proverID);
	}

	public static NonInteractiveSigmaChallengeGenerator getInstance(Function function, RandomOracle randomOracle, Element proverID) {
		if (function == null || !function.getCoDomain().isSemiGroup()) {
			throw new IllegalArgumentException();
		}
		if (randomOracle == null) {
			randomOracle = PseudoRandomOracle.DEFAULT;
		}
		return new NonInteractiveSigmaChallengeGenerator(
					 function.getCoDomain(), (SemiGroup) function.getCoDomain(), ZMod.getInstance(function.getDomain().getMinimalOrder()), randomOracle, proverID);

	}

}
