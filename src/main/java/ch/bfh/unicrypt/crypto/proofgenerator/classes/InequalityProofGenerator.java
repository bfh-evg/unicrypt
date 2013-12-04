package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.TCSProofGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.ApplyInverseFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.Random;

public class InequalityProofGenerator
	   extends AbstractProofGenerator<SemiGroup, Element, ProductGroup, Pair, Set, Pair> {

	private final Function firstFunction;
	private final Function secondFunction;
	private final HashMethod hashMethod;

	protected InequalityProofGenerator(Function firstFunction, Function secondFunction, HashMethod hashMethod) {
		this.firstFunction = firstFunction;
		this.secondFunction = secondFunction;
		this.hashMethod = hashMethod;
	}

	public static InequalityProofGenerator getInstance(Function firstFunction, Function secondFunction) {
		return InequalityProofGenerator.getInstance(firstFunction, secondFunction, HashMethod.DEFAULT);
	}

	public static InequalityProofGenerator getInstance(Function firstFunction, Function secondFunction, HashMethod hashMethod) {
		if (firstFunction == null || secondFunction == null || !firstFunction.getDomain().isEqual(secondFunction.getDomain())
			   || !firstFunction.getCoDomain().isCyclic() || !secondFunction.getCoDomain().isCyclic() || hashMethod == null) {
			throw new IllegalArgumentException();
		}

		return new InequalityProofGenerator(firstFunction, secondFunction, hashMethod);
	}

	@Override
	protected SemiGroup abstractGetPrivateInputSpace() {
		return (SemiGroup) this.getFirstFunction().getDomain();
	}

	@Override
	protected ProductGroup abstractGetPublicInputSpace() {
		return ProductGroup.getInstance((Group) this.getFirstFunction().getCoDomain(), (Group) this.getSecondFunction().getCoDomain());
	}

	@Override
	protected ProductSet abstractGetProofSpace() {
		return ProductSet.getInstance(this.getPreimageProofSpace(), this.getSecondFunction().getCoDomain());
	}

	public ZMod getChallengeSpace() {
		return this.getFirstFunction().getDomain().getZModOrder();
	}

	public ProductSet getPreimageProofSpace() {
		return ProductSet.getInstance(ProductGroup.getInstance(this.getFirstFunction().getCoDomain(), this.getSecondFunction().getCoDomain()),
									  this.getChallengeSpace(),
									  ProductGroup.getInstance(this.getPrivateInputSpace(), 2));
	}

	public Triple getPreimageProof(Pair proof) {
		return (Triple) proof.getFirst();
	}

	public Element getProofCommitment(Pair proof) {
		return proof.getSecond();
	}

	public Function getFirstFunction() {
		return this.firstFunction;
	}

	public Function getSecondFunction() {
		return this.secondFunction;
	}

	public HashMethod getHashMethod() {
		return this.hashMethod;
	}

	@Override
	protected Pair abstractGenerate(Element privateInput, Pair publicInput, Element proverId, Random random) {

		// 1. Create commitment C = (h^x/z)^r with random r
		Element r = this.getSecondFunction().getCoDomain().getZModOrder().getRandomElement(random);
		Element x = privateInput;
		Element z = publicInput.getSecond();

		Element c = this.secondFunction.apply(x).apply(z.invert()).selfApply(r);

		// 2. Create preimage proof:
		//    f(a,b) = (h^a/z^b, g^a/y^b) = (C,1)  // a=xr, b=r
		TCSProofGenerator preimageProofGenerator = this.createPreimageProofGenerator(publicInput);

		Triple preimageProof = preimageProofGenerator.generate(
			   Tuple.getInstance(x.selfApply(r), r),
			   Tuple.getInstance(c, ((CyclicGroup) this.getFirstFunction().getCoDomain()).getIdentityElement()),
			   proverId);

		return Pair.getInstance(preimageProof, c);
	}

	@Override
	protected BooleanElement abstractVerify(Pair proof, Pair publicInput, Element proverId) {

		// 1. Verify preimage proof
		TCSProofGenerator preimageProofGenerator = this.createPreimageProofGenerator(publicInput);
		BooleanElement v = preimageProofGenerator.verify(
			   this.getPreimageProof(proof),
			   Tuple.getInstance(this.getProofCommitment(proof), ((CyclicGroup) this.getFirstFunction().getCoDomain()).getIdentityElement()),
			   proverId);

		// 2. Check C != 1
		boolean c = !this.getProofCommitment(proof).isEqual(((CyclicGroup) this.getFirstFunction().getCoDomain()).getIdentityElement());

		return BooleanSet.getInstance().getElement(v.getBoolean() && c);
	}

	// f(a,b) = (f2(a)/z^b, f2(a)/y^b)
	private TCSProofGenerator createPreimageProofGenerator(Pair publicInput) {

		Element y = publicInput.getFirst();
		Element z = publicInput.getSecond();

		Function[] functions = new Function[2];
		ProductSet domain = ProductSet.getInstance(this.getPrivateInputSpace(), 2);
		functions[0] = this.createSinglePreimageProofFunction(domain, this.getSecondFunction(), z);
		functions[1] = this.createSinglePreimageProofFunction(domain, this.getFirstFunction(), y);

		return PreimageEqualityProofGenerator.getInstance(functions, this.getHashMethod());
	}

	// f(a,b) = f(a)/y^b
	private Function createSinglePreimageProofFunction(ProductSet domain, Function f, Element y) {
		return CompositeFunction.getInstance(MultiIdentityFunction.getInstance(domain, 2),
											 ProductFunction.getInstance(CompositeFunction.getInstance(SelectionFunction.getInstance(domain, 0),
																									   f),
																		 CompositeFunction.getInstance(SelectionFunction.getInstance(domain, 1),
																									   MultiIdentityFunction.getInstance(domain.getAt(1), 1),
																									   SelfApplyFunction.getInstance((Group) y.getSet()).partiallyApply(y, 0))),
											 ApplyInverseFunction.getInstance((Group) f.getCoDomain()));
	}

}
