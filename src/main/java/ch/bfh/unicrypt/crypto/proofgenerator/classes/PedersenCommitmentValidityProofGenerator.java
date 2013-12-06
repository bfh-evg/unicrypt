package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractSigmaSetMembershipProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.NonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.InvertFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public class PedersenCommitmentValidityProofGenerator
	   extends AbstractSigmaSetMembershipProofGenerator<CyclicGroup, Element> {

	private final PedersenCommitmentScheme pedersenCS;

	protected PedersenCommitmentValidityProofGenerator(final SigmaChallengeGenerator challengeGenerator, final PedersenCommitmentScheme pedersenCS, final Element[] messages) {
		super(challengeGenerator, messages);
		this.pedersenCS = pedersenCS;
	}

	public static PedersenCommitmentValidityProofGenerator getInstance(final SigmaChallengeGenerator challengeGenerator, final PedersenCommitmentScheme pedersenCS, final Element[] messages) {
		if (challengeGenerator == null || pedersenCS == null || messages == null || messages.length < 1) {
			throw new IllegalArgumentException();
		}

		Set codomain = ProductGroup.getInstance(pedersenCS.getCommitmentFunction().getCoDomain(), messages.length);
		if (!codomain.isEqual(challengeGenerator.getPublicInputSpace()) || !codomain.isEqual(challengeGenerator.getCommitmentSpace())
			   || !pedersenCS.getCyclicGroup().getZModOrder().isEqual(challengeGenerator.getChallengeSpace())) {
			throw new IllegalArgumentException("Spaces of challenge generator don't match!");
		}
		return new PedersenCommitmentValidityProofGenerator(challengeGenerator, pedersenCS, messages);
	}

	@Override
	protected Function abstractGetSetMembershipFunction() {
		return this.pedersenCS.getCommitmentFunction();
	}

	@Override
	protected Function abstractGetDeltaFunction() {
		ProductSet deltaFunctionDomain = ProductSet.getInstance(this.pedersenCS.getMessageSpace(), this.getSetMembershipProofFunction().getCoDomain());
		Function deltaFunction =
			   CompositeFunction.getInstance(MultiIdentityFunction.getInstance(deltaFunctionDomain, 2),
											 ProductFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 1),
																		 CompositeFunction.getInstance(SelectionFunction.getInstance(deltaFunctionDomain, 0),
																									   GeneratorFunction.getInstance(this.pedersenCS.getMessageGenerator()),
																									   InvertFunction.getInstance(this.pedersenCS.getCyclicGroup()))),
											 ApplyFunction.getInstance(this.pedersenCS.getCyclicGroup()));
		return deltaFunction;
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final PedersenCommitmentScheme pedersenCS, final int numberOfMessages) {
		return PedersenCommitmentValidityProofGenerator.createNonInteractiveChallengeGenerator(pedersenCS, numberOfMessages, HashMethod.DEFAULT);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final PedersenCommitmentScheme pedersenCS, final int numberOfMessages, final Element proverId) {
		return PedersenCommitmentValidityProofGenerator.createNonInteractiveChallengeGenerator(pedersenCS, numberOfMessages, proverId, HashMethod.DEFAULT);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final PedersenCommitmentScheme pedersenCS, final int numberOfMessages, final HashMethod hashMethod) {
		return PedersenCommitmentValidityProofGenerator.createNonInteractiveChallengeGenerator(pedersenCS, numberOfMessages, (Element) null, hashMethod);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveChallengeGenerator(final PedersenCommitmentScheme pedersenCS, final int numberOfMessages, final Element proverId, final HashMethod hashMethod) {
		if (pedersenCS == null || numberOfMessages < 1 || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		Group codomain = ProductGroup.getInstance((Group) pedersenCS.getCommitmentFunction().getCoDomain(), numberOfMessages);
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(codomain, codomain, pedersenCS.getCyclicGroup().getZModOrder(), proverId, hashMethod);

	}

}
