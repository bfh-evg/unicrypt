package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractSigmaSetMembershipProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.NonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
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

public class ElGamalEncryptionValidityProofGenerator
			 extends AbstractSigmaSetMembershipProofGenerator<ProductGroup, Pair> {

	private final ElGamalEncryptionScheme elGamalES;
	private final Element publicKey;

	protected ElGamalEncryptionValidityProofGenerator(final SigmaChallengeGenerator challengeGenerator, final ElGamalEncryptionScheme elGamalES, Element publicKey, final Subset plaintexts) {
		super(challengeGenerator, plaintexts);
		this.elGamalES = elGamalES;
		this.publicKey = publicKey;
	}

	public static ElGamalEncryptionValidityProofGenerator getInstance(final SigmaChallengeGenerator challengeGenerator, final ElGamalEncryptionScheme elGamalES, final Element publicKey, final Subset plaintexts) {
		if (challengeGenerator == null || elGamalES == null || publicKey == null || !elGamalES.getCyclicGroup().contains(publicKey)
					 || plaintexts == null || plaintexts.getOrder().intValue() < 1) {
			throw new IllegalArgumentException();
		}
		final Set codomain = ProductGroup.getInstance(elGamalES.getEncryptionFunction().getCoDomain(), plaintexts.getOrder().intValue());
		if (!codomain.isEquivalent(challengeGenerator.getPublicInputSpace()) || !codomain.isEquivalent(challengeGenerator.getCommitmentSpace())
					 || !elGamalES.getCyclicGroup().getZModOrder().isEquivalent(challengeGenerator.getChallengeSpace())) {
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
		final CyclicGroup elGamalCyclicGroup = this.elGamalES.getCyclicGroup();
		final ProductSet deltaFunctionDomain = ProductSet.getInstance(elGamalCyclicGroup, this.getSetMembershipProofFunction().getCoDomain());
		final Function deltaFunction
					 = CompositeFunction.getInstance(MultiIdentityFunction.getInstance(deltaFunctionDomain, 2),
																					 ProductFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 1, 0),
																																			 CompositeFunction.getInstance(MultiIdentityFunction.getInstance(deltaFunctionDomain, 2),
																																																		 ProductFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 1, 1),
																																																																 CompositeFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 0),
																																																																															 InvertFunction.getInstance(elGamalCyclicGroup))),
																																																		 ApplyFunction.getInstance(elGamalCyclicGroup))));
		return deltaFunction;
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final ElGamalEncryptionScheme elGamalES, final int numberOfPlaintexts) {
		return ElGamalEncryptionValidityProofGenerator.createNonInteractiveChallengeGenerator(elGamalES, numberOfPlaintexts, PseudoRandomOracle.DEFAULT);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final ElGamalEncryptionScheme elGamalES, final int numberOfPlaintexts, final Element proverID) {
		return ElGamalEncryptionValidityProofGenerator.createNonInteractiveChallengeGenerator(elGamalES, numberOfPlaintexts, proverID, PseudoRandomOracle.DEFAULT);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final ElGamalEncryptionScheme elGamalES, final int numberOfPlaintexts, final RandomOracle randomOracle) {
		return ElGamalEncryptionValidityProofGenerator.createNonInteractiveChallengeGenerator(elGamalES, numberOfPlaintexts, (Element) null, randomOracle);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final ElGamalEncryptionScheme elGamalES, final int numberOfPlaintexts, final Element proverID, final RandomOracle randomOracle) {
		if (elGamalES == null || numberOfPlaintexts < 1 || randomOracle == null) {
			throw new IllegalArgumentException();
		}
		final ProductGroup codomain = ProductGroup.getInstance((ProductGroup) elGamalES.getEncryptionFunction().getCoDomain(), numberOfPlaintexts);
		return NonInteractiveSigmaChallengeGenerator.getInstance(codomain, codomain, elGamalES.getCyclicGroup().getZModOrder(), randomOracle, proverID);
	}

}
