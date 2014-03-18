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

import ch.bfh.unicrypt.crypto.proofgenerator.abstracts.AbstractShuffleProofGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.ChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.GeneralizedPedersenCommitmentScheme;
import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.ReEncryptionScheme;
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
import ch.bfh.unicrypt.random.classes.ReferenceRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomOracle;

//
//
// @see [Wik09] Protocol 2: Commitment-Consistent Proof of a Shuffle
//
public class ReEncryptionShuffleProofGenerator
	   extends AbstractShuffleProofGenerator {

	final private SigmaChallengeGenerator sigmaChallengeGenerator;
	final private ChallengeGenerator eValuesGenerator;
	final private CyclicGroup comGroup;
	final private int size;
	final private Tuple independentGenerators;
	final private ReEncryptionScheme encryptionScheme;
	final private Element encryptionPK;

	protected ReEncryptionShuffleProofGenerator(SigmaChallengeGenerator sigmaChallengeGenerator, ChallengeGenerator eValuesGenerator,
		   CyclicGroup comGroup, int size, Tuple independentGenerators, ReEncryptionScheme encryptionScheme, Element encryptionPK) {
		this.sigmaChallengeGenerator = sigmaChallengeGenerator;
		this.eValuesGenerator = eValuesGenerator;
		this.comGroup = comGroup;
		this.size = size;
		this.independentGenerators = independentGenerators;
		this.encryptionScheme = encryptionScheme;
		this.encryptionPK = encryptionPK;
	}

	public static ReEncryptionShuffleProofGenerator getInstance(CyclicGroup comGroup, int size, ReEncryptionScheme encryptionScheme, Element encryptionPK) {
		if (encryptionScheme == null || !encryptionScheme.getEncryptionSpace().isGroup()) {
			throw new IllegalArgumentException();
		}

		return ReEncryptionShuffleProofGenerator.getInstance(
			   ReEncryptionShuffleProofGenerator.createNonInteractiveSigmaChallengeGenerator(comGroup, encryptionScheme, size),
			   ReEncryptionShuffleProofGenerator.createNonInteractiveEValuesGenerator(comGroup, encryptionScheme, size),
			   comGroup, size, encryptionScheme, encryptionPK, ReferenceRandomByteSequence.getInstance());
	}

	public static ReEncryptionShuffleProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, CyclicGroup comGroup, int size, ReEncryptionScheme encryptionScheme, Element encryptionPK) {
		return ReEncryptionShuffleProofGenerator.getInstance(sigmaChallengeGenerator, eValuesGenerator, comGroup, size, encryptionScheme, encryptionPK, ReferenceRandomByteSequence.getInstance());
	}

	public static ReEncryptionShuffleProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, CyclicGroup comGroup, int size, ReEncryptionScheme encryptionScheme, Element encryptionPK, ReferenceRandomByteSequence referenceRandomByteSequence) {

		if (comGroup == null || size < 1 || referenceRandomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		Tuple independentGenerators = comGroup.getIndependentGenerators(size, referenceRandomByteSequence);
		return ReEncryptionShuffleProofGenerator.getInstance(sigmaChallengeGenerator, eValuesGenerator, independentGenerators, encryptionScheme, encryptionPK);
	}

	public static ReEncryptionShuffleProofGenerator getInstance(Tuple independentGenerators, ReEncryptionScheme encryptionScheme, Element encryptionPK) {
		if (independentGenerators == null || independentGenerators.getArity() < 2 || !independentGenerators.getFirst().getSet().isCyclic()
			   || encryptionScheme == null || !encryptionScheme.getEncryptionSpace().isGroup()) {
			throw new IllegalArgumentException();
		}
		CyclicGroup comGroup = (CyclicGroup) independentGenerators.getFirst().getSet();
		int size = independentGenerators.getArity() - 1;
		return ReEncryptionShuffleProofGenerator.getInstance(
			   ReEncryptionShuffleProofGenerator.createNonInteractiveSigmaChallengeGenerator(comGroup, encryptionScheme, size),
			   ReEncryptionShuffleProofGenerator.createNonInteractiveEValuesGenerator(comGroup, encryptionScheme, size),
			   independentGenerators,
			   encryptionScheme, encryptionPK);
	}

	public static ReEncryptionShuffleProofGenerator getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, Tuple independentGenerators, ReEncryptionScheme encryptionScheme, Element encryptionPK) {

		if (sigmaChallengeGenerator == null || eValuesGenerator == null || independentGenerators == null
			   || independentGenerators.getArity() < 2 || !independentGenerators.getSet().isUniform() || !independentGenerators.getFirst().getSet().isCyclic()
			   || encryptionScheme == null || !encryptionScheme.getKeyPairGenerator().getPublicKeySpace().contains(encryptionPK)
			   || !encryptionScheme.getEncryptionSpace().isGroup() || !encryptionScheme.getRandomizationSpace().isGroup()) {
			throw new IllegalArgumentException();
		}
		CyclicGroup comGroup = (CyclicGroup) independentGenerators.getFirst().getSet();
		int size = independentGenerators.getArity() - 1;
		if (!sigmaChallengeGenerator.getPublicInputSpace().isEquivalent(ReEncryptionShuffleProofGenerator.createChallengeGeneratorPublicInputSpace(comGroup, encryptionScheme, size))
			   || !sigmaChallengeGenerator.getCommitmentSpace().isEquivalent(ReEncryptionShuffleProofGenerator.createCommitmentSpace(comGroup, encryptionScheme))
			   || !sigmaChallengeGenerator.getChallengeSpace().isEquivalent(ReEncryptionShuffleProofGenerator.createChallengeSpace(comGroup))
			   || !eValuesGenerator.getInputSpace().isEquivalent(ReEncryptionShuffleProofGenerator.createChallengeGeneratorPublicInputSpace(comGroup, encryptionScheme, size))
			   || !eValuesGenerator.getChallengeSpace().isEquivalent(ReEncryptionShuffleProofGenerator.createEValuesGeneratorChallengeSpace(comGroup, size))) {
			throw new IllegalArgumentException();
		}

		return new ReEncryptionShuffleProofGenerator(sigmaChallengeGenerator, eValuesGenerator, comGroup, size, independentGenerators, encryptionScheme, encryptionPK);
	}

	// Private: (PermutationElement pi, PermutationCommitment-Randomizations sV, ReEncryption-Randomizations rV)
	@Override
	protected ProductGroup abstractGetPrivateInputSpace() {
		return ProductGroup.getInstance(PermutationGroup.getInstance(this.size),
										ProductGroup.getInstance(this.comGroup.getZModOrder(), this.size),
										ProductGroup.getInstance((Group) this.encryptionScheme.getRandomizationSpace(), this.size));
	}

	// Public:  (PermutationCommitment cPiV, Input-Ciphertexts uV, Output-Ciphertexts uPrimeV)
	@Override
	protected ProductGroup abstractGetPublicInputSpace() {
		return ProductGroup.getInstance(ProductGroup.getInstance(this.comGroup, this.size),
										ProductGroup.getInstance((Group) this.encryptionScheme.getEncryptionSpace(), this.size),
										ProductGroup.getInstance((Group) this.encryptionScheme.getEncryptionSpace(), this.size));
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
		return ReEncryptionShuffleProofGenerator.createCommitmentSpace(this.comGroup, this.encryptionScheme);
	}

	public ZMod getChallengeSpace() {
		return ReEncryptionShuffleProofGenerator.createChallengeSpace(this.comGroup);
	}

	public ProductGroup getResponseSpace() {
		return ProductGroup.getInstance((Group) this.encryptionScheme.getRandomizationSpace(),
										this.comGroup.getZModOrder(),
										ProductGroup.getInstance(this.comGroup.getZModOrder(), size));
	}

	//===================================================================================
	// Helpers to create spaces
	//
	private static ProductGroup createCommitmentSpace(CyclicGroup comGroup, ReEncryptionScheme encryptionScheme) {
		return ProductGroup.getInstance(comGroup, (Group) encryptionScheme.getEncryptionSpace());
	}

	private static ZMod createChallengeSpace(CyclicGroup comGroup) {
		return comGroup.getZModOrder();
	}

	private static ProductGroup createChallengeGeneratorPublicInputSpace(CyclicGroup comGroup, ReEncryptionScheme encryptionScheme, int size) {
		// (Permutation Commitment, Input Ciphertexts, Output Ciphertexts)
		return ProductGroup.getInstance(ProductGroup.getInstance(comGroup, size),
										ProductGroup.getInstance((Group) encryptionScheme.getEncryptionSpace(), size),
										ProductGroup.getInstance((Group) encryptionScheme.getEncryptionSpace(), size));
	}

	private static ProductGroup createEValuesGeneratorChallengeSpace(CyclicGroup comGroup, int size) {
		return ProductGroup.getInstance(comGroup.getZModOrder(), size);
	}

	//===================================================================================
	// Generate and Validate
	//
	@Override
	protected Triple abstractGenerate(Triple privateInput, Triple publicInput, RandomByteSequence randomByteSequence) {

		// Unfold private and public input
		final PermutationElement pi = (PermutationElement) privateInput.getFirst();
		final Tuple sV = (Tuple) privateInput.getSecond();
		final Tuple rV = (Tuple) privateInput.getThird();
		final Tuple uPrimeV = (Tuple) publicInput.getThird();
		final Tuple eV = (Tuple) this.eValuesGenerator.generate(publicInput);

		// Compute private values for sigma proof
		final Element r = ReEncryptionShuffleProofGenerator.computeInnerProduct(rV, eV);
		final Element w = ReEncryptionShuffleProofGenerator.computeInnerProduct(sV, eV);
		final Tuple ePrimeV = PermutationFunction.getInstance(eV.getSet()).apply(eV, pi);

		// Create sigma proof
		PreimageProofFunction f = new PreimageProofFunction(this.comGroup, this.size, this.getResponseSpace(), this.getCommitmentSpace(), this.independentGenerators, uPrimeV, this.encryptionScheme, this.encryptionPK);
		final Element randomElement = this.getResponseSpace().getRandomElement(randomByteSequence);
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
		ps[0] = ReEncryptionShuffleProofGenerator.computeInnerProduct(cPiV, eV);
		// - p_2 = u
		ps[1] = ReEncryptionShuffleProofGenerator.computeInnerProduct(uV, eV);                //   [2N]

		final Tuple pV = Tuple.getInstance(ps);

		// 1. Verify preimage proof
		PreimageProofFunction f = new PreimageProofFunction(this.comGroup, this.size, this.getResponseSpace(), this.getCommitmentSpace(), this.independentGenerators, uPrimeV, this.encryptionScheme, this.encryptionPK);
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
		   extends AbstractFunction<PreimageProofFunction, ProductGroup, Tuple, ProductGroup, Tuple> {

		private final Tuple uPrimeV;
		private final ReEncryptionScheme encryptionScheme;
		private final Element encryptionPK;
		final GeneralizedPedersenCommitmentScheme gpcs;

		protected PreimageProofFunction(CyclicGroup comGroup, int size, ProductGroup domain, ProductGroup coDomain, Tuple independentGenerators, Tuple uPrimeV, ReEncryptionScheme encryptionScheme, Element encryptionPK) {
			super(domain, coDomain);
			this.uPrimeV = uPrimeV;
			this.encryptionScheme = encryptionScheme;
			this.encryptionPK = encryptionPK;
			this.gpcs = GeneralizedPedersenCommitmentScheme.getInstance(independentGenerators.getAt(0), independentGenerators.extract(1, size));
		}

		@Override
		protected Tuple abstractApply(Tuple element, RandomByteSequence randomByteSequence) {

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
			final Element a = ReEncryptionShuffleProofGenerator.computeInnerProduct(this.uPrimeV, ePrimeV);
			final Element b = encryptionScheme.encrypt(encryptionPK, encryptionScheme.getMessageSpace().getIdentityElement(), r.invert());
			cV[1] = a.apply(b);

			//                                        ---------
			//                                          [3n+3]
			return Tuple.getInstance(cV);
		}

	}

	//===================================================================================
	// Service functions to create non-interactive SigmaChallengeGenerator and MultiChallengeGenerator
	//
	public static StandardNonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup comGroup, final ReEncryptionScheme encryptionScheme, final int size) {
		return ReEncryptionShuffleProofGenerator.createNonInteractiveSigmaChallengeGenerator(comGroup, encryptionScheme, size, (Element) null);
	}

	public static StandardNonInteractiveSigmaChallengeGenerator createNonInteractiveSigmaChallengeGenerator(final CyclicGroup comGroup, final ReEncryptionScheme encryptionScheme, final int size, final Element proverId) {
		if (comGroup == null || encryptionScheme == null || !encryptionScheme.getEncryptionSpace().isGroup() || size < 1) {
			throw new IllegalArgumentException();
		}

		return StandardNonInteractiveSigmaChallengeGenerator.getInstance(ReEncryptionShuffleProofGenerator.createChallengeGeneratorPublicInputSpace(comGroup, encryptionScheme, size),
																		 ReEncryptionShuffleProofGenerator.createCommitmentSpace(comGroup, encryptionScheme),
																		 ReEncryptionShuffleProofGenerator.createChallengeSpace(comGroup),
																		 proverId);
	}

	public static StandardNonInteractiveChallengeGenerator createNonInteractiveEValuesGenerator(final CyclicGroup comGroup, final ReEncryptionScheme encryptionScheme, final int size) {
		return ReEncryptionShuffleProofGenerator.createNonInteractiveEValuesGenerator(comGroup, encryptionScheme, size, (RandomOracle) null);
	}

	public static StandardNonInteractiveChallengeGenerator createNonInteractiveEValuesGenerator(final CyclicGroup comGroup, final ReEncryptionScheme encryptionScheme, final int size, RandomOracle randomOracle) {
		if (comGroup == null || encryptionScheme == null || !encryptionScheme.getEncryptionSpace().isGroup() || size < 1) {
			throw new IllegalArgumentException();
		}
		return StandardNonInteractiveChallengeGenerator.getInstance(ReEncryptionShuffleProofGenerator.createChallengeGeneratorPublicInputSpace(comGroup, encryptionScheme, size),
																	ReEncryptionShuffleProofGenerator.createEValuesGeneratorChallengeSpace(comGroup, size),
																	randomOracle);
	}

}
