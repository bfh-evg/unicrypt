package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractPreimageProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class PreimageEqualityProofGenerator
			 extends AbstractPreimageProofGenerator<SemiGroup, Element, ProductSemiGroup, Tuple, Function> {

	protected PreimageEqualityProofGenerator(final Function proofFunction, HashMethod hashMethod) {
		super(proofFunction, hashMethod);
	}

	public static PreimageEqualityProofGenerator getInstance(final Function... proofFunctions) {
		return PreimageEqualityProofGenerator.getInstance(proofFunctions, HashMethod.DEFAULT);
	}

	public static PreimageEqualityProofGenerator getInstance(final Function[] proofFunctions, final HashMethod hashMethod) {
		if (hashMethod == null || proofFunctions == null || proofFunctions.length < 1) {
			throw new IllegalArgumentException();
		}
		Set domain = proofFunctions[0].getDomain();
		for (int i = 1; i < proofFunctions.length; i++) {
			if (!domain.isEqual(proofFunctions[i].getDomain())) {
				throw new IllegalArgumentException("All proof functions must have the same domain!");
			}
		}
		Function proofFunction = CompositeFunction.getInstance(
					 MultiIdentityFunction.getInstance(domain, proofFunctions.length),
					 ProductFunction.getInstance(proofFunctions));

		return new PreimageEqualityProofGenerator(proofFunction, hashMethod);
	}

	public Function[] getProofFunctions() {
		return ((ProductFunction) ((CompositeFunction) this.getProofFunction()).getAt(1)).getAll();
	}

}
