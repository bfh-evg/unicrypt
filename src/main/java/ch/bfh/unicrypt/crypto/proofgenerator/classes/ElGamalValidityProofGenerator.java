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

	protected ElGamalValidityProofGenerator(Function oneWayFunction, Function deltaFunction, Tuple members, HashMethod hashMethod) {
		super(oneWayFunction, deltaFunction, members, hashMethod);
	}

	public static ElGamalValidityProofGenerator getInstance(ElGamalEncryptionScheme elGamalES, Element publicKey, Tuple plaintexts) {
		return ElGamalValidityProofGenerator.getInstance(elGamalES, publicKey, plaintexts, HashMethod.DEFAULT);
	}

	public static ElGamalValidityProofGenerator getInstance(ElGamalEncryptionScheme elGamalES, Element publicKey, Tuple plaintexts, HashMethod hashMethod) {
		if (elGamalES == null || publicKey == null || !elGamalES.getCyclicGroup().contains(publicKey) || plaintexts == null || plaintexts.getArity() < 1 || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		Function oneWayFunction = elGamalES.getEncryptionFunction().partiallyApply(publicKey, 0);

		CyclicGroup elGamalCyclicGroup = elGamalES.getCyclicGroup();
		ProductSet deltaFunctionDomain = ProductSet.getInstance(elGamalCyclicGroup, oneWayFunction.getCoDomain());
		Function deltaFunction =
			   CompositeFunction.getInstance(MultiIdentityFunction.getInstance(deltaFunctionDomain, 2),
											 ProductFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 1, 0),
																		 CompositeFunction.getInstance(MultiIdentityFunction.getInstance(deltaFunctionDomain, 2),
																									   ProductFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 1, 1),
																																   CompositeFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 0),
																																								 InvertFunction.getInstance(elGamalCyclicGroup))),
																									   ApplyFunction.getInstance(elGamalCyclicGroup))));

		return new ElGamalValidityProofGenerator(oneWayFunction, deltaFunction, plaintexts, hashMethod);
	}

}
