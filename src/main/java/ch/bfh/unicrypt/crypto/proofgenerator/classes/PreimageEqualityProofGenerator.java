package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractPreimageProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class PreimageEqualityProofGenerator
	   extends AbstractPreimageProofGenerator<SemiGroup, ProductSemiGroup, ProductFunction, Tuple, Element> {

	protected PreimageEqualityProofGenerator(final ProductFunction proofFunction, HashMethod hashMethod) {
		super(proofFunction, hashMethod);
	}

	public static PreimageAndProofGenerator getInstance(final Function... proofFunctions) {
		return PreimageAndProofGenerator.getInstance(proofFunctions, HashMethod.DEFAULT);
	}

	public static PreimageAndProofGenerator getInstance(final Function[] proofFunctions, final HashMethod hashMethod) {
		if (hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new PreimageAndProofGenerator(ProductFunction.getInstance(proofFunctions), hashMethod);
	}

	public Function[] getProofFunctions() {
		return this.getProofFunction().getAll();
	}

}
