package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractPreimageProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class PreimageProofGenerator
	   extends AbstractPreimageProofGenerator<SemiGroup, Element, SemiGroup, Element, Function> {

	protected PreimageProofGenerator(final SigmaChallengeGenerator challengeGenerator, final Function function) {
		super(challengeGenerator, function);
	}

	public static PreimageProofGenerator getInstance(final SigmaChallengeGenerator challengeGenerator, final Function proofFunction) {
		if (challengeGenerator == null || proofFunction == null || !proofFunction.getDomain().isSemiGroup() || !proofFunction.getCoDomain().isSemiGroup()) {
			throw new IllegalArgumentException();
		}
		if (PreimageProofGenerator.checkSpaceEquality(challengeGenerator, proofFunction)) {
			throw new IllegalArgumentException("Spaces of challenge generator and proof function are inequal.");
		}

		return new PreimageProofGenerator(challengeGenerator, proofFunction);
	}

}
