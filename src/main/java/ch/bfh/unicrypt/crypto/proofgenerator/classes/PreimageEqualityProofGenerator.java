package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractPreimageProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class PreimageEqualityProofGenerator
	   extends AbstractPreimageProofGenerator<SemiGroup, Element, ProductSemiGroup, Tuple, Function> {

	protected PreimageEqualityProofGenerator(final SigmaChallengeGenerator challengeGenerator, final Function proofFunction) {
		super(challengeGenerator, proofFunction);
	}

	public static PreimageEqualityProofGenerator getInstance(final SigmaChallengeGenerator challengeGenerator, final Function... proofFunctions) {
		if (challengeGenerator == null || proofFunctions == null || proofFunctions.length < 1
			   || !proofFunctions[0].getDomain().isSemiGroup() || !proofFunctions[0].getCoDomain().isSemiGroup()) {
			throw new IllegalArgumentException();
		}

		Set domain = proofFunctions[0].getDomain();
		for (int i = 1;
			   i < proofFunctions.length;
			   i++) {
			if (!domain.isEqual(proofFunctions[i].getDomain())) {
				throw new IllegalArgumentException("All proof functions must have the same domain!");
			}
		}

		Function proofFunction = CompositeFunction.getInstance(
			   MultiIdentityFunction.getInstance(domain, proofFunctions.length),
			   ProductFunction.getInstance(proofFunctions));

		// TODO check space equality of proofFunction and challengeGenerator!
		return new PreimageEqualityProofGenerator(challengeGenerator, proofFunction);

	}

	public Function[] getProofFunctions() {
		return ((ProductFunction) ((CompositeFunction) this.getPreimageProofFunction()).getAt(1)).getAll();
	}

}
