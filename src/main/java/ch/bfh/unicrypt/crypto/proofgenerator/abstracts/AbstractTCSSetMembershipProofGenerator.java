package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.classes.PreimageOrProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.TCSSetMembershipProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.Random;

//
// setMembershipProofFunction: f(x,r)
// deltaFunction: f(x,y)
// preiamgeProofFunction: setMemebershipFunction_x(r) o deltaFunction_x(y)
//
public abstract class AbstractTCSSetMembershipProofGenerator<PUS extends SemiGroup, PUE extends Element>
	   extends AbstractTCSProofGenerator<ProductSet, Pair, PUS, PUE, ProductFunction>
	   implements TCSSetMembershipProofGenerator {

	private final Element[] members;
	private Function setMembershipProofFunction;
	private Function deltaFunction;
	private ProductFunction preimageProofFunction;
	private PreimageOrProofGenerator orProofGenerator;

	protected AbstractTCSSetMembershipProofGenerator(Element[] members, HashMethod hashMethod) {
		super(hashMethod);
		this.members = members.clone();
	}

	@Override
	public Element[] getMembers() {
		return this.members.clone();
	}

	@Override
	public Function getSetMembershipProofFunction() {
		if (this.setMembershipProofFunction == null) {
			this.setMembershipProofFunction = this.abstractGetSetMembershipFunction();
		}
		return this.setMembershipProofFunction;
	}

	@Override
	public Function getDeltaFunction() {
		if (this.deltaFunction == null) {
			this.deltaFunction = this.abstractGetDeltaFunction();
		}
		return this.deltaFunction;
	}

	@Override
	protected ProductSet abstractGetPrivateInputSpace() {
		return this.getOrProofGenerator().getPrivateInputSpace();
	}

	@Override
	protected PUS abstractGetPublicInputSpace() {
		return (PUS) this.getPreimageProofFunction().getAt(0).getCoDomain();
	}

	@Override
	protected ProductSet abstractGetProofSpace() {
		return ProductSet.getInstance(
			   this.getCommitmentSpace(),
			   ProductSet.getInstance(this.getChallengeSpace(), this.members.length),
			   this.getResponseSpace());
	}

	@Override
	protected ProductFunction abstractGetPreimageProofFunction() {
		if (this.preimageProofFunction == null) {
			this.preimageProofFunction = this.createPreimageProofFunction();
		}
		return this.preimageProofFunction;
	}

	@Override
	protected Triple abstractGenerate(Pair privateInput, PUE publicInput, Element proverId, Random random) {
		return this.getOrProofGenerator().generate(privateInput, this.createProofImages(publicInput), proverId);
	}

	@Override
	protected BooleanElement abstractVerify(Triple proof, PUE publicInput, Element proverId) {
		return this.getOrProofGenerator().verify(proof, this.createProofImages(publicInput), proverId);
	}

	public Pair createPrivateInput(Element secret, int index) {
		return (Pair) this.getOrProofGenerator().createPrivateInput(secret, index);
	}

	private Tuple createProofImages(PUE publicInput) {
		final Element[] images = new Element[this.members.length];

		for (int i = 0; i < this.members.length; i++) {
			images[i] = this.getDeltaFunction().apply(this.members[i], publicInput);
		}
		return Tuple.getInstance(images);
	}

	private PreimageOrProofGenerator getOrProofGenerator() {
		if (this.orProofGenerator == null) {
			this.orProofGenerator = PreimageOrProofGenerator.getInstance(this.getPreimageProofFunction().getAll(), this.getHashMethod());
		}
		return this.orProofGenerator;
	}

	private ProductFunction createPreimageProofFunction() {

		// proofFunction = composite( multiIdentity(2), productFunction(selction(0), setMembershipProofFunction), deltaFunction)
		final ProductSet setMembershipPFDomain = (ProductSet) this.getSetMembershipProofFunction().getDomain();
		Function proofFunction = CompositeFunction.getInstance(MultiIdentityFunction.getInstance(setMembershipPFDomain, 2),
															   ProductFunction.getInstance(SelectionFunction.getInstance(setMembershipPFDomain, 0),
																						   this.getSetMembershipProofFunction()),
															   this.getDeltaFunction());

		// proofFunction_x = composite( multiIdentity(1), proofFunction.partiallyApply(x, 0))
		Function[] proofFunctions = new Function[this.members.length];
		Set rSet = setMembershipPFDomain.getAt(1);
		for (int i = 0; i < this.members.length; i++) {
			proofFunctions[i] = CompositeFunction.getInstance(MultiIdentityFunction.getInstance(rSet, 1),
															  proofFunction.partiallyApply(this.members[i], 0));
		}

		return ProductFunction.getInstance(proofFunctions);
	}

	protected abstract Function abstractGetSetMembershipFunction();

	protected abstract Function abstractGetDeltaFunction();

}
