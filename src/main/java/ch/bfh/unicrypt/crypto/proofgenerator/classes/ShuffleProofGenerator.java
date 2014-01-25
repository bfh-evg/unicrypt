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
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.classes.RandomNumberGenerator;
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

		if (sigmaChallengeGenerator == null || eValuesGenerator == null || cyclicGroup == null || size < 1 || !cyclicGroup.contains(encryptionPK) || randomReferenceString == null) {
			throw new IllegalArgumentException();
		}
		if (!sigmaChallengeGenerator.getPublicInputSpace().isEquivalent(ShuffleProofGenerator.createChallengeGeneratorPublicInputSpace(cyclicGroup, size))
			   || !sigmaChallengeGenerator.getCommitmentSpace().isEquivalent(ShuffleProofGenerator.createCommitmentSpace(cyclicGroup, size))
			   || !sigmaChallengeGenerator.getChallengeSpace().isEquivalent(ShuffleProofGenerator.createChallengeSpace(cyclicGroup))
			   || !eValuesGenerator.getInputSpace().isEquivalent(ShuffleProofGenerator.createChallengeGeneratorPublicInputSpace(cyclicGroup, size))
			   || !eValuesGenerator.getChallengeSpace().isEquivalent(ShuffleProofGenerator.createEValuesGeneratorChallengeSpace(cyclicGroup, size))) {
			throw new IllegalArgumentException();
		}

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
		return ShuffleProofGenerator.createCommitmentSpace(this.cyclicGroup, this.size);
	}

	public ZMod getChallengeSpace() {
		return ShuffleProofGenerator.createChallengeSpace(this.cyclicGroup);
	}

	public ProductGroup getResponseSpace() {
		return ProductGroup.getInstance(cyclicGroup.getZModOrder(),
										cyclicGroup.getZModOrder(),
										ProductGroup.getInstance(cyclicGroup.getZModOrder(), size));
	}

	//===================================================================================
	// Helpers to create spaces
	//
	private static ProductGroup createCommitmentSpace(CyclicGroup cyclicGroup, int size) {
		return ProductGroup.getInstance(cyclicGroup, ProductGroup.getInstance(cyclicGroup, 2));
	}

	private static ZMod createChallengeSpace(CyclicGroup cyclicGroup) {
		return cyclicGroup.getZModOrder();
	}

	private static ProductGroup createChallengeGeneratorPublicInputSpace(CyclicGroup cyclicGroup, int size) {
		// (Permutation Commitment, Input Ciphertexts, Output Ciphertexts)
		return ProductGroup.getInstance(ProductGroup.getInstance(cyclicGroup, size),
										ProductGroup.getInstance(ProductGroup.getInstance(cyclicGroup, 2), size),
										ProductGroup.getInstance(ProductGroup.getInstance(cyclicGroup, 2), size));
	}

	private static ProductGroup createEValuesGeneratorChallengeSpace(CyclicGroup cyclicGroup, int size) {
		return ProductGroup.getInstance(cyclicGroup.getZModOrder(), size);
	}

	//===================================================================================
	// Generate and Validate
	//
	@Override
	protected Triple abstractGenerate(Triple privateInput, Triple publicInput, RandomNumberGenerator randomGenerator) {

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
		final Element randomElement = this.getResponseSpace().getRandomElement(randomGenerator);
		final Element commitment = f.apply(randomElement);                        // [3N+3]
		final Element challenge = this.sigmaChallengeGenerator.generate(publicInput, commitment);
		final Element response = randomElement.apply(Tuple.getInstance(r, w, ePrimeV).selfApply(challenge));
		Triple preimageProof = (Triple) Triple.getInstance(commitment, challenge, response);
		//                                                                          --------
		return preimageProof;                                                     // [3N+3]
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
		// - p_1 == c_pi^e                                                        //    [N]
		ps[0] = ShuffleProofGenerator.computeInnerProduct(cPiV, eV);
		// - p_2 = u
		ps[1] = ShuffleProofGenerator.computeInnerProduct(uV, eV);                //   [2N]

		final Tuple pV = Tuple.getInstance(ps);

		// 1. Verify preimage proof
		PreimageProofFunction f = new PreimageProofFunction(this.cyclicGroup, this.size, this.getResponseSpace(), this.getCommitmentSpace(), this.randomReferenceString, uPrimeV, this.encryptionPK);
		final Element challenge = this.sigmaChallengeGenerator.generate(publicInput, commitment);
		final Element left = f.apply(response);                                   // [3N+3]
		final Element right = commitment.apply(pV.selfApply(challenge));          //    [3]
		//                                                                          --------
		return BooleanSet.getInstance().getElement(left.isEquivalent(right));     // [6N+6]
	}

	//===================================================================================
	// Private Helpers
	//
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
		protected Tuple abstractApply(Tuple element, RandomNumberGenerator randomGenerator) {

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

		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(ShuffleProofGenerator.createChallengeGeneratorPublicInputSpace(cyclicGroup, size),
																		 ShuffleProofGenerator.createCommitmentSpace(cyclicGroup, size),
																		 ShuffleProofGenerator.createChallengeSpace(cyclicGroup),
																		 proverId);
	}

	public static StandardNonInteractiveChallengeGenerator createNonInteractiveEValuesGenerator(final CyclicGroup cyclicGroup, final int size) {
		return ShuffleProofGenerator.createNonInteractiveEValuesGenerator(cyclicGroup, size, (RandomOracle) null);
	}

	public static StandardNonInteractiveChallengeGenerator createNonInteractiveEValuesGenerator(final CyclicGroup cyclicGroup, final int size, RandomOracle randomOracle) {
		if (cyclicGroup == null || size < 1) {
			throw new IllegalArgumentException();
		}
		return StandardNonInteractiveChallengeGenerator.getInstance(ShuffleProofGenerator.createChallengeGeneratorPublicInputSpace(cyclicGroup, size),
																	ShuffleProofGenerator.createEValuesGeneratorChallengeSpace(cyclicGroup, size),
																	randomOracle);
	}

}
