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
		if (proofFunction == null || challengeGenerator == null) {
			throw new IllegalArgumentException();
		}
		// TODO check space equality of proofFunction and challengeGenerator!
		return new PreimageProofGenerator(challengeGenerator, proofFunction);
	}

}
