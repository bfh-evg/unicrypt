package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveElementChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.ElementChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.NonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.GeneralizedPedersenCommitmentScheme;
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
//
// @see [Wik09] Protocol 2: Commitment-Consistent Proof of a Shuffle
//
public class ShuffleProofGenerator
	   extends AbstractProofGenerator<ProductGroup, Triple, ProductGroup, Triple, ProductGroup, Pair> {

	final private SigmaChallengeGenerator sigmaChallengeGenerator;
	final private ElementChallengeGenerator eValuesGenerator;
	final private CyclicGroup cyclicGroup;
	final private int size;
	final private Element encryptionPK;
	final private RandomOracle randomOracle;

	protected ShuffleProofGenerator(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ElementChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size, Element encryptionPK, RandomOracle randomOracle) {

		this.sigmaChallengeGenerator = sigmaChallengeGenerator;
		this.eValuesGenerator = eValuesGenerator;
		this.cyclicGroup = cyclicGroup;
		this.size = size;
		this.encryptionPK = encryptionPK;
		this.randomOracle = randomOracle;
	}

	public static ShuffleProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ElementChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size, Element encryptionPK) {
		return ShuffleProofGenerator.getInstance(sigmaChallengeGenerator, eValuesGenerator, cyclicGroup, size, encryptionPK, RandomOracle.DEFAULT);
	}

	public static ShuffleProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ElementChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size, Element encryptionPK, RandomOracle randomOracle) {

		// TODO Argument validation

		return new ShuffleProofGenerator(sigmaChallengeGenerator, eValuesGenerator, cyclicGroup, size, encryptionPK, randomOracle);
	}

	// Private: (PermutationElement pi, PermutationCommitment-Randomizations sV, ReEncryption-Randomizations rV)
	@Override
	protected ProductGroup abstractGetPrivateInputSpace() {
		return ProductGroup.getInstance(PermutationGroup.getInstance(this.size),
										ProductGroup.getInstance(this.cyclicGroup.getZModOrder(), this.size),
										ProductGroup.getInstance(this.cyclicGroup.getZModOrder(), this.size));
	}

	// Public:  (PermutationCommitment cPiV, Input-Ciphertexts uV, Output-Ciphertexts uPrimeV)
	@Override
	protected ProductGroup abstractGetPublicInputSpace() {
		return ProductGroup.getInstance(ProductGroup.getInstance(this.cyclicGroup, this.size),
										ProductGroup.getInstance(ProductGroup.getInstance(this.cyclicGroup, 2), this.size),
										ProductGroup.getInstance(ProductGroup.getInstance(this.cyclicGroup, 2), this.size));
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
		return ProductGroup.getInstance(this.cyclicGroup,
										ProductGroup.getInstance(this.cyclicGroup, 2),
										ProductGroup.getInstance(this.cyclicGroup, 2));
	}

	public ZMod getChallengeSpace() {
		return this.cyclicGroup.getZModOrder();
	}

	public ProductGroup getResponceSpace() {
		return ProductGroup.getInstance(cyclicGroup.getZModOrder(),
										cyclicGroup.getZModOrder(),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), 2));
	}

	@Override
	protected Pair abstractGenerate(Triple privateInput, Triple publicInput, Random random) {

		// Unfold private and public input
		final PermutationElement pi = (PermutationElement) privateInput.getFirst();
		final Tuple sV = (Tuple) privateInput.getSecond();
		final Tuple rV = (Tuple) privateInput.getThird();
		final Tuple uPrimeV = (Tuple) publicInput.getThird();
		final Tuple eV = this.eValuesGenerator.generate(publicInput);

		// Compute private values for sigma proof
		final Element r = ShuffleProofGenerator.computeInnerProduct(eV, rV);
		final Element w = ShuffleProofGenerator.computeInnerProduct(eV, sV);
		final Tuple ePrimeV = PermutationFunction.getInstance(eV.getSet()).apply(eV, pi);
		final Tuple tV = ProductGroup.getInstance(cyclicGroup.getZModOrder(), 2).getRandomElement(random);

		// Create sigma proof
		PreimageProofFunction f = new PreimageProofFunction(this.cyclicGroup, this.size, this.getResponceSpace(), this.getCommitmentSpace(), this.randomOracle, uPrimeV, this.encryptionPK);
		Tuple cV = f.apply(Tuple.getInstance(r, w, ePrimeV, tV));
		PreimageProofGenerator ppg = PreimageProofGenerator.getInstance(this.sigmaChallengeGenerator, f);
		Triple preimageProof = ppg.generate(Tuple.getInstance(r, w, ePrimeV, tV), cV, random);

		return Pair.getInstance(preimageProof, cV);
	}

	@Override
	protected BooleanElement abstractVerify(Pair proof, Triple publicInput) {

		// Unfold proof and public input
		final Triple preimageProof = (Triple) proof.getFirst();
		final Tuple cV = (Tuple) proof.getSecond();
		final Tuple cPiV = (Tuple) publicInput.getFirst();
		final Tuple uV = (Tuple) publicInput.getSecond();
		final Tuple uPrimeV = (Tuple) publicInput.getThird();
		final Tuple eV = this.eValuesGenerator.generate(publicInput);

		// 1. Verify preimage proof
		PreimageProofFunction f = new PreimageProofFunction(this.cyclicGroup, this.size, this.getResponceSpace(), this.getCommitmentSpace(), this.randomOracle, uPrimeV, this.encryptionPK);
		PreimageProofGenerator ppg = PreimageProofGenerator.getInstance(this.sigmaChallengeGenerator, f);
		BooleanElement v = ppg.verify(preimageProof, cV);
		if (!v.getBoolean()) {
			return v;
		}

		// 2. Check correctness of cV
		// 2.1 c_1 == c_pi^e
		boolean v1 = cV.getAt(0).isEqual(ShuffleProofGenerator.computeInnerProduct(cPiV, eV));

		// 2.2 c_3 = c_2/u
		Element u = ShuffleProofGenerator.computeInnerProduct(uV, eV);
		boolean v2 = cV.getAt(2).isEqual(cV.getAt(1).applyInverse(u));

		return BooleanSet.getInstance().getElement(v1 && v2);
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

	//
	// Nested class PreimageProofFunction
	//
	private class PreimageProofFunction
		   extends AbstractFunction<ProductGroup, Tuple, ProductGroup, Tuple> {

		private final CyclicGroup cyclicGroup;
		private final int size;
		private final RandomOracle randomOracle;
		private final Tuple uPrimeV;
		private final Element encryptionPK;

		protected PreimageProofFunction(CyclicGroup cyclicGroup, int size, ProductGroup domain, ProductGroup coDomain, RandomOracle randomOracle, Tuple uPrimeV, Element encryptionPK) {
			super(domain, coDomain);
			this.cyclicGroup = cyclicGroup;
			this.size = size;
			this.randomOracle = randomOracle;
			this.uPrimeV = uPrimeV;
			this.encryptionPK = encryptionPK;
		}

		@Override
		protected Tuple abstractApply(Tuple element, Random random) {

			// Unfold element
			final Element r = element.getAt(0);
			final Element w = element.getAt(1);
			final Tuple ePrimeV = (Tuple) element.getAt(2);
			final Tuple tV = (Tuple) element.getAt(3);

			// Prepare generalized pedersen commitment scheme
			final GeneralizedPedersenCommitmentScheme gpcs = GeneralizedPedersenCommitmentScheme.getInstance(this.cyclicGroup, this.size, this.randomOracle);
			final Element g = this.cyclicGroup.getIndependentGenerator(0, this.randomOracle);

			// Result array
			final Element[] cV = new Element[3];

			// COMPUTE...
			// - Com(e', w)
			cV[0] = gpcs.commit(ePrimeV, w);

			// - (g^t_1, g^t_2) * Prod(u'_i^(e'_i))
			final Tuple bV = Tuple.getInstance(g.selfApply(tV.getAt(0)), g.selfApply(tV.getAt(1)));
			cV[1] = bV.apply(ShuffleProofGenerator.computeInnerProduct(this.uPrimeV, ePrimeV));

			// - (g^t_1, g^t_2) * Enc(1,r)
			cV[2] = bV.apply(Tuple.getInstance(g.selfApply(r), this.encryptionPK.selfApply(r)));

			return Tuple.getInstance(cV);
		}

	}

	//===================================================================================
	// Service functions to create non-interactive Sigma- and Element-ChallengeGenerator
	//
	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size) {
		return ShuffleProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size, HashMethod.DEFAULT);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final HashMethod hashMethod) {
		return ShuffleProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size, (Element) null, hashMethod);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final Element proverId) {
		return ShuffleProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size, proverId, HashMethod.DEFAULT);
	}

	public static NonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final Element proverId, final HashMethod hashMethod) {
		if (cyclicGroup == null || size < 1 || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		ProductGroup space = ProductGroup.getInstance(cyclicGroup,
													  ProductGroup.getInstance(cyclicGroup, 2),
													  ProductGroup.getInstance(cyclicGroup, 2));
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(space,
																		 space,
																		 cyclicGroup.getZModOrder(),
																		 proverId,
																		 hashMethod);

	}

	public static StandardNonInteractiveElementChallengeGenerator createNonInteractiveElementChallengeGenerator(final CyclicGroup cyclicGroup, final int size) {
		return ShuffleProofGenerator.createNonInteractiveElementChallengeGenerator(cyclicGroup, size, RandomOracle.DEFAULT);
	}

	public static StandardNonInteractiveElementChallengeGenerator createNonInteractiveElementChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final HashMethod hashMethod) {
		return ShuffleProofGenerator.createNonInteractiveElementChallengeGenerator(cyclicGroup, size, RandomOracle.DEFAULT, hashMethod);
	}

	public static StandardNonInteractiveElementChallengeGenerator createNonInteractiveElementChallengeGenerator(final CyclicGroup cyclicGroup, final int size, RandomOracle randomOracle) {
		return ShuffleProofGenerator.createNonInteractiveElementChallengeGenerator(cyclicGroup, size, randomOracle, HashMethod.DEFAULT);
	}

	public static StandardNonInteractiveElementChallengeGenerator createNonInteractiveElementChallengeGenerator(final CyclicGroup cyclicGroup, final int size, RandomOracle randomOracle, HashMethod hashMethod) {
		if (cyclicGroup == null || size < 1 || randomOracle == null || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		ProductGroup inputSpace = ProductGroup.getInstance(ProductGroup.getInstance(cyclicGroup, size),
														   ProductGroup.getInstance(ProductGroup.getInstance(cyclicGroup, 2), size),
														   ProductGroup.getInstance(ProductGroup.getInstance(cyclicGroup, 2), size));
		return StandardNonInteractiveElementChallengeGenerator.getInstance(inputSpace, cyclicGroup.getZModOrder(), size, randomOracle, hashMethod);
	}

}
