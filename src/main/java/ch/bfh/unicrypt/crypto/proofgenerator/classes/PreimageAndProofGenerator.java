package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractPreimageProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class PreimageAndProofGenerator
	   extends AbstractPreimageProofGenerator<ProductSemiGroup, Tuple, ProductSemiGroup, Tuple, ProductFunction> {

	protected PreimageAndProofGenerator(final SigmaChallengeGenerator challengeGenerator, final ProductFunction proofFunction) {
		super(challengeGenerator, proofFunction);
	}

	public static PreimageAndProofGenerator getInstance(final SigmaChallengeGenerator challengeGenerator, final Function... proofFunctions) {
		if (challengeGenerator == null || proofFunctions == null || proofFunctions.length < 1) {
			throw new IllegalArgumentException();
		}
		return new PreimageAndProofGenerator(challengeGenerator, ProductFunction.getInstance(proofFunctions));
	}

	public static PreimageAndProofGenerator getInstance(final SigmaChallengeGenerator challengeGenerator, final Function proofFunction, int arity) {
		if (challengeGenerator == null || proofFunction == null || arity < 1) {
			throw new IllegalArgumentException();
		}
		return new PreimageAndProofGenerator(challengeGenerator, ProductFunction.getInstance(proofFunction, arity));
	}

	public Function[] getProofFunctions() {
		return this.getPreimageProofFunction().getAll();
	}

}
