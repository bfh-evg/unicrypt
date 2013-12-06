package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractSigmaSetMembershipProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.NonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class ElGamalEncryptionValidityProofGenerator
	   extends AbstractSigmaSetMembershipProofGenerator<ProductGroup, Pair> {

	private final ElGamalEncryptionScheme elGamalES;
	private final Element publicKey;

	protected ElGamalEncryptionValidityProofGenerator(final SigmaChallengeGenerator challengeGenerator, final ElGamalEncryptionScheme elGamalES, Element publicKey, final Element[] plaintexts) {
		super(challengeGenerator, plaintexts);
		this.elGamalES = elGamalES;
		this.publicKey = publicKey;
	}

	public static ElGamalEncryptionValidityProofGenerator getInstance(final SigmaChallengeGenerator challengeGenerator, final ElGamalEncryptionScheme elGamalES, final Element publicKey, final Element[] plaintexts) {
		if (challengeGenerator == null || elGamalES == null || publicKey == null || !elGamalES.getCyclicGroup().contains(publicKey)
			   || plaintexts == null || plaintexts.length < 1) {
			throw new IllegalArgumentException();
		}
		Set codomain = ProductGroup.getInstance(elGamalES.getEncryptionFunction().getCoDomain(), plaintexts.length);
		if (!codomain.isEqual(challengeGenerator.getPublicInputSpace()) || !codomain.isEqual(challengeGenerator.getCommitmentSpace())
			   || !elGamalES.getCyclicGroup().getZModOrder().isEqual(challengeGenerator.getChallengeSpace())) {
			throw new IllegalArgumentException("Spaces of challenge generator don't match!");
		}
		return new ElGamalEncryptionValidityProofGenerator(challengeGenerator, elGamalES, publicKey, plaintexts);
	}

	@Override
	protected Function abstractGetSetMembershipFunction() {
		return this.elGamalES.getEncryptionFunction().partiallyApply(this.publicKey, 0);
	}

	@Override
	protected Function abstractGetDeltaFunction() {
		CyclicGroup elGamalCyclicGroup = this.elGamalES.getCyclicGroup();
		ProductSet deltaFunctionDomain = ProductSet.getInstance(elGamalCyclicGroup, this.getSetMembershipProofFunction().getCoDomain());
		Function deltaFunction =
			   CompositeFunction.getInstance(MultiIdentityFunction.getInstance(deltaFunctionDomain, 2),
											 ProductFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 1, 0),
																		 CompositeFunction.getInstance(MultiIdentityFunction.getInstance(deltaFunctionDomain, 2),
																									   ProductFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 1, 1),
																																   CompositeFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 0),
																																								 InvertFunction.getInstance(elGamalCyclicGroup))),
																									   ApplyFunction.getInstance(elGamalCyclicGroup))));
		return deltaFunction;
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final ElGamalEncryptionScheme elGamalES, final int numberOfPlaintexts) {
		return ElGamalEncryptionValidityProofGenerator.createNonInteractiveChallengeGenerator(elGamalES, numberOfPlaintexts, HashMethod.DEFAULT);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final ElGamalEncryptionScheme elGamalES, final int numberOfPlaintexts, final Element proverId) {
		return ElGamalEncryptionValidityProofGenerator.createNonInteractiveChallengeGenerator(elGamalES, numberOfPlaintexts, proverId, HashMethod.DEFAULT);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final ElGamalEncryptionScheme elGamalES, final int numberOfPlaintexts, final HashMethod hashMethod) {
		return ElGamalEncryptionValidityProofGenerator.createNonInteractiveChallengeGenerator(elGamalES, numberOfPlaintexts, (Element) null, hashMethod);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final ElGamalEncryptionScheme elGamalES, final int numberOfPlaintexts, final Element proverId, final HashMethod hashMethod) {
		if (elGamalES == null || numberOfPlaintexts < 1 || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		ProductGroup codomain = ProductGroup.getInstance((ProductGroup) elGamalES.getEncryptionFunction().getCoDomain(), numberOfPlaintexts);
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(codomain, codomain, elGamalES.getCyclicGroup().getZModOrder(), proverId, hashMethod);

	}

}
