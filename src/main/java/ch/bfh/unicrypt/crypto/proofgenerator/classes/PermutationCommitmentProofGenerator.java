/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.crypto.proofgenerator.classes;

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.ChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.GeneralizedPedersenCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
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
import ch.bfh.unicrypt.random.classes.PseudoRandomOracle;
import ch.bfh.unicrypt.random.classes.ReferenceRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomOracle;

//
// @see [TW10] Protocol 1: Permutation Matrix
//
public class PermutationCommitmentProofGenerator
	   extends AbstractProofGenerator<ProductGroup, Pair, ProductGroup, Tuple, ProductGroup, Pair> {

	final private SigmaChallengeGenerator sigmaChallengeGenerator;
	final private ChallengeGenerator eValuesGenerator;
	final private CyclicGroup cyclicGroup;
	final private int size;
	final private Tuple independentGenerators;

	protected PermutationCommitmentProofGenerator(SigmaChallengeGenerator sigmaChallengeGenerator, ChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size, Tuple independentGenerators) {

		this.sigmaChallengeGenerator = sigmaChallengeGenerator;
		this.eValuesGenerator = eValuesGenerator;
		this.cyclicGroup = cyclicGroup;
		this.size = size;
		this.independentGenerators = independentGenerators;
	}

	public static PermutationCommitmentProofGenerator getInstance(CyclicGroup cyclicGroup, int size) {
		return PermutationCommitmentProofGenerator.getInstance(
			   PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size),
			   PermutationCommitmentProofGenerator.createNonInteractiveEValuesGenerator(cyclicGroup, size),
			   cyclicGroup, size, ReferenceRandomByteSequence.getInstance());
	}

	public static PermutationCommitmentProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size) {
		return PermutationCommitmentProofGenerator.getInstance(sigmaChallengeGenerator, eValuesGenerator, cyclicGroup, size, ReferenceRandomByteSequence.getInstance());
	}

	public static PermutationCommitmentProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size, ReferenceRandomByteSequence referenceRandomByteSequence) {

		if (cyclicGroup == null || size < 1 || referenceRandomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		Tuple generators = cyclicGroup.getIndependentGenerators(size, referenceRandomByteSequence);
		return PermutationCommitmentProofGenerator.getInstance(sigmaChallengeGenerator, eValuesGenerator, generators);
	}

	public static PermutationCommitmentProofGenerator getInstance(Tuple independentGenerators) {
		if (independentGenerators == null || independentGenerators.getArity() < 2 || !independentGenerators.getFirst().getSet().isCyclic()) {
			throw new IllegalArgumentException();
		}
		CyclicGroup cyclicGroup = (CyclicGroup) independentGenerators.getFirst().getSet();
		int size = independentGenerators.getArity() - 1;
		return PermutationCommitmentProofGenerator.getInstance(
			   PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size),
			   PermutationCommitmentProofGenerator.createNonInteractiveEValuesGenerator(cyclicGroup, size),
			   independentGenerators);
	}

	public static PermutationCommitmentProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, Tuple independentGenerators) {

		if (sigmaChallengeGenerator == null || eValuesGenerator == null || independentGenerators == null || independentGenerators.getArity() < 2
			   || !independentGenerators.getSet().isUniform() || !independentGenerators.getFirst().getSet().isCyclic()) {
			throw new IllegalArgumentException();
		}
		CyclicGroup cyclicGroup = (CyclicGroup) independentGenerators.getFirst().getSet();
		int size = independentGenerators.getArity() - 1;

		if (!sigmaChallengeGenerator.getPublicInputSpace().isEquivalent(PermutationCommitmentProofGenerator.createSigmaChallengeGeneratorPublicInputSpace(cyclicGroup, size))
			   || !sigmaChallengeGenerator.getCommitmentSpace().isEquivalent(PermutationCommitmentProofGenerator.createCommitmentSpace(cyclicGroup, size))
			   || !sigmaChallengeGenerator.getChallengeSpace().isEquivalent(PermutationCommitmentProofGenerator.createChallengeSpace(cyclicGroup))
			   || !eValuesGenerator.getInputSpace().isEquivalent(PermutationCommitmentProofGenerator.createEValuesGeneratorPublicInputSpace(cyclicGroup, size))
			   || !eValuesGenerator.getChallengeSpace().isEquivalent(PermutationCommitmentProofGenerator.createEValuesGeneratorChallengeSpace(cyclicGroup, size))) {
			throw new IllegalArgumentException();
		}

		return new PermutationCommitmentProofGenerator(sigmaChallengeGenerator, eValuesGenerator, cyclicGroup, size, independentGenerators);
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

	// Proof:   (SigmaProof-Triple, Bridging Commitments)
	@Override
	protected ProductGroup abstractGetProofSpace() {
		return ProductGroup.getInstance(this.getPreimageProofSpace(),
										ProductGroup.getInstance(this.cyclicGroup, this.size));
	}

	public ProductGroup getPreimageProofSpace() {
		return ProductGroup.getInstance(this.getCommitmentSpace(),
										this.getChallengeSpace(),
										this.getResponseSpace());
	}

	public ProductGroup getCommitmentSpace() {
		return PermutationCommitmentProofGenerator.createCommitmentSpace(this.cyclicGroup, this.size);
	}

	public Z getChallengeSpace() {
		return PermutationCommitmentProofGenerator.createChallengeSpace(this.cyclicGroup);
	}

	public ProductGroup getResponseSpace() {
		return ProductGroup.getInstance(cyclicGroup.getZModOrder(),
										cyclicGroup.getZModOrder(),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size),
										cyclicGroup.getZModOrder(),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size));
	}

	//===================================================================================
	// Helpers to create spaces
	//
	private static ProductGroup createCommitmentSpace(CyclicGroup cyclicGroup, int size) {
		return ProductGroup.getInstance(cyclicGroup, size + 3);
	}

	private static Z createChallengeSpace(CyclicGroup cyclicGroup) {
		return Z.getInstance(cyclicGroup.getOrder());
	}

	private static ProductGroup createSigmaChallengeGeneratorPublicInputSpace(CyclicGroup cyclicGroup, int size) {
		// (Permutation Commitment, Bridging Commitments)
		return ProductGroup.getInstance(ProductGroup.getInstance(cyclicGroup, size), 2);
	}

	private static ProductGroup createEValuesGeneratorPublicInputSpace(CyclicGroup cyclicGroup, int size) {
		// (Permutation Commitment)
		return ProductGroup.getInstance(cyclicGroup, size);
	}

	private static ProductGroup createEValuesGeneratorChallengeSpace(CyclicGroup cyclicGroup, int size) {
		return ProductGroup.getInstance(cyclicGroup.getZModOrder(), size);
	}

	//===================================================================================
	// Generate and Validate
	//
	@Override
	protected Pair abstractGenerate(Pair privateInput, Tuple publicInput, RandomByteSequence randomByteSequence) {

		// Unfold privat and public input
		final PermutationElement pi = (PermutationElement) privateInput.getFirst();
		final Tuple sV = (Tuple) privateInput.getSecond();
		final Tuple eV = (Tuple) this.eValuesGenerator.generate(publicInput);

		// Compute private values for sigma proof
		final Tuple oneV = PermutationCommitmentProofGenerator.createOneVector(this.cyclicGroup.getZModOrder(), this.size);
		final Element v = PermutationCommitmentProofGenerator.computeInnerProduct(oneV, sV);
		final Element w = PermutationCommitmentProofGenerator.computeInnerProduct(sV, eV);
		final Tuple ePrimeV = PermutationFunction.getInstance(eV.getSet()).apply(eV, pi);
		final Tuple rV = ProductGroup.getInstance(this.cyclicGroup.getZModOrder(), this.size).getRandomElement(randomByteSequence);

		// Compute commitments c_i and d
		final Element g = this.independentGenerators.getAt(0);
		final Element h = this.independentGenerators.getAt(1);

		final Element[] cs = new Element[this.size];
		final Element[] ds = new Element[this.size];
		ds[0] = rV.getAt(0);
		for (int i = 0; i < this.size; i++) {
			Element c_i_1 = i == 0 ? h : cs[i - 1];
			cs[i] = g.selfApply(rV.getAt(i)).apply(c_i_1.selfApply(ePrimeV.getAt(i)));  //   [2n]
			if (i > 0) {
				ds[i] = rV.getAt(i).apply(ePrimeV.getAt(i).selfApply(ds[i - 1]));
			}
		}
		final Tuple cV = Tuple.getInstance(cs);
		final Element d = ds[ds.length - 1];

		// Create sigma proof
		PreimageProofFunction f = new PreimageProofFunction(this.cyclicGroup, this.size, this.getResponseSpace(), this.getCommitmentSpace(), this.independentGenerators, cV);
		final Element randomElement = this.getResponseSpace().getRandomElement(randomByteSequence);
		final Element commitment = f.apply(randomElement);                              // [3n+3]
		final Element challenge = this.sigmaChallengeGenerator.generate(Pair.getInstance(publicInput, cV), commitment);
		final Element response = randomElement.apply(Tuple.getInstance(v, w, rV, d, ePrimeV).selfApply(challenge));
		Triple preimageProof = (Triple) Triple.getInstance(commitment, challenge, response);
		//                                                                                -------
		return Pair.getInstance(preimageProof, cV);                                     // [5n+3]
	}

	@Override
	protected BooleanElement abstractVerify(Pair proof, Tuple publicInput) {

		// Unfold proof
		final Triple preimageProof = (Triple) proof.getFirst();
		final Tuple commitment = (Tuple) preimageProof.getAt(0);
		final Tuple response = (Tuple) preimageProof.getAt(2);
		final Tuple cV = (Tuple) proof.getSecond();

		// Get additional values
		final Tuple eV = (Tuple) this.eValuesGenerator.generate(publicInput);
		final Tuple gV = this.independentGenerators.extract(1, this.size);

		// Compute image of preimage proof
		final Element[] ps = new Element[this.size + 3];
		// - p_0 = c_pi^1/prod(g_i) = prod(c_pi_i)/prod(g_i)
		ps[0] = this.cyclicGroup.apply(publicInput.getAll()).applyInverse(this.cyclicGroup.apply(gV.getAll()));
		// - p_1 = c_pi^e                                                                     [N]
		ps[1] = PermutationCommitmentProofGenerator.computeInnerProduct(publicInput, eV);
		// - p_2...p_(N+2) = c_1 ... c_N
		for (int i = 0; i < this.size; i++) {
			ps[i + 2] = cV.getAt(i);
		}
		// - p_(N+3) = c_N/h^(prod(e))                                                        [1]
		Element eProd = eV.getAt(0);
		for (int i = 1; i < this.size; i++) {
			eProd = eProd.selfApply(eV.getAt(i));
		}
		ps[this.size + 2] = cV.getAt(this.size - 1).applyInverse(gV.getAt(0).selfApply(eProd));
		final Tuple pV = Tuple.getInstance(ps);

		// Verify preimage proof
		PreimageProofFunction f = new PreimageProofFunction(this.cyclicGroup, this.size, this.getResponseSpace(), this.getCommitmentSpace(), this.independentGenerators, cV);
		final Element challenge = this.sigmaChallengeGenerator.generate(Pair.getInstance(publicInput, cV), commitment);
		final Element left = f.apply(response);                                         // [3N+3]
		final Element right = commitment.apply(pV.selfApply(challenge));                //  [N+3]
		//                                                                                -------
		return BooleanSet.getInstance().getElement(left.isEquivalent(right));           // [5N+7]
	}

	//===================================================================================
	// Private Helpers
	//
	// Helper to crate a one-vector
	private static Tuple createOneVector(ZMod group, int size) {
		if (group == null || size < 1) {
			throw new IllegalArgumentException();
		}
		final Element one = group.getOneElement();
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

	//===================================================================================
	// Nested class PreimageProofFunction
	//
	private class PreimageProofFunction
		   extends AbstractFunction<PreimageProofFunction, ProductGroup, Tuple, ProductGroup, Tuple> {

		private final int size;
		private final Tuple cV;
		private final GeneralizedPedersenCommitmentScheme gpcs;
		private final Element g;
		private final Element h;

		protected PreimageProofFunction(CyclicGroup cyclicGroup, int size, ProductGroup domain, ProductGroup coDomain, Tuple independentGenerators, Tuple cV) {
			super(domain, coDomain);
			this.size = size;
			this.cV = cV;
			this.g = independentGenerators.getAt(0);
			this.h = independentGenerators.getAt(1);

			this.gpcs = GeneralizedPedersenCommitmentScheme.getInstance(g, independentGenerators.extract(1, size));
		}

		@Override
		protected Tuple abstractApply(Tuple element, RandomByteSequence randomByteSequence) {

			// Unfold element
			final Element v = element.getAt(0);
			final Element w = element.getAt(1);
			final Tuple rV = (Tuple) element.getAt(2);
			final Element d = element.getAt(3);
			final Tuple ePrimeV = (Tuple) element.getAt(4);

			// Result array
			final Element[] pV = new Element[this.size + 3];

			// COMPUTE...
			// - Com(0, v)                          [1]
			pV[0] = this.gpcs.getRandomizationGenerator().selfApply(v);

			// - Com(e', w)                       [n+1]
			pV[1] = this.gpcs.commit(ePrimeV, w);

			// - g^r_i * c_i-1^e'_i                [2n]
			for (int i = 0; i < this.size; i++) {
				Element c_i_1 = i == 0 ? this.h : this.cV.getAt(i - 1);
				pV[i + 2] = g.selfApply(rV.getAt(i)).apply(c_i_1.selfApply(ePrimeV.getAt(i)));
			}

			// - Com(0, d)                          [1]
			pV[this.size + 2] = this.gpcs.getRandomizationGenerator().selfApply(d);

			//                                 ---------
			//                                   [3n+3]
			return Tuple.getInstance(pV);
		}

	}

	//===================================================================================
	// Service functions to create non-interactive Sigma- and Element-ChallengeGenerator
	//
	public static StandardNonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size) {
		return PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size, (RandomOracle) null);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final RandomOracle randomOracle) {
		return PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size, (Element) null, randomOracle);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final Element proverId) {
		return PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size, proverId, (RandomOracle) null);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final Element proverId, RandomOracle randomOracle) {
		if (cyclicGroup == null || size < 1) {
			throw new IllegalArgumentException();
		}
		if (randomOracle == null) {
			randomOracle = PseudoRandomOracle.DEFAULT;
		}
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(PermutationCommitmentProofGenerator.createSigmaChallengeGeneratorPublicInputSpace(cyclicGroup, size),
																		 PermutationCommitmentProofGenerator.createCommitmentSpace(cyclicGroup, size),
																		 PermutationCommitmentProofGenerator.createChallengeSpace(cyclicGroup),
																		 randomOracle,
																		 proverId);
	}

	public static StandardNonInteractiveChallengeGenerator createNonInteractiveEValuesGenerator(final CyclicGroup cyclicGroup, final int size) {
		return PermutationCommitmentProofGenerator.createNonInteractiveEValuesGenerator(cyclicGroup, size, PseudoRandomOracle.DEFAULT);
	}

	public static StandardNonInteractiveChallengeGenerator createNonInteractiveEValuesGenerator(final CyclicGroup cyclicGroup, final int size, final RandomOracle randomOracle) {
		if (cyclicGroup == null || size < 1 || randomOracle == null) {
			throw new IllegalArgumentException();
		}
		return StandardNonInteractiveChallengeGenerator.getInstance(PermutationCommitmentProofGenerator.createEValuesGeneratorPublicInputSpace(cyclicGroup, size),
																	PermutationCommitmentProofGenerator.createEValuesGeneratorChallengeSpace(cyclicGroup, size),
																	randomOracle);
	}

}
