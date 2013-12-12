package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveElementChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.ElementChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.NonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.GeneralizedPedersenCommitmentScheme;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.classes.PermutationFunction;
import ch.bfh.unicrypt.math.helper.HashMethod;
import ch.bfh.unicrypt.math.random.RandomOracle;
import java.util.Random;

//
// @see [TW10] Protocol 1: Permutation Matrix
//
public class PermutationCommitmentProofGenerator
	   extends AbstractProofGenerator<ProductGroup, Pair, ProductGroup, Tuple, ProductGroup, Pair> {

	final private SigmaChallengeGenerator sigmaChallengeGenerator;
	final private ElementChallengeGenerator eValuesGenerator;
	final private CyclicGroup cyclicGroup;
	final private int size;
	final private RandomOracle randomOracle;

	protected PermutationCommitmentProofGenerator(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ElementChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size, RandomOracle randomOracle) {

		this.sigmaChallengeGenerator = sigmaChallengeGenerator;
		this.eValuesGenerator = eValuesGenerator;
		this.cyclicGroup = cyclicGroup;
		this.size = size;
		this.randomOracle = randomOracle;
	}

	public static PermutationCommitmentProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ElementChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size) {
		return PermutationCommitmentProofGenerator.getInstance(sigmaChallengeGenerator, eValuesGenerator, cyclicGroup, size, RandomOracle.DEFAULT);
	}

	public static PermutationCommitmentProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ElementChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size, RandomOracle randomOracle) {

		// TODO Argument validation

		return new PermutationCommitmentProofGenerator(sigmaChallengeGenerator, eValuesGenerator, cyclicGroup, size, randomOracle);
	}

	// Private: (PermutationElement pi, PermutationCommitment-Randomizations sV)
	@Override
	protected ProductGroup abstractGetPrivateInputSpace() {
		return ProductGroup.getInstance(PermutationGroup.getInstance(this.size),
										ProductGroup.getInstance(this.cyclicGroup.getZModOrder(), this.size));
	}

	// Public:  (PermutationCommitment cPiV)
	@Override
	protected ProductGroup abstractGetPublicInputSpace() {
		return ProductGroup.getInstance(this.cyclicGroup, this.size);
	}

	// Proof:   (SigmaProof-Triple, public inputs for sigma proof cV)
	@Override
	protected ProductGroup abstractGetProofSpace() {
		return ProductGroup.getInstance(this.getPreimageProofSpace(),
										this.getCommitmentSpace());
	}

	public ProductGroup getPreimageProofSpace() {
		return ProductGroup.getInstance(this.getCommitmentSpace(),
										this.getChallengeSpace(),
										this.getResponceSpace());
	}

	public ProductGroup getCommitmentSpace() {
		return ProductGroup.getInstance(this.cyclicGroup, 3 * this.size);
	}

	public ZMod getChallengeSpace() {
		return this.cyclicGroup.getZModOrder();
	}

	public ProductGroup getResponceSpace() {
		return ProductGroup.getInstance(cyclicGroup.getZModOrder(),
										cyclicGroup.getZModOrder(),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size - 1),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size - 1),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size));
	}

	@Override
	protected Pair abstractGenerate(Pair privateInput, Tuple publicInput, Random random) {

		// Unfold privat and public input
		final PermutationElement pi = (PermutationElement) privateInput.getFirst();
		final Tuple sV = (Tuple) privateInput.getSecond();
		final Tuple eV = this.eValuesGenerator.generate(publicInput);

		// Compute private values for sigma proof
		final Tuple oneV = PermutationCommitmentProofGenerator.createOneVector(this.cyclicGroup.getZModOrder(), this.size);
		final Element v = PermutationCommitmentProofGenerator.computeInnerProduct(oneV, sV);
		final Element w = PermutationCommitmentProofGenerator.computeInnerProduct(sV, eV);
		final Tuple ePrimeV = PermutationFunction.getInstance(eV.getSet()).apply(eV, pi);
		final Tuple rV = ProductGroup.getInstance(this.cyclicGroup.getZModOrder(), this.size - 1).getRandomElement(random);
		final Element[] rPrimes = ProductGroup.getInstance(this.cyclicGroup.getZModOrder(), this.size).getRandomElement(random).getAll();
		// Set r'_N = 0
		rPrimes[this.size - 1] = this.cyclicGroup.getZModOrder().getIdentityElement();
		final Tuple rPrimeV = Tuple.getInstance(rPrimes);
		final Tuple rPPrimeV = PermutationCommitmentProofGenerator.computeRPrimePrime(ePrimeV, rPrimeV);

		// Compute commitments c'_1,...,c'_N
		final PedersenCommitmentScheme pcs = PedersenCommitmentScheme.getInstance(this.cyclicGroup, this.randomOracle);
		final Element[] cPrimes = new Element[this.size];
		final Element[] ePrimeMuls = new Element[this.size];
		Element eProd = this.cyclicGroup.getZModOrder().getElement(1);
		for (int i = 0; i < this.size; i++) {
			eProd = eProd.selfApply(ePrimeV.getAt(i));
			ePrimeMuls[i] = eProd;
			cPrimes[i] = pcs.commit(eProd, rPrimeV.getAt(i));
		}
		final Tuple ePrimeMulV = Tuple.getInstance(ePrimeMuls);
		final Tuple cPrimeV = Tuple.getInstance(cPrimes);

		// Create sigma proof
		PreimageProofFunction f = new PreimageProofFunction(this.cyclicGroup, this.size, this.getResponceSpace(), this.getCommitmentSpace(), this.randomOracle, cPrimeV);
		Tuple cV = f.apply(Tuple.getInstance(v, w, oneV, rV, rPrimeV, rPPrimeV, ePrimeV, ePrimeMulV));
		PreimageProofGenerator ppg = PreimageProofGenerator.getInstance(this.sigmaChallengeGenerator, f);
		Triple preimageProof = ppg.generate(Tuple.getInstance(v, w, oneV, rV, rPrimeV, rPPrimeV, ePrimeV, ePrimeMulV), cV, random);

		return Pair.getInstance(preimageProof, cV);
	}

	@Override
	protected BooleanElement abstractVerify(Pair proof, Tuple publicInput) {

		// Unfold proof
		final Triple preimageProof = (Triple) proof.getFirst();
		final Tuple cV = (Tuple) proof.getSecond();
		final Element[] cPrimeV = new Element[this.size];
		for (int i = 0; i < this.size; i++) {
			cPrimeV[i] = cV.getAt(i + this.size + 1);
		}

		// Get additional values
		final Tuple eV = this.eValuesGenerator.generate(publicInput);
		final Tuple oneV = PermutationCommitmentProofGenerator.createOneVector(this.cyclicGroup.getZModOrder(), this.size);
		final Element g1 = this.cyclicGroup.getIndependentGenerator(1, this.randomOracle);


		// 1. Verify preimage proof
		PreimageProofFunction f = new PreimageProofFunction(this.cyclicGroup, this.size, this.getResponceSpace(), this.getCommitmentSpace(), this.randomOracle, Tuple.getInstance(cPrimeV));
		PreimageProofGenerator ppg = PreimageProofGenerator.getInstance(this.sigmaChallengeGenerator, f);
		BooleanElement v = ppg.verify(preimageProof, cV);
		if (!v.getBoolean()) {
			return v;
		}

		// 2. Check correctness of cV
		// 2.1 c_1 == c_pi^1
		boolean v1 = cV.getAt(0).isEqual(PermutationCommitmentProofGenerator.computeInnerProduct(publicInput, oneV));

		// 2.2 c_2 == c_pi^e
		boolean v2 = cV.getAt(1).isEqual(PermutationCommitmentProofGenerator.computeInnerProduct(publicInput, eV));

		// 2.3 c_(2n+1) = c'_N == g1^(prod(e))
		Element eProd = eV.getAt(0);
		for (int i = 1; i < this.size; i++) {
			eProd = eProd.selfApply(eV.getAt(i));
		}
		boolean v3 = cV.getAt(2 * this.size).isEqual(g1.selfApply(eProd));

		// 2.4 c_i = c_(i+n) for n+2 < i < 2n
		boolean v4 = true;
		int offset1 = this.size + 2;
		int offset2 = offset1 + this.size - 1;
		for (int i = 0; i < this.size - 1; i++) {
			if (!cV.getAt(offset1 + i).isEqual(cV.getAt(offset2 + i))) {
				v4 = false;
				break;
			}
		}

		return BooleanSet.getInstance().getElement(v1 && v2 && v3 && v4);
	}

	// Helper to crate a one-vector
	private static Tuple createOneVector(Group group, int size) {
		if (group == null || size < 1) {
			throw new IllegalArgumentException();
		}
		final Element one = group.getElement(1);
		final Element[] vector = new Element[size];
		for (int i = 0; i < size; i++) {
			vector[i] = one;
		}
		return Tuple.getInstance(vector);
	}

	// Helper to compute the inner product
	// - Additive:       Sum(t1_i*t2_i)
	// - Multiplicative: Prod(t1_i^(t2_i))
	private static Element computeInnerProduct(Tuple t1, Tuple t2) {
		if (!t1.getSet().isGroup() || t1.getArity() < 1) {
			throw new IllegalArgumentException();
		}
		Element innerProduct = ((Group) t1.getSet().getAt(0)).getIdentityElement();
		for (int i = 0; i < t1.getArity(); i++) {
			innerProduct = innerProduct.apply(t1.getAt(i).selfApply(t2.getAt(i)));
		}
		return innerProduct;
	}

	// Helper to compute vector r''
	// rPPrim_i = rPrime_i+1 - ePrime_i+1 * rPrime_i
	private static Tuple computeRPrimePrime(Tuple ePrime, Tuple rPrime) {
		Element[] rPPrime = new Element[rPrime.getArity() - 1];
		for (int i = 0; i < rPPrime.length; i++) {
			rPPrime[i] = rPrime.getAt(i + 1).applyInverse(ePrime.getAt(i + 1).selfApply(rPrime.getAt(i)));
		}
		return Tuple.getInstance(rPPrime);
	}

	//
	// Nested class PreimageProofFunction
	//
	private class PreimageProofFunction
		   extends AbstractFunction<ProductGroup, Tuple, ProductGroup, Tuple> {

		private final CyclicGroup cyclicGroup;
		private final int size;
		private final RandomOracle randomOracle;
		private final Tuple cPrimeV;

		protected PreimageProofFunction(CyclicGroup cyclicGroup, int size, ProductGroup domain, ProductGroup coDomain, RandomOracle randomOracle, Tuple cPrimeV) {
			super(domain, coDomain);
			this.cyclicGroup = cyclicGroup;
			this.size = size;
			this.randomOracle = randomOracle;
			this.cPrimeV = cPrimeV;
		}

		@Override
		protected Tuple abstractApply(Tuple element, Random random) {

			// Unfold element
			final Element v = element.getAt(0);
			final Element w = element.getAt(1);
			final Tuple oneV = (Tuple) element.getAt(2);
			final Tuple rV = (Tuple) element.getAt(3);
			final Tuple rPrimeV = (Tuple) element.getAt(4);
			final Tuple rPPrimeV = (Tuple) element.getAt(5);
			final Tuple ePrimeV = (Tuple) element.getAt(6);
			final Tuple ePrimeMulV = (Tuple) element.getAt(7);

			// Prepare commitment schemes
			final PedersenCommitmentScheme pcs = PedersenCommitmentScheme.getInstance(this.cyclicGroup, this.randomOracle);
			final GeneralizedPedersenCommitmentScheme gpcs = GeneralizedPedersenCommitmentScheme.getInstance(this.cyclicGroup, this.size, this.randomOracle);
			final Element g = pcs.getRandomizationGenerator();

			// Result array
			final Element[] cV = new Element[3 * this.size];

			// COMPUTE...
			// - Com(1, v)
			cV[0] = gpcs.commit(oneV, v);

			// - Com(e', w)
			cV[1] = gpcs.commit(ePrimeV, w);

			// - Com(e'_i+1, r_i)
			int offset = 2;
			for (int i = 0; i < this.size - 1; i++) {
				cV[i + offset] = pcs.commit(ePrimeV.getAt(i + 1), rV.getAt(i));
			}

			// - Com(e'_1 * * * e'_i, r'_i)
			offset += this.size - 1;
			for (int i = 0; i < this.size; i++) {
				cV[i + offset] = pcs.commit(ePrimeMulV.getAt(i), rPrimeV.getAt(i));
			}

			// - g^(r''_i) * c'_i^(e'_i+1)
			offset += this.size;
			for (int i = 0; i < this.size - 1; i++) {
				Element a1 = g.selfApply(rPPrimeV.getAt(i));
				Element a2 = this.cPrimeV.getAt(i).selfApply(ePrimeV.getAt(i + 1));
				cV[i + offset] = a1.apply(a2);
			}

			return Tuple.getInstance(cV);
		}

	}

	//===================================================================================
	// Service functions to create non-interactive Sigma- and Element-ChallengeGenerator
	//
	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size) {
		return PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size, HashMethod.DEFAULT);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final HashMethod hashMethod) {
		return PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size, (Element) null, hashMethod);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final Element proverId) {
		return PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size, proverId, HashMethod.DEFAULT);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final Element proverId, final HashMethod hashMethod) {
		if (cyclicGroup == null || size < 1 || hashMethod == null) {
			throw new IllegalArgumentException();
		}

		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(ProductGroup.getInstance(cyclicGroup, 3 * size),
																		 ProductGroup.getInstance(cyclicGroup, 3 * size),
																		 cyclicGroup.getZModOrder(),
																		 proverId,
																		 hashMethod);

	}

	public static StandardNonInteractiveElementChallengeGenerator createNonInteractiveElementChallengeGenerator(final CyclicGroup cyclicGroup, final int size) {
		return PermutationCommitmentProofGenerator.createNonInteractiveElementChallengeGenerator(cyclicGroup, size, RandomOracle.DEFAULT);
	}

	public static StandardNonInteractiveElementChallengeGenerator createNonInteractiveElementChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final HashMethod hashMethod) {
		return PermutationCommitmentProofGenerator.createNonInteractiveElementChallengeGenerator(cyclicGroup, size, RandomOracle.DEFAULT, hashMethod);
	}

	public static StandardNonInteractiveElementChallengeGenerator createNonInteractiveElementChallengeGenerator(final CyclicGroup cyclicGroup, final int size, RandomOracle randomOracle) {
		return PermutationCommitmentProofGenerator.createNonInteractiveElementChallengeGenerator(cyclicGroup, size, randomOracle, HashMethod.DEFAULT);
	}

	public static StandardNonInteractiveElementChallengeGenerator createNonInteractiveElementChallengeGenerator(final CyclicGroup cyclicGroup, final int size, RandomOracle randomOracle, HashMethod hashMethod) {
		if (cyclicGroup == null || size < 1 || randomOracle == null || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return StandardNonInteractiveElementChallengeGenerator.getInstance(ProductGroup.getInstance(cyclicGroup, size), cyclicGroup.getZModOrder(), size, randomOracle, hashMethod);
	}

}
