package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.abstracts.AbstractNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class StandardNonInteractiveSigmaChallengeGenerator
	   extends AbstractNonInteractiveSigmaChallengeGenerator {

	protected StandardNonInteractiveSigmaChallengeGenerator(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, Element proverId, HashMethod hashMethod) {
		super(publicInputSpace, commitmentSpace, challengeSpace, proverId, hashMethod);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(publicInputSpace, commitmentSpace, challengeSpace, (Element) null);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, Element proverId) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(publicInputSpace, commitmentSpace, challengeSpace, (Element) proverId, HashMethod.DEFAULT);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, HashMethod hashMethod) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(publicInputSpace, commitmentSpace, challengeSpace, (Element) null, hashMethod);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Set publicInputSpace, SemiGroup commitmentSpace, ZMod challengeSpace, Element proverId, HashMethod hashMethod) {
		if (publicInputSpace == null || commitmentSpace == null || challengeSpace == null || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new StandardNonInteractiveSigmaChallengeGenerator(publicInputSpace, commitmentSpace, challengeSpace, proverId, hashMethod);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Function function) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(function, HashMethod.DEFAULT);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Function function, HashMethod hashMethod) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(function, (Element) null, hashMethod);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Function function, Element proverId) {
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(function, proverId, HashMethod.DEFAULT);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator getInstance(Function function, Element proverId, HashMethod hashMethod) {
		if (function == null || !function.getCoDomain().isSemiGroup() || hashMethod == null) {
			throw new IllegalArgumentException();
		}

		return new StandardNonInteractiveSigmaChallengeGenerator(
			   function.getCoDomain(), (SemiGroup) function.getCoDomain(), ZMod.getInstance(function.getDomain().getMinimalOrder()), proverId, hashMethod);

	}

}
