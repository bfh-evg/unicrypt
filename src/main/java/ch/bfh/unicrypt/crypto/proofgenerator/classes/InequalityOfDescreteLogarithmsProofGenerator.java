package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.SigmaProofGenerator;
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
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.ApplyInverseFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

public class InequalityOfDescreteLogarithmsProofGenerator
	   extends AbstractProofGenerator<SemiGroup, Element, ProductGroup, Pair, Set, Pair> {

	private final SigmaChallengeGenerator challengeGenerator;
	private final Element firstGenerator;
	private final Element secondGenerator;
	private final CyclicGroup cyclicGroup;

	protected InequalityOfDescreteLogarithmsProofGenerator(final SigmaChallengeGenerator challengeGenerator, final Element firstGenerator, final Element secondGenerator) {
		this.challengeGenerator = challengeGenerator;
		this.firstGenerator = firstGenerator;
		this.secondGenerator = secondGenerator;
		this.cyclicGroup = (CyclicGroup) firstGenerator.getSet();
	}

	public static InequalityOfDescreteLogarithmsProofGenerator getInstance(final SigmaChallengeGenerator challengeGenerator, final Element firstGenerator, final Element secondGenerator) {
		if (challengeGenerator == null || firstGenerator == null || secondGenerator == null || !firstGenerator.getSet().isEqual(secondGenerator.getSet())
			   || !firstGenerator.getSet().isCyclic() || !firstGenerator.isGenerator() || !secondGenerator.isGenerator()) {
			throw new IllegalArgumentException();
		}

		return new InequalityOfDescreteLogarithmsProofGenerator(challengeGenerator, firstGenerator, secondGenerator);
	}

	@Override
	protected SemiGroup abstractGetPrivateInputSpace() {
		return this.getCyclicGroup().getZModOrder();
	}

	@Override
	protected ProductGroup abstractGetPublicInputSpace() {
		return ProductGroup.getInstance(this.getCyclicGroup(), 2);
	}

	@Override
	protected ProductSet abstractGetProofSpace() {
		return ProductSet.getInstance(this.getPreimageProofSpace(), this.getCyclicGroup());
	}

	public ZMod getChallengeSpace() {
		return this.getCyclicGroup().getZModOrder();
	}

	public ProductSet getPreimageProofSpace() {
		return ProductSet.getInstance(ProductGroup.getInstance(this.getCyclicGroup(), 2),
									  this.getChallengeSpace(),
									  ProductGroup.getInstance(this.getPrivateInputSpace(), 2));
	}

	public Triple getPreimageProof(Pair proof) {
		return (Triple) proof.getFirst();
	}

	public Element getProofCommitment(Pair proof) {
		return proof.getSecond();
	}

	public CyclicGroup getCyclicGroup() {
		return this.cyclicGroup;
	}

	public Element getFirstGenerator() {
		return this.firstGenerator;
	}

	public Element getSecondGenerator() {
		return this.secondGenerator;
	}

	public SigmaChallengeGenerator getChallengeGenerator() {
		return this.challengeGenerator;
	}

	@Override
	protected Pair abstractGenerate(Element privateInput, Pair publicInput, Random random) {

		// 1. Create commitment C = (h^x/z)^r with random r
		Element r = this.getCyclicGroup().getZModOrder().getRandomElement(random);
		Element h = this.getSecondGenerator();
		Element x = privateInput;
		Element z = publicInput.getSecond();

		Element c = h.selfApply(x).apply(z.invert()).selfApply(r);

		// 2. Create preimage proof:
		//    f(a,b) = (h^a/z^b, g^a/y^b) = (C,1)  // a=xr, b=r
		SigmaProofGenerator preimageProofGenerator = this.createPreimageProofGenerator(publicInput);

		Triple preimageProof = preimageProofGenerator.generate(
			   Tuple.getInstance(x.selfApply(r), r),
			   Tuple.getInstance(c, this.getCyclicGroup().getIdentityElement()));

		return Pair.getInstance(preimageProof, c);
	}

	@Override
	protected BooleanElement abstractVerify(Pair proof, Pair publicInput) {

		// 1. Verify preimage proof
		SigmaProofGenerator preimageProofGenerator = this.createPreimageProofGenerator(publicInput);
		BooleanElement v = preimageProofGenerator.verify(
			   this.getPreimageProof(proof),
			   Tuple.getInstance(this.getProofCommitment(proof), this.getCyclicGroup().getIdentityElement()));

		// 2. Check C != 1
		boolean c = !this.getProofCommitment(proof).isEqual(this.getCyclicGroup().getIdentityElement());

		return BooleanSet.getInstance().getElement(v.getBoolean() && c);
	}

	// f(a,b) = (h^a/z^b, g^a/y^b)
	private SigmaProofGenerator createPreimageProofGenerator(Pair publicInput) {
		Element g = this.getFirstGenerator();
		Element h = this.getSecondGenerator();
		Element y = publicInput.getFirst();
		Element z = publicInput.getSecond();

		Function[] functions = new Function[2];
		ProductSet domain = ProductSet.getInstance(this.getPrivateInputSpace(), 2);
		functions[0] = this.createSinglePreimageProofFunction(domain, h, z);
		functions[1] = this.createSinglePreimageProofFunction(domain, g, y);

		return PreimageEqualityProofGenerator.getInstance(this.getChallengeGenerator(), functions);
	}

	// f(a,b) = g1^a/g2^b
	private Function createSinglePreimageProofFunction(ProductSet domain, Element g1, Element g2) {
		return CompositeFunction.getInstance(MultiIdentityFunction.getInstance(domain, 2),
											 ProductFunction.getInstance(CompositeFunction.getInstance(SelectionFunction.getInstance(domain, 0),
																									   GeneratorFunction.getInstance(g1)),
																		 CompositeFunction.getInstance(SelectionFunction.getInstance(domain, 1),
																									   GeneratorFunction.getInstance(g2))),
											 ApplyInverseFunction.getInstance(this.getCyclicGroup()));
	}

}
