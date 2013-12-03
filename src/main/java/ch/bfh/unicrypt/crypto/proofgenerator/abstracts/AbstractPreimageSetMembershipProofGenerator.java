package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.classes.PreimageOrProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.SetMembershipProofGenerator;
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

public abstract class AbstractPreimageSetMembershipProofGenerator<PUS extends SemiGroup, PUE extends Element>
	   extends AbstractPreimageProofGenerator<ProductSet, Pair, PUS, PUE, Function>
	   implements SetMembershipProofGenerator {

	private final Function setMembershipProofFunction;
	private final Function deltaFunction;
	private final ProductFunction preimageProofFunction;
	private final Tuple members;
	private final PreimageOrProofGenerator orProofGenerator;

	// setMembershipProofFunction: f(x,r)
	// deltaFunction: f(x,y)
	// preiamgeProofFunction: oneWayFunction_x(r) o deltaFunction_x(y)
	protected AbstractPreimageSetMembershipProofGenerator(Function setMembershipProofFunction, Function deltaFunction, Tuple members, HashMethod hashMethod) {
		if (setMembershipProofFunction == null || !setMembershipProofFunction.getDomain().isProduct() || ((ProductSet) setMembershipProofFunction.getDomain()).getArity() != 2) {
			throw new IllegalArgumentException("Illegal oneWayFunction! OneWayFunction's doman must be a product set with arity 2 (f(x,r))");
		}
		if (deltaFunction == null || !deltaFunction.getDomain().isProduct() || ((ProductSet) deltaFunction.getDomain()).getArity() != 2) {
			throw new IllegalArgumentException("Illegal deltaFunction! DeltaFunction's doman must be a product set with arity 2 (f(x,y))");
		}
		if (members == null || members.getArity() < 1) {
			throw new IllegalArgumentException("Members must have at least arity two");
		}

		this.setMembershipProofFunction = setMembershipProofFunction;
		this.deltaFunction = deltaFunction;
		this.members = members;

		// proofFunction = composite( multiIdentity(2), productFunction(selction(0), oneWayFunction), deltaFunction)
		Function proofFunction = CompositeFunction.getInstance(MultiIdentityFunction.getInstance(setMembershipProofFunction.getDomain(), 2),
															   ProductFunction.getInstance(SelectionFunction.getInstance((ProductSet) setMembershipProofFunction.getDomain(), 0),
																						   setMembershipProofFunction),
															   deltaFunction);
		// proofFunction_x = composite( multiIdentity(1), proofFunction.partiallyApply(x, 0))
		Function[] proofFunctions = new Function[members.getArity()];
		Set rSet = ((ProductSet) setMembershipProofFunction.getDomain()).getAt(1);
		for (int i = 0; i < members.getArity(); i++) {
			proofFunctions[i] = CompositeFunction.getInstance(MultiIdentityFunction.getInstance(rSet, 1),
															  proofFunction.partiallyApply(members.getAt(i), 0));
		}
		this.preimageProofFunction = ProductFunction.getInstance(proofFunctions);

		this.orProofGenerator = PreimageOrProofGenerator.getInstance(proofFunctions, hashMethod);
	}

	@Override
	public Tuple getMembers() {
		return this.members;
	}

	@Override
	public Function getSetMembershipProofFunction() {
		return this.setMembershipProofFunction;
	}

	@Override
	protected ProductSet abstractGetPrivateInputSpace() {
		return this.orProofGenerator.getPrivateInputSpace();
	}

	@Override
	protected PUS abstractGetPublicInputSpace() {
		return (PUS) preimageProofFunction.getAt(0).getCoDomain();
	}

	@Override
	protected ProductSet abstractGetProofSpace() {
		return ProductSet.getInstance(
			   this.getCommitmentSpace(),
			   ProductSet.getInstance(this.getChallengeSpace(), members.getArity()),
			   this.getResponseSpace());
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
		final Element[] images = new Element[members.getArity()];

		for (int i = 0; i < members.getArity(); i++) {
			images[i] = this.deltaFunction.apply(members.getAt(i), publicInput);
		}
		return Tuple.getInstance(images);
	}

	@Override
	protected Function abstractGetPreimageProofFunction() {
		return this.preimageProofFunction;
	}

	@Override
	protected HashMethod abstractGetHashMethod() {
		return this.orProofGenerator.getHashMethod();
	}

}
