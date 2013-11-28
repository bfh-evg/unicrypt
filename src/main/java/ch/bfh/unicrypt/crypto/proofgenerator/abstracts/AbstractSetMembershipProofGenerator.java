package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.classes.PreimageOrProofGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.Random;

public abstract class AbstractSetMembershipProofGenerator
	   extends AbstractProofGenerator<ProductSet, SemiGroup, ProductSet, Tuple> {

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
	protected SemiGroup abstractGetPublicInputSpace() {
		return (SemiGroup) proofFunction.getCoDomain();
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

	public final Tuple getCommitments(final Tuple proof) {
		return this.orProofGenerator.getCommitments(proof);
	}

	public final Tuple getChallenges(final Tuple proof) {
		return this.orProofGenerator.getChallenges(proof);
	}

	public final Tuple getResponses(final Tuple proof) {
		return this.orProofGenerator.getResponses(proof);
	}

	public Tuple createPrivateInput(Element secret, int index) {
		return orProofGenerator.createPrivateInput(secret, index);
	}

	@Override
	protected Tuple abstractGenerate(Element privateInput, Element publicInput, Element proverId, Random random) {
		return this.orProofGenerator.generate(privateInput, this.createProofImages(publicInput), proverId);
	}

	@Override
	protected BooleanElement abstractVerify(Element proof, Element publicInput, Element proverId) {
		return this.orProofGenerator.verify(proof, this.createProofImages(publicInput), proverId);
	}

	private Tuple createProofImages(Element publicInput) {
		final Tuple[] inputs = new Tuple[members.getArity()];
		for (int i = 0; i < members.getArity(); i++) {
			inputs[i] = Tuple.getInstance(publicInput, members.getAt(i));
		}
		return (Tuple) this.createProofImagesFunction.apply(Tuple.getInstance(inputs));
	}

}
