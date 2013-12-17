package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractPreimageProofGenerator<PRS extends SemiGroup, PRE extends Element, PUS extends SemiGroup, PUE extends Element, F extends Function>
			 extends AbstractSigmaProofGenerator<PRS, PRE, PUS, PUE, F> {

	private final F preimageProofFunction;

	protected AbstractPreimageProofGenerator(final SigmaChallengeGenerator challengeGenerator, final F function) {
		super(challengeGenerator);
		this.preimageProofFunction = function;
	}

	@Override
	protected final PRS abstractGetPrivateInputSpace() {
		return (PRS) this.getPreimageProofFunction().getDomain();
	}

	@Override
	protected final PUS abstractGetPublicInputSpace() {
		return (PUS) this.getPreimageProofFunction().getCoDomain();
	}

	@Override
	protected final ProductSet abstractGetProofSpace() {
		return ProductSet.getInstance(this.getCommitmentSpace(), this.getChallengeSpace(), this.getResponseSpace());
	}

	@Override
	protected F abstractGetPreimageProofFunction() {
		return this.preimageProofFunction;
	}

	@Override
	protected final Triple abstractGenerate(final Element secretInput, final Element publicInput, final RandomGenerator randomGenerator) {
		final Element randomElement = this.getResponseSpace().getRandomElement(randomGenerator);
		final Element commitment = this.getPreimageProofFunction().apply(randomElement);
		final Element challenge = this.getChallengeGenerator().generate(publicInput, commitment);
		final Element response = randomElement.apply(secretInput.selfApply(challenge));
		return (Triple) this.getProofSpace().getElement(commitment, challenge, response);
	}

	@Override
	protected final BooleanElement abstractVerify(final Triple proof, final Element publicInput) {
		final Element challenge = this.getChallengeGenerator().generate(publicInput, this.getCommitment(proof));
		final Element left = this.getPreimageProofFunction().apply(this.getResponse(proof));
		final Element right = this.getCommitment(proof).apply(publicInput.selfApply(challenge));
		return BooleanSet.getInstance().getElement(left.isEqual(right));
	}

}
