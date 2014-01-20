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
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomOracle;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
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

//
// @see [TW10] Protocol 1: Permutation Matrix
//
public class PermutationCommitmentProofGenerator
	   extends AbstractProofGenerator<ProductGroup, Pair, ProductGroup, Tuple, ProductGroup, Pair> {

	final private SigmaChallengeGenerator sigmaChallengeGenerator;
	final private ChallengeGenerator eValuesGenerator;
	final private CyclicGroup cyclicGroup;
	final private int size;
	final private RandomReferenceString randomReferenceString;

	protected PermutationCommitmentProofGenerator(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size, RandomReferenceString randomReferenceString) {
		this.sigmaChallengeGenerator = sigmaChallengeGenerator;
		this.eValuesGenerator = eValuesGenerator;
		this.cyclicGroup = cyclicGroup;
		this.size = size;
		this.randomReferenceString = randomReferenceString;
	}

	public static PermutationCommitmentProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size) {
		return PermutationCommitmentProofGenerator.getInstance(sigmaChallengeGenerator, eValuesGenerator, cyclicGroup, size, PseudoRandomReferenceString.getInstance());
	}

	public static PermutationCommitmentProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, CyclicGroup cyclicGroup, int size, RandomReferenceString randomReferenceString) {

		// TODO Argument validation
		return new PermutationCommitmentProofGenerator(sigmaChallengeGenerator, eValuesGenerator, cyclicGroup, size, randomReferenceString);
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
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size - 1),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size - 1),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size));
	}

	@Override
	protected Pair abstractGenerate(Pair privateInput, Tuple publicInput, RandomGenerator randomGenerator) {

		// Unfold privat and public input
		final PermutationElement pi = (PermutationElement) privateInput.getFirst();
		final Tuple sV = (Tuple) privateInput.getSecond();
		final Tuple eV = (Tuple) this.eValuesGenerator.generate(publicInput);

		// Compute private values for sigma proof
		final Tuple oneV = PermutationCommitmentProofGenerator.createOneVector(this.cyclicGroup.getZModOrder(), this.size);
		final Element v = PermutationCommitmentProofGenerator.computeInnerProduct(oneV, sV);
		final Element w = PermutationCommitmentProofGenerator.computeInnerProduct(sV, eV);
		final Tuple ePrimeV = PermutationFunction.getInstance(eV.getSet()).apply(eV, pi);
		final Tuple rV = ProductGroup.getInstance(this.cyclicGroup.getZModOrder(), this.size - 1).getRandomElement(randomGenerator);
		final Element[] rPrimes = ProductGroup.getInstance(this.cyclicGroup.getZModOrder(), this.size).getRandomElement(randomGenerator).getAll();
		// Set r'_N = 0
		rPrimes[this.size - 1] = this.cyclicGroup.getZModOrder().getIdentityElement();
		final Tuple rPrimeV = Tuple.getInstance(rPrimes);
		final Tuple rPPrimeV = PermutationCommitmentProofGenerator.computeRPrimePrime(ePrimeV, rPrimeV);

		// Compute commitments c'_i and multiplied e-values e''_i
		final PedersenCommitmentScheme pcs = PedersenCommitmentScheme.getInstance(this.cyclicGroup, this.randomReferenceString);
		final Element[] cPrimes = new Element[this.size];
		final Element[] ePPrimes = new Element[this.size];
		Element eProd = this.cyclicGroup.getZModOrder().getElement(1);
		for (int i = 0; i < this.size; i++) {
			eProd = eProd.selfApply(ePrimeV.getAt(i));
			ePPrimes[i] = eProd;
			cPrimes[i] = pcs.commit(eProd, rPrimeV.getAt(i));    //   [2n]
		}
		final Tuple ePPrimeV = Tuple.getInstance(ePPrimes);
		final Tuple cPrimeV = Tuple.getInstance(cPrimes);

		// Create sigma proof
		PreimageProofFunction f = new PreimageProofFunction(this.cyclicGroup, this.size, this.getResponceSpace(), this.getCommitmentSpace(), this.randomReferenceString, cPrimeV);
		Tuple cV = f.computePublicValues(v, w, rV, ePrimeV);    //    [3n]
		PreimageProofGenerator ppg = PreimageProofGenerator.getInstance(this.sigmaChallengeGenerator, f);
		Triple preimageProof = ppg.generate(Tuple.getInstance(v, w, rV, rPrimeV, rPPrimeV, ePrimeV, ePPrimeV), cV, randomGenerator);
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
		final Tuple eV = (Tuple) this.eValuesGenerator.generate(publicInput);
		final Tuple oneV = PermutationCommitmentProofGenerator.createOneVector(this.cyclicGroup.getZModOrder(), this.size);
		final Element[] gV = GeneralizedPedersenCommitmentScheme.getInstance(this.cyclicGroup, this.size, this.randomReferenceString).getMessageGenerators();

		// 1. Verify preimage proof
		PreimageProofFunction f = new PreimageProofFunction(this.cyclicGroup, this.size, this.getResponceSpace(), this.getCommitmentSpace(), this.randomReferenceString, Tuple.getInstance(cPrimeV));
		PreimageProofGenerator ppg = PreimageProofGenerator.getInstance(this.sigmaChallengeGenerator, f);
		BooleanElement v = ppg.verify(preimageProof, cV);
		if (!v.getValue()) {
			return v;
		}

		// 2. Check correctness of cV
		// 2.1 c_1 == c_pi^1/prod(g_i)
		boolean v1 = cV.getAt(0).isEquivalent(PermutationCommitmentProofGenerator.computeInnerProduct(publicInput, oneV).applyInverse(this.cyclicGroup.apply(gV)));

		// 2.2 c_2 == c_pi^e
		boolean v2 = cV.getAt(1).isEquivalent(PermutationCommitmentProofGenerator.computeInnerProduct(publicInput, eV));

		// 2.3 c_(2n+1) = c'_N == g1^(prod(e))
		Element eProd = eV.getAt(0);
		for (int i = 1; i < this.size; i++) {
			eProd = eProd.selfApply(eV.getAt(i));
		}
		boolean v3 = cV.getAt(2 * this.size).isEquivalent(gV[0].selfApply(eProd));

		// 2.4 c_i = c_(i+n) for n+2 < i < 2n
		boolean v4 = true;
		int offset1 = this.size + 2;
		int offset2 = offset1 + this.size - 1;
		for (int i = 0; i < this.size - 1; i++) {
			if (!cV.getAt(offset1 + i).isEquivalent(cV.getAt(offset2 + i))) {
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

		private final int size;
		private final Tuple cPrimeV;
		private final PedersenCommitmentScheme pcs;
		private final GeneralizedPedersenCommitmentScheme gpcs;
		private final Element g;

		protected PreimageProofFunction(CyclicGroup cyclicGroup, int size, ProductGroup domain, ProductGroup coDomain, RandomReferenceString randomReferenceString, Tuple cPrimeV) {
			super(domain, coDomain);
			this.size = size;
			this.cPrimeV = cPrimeV;

			// Prepare commitment schemes
			this.pcs = PedersenCommitmentScheme.getInstance(cyclicGroup, randomReferenceString);
			this.gpcs = GeneralizedPedersenCommitmentScheme.getInstance(cyclicGroup, size, randomReferenceString);
			this.g = pcs.getRandomizationGenerator();

		}

		@Override
		protected Tuple abstractApply(Tuple element, RandomGenerator randomGenerator) {

			// Unfold element
			final Element v = element.getAt(0);
			final Element w = element.getAt(1);
			final Tuple rV = (Tuple) element.getAt(2);
			final Tuple rPrimeV = (Tuple) element.getAt(3);
			final Tuple rPPrimeV = (Tuple) element.getAt(4);
			final Tuple ePrimeV = (Tuple) element.getAt(5);
			final Tuple ePPrimeV = (Tuple) element.getAt(6);

			// Result array
			final Element[] cV = new Element[3 * this.size];

			// COMPUTE...
			// - Com(0, v)                          [1]
			this.computeFirst(cV, v);

			// - Com(e', w)                       [n+1]
			this.computeSecond(cV, w, ePrimeV);

			// - Com(e'_i+1, r_i)                [2n-2]
			this.computeThird(cV, rV, ePrimeV);

			// - Com(e''_i, r'_i)                  [2n]
			this.computeFourth(cV, rPrimeV, ePPrimeV);

			// - g^(r''_i) * c'_i^(e'_i+1)       [2n-2]
			this.computeFifth(cV, rPPrimeV, ePrimeV);
			//                                 ---------
			//                                   [7n-2]
			return Tuple.getInstance(cV);
		}

		protected Tuple computePublicValues(Element v, Element w, Tuple rV, Tuple ePrimeV) {
			// Result array
			final Element[] cV = new Element[3 * this.size];

			// COMPUTE...
			// - Com(0, v)                          [1]
			this.computeFirst(cV, v);

			// - Com(e', w)                       [n+1]
			this.computeSecond(cV, w, ePrimeV);

			// - Com(e'_i+1, r_i)                  [2n]
			this.computeThird(cV, rV, ePrimeV);

			// - Fill in c'
			int offset1 = this.size + 1;
			int offset2 = 2 * this.size;
			for (int i = 0; i < this.size; i++) {
				cV[i + offset1] = this.cPrimeV.getAt(i);
				if (i > 0) {
					cV[i + offset2] = this.cPrimeV.getAt(i);
				}
			}
			//                                  ---------
			//                                     [3n]
			return Tuple.getInstance(cV);
		}

		// Com(0, v)
		private void computeFirst(Element[] cV, Element v) {
			cV[0] = this.gpcs.getRandomizationGenerator().selfApply(v);
		}

		// Com(e', w)
		private void computeSecond(Element[] cV, Element w, Tuple ePrimeV) {
			cV[1] = this.gpcs.commit(ePrimeV, w);
		}

		// Com(e'_i+1, r_i)
		private void computeThird(Element[] cV, Tuple rV, Tuple ePrimeV) {
			int offset = 2;
			for (int i = 0; i < this.size - 1; i++) {
				cV[i + offset] = this.pcs.commit(ePrimeV.getAt(i + 1), rV.getAt(i));
			}
		}

		// Com(e''_i, r'_i)
		private void computeFourth(Element[] cV, Tuple rPrimeV, Tuple ePPrimeV) {
			int offset = this.size + 1;
			for (int i = 0; i < this.size; i++) {
				cV[i + offset] = this.pcs.commit(ePPrimeV.getAt(i), rPrimeV.getAt(i));
			}
		}

		// g^(r''_i) * c'_i^(e'_i+1)
		private void computeFifth(Element[] cV, Tuple rPPrimeV, Tuple ePrimeV) {
			int offset = 2 * this.size + 1;
			for (int i = 0; i < this.size - 1; i++) {
				Element a1 = this.g.selfApply(rPPrimeV.getAt(i));
				Element a2 = this.cPrimeV.getAt(i).selfApply(ePrimeV.getAt(i + 1));
				cV[i + offset] = a1.apply(a2);
			}

		}

	}

	//===================================================================================
	// Service functions to create non-interactive SigmaChallengeGenerator and MultiChallengeGenerator
	//
	public static StandardNonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size) {
		return PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size, PseudoRandomOracle.DEFAULT);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final RandomOracle randomOracle) {
		return PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size, (Element) null, randomOracle);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final Element proverId) {
		return PermutationCommitmentProofGenerator.createNonInteractiveSigmaChallengeGenerator(cyclicGroup, size, proverId, PseudoRandomOracle.DEFAULT);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup cyclicGroup, final int size, final Element proverId, final RandomOracle randomOracle) {
		if (cyclicGroup == null || size < 1 || randomOracle == null) {
			throw new IllegalArgumentException();
		}
		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(ProductGroup.getInstance(cyclicGroup, 3 * size),
																		 ProductGroup.getInstance(cyclicGroup, 3 * size),
																		 cyclicGroup.getZModOrder(),
																		 randomOracle,
																		 proverId);
	}

	public static StandardNonInteractiveChallengeGenerator createNonInteractiveChallengeGenerator(final CyclicGroup cyclicGroup, final int size) {
		return PermutationCommitmentProofGenerator.createNonInteractiveChallengeGenerator(cyclicGroup, size, PseudoRandomOracle.DEFAULT);
	}

	public static StandardNonInteractiveChallengeGenerator createNonInteractiveChallengeGenerator(final CyclicGroup cyclicGroup, final int size, RandomOracle randomOracle) {
		if (cyclicGroup == null || size < 1 || randomOracle == null) {
			throw new IllegalArgumentException();
		}
		return StandardNonInteractiveChallengeGenerator.getInstance(ProductGroup.getInstance(cyclicGroup, size), cyclicGroup.getZModOrder(), size, randomOracle);
	}

}
