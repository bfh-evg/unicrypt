package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.SigmaProofGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractSigmaProofGenerator<PRS extends Set, PRE extends Element, PUS extends SemiGroup, PUE extends Element, F extends Function>
	   extends AbstractProofGenerator<PRS, PRE, PUS, PUE, ProductSet, Triple>
	   implements SigmaProofGenerator {

	private final SigmaChallengeGenerator challengeGenerator;

	protected AbstractSigmaProofGenerator(final SigmaChallengeGenerator challengeGenerator) {
		this.challengeGenerator = challengeGenerator;
	}

	@Override
	public final F getPreimageProofFunction() {
		return this.abstractGetPreimageProofFunction();
	}

	@Override
	public final SigmaChallengeGenerator getChallengeGenerator() {
		return this.challengeGenerator;
	}

	@Override
	public final Set getCommitmentSpace() {
		return this.getPreimageProofFunction().getCoDomain();
	}

	@Override
	public final ZMod getChallengeSpace() {
		return ZMod.getInstance(this.getPreimageProofFunction().getDomain().getMinimalOrder());
	}

	@Override
	public final Set getResponseSpace() {
		return this.getPreimageProofFunction().getDomain();
	}

	@Override
	public final PUE getCommitment(final Triple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return (PUE) proof.getFirst();
	}

	@Override
	public final Element getChallenge(final Triple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return proof.getSecond();
	}

	@Override
	public final PRE getResponse(final Triple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return (PRE) proof.getThird();
	}

	protected abstract F abstractGetPreimageProofFunction();

}
