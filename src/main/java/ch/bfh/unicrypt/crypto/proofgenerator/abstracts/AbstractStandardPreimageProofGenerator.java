package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.Random;

public abstract class AbstractStandardPreimageProofGenerator<PRS extends SemiGroup, PRE extends Element, PUS extends SemiGroup, PUE extends Element, F extends Function>
	   extends AbstractPreimageProofGenerator<PRS, PRE, PUS, PUE, F> {

	private final F preimageProofFunction;
	private final HashMethod hashMethod;

	protected AbstractStandardPreimageProofGenerator(final F function, HashMethod hashMethod) {
		this.preimageProofFunction = function;
		this.hashMethod = hashMethod;
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
	protected HashMethod abstractGetHashMethod() {
		return this.hashMethod;
	}

	@Override
	protected final Triple abstractGenerate(final Element secretInput, final Element publicInput, final Element proverID, final Random random) {
		final Element randomElement = this.getResponseSpace().getRandomElement(random);
		final Element commitment = this.getPreimageProofFunction().apply(randomElement);
		final Element challenge = this.createChallenge(commitment, publicInput, proverID);
		final Element response = randomElement.apply(secretInput.selfApply(challenge));
		return (Triple) this.getProofSpace().getElement(commitment, challenge, response);
	}

	@Override
	protected final BooleanElement abstractVerify(final Triple proof, final Element publicInput, final Element proverID) {
		final Element challenge = this.createChallenge(this.getCommitment(proof), publicInput, proverID);
		final Element left = this.getPreimageProofFunction().apply(this.getResponse(proof));
		final Element right = this.getCommitment(proof).apply(publicInput.selfApply(challenge));
		return BooleanSet.getInstance().getElement(left.isEqual(right));
	}

}
