package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.ChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.GeneralizedPedersenCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
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

//
//
// @see [Wik09] Protocol 2: Commitment-Consistent Proof of a Shuffle
//
public class ShuffleProofGenerator
	   extends AbstractProofGenerator<ProductGroup, Triple, ProductGroup, Triple, ProductGroup, Triple> {

	final private SigmaChallengeGenerator sigmaChallengeGenerator;
	final private ChallengeGenerator eValuesGenerator;
	final private CyclicGroup cyclicGroup;
	final private int size;
	final private Element encryptionPK;
	final private RandomReferenceString randomReferenceString;

	protected ShuffleProofGenerator(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size, Element encryptionPK, RandomReferenceString randomReferenceString) {
		this.sigmaChallengeGenerator = sigmaChallengeGenerator;
		this.eValuesGenerator = eValuesGenerator;
		this.cyclicGroup = cyclicGroup;
		this.size = size;
		this.encryptionPK = encryptionPK;
		this.randomReferenceString = randomReferenceString;
	}

	public static ShuffleProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size, Element encryptionPK) {
		return ShuffleProofGenerator.getInstance(sigmaChallengeGenerator, eValuesGenerator, cyclicGroup, size, encryptionPK, PseudoRandomReferenceString.getInstance());
	}

	public static ShuffleProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size, Element encryptionPK, RandomReferenceString randomReferenceString) {

		// TODO Argument validation
		return new ShuffleProofGenerator(sigmaChallengeGenerator, eValuesGenerator, cyclicGroup, size, encryptionPK, randomReferenceString);
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
		return this.getPreimageProofSpace();
	}

	public ProductGroup getPreimageProofSpace() {
		return ProductGroup.getInstance(this.getCommitmentSpace(),
										this.getChallengeSpace(),
										this.getResponseSpace());
	}

	public ProductGroup getCommitmentSpace() {
		return ProductGroup.getInstance(this.cyclicGroup,
										ProductGroup.getInstance(this.cyclicGroup, 2));
	}

	public ZMod getChallengeSpace() {
		return this.cyclicGroup.getZModOrder();
	}

	public ProductGroup getResponseSpace() {
		return ProductGroup.getInstance(cyclicGroup.getZModOrder(),
										cyclicGroup.getZModOrder(),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size));
	}

	@Override
	protected Triple abstractGenerate(Triple privateInput, Triple publicInput, RandomGenerator randomGenerator) {

		// Unfold private and public input
		final PermutationElement pi = (PermutationElement) privateInput.getFirst();
		final Tuple sV = (Tuple) privateInput.getSecond();
		final Tuple rV = (Tuple) privateInput.getThird();
		final Tuple uPrimeV = (Tuple) publicInput.getThird();
		final Tuple eV = (Tuple) this.eValuesGenerator.generate(publicInput);

		// Compute private values for sigma proof
		final Element r = ShuffleProofGenerator.computeInnerProduct(eV, rV);
		final Element w = ShuffleProofGenerator.computeInnerProduct(eV, sV);
		final Tuple ePrimeV = PermutationFunction.getInstance(eV.getSet()).apply(eV, pi);

		// Create sigma proof
		PreimageProofFunction f = new PreimageProofFunction(this.cyclicGroup, this.size, this.getResponseSpace(), this.getCommitmentSpace(), this.randomReferenceString, uPrimeV, this.encryptionPK);
		//Tuple cV = f.apply(Tuple.getInstance(r, w, ePrimeV));   // [3n+5]
		//PreimageProofGenerator ppg = PreimageProofGenerator.getInstance(this.sigmaChallengeGenerator, f);
		//Triple preimageProof = ppg.generate(Tuple.getInstance(r, w, ePrimeV), cV, randomGenerator);
		//return Pair.getInstance(preimageProof, cV);
		final Element randomElement = this.getResponseSpace().getRandomElement(randomGenerator);
		final Element commitment = f.apply(randomElement);
		final Element challenge = this.sigmaChallengeGenerator.generate(publicInput, commitment);
		final Element response = randomElement.apply(Tuple.getInstance(r, w, ePrimeV).selfApply(challenge));
		Triple preimageProof = (Triple) Triple.getInstance(commitment, challenge, response);

		return preimageProof;
	}

	@Override
	protected BooleanElement abstractVerify(Triple proof, Triple publicInput) {

		// Unfold proof and public input
		final Tuple commitment = (Tuple) proof.getAt(0);
		final Tuple response = (Tuple) proof.getAt(2);
		final Tuple cPiV = (Tuple) publicInput.getFirst();
		final Tuple uV = (Tuple) publicInput.getSecond();
		final Tuple uPrimeV = (Tuple) publicInput.getThird();
		final Tuple eV = (Tuple) this.eValuesGenerator.generate(publicInput);

		// Compute image of preimage proof
		final Element[] ps = new Element[2];
		// - p_1 == c_pi^e
		ps[0] = ShuffleProofGenerator.computeInnerProduct(cPiV, eV);
		// - p_2 = u
		ps[1] = ShuffleProofGenerator.computeInnerProduct(uV, eV);

		final Tuple pV = Tuple.getInstance(ps);

		// 1. Verify preimage proof
		PreimageProofFunction f = new PreimageProofFunction(this.cyclicGroup, this.size, this.getResponseSpace(), this.getCommitmentSpace(), this.randomReferenceString, uPrimeV, this.encryptionPK);
		final Element challenge = this.sigmaChallengeGenerator.generate(publicInput, commitment);
		final Element left = f.apply(response);
		final Element right = commitment.apply(pV.selfApply(challenge));
		return BooleanSet.getInstance().getElement(left.isEquivalent(right));
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

		private final int size;
		private final Tuple uPrimeV;
		private final Element encryptionPK;
		// Prepare generalized pedersen commitment scheme
		final GeneralizedPedersenCommitmentScheme gpcs;
		final Element g;

		protected PreimageProofFunction(CyclicGroup cyclicGroup, int size, ProductGroup domain, ProductGroup coDomain, RandomReferenceString randomReferenceString, Tuple uPrimeV, Element encryptionPK) {
			super(domain, coDomain);
			this.size = size;
			this.uPrimeV = uPrimeV;
			this.encryptionPK = encryptionPK;
			// Prepare generalized pedersen commitment scheme
			this.gpcs = GeneralizedPedersenCommitmentScheme.getInstance(cyclicGroup, this.size, randomReferenceString);
			this.g = cyclicGroup.getIndependentGenerator(0, randomReferenceString);
		}

		@Override
		protected Tuple abstractApply(Tuple element, RandomGenerator randomGenerator) {

			// Unfold element
			final Element r = element.getAt(0);
			final Element w = element.getAt(1);
			final Tuple ePrimeV = (Tuple) element.getAt(2);

			// Result array
			final Element[] cV = new Element[2];

			// COMPUTE...
			// - Com(e', w)                              [n+1]
			cV[0] = gpcs.commit(ePrimeV, w);

			// - Prod(u'_i^(e'_i)) * Enc(1, -r)         [2n+2]
			final Element a = ShuffleProofGenerator.computeInnerProduct(this.uPrimeV, ePrimeV);
			final Element b = Tuple.getInstance(g.selfApply(r.invert()), this.encryptionPK.selfApply(r.invert()));
			cV[1] = a.apply(b);

			//                                        ---------
			//                                          [3n+3]
			return Tuple.getInstance(cV);
		}

	}

	//===================================================================================
	// Service functions to create non-interactive SigmaChallengeGenerator and MultiChallengeGenerator
	//
	public static StandardNonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size) {
		return ShuffleProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size, (Element) null);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final Element proverId) {
		if (cyclicGroup == null || size < 1) {
			throw new IllegalArgumentException();
		}

		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(ProductGroup.getInstance(ProductGroup.getInstance(cyclicGroup, size),
																								  ProductGroup.getInstance(ProductGroup.getInstance(cyclicGroup, 2), size),
																								  ProductGroup.getInstance(ProductGroup.getInstance(cyclicGroup, 2), size)),
																		 ProductGroup.getInstance(cyclicGroup, ProductGroup.getInstance(cyclicGroup, 2)),
																		 cyclicGroup.getZModOrder(),
																		 proverId);
	}

	public static StandardNonInteractiveChallengeGenerator createNonInteractiveChallengeGenerator(final CyclicGroup cyclicGroup, final int size) {
		return ShuffleProofGenerator.createNonInteractiveChallengeGenerator(cyclicGroup, size, (RandomOracle) null);
	}

	public static StandardNonInteractiveChallengeGenerator createNonInteractiveChallengeGenerator(final CyclicGroup cyclicGroup, final int size, RandomOracle randomOracle) {
		if (cyclicGroup == null || size < 1) {
			throw new IllegalArgumentException();
		}
		ProductGroup inputSpace = ProductGroup.getInstance(ProductGroup.getInstance(cyclicGroup, size),
														   ProductGroup.getInstance(ProductGroup.getInstance(cyclicGroup, 2), size),
														   ProductGroup.getInstance(ProductGroup.getInstance(cyclicGroup, 2), size));
		return StandardNonInteractiveChallengeGenerator.getInstance(inputSpace, cyclicGroup.getZModOrder(), size, randomOracle);
	}

}
