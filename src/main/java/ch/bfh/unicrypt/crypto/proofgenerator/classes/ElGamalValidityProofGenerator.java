package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractSetMembershipProofGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class ElGamalValidityProofGenerator
	   extends AbstractSetMembershipProofGenerator {

	protected ElGamalValidityProofGenerator(Function proofFunction, Function createProofImageFunction, Tuple members, HashMethod hashMethod) {
		super(proofFunction, createProofImageFunction, members, hashMethod);
	}

	public static ElGamalValidityProofGenerator getInstance(ElGamalEncryptionScheme elGamalES, Element publicKey, Tuple plaintexts) {
		return ElGamalValidityProofGenerator.getInstance(elGamalES, publicKey, plaintexts, HashMethod.DEFAULT);
	}

	public static ElGamalValidityProofGenerator getInstance(ElGamalEncryptionScheme elGamalES, Element publicKey, Tuple plaintexts, HashMethod hashMethod) {
		if (elGamalES == null || publicKey == null || !elGamalES.getCyclicGroup().contains(publicKey) || plaintexts == null || plaintexts.getArity() < 1 || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		Function proofFunction =
			   CompositeFunction.getInstance(MultiIdentityFunction.getInstance(elGamalES.getCyclicGroup().getZModOrder(), 1),
											 elGamalES.getIdentityEncryptionFunction().partiallyApply(publicKey, 0));

		CyclicGroup elGamalCyclicGroup = elGamalES.getCyclicGroup();
		ProductSet proofImageFunctionDomain = ProductSet.getInstance(proofFunction.getCoDomain(), elGamalCyclicGroup);
		Function createProofImageFunction =
			   CompositeFunction.getInstance(MultiIdentityFunction.getInstance(proofImageFunctionDomain, 2),
											 ProductFunction.getInstance(SelectionFunction.getInstance(proofImageFunctionDomain, 0, 0),
																		 CompositeFunction.getInstance(MultiIdentityFunction.getInstance(proofImageFunctionDomain, 2),
																									   ProductFunction.getInstance(SelectionFunction.getInstance(proofImageFunctionDomain, 0, 1),
																																   CompositeFunction.getInstance(SelectionFunction.getInstance(proofImageFunctionDomain, 1),
																																								 InvertFunction.getInstance(elGamalCyclicGroup))),
																									   ApplyFunction.getInstance(elGamalCyclicGroup))));

		return new ElGamalValidityProofGenerator(proofFunction, createProofImageFunction, plaintexts, hashMethod);
	}

}
