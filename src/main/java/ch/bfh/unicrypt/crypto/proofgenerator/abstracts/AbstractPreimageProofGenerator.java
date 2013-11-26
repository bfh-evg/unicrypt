package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.function.classes.ModuloFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.Random;

public abstract class AbstractPreimageProofGenerator<PRS extends SemiGroup, PUS extends SemiGroup, F extends Function, PUE extends Element, PRE extends Element>
	   extends AbstractProofGenerator<PRS, PUS, ProductSet, Tuple> {

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
		// TODO Change it! It's only correct if the private input space is not a product set!
		return ZMod.getInstance(this.getPrivateInputSpace().getMinOrder());
	}

	public final PRS getResponseSpace() {
		return this.getPrivateInputSpace();
	}

	public final PUE getCommitment(final Tuple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return (PUE) proof.getAt(0);
	}

	public final ZModElement getChallenge(final Tuple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return (ZModElement) proof.getAt(1);
	}

	public final PRE getResponse(final Tuple proof) {
		if (!this.getProofSpace().contains(proof)) {
			throw new IllegalArgumentException();
		}
		return (PRE) proof.getAt(2);
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
	protected final Tuple abstractGenerate(final Element secretInput, final Element publicInput, final Element proverID, final Random random) {

		final Element randomElement = this.getResponseSpace().getRandomElement(random);
		final Element commitment = this.getProofFunction().apply(randomElement);
		final Element challenge = this.createChallenge(commitment, publicInput, proverID);
		final Element response = randomElement.apply(secretInput.selfApply(challenge));
		return this.getProofSpace().getElement(commitment, response);
	}

	protected ZModElement createChallenge(final Element commitment, final Element publicInput, final Element proverId) {
		Tuple toHash = (proverId == null
			   ? Tuple.getInstance(publicInput, commitment)
			   : Tuple.getInstance(publicInput, commitment, proverId));

		FiniteByteArrayElement hashValue = toHash.getHashValue(hashMethod);
		return ModuloFunction.getInstance(hashValue.getSet(), this.getChallengeSpace()).apply(hashValue);
	}

	@Override
	protected final BooleanElement abstractVerify(final Element proof, final Element publicInput, final Element proverID) {

		final Tuple proofT = (Tuple) proof;
		final Element challenge = this.createChallenge(this.getCommitment(proofT), publicInput, proverID);
		final Element left = this.getProofFunction().apply(this.getResponse(proofT));
		final Element right = this.getCommitment(proofT).apply(publicInput.selfApply(challenge));
		return BooleanSet.getInstance().getElement(left.isEqual(right));
	}

}
