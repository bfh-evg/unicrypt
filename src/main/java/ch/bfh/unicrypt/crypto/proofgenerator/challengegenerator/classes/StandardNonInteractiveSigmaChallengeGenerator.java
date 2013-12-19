package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts.AbstractNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class StandardNonInteractiveSigmaChallengeGenerator
			 extends AbstractNonInteractiveSigmaChallengeGenerator {

	protected StandardNonInteractiveSigmaChallengeGenerator(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, RandomOracle randomOracle, Element proverID) {
		super(publicInputSpace, commitmentSpace, challengeSpace, randomOracle, proverID);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(publicInputSpace, commitmentSpace, challengeSpace, (RandomOracle) null, (Element) null);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, Element proverID) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(publicInputSpace, commitmentSpace, challengeSpace, (RandomOracle) null, (Element) proverID);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, RandomOracle randomOracle) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(publicInputSpace, commitmentSpace, challengeSpace, randomOracle, (Element) null);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, RandomOracle randomOracle, Element proverID) {
		if (publicInputSpace == null || commitmentSpace == null || challengeSpace == null) {
			throw new IllegalArgumentException();
		}
		if (randomOracle == null) {
			randomOracle = PseudoRandomOracle.DEFAULT;
		}
		return new StandardNonInteractiveSigmaChallengeGenerator(publicInputSpace, commitmentSpace, challengeSpace, randomOracle, proverID);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Function function) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(function, (RandomOracle) null, (Element) null);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Function function, RandomOracle randomOracle) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(function, randomOracle, (Element) null);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Function function, Element proverID) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(function, (RandomOracle) null, proverID);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Function function, RandomOracle randomOracle, Element proverID) {
		if (function == null || !function.getCoDomain().isSemiGroup()) {
			throw new IllegalArgumentException();
		}
		if (randomOracle == null) {
			randomOracle = PseudoRandomOracle.DEFAULT;
		}
		return new StandardNonInteractiveSigmaChallengeGenerator(
					 function.getCoDomain(), (SemiGroup) function.getCoDomain(), ZMod.getInstance(function.getDomain().getMinimalOrder()), randomOracle, proverID);

	}

}
