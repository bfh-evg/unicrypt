package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.classes.PreimageOrProofGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.Random;

public abstract class AbstractSetMembershipProofGenerator<PUS extends SemiGroup, PUE extends Element>
			 extends AbstractProofGenerator<ProductSet, Pair, PUS, PUE, ProductSet, Triple> {

	private final Function proofFunction;
	private final Function createProofImagesFunction;
	private final Tuple members;
	private final PreimageOrProofGenerator orProofGenerator;

	protected AbstractSetMembershipProofGenerator(Function proofFunction, Function createProofImageFunction, Tuple members, HashMethod hashMethod) {
		this.proofFunction = proofFunction;
		this.members = members;
		this.createProofImagesFunction = ProductFunction.getInstance(createProofImageFunction, members.getArity());
		this.orProofGenerator = PreimageOrProofGenerator.getInstance(this.proofFunction, members.getArity(), hashMethod);
	}

	@Override
	protected ProductSet abstractGetPrivateInputSpace() {
		return this.orProofGenerator.getPrivateInputSpace();
	}

	@Override
	protected PUS abstractGetPublicInputSpace() {
		return (PUS) this.proofFunction.getCoDomain();
	}

	@Override
	protected ProductSet abstractGetProofSpace() {
		return ProductSet.getInstance(
					 this.getCommitmentSpace(),
					 ProductSet.getInstance(this.getChallengeSpace(), members.getArity()),
					 this.getResponseSpace());
	}

	public final ProductSet getCommitmentSpace() {
		return ProductSet.getInstance(this.proofFunction.getCoDomain(), members.getArity());
	}

	public final ProductSet getResponseSpace() {
		return ProductSet.getInstance(this.proofFunction.getDomain(), members.getArity());
	}

	public final ZMod getChallengeSpace() {
		return ZMod.getInstance(this.proofFunction.getDomain().getMinimalOrder());
	}

	public final Tuple getCommitments(final Triple proof) {
		return this.orProofGenerator.getCommitments(proof);
	}

	public final Tuple getChallenges(final Triple proof) {
		return this.orProofGenerator.getChallenges(proof);
	}

	public final Tuple getResponses(final Triple proof) {
		return this.orProofGenerator.getResponses(proof);
	}

	public Pair createPrivateInput(Element secret, int index) {
		return (Pair) this.orProofGenerator.createPrivateInput(secret, index);
	}

	@Override
	protected Triple abstractGenerate(Pair privateInput, PUE publicInput, Element proverId, Random random) {
		return this.orProofGenerator.generate(privateInput, this.createProofImages(publicInput), proverId);
	}

	@Override
	protected BooleanElement abstractVerify(Triple proof, PUE publicInput, Element proverId) {
		return this.orProofGenerator.verify(proof, this.createProofImages(publicInput), proverId);
	}

	private Tuple createProofImages(PUE publicInput) {
		final Tuple[] inputs = new Tuple[members.getArity()];
		for (int i = 0; i < members.getArity(); i++) {
			inputs[i] = Tuple.getInstance(publicInput, members.getAt(i));
		}
		return (Tuple) this.createProofImagesFunction.apply(Tuple.getInstance(inputs));
	}

}
