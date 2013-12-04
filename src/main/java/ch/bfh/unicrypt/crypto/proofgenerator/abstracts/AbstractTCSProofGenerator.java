package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.TCSProofGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.ModuloFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;

public abstract class AbstractTCSProofGenerator<PRS extends Set, PRE extends Element, PUS extends SemiGroup, PUE extends Element, F extends Function>
	   extends AbstractProofGenerator<PRS, PRE, PUS, PUE, ProductSet, Triple>
	   implements TCSProofGenerator {

	private final HashMethod hashMethod;

	protected AbstractTCSProofGenerator(HashMethod hashMethod) {
		this.hashMethod = hashMethod;
	}

	@Override
	public final F getPreimageProofFunction() {
		return this.abstractGetPreimageProofFunction();
	}

	@Override
	public final HashMethod getHashMethod() {
		return this.hashMethod;
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

	protected final ZModElement createChallenge(final Element commitment, final Element publicInput, final Element proverId) {
		Tuple toHash = (proverId == null
			   ? Tuple.getInstance(publicInput, commitment)
			   : Tuple.getInstance(publicInput, commitment, proverId));
		FiniteByteArrayElement hashValue = toHash.getHashValue(this.getHashMethod());
		return ModuloFunction.getInstance(hashValue.getSet(), this.getChallengeSpace()).apply(hashValue);
	}

	protected abstract F abstractGetPreimageProofFunction();

}
