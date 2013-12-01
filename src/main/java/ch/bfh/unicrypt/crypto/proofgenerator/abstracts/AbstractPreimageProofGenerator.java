package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.function.classes.ModuloFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.Random;

public abstract class AbstractPreimageProofGenerator<PRS extends SemiGroup, PRE extends Element, PUS extends SemiGroup, PUE extends Element, F extends Function>
			 extends AbstractProofGenerator<PRS, PRE, PUS, PUE, ProductSet, Triple> {

	private final F proofFunction;
	private final HashMethod hashMethod;

	protected AbstractPreimageProofGenerator(F proofFunction, HashMethod hashMethod) {
		this.proofFunction = proofFunction;
		this.hashMethod = hashMethod;
	}

	public final F getProofFunction() {
		return this.proofFunction;
	}

	public final HashMethod getHashMethod() {
		return this.hashMethod;
	}

	public final PUS getCommitmentSpace() {
		return this.getPublicInputSpace();
	}

	public final ZMod getChallengeSpace() {
		return ZMod.getInstance(this.getPrivateInputSpace().getMinimalOrder());
	}

	public final PRS getResponseSpace() {
		return this.getPrivateInputSpace();
	}

	public final PUE getCommitment(final Triple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return (PUE) proof.getFirst();
	}

	public final ZModElement getChallenge(final Triple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return (ZModElement) proof.getSecond();
	}

	public final PRE getResponse(final Triple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return (PRE) proof.getThird();
	}

	@Override
	protected final PRS abstractGetPrivateInputSpace() {
		return (PRS) this.getProofFunction().getDomain();
	}

	@Override
	protected final PUS abstractGetPublicInputSpace() {
		return (PUS) this.getProofFunction().getCoDomain();
	}

	@Override
	protected final ProductSet abstractGetProofSpace() {
		return ProductSet.getInstance(this.getCommitmentSpace(), this.getChallengeSpace(), this.getResponseSpace());
	}

	@Override
	protected final Triple abstractGenerate(final Element secretInput, final Element publicInput, final Element proverID, final Random random) {
		final Element randomElement = this.getResponseSpace().getRandomElement(random);
		final Element commitment = this.getProofFunction().apply(randomElement);
		final Element challenge = this.createChallenge(commitment, publicInput, proverID);
		final Element response = randomElement.apply(secretInput.selfApply(challenge));
		return (Triple) this.getProofSpace().getElement(commitment, challenge, response);
	}

	protected ZModElement createChallenge(final Element commitment, final Element publicInput, final Element proverId) {
		Tuple toHash = (proverId == null
					 ? Tuple.getInstance(publicInput, commitment)
					 : Tuple.getInstance(publicInput, commitment, proverId));
		FiniteByteArrayElement hashValue = toHash.getHashValue(this.getHashMethod());
		return ModuloFunction.getInstance(hashValue.getSet(), this.getChallengeSpace()).apply(hashValue);
	}

	@Override
	protected final BooleanElement abstractVerify(final Triple proof, final Element publicInput, final Element proverID) {
		final Element challenge = this.createChallenge(this.getCommitment(proof), publicInput, proverID);
		final Element left = this.getProofFunction().apply(this.getResponse(proof));
		final Element right = this.getCommitment(proof).apply(publicInput.selfApply(challenge));
		return BooleanSet.getInstance().getElement(left.isEqual(right));
	}

}
