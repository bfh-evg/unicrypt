package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractStandardPreimageProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class StandardPreimageProofGenerator
	   extends AbstractStandardPreimageProofGenerator<SemiGroup, Element, SemiGroup, Element, Function> {

	protected StandardPreimageProofGenerator(final Function function, HashMethod hashMethod) {
		super(function, hashMethod);
	}

	public static StandardPreimageProofGenerator getInstance(Function proofFunction) {
		return StandardPreimageProofGenerator.getInstance(proofFunction, HashMethod.DEFAULT);
	}

	public static StandardPreimageProofGenerator getInstance(Function proofFunction, HashMethod hashMethod) {
		if (proofFunction == null || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new StandardPreimageProofGenerator(proofFunction, hashMethod);
	}

}
