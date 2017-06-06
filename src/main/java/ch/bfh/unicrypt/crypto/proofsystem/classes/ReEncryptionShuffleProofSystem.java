/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.crypto.proofsystem.classes;

import ch.bfh.unicrypt.crypto.proofsystem.abstracts.AbstractShuffleProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.ChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.GeneralizedPedersenCommitmentScheme;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.ReEncryptionScheme;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.classes.ConvertFunction;
import ch.bfh.unicrypt.math.function.classes.PermutationFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;

/**
 * The implementation of the re-encryption shuffle proof according to Wikström (@see Wik09 Protocol2:
 * Commitment-Consistent Proof of a Shuffle). It covers only the online part; the proof that the commitment to the
 * permutation is indeed a commitment to a valid permutation must be done separately by a
 * {@link PermutationCommitmentProofSystem}.
 * <p>
 * @see "Wik09"
 * @author P. Locher
 */
public class ReEncryptionShuffleProofSystem
	   extends AbstractShuffleProofSystem {

	final private ReEncryptionScheme encryptionScheme;
	final private Element encryptionPK;

	private ReEncryptionShuffleProofSystem(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator,
		   CyclicGroup cyclicGroup, int size, int kr, Tuple independentGenerators,
		   ReEncryptionScheme encryptionScheme, Element encryptionPK) {

		super(sigmaChallengeGenerator, eValuesGenerator, cyclicGroup, size, kr, independentGenerators);

		this.encryptionScheme = encryptionScheme;
		this.encryptionPK = encryptionPK;
	}

	//===================================================================================
	// Interface implementation
	//
	// Private: (PermutationElement pi, PermutationCommitment-Randomizations sV, ReEncryption-Randomizations rV)
	@Override
	protected ProductGroup abstractGetPrivateInputSpace() {
		return ProductGroup.getInstance(PermutationGroup.getInstance(this.getSize()),
										ProductGroup.getInstance(this.getCyclicGroup().getZModOrder(), this.getSize()),
										ProductGroup.getInstance((Group) this.encryptionScheme.getRandomizationSpace(),
																 this.getSize()));
	}

	// Public:  (PermutationCommitment cPiV, Input-Ciphertexts uV, Output-Ciphertexts uPrimeV)
	@Override
	protected ProductGroup abstractGetPublicInputSpace() {
		return ProductGroup.getInstance(ProductGroup.getInstance(this.getCyclicGroup(), this.getSize()),
										ProductGroup.getInstance((Group) this.encryptionScheme.getEncryptionSpace(),
																 this.getSize()),
										ProductGroup.getInstance((Group) this.encryptionScheme.getEncryptionSpace(),
																 this.getSize()));
	}

	// t: (Generalized Pedersen Commitemnt, Encryption)
	@Override
	public ProductGroup getCommitmentSpace() {
		return ProductGroup.getInstance(this.getCyclicGroup(), (Group) this.encryptionScheme.getEncryptionSpace());
	}

	// s: (r, w, ePrimeV)
	@Override
	public ProductGroup getResponseSpace() {
		return ProductGroup.getInstance((Group) this.encryptionScheme.getRandomizationSpace(),
										this.getCyclicGroup().getZModOrder(),
										ProductGroup.getInstance(this.getCyclicGroup().getZModOrder(), this.getSize()));
	}

	public ReEncryptionScheme getEncryptionScheme() {
		return this.encryptionScheme;
	}

	public Element getEncryptionPK() {
		return this.encryptionPK;
	}

	//===================================================================================
	// Generate and Validate
	//
	@Override
	protected Tuple abstractGenerate(Triple privateInput, Tuple publicInput, RandomByteSequence randomByteSequence) {

		// Unfold private and public input
		final PermutationElement pi = (PermutationElement) privateInput.getFirst();
		final Tuple sV = (Tuple) privateInput.getSecond();
		final Tuple rV = (Tuple) privateInput.getThird();
		final Tuple uPrimeV = (Tuple) publicInput.getAt(2);
		final Tuple eV = (Tuple) this.getEValuesGenerator().generate(publicInput);

		// Compute private values for sigma proof
		final Element r = computeInnerProduct(rV, eV);
		final Element w = computeInnerProduct(sV, eV);
		Tuple ePrimeV = PermutationFunction.getInstance(eV.getSet()).apply(eV, pi);

		// Map ePrimeV to Z_q^N
		ePrimeV = ProductFunction.getInstance(
			   ConvertFunction.getInstance(ePrimeV.getFirst().getSet(), this.getCyclicGroup().getZModOrder()),
			   ePrimeV.getLength()).apply(ePrimeV);

		// Create sigma proof
		PreimageProofFunction f
			   = new PreimageProofFunction(this.getCyclicGroup(), this.getSize(), this.getResponseSpace(),
										   this.getCommitmentSpace(), this.getIndependentGenerators(), uPrimeV,
										   this.encryptionScheme, this.encryptionPK);

		Tuple randomElement = this.getResponseSpace().extractPrefix(2).getRandomElement(randomByteSequence);
		Tuple randEV = ProductGroup.getInstance(
			   ZMod.getInstance(MathUtil.powerOfTwo(this.getKe() + this.getKc() + this.getKr())),
			   this.getSize()).getRandomElement(randomByteSequence);

		// 'Normally' ke+kc+kr < cyclicGroup.getOrder, but in case it isn't!
		Element[] randEVs = new Element[this.getSize()];
		for (int i = 0; i < randEVs.length; i++) {
			randEVs[i] = this.getCyclicGroup().getZModOrder().getElement(randEV.getAt(i).convertToBigInteger()
				   .mod(this.getCyclicGroup().getOrder()));
		}
		randEV = Tuple.getInstance(randEVs);
		randomElement = randomElement.append(Tuple.getInstance(randEV));

		final Element commitment = f.apply(randomElement);                        // [3N+3]
		final Element challenge = this.getSigmaChallengeGenerator().generate(publicInput, commitment);
		final Element response = randomElement.apply(Tuple.getInstance(r, w, ePrimeV).selfApply(challenge));
		Triple preimageProof = Triple.getInstance(commitment, challenge, response);
		//                                                                          --------
		return Tuple.getInstance(eV).append(preimageProof);                      // [3N+3]
	}

	@Override
	protected boolean abstractVerify(Tuple proof, Tuple publicInput) {

		// Unfold proof and public input
		final Tuple commitment = (Tuple) proof.getAt(1);
		final Tuple response = (Tuple) proof.getAt(3);
		final Tuple cPiV = (Tuple) publicInput.getFirst();
		final Tuple uV = (Tuple) publicInput.getAt(1);
		final Tuple uPrimeV = (Tuple) publicInput.getAt(2);
		final Tuple eV = (Tuple) this.getEValuesGenerator().generate(publicInput);

		// Compute image of preimage proof
		final Element[] ps = new Element[2];
		// - p_1 == c_pi^e                                                              [N]
		ps[0] = computeInnerProduct(cPiV, eV);
		// - p_2 = u                                                                   [2N]
		ps[1] = computeInnerProduct(uV, eV);

		final Tuple pV = Tuple.getInstance(ps);

		// 1. Verify preimage proof
		PreimageProofFunction f = new PreimageProofFunction(this.getCyclicGroup(), this.getSize(),
															this.getResponseSpace(), this.getCommitmentSpace(),
															this.getIndependentGenerators(), uPrimeV,
															this.encryptionScheme, this.encryptionPK);
		final Element challenge = this.getSigmaChallengeGenerator().generate(publicInput, commitment);
		final Element left = f.apply(response);                                   // [3N+3]
		final Element right = commitment.apply(pV.selfApply(challenge));          //    [3]
		//                                                                          --------
		return left.isEquivalent(right);                                          // [6N+6]
	}

	//===================================================================================
	// Nested class PreimageProofFunction
	//
	private static class PreimageProofFunction
		   extends AbstractFunction<PreimageProofFunction, ProductGroup, Tuple, ProductGroup, Tuple> {

		private final CyclicGroup cyclicGroup;
		private final Tuple uPrimeV;
		private final ReEncryptionScheme encryptionScheme;
		private final Element encryptionPK;
		private final GeneralizedPedersenCommitmentScheme gpcs;

		protected PreimageProofFunction(CyclicGroup cyclicGroup, int size, ProductGroup domain, ProductGroup coDomain,
			   Tuple independentGenerators, Tuple uPrimeV, ReEncryptionScheme encryptionScheme, Element encryptionPK) {
			super(domain, coDomain);
			this.cyclicGroup = cyclicGroup;
			this.uPrimeV = uPrimeV;
			this.encryptionScheme = encryptionScheme;
			this.encryptionPK = encryptionPK;
			this.gpcs = GeneralizedPedersenCommitmentScheme.getInstance(independentGenerators.getAt(0),
																		independentGenerators.extract(1, size));
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
			ZMod zMod = this.cyclicGroup.getZModOrder();
			Element ePrimeVs[] = new Element[ePrimeV.getArity()];
			for (int i = 0; i < ePrimeV.getArity(); i++) {
				ePrimeVs[i] = zMod.getElement(((ZModElement) ePrimeV.getAt(i)).getValue().mod(zMod.getOrder()));
			}
			cV[0] = gpcs.commit(Tuple.getInstance(ePrimeVs), w);

			// - Prod(u'_i^(e'_i)) * Enc(1, -r)         [2n+2]
			final Element a = computeInnerProduct(this.uPrimeV, ePrimeV);
			final Element b
				   = encryptionScheme.encrypt(encryptionPK, encryptionScheme.getMessageSpace().getIdentityElement(),
											  r.invert());
			cV[1] = a.apply(b);

			//                                        ---------
			//                                          [3n+3]
			return Tuple.getInstance(cV);
		}

	}

	//===================================================================================
	// getInstance...
	//
	public static ReEncryptionShuffleProofSystem getInstance(int size,
		   ElGamalEncryptionScheme elgamal, Element encryptionPK) {
		return getInstance(size, elgamal, encryptionPK, DeterministicRandomByteSequence.getInstance());
	}

	public static ReEncryptionShuffleProofSystem getInstance(int size,
		   ElGamalEncryptionScheme elgamal, Element encryptionPK, DeterministicRandomByteSequence rrbs) {
		if (elgamal == null) {
			throw new IllegalArgumentException();
		}
		ZMod challengeSpace = elgamal.getCyclicGroup().getZModOrder();
		return getInstance(
			   createNonInteractiveSigmaChallengeGenerator(challengeSpace),
			   createNonInteractiveEValuesGenerator(challengeSpace, size),
			   size, elgamal, encryptionPK, DEFAULT_KR, rrbs);
	}

	public static ReEncryptionShuffleProofSystem
		   getInstance(int size, ElGamalEncryptionScheme elgamal,
				  Element encryptionPK, Element proverId, int ke, int kc, int kr,
				  DeterministicRandomByteSequence rrbs) {
		return getInstance(
			   createNonInteractiveSigmaChallengeGenerator(kc, proverId),
			   createNonInteractiveEValuesGenerator(ke, size),
			   size, elgamal, encryptionPK, kr, rrbs);
	}

	public static ReEncryptionShuffleProofSystem
		   getInstance(Tuple independentGenerators, ElGamalEncryptionScheme elgamal, Element encryptionPK,
				  Element proverId, int ke, int kc, int kr) {
		if (independentGenerators == null || independentGenerators.getArity() < 2
			   || !independentGenerators.getFirst().getSet().isCyclic()) {
			throw new IllegalArgumentException();
		}
		int size = independentGenerators.getArity() - 1;
		return getInstance(
			   createNonInteractiveSigmaChallengeGenerator(kc, proverId),
			   createNonInteractiveEValuesGenerator(ke, size),
			   independentGenerators,
			   elgamal, encryptionPK, kr);
	}

	public static ReEncryptionShuffleProofSystem getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, int size, ElGamalEncryptionScheme elgamal,
		   Element encryptionPK) {
		return getInstance(sigmaChallengeGenerator, eValuesGenerator, size, elgamal,
						   encryptionPK, DEFAULT_KR, DeterministicRandomByteSequence.getInstance());
	}

	public static ReEncryptionShuffleProofSystem getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, int size, ElGamalEncryptionScheme elgamal,
		   Element encryptionPK, int kr, DeterministicRandomByteSequence randomByteSequence) {

		if (elgamal == null || size < 1 || randomByteSequence == null) {
			throw new IllegalArgumentException();
		}
		Tuple independentGenerators = Tuple.getInstance(
			   elgamal.getCyclicGroup().getIndependentGenerators(randomByteSequence).limit(size + 1));
		return getInstance(sigmaChallengeGenerator, eValuesGenerator, independentGenerators,
						   elgamal, encryptionPK, kr);
	}

	public static ReEncryptionShuffleProofSystem getInstance(SigmaChallengeGenerator sigmaChallengeGenerator,
		   ChallengeGenerator eValuesGenerator, Tuple independentGenerators, ElGamalEncryptionScheme elgamal,
		   Element encryptionPK, int kr) {

		if (sigmaChallengeGenerator == null || eValuesGenerator == null || independentGenerators == null
			   || independentGenerators.getArity() < 2 || !independentGenerators.getSet().isUniform()
			   || !independentGenerators.getFirst().getSet().isCyclic()
			   || elgamal == null || !elgamal.getKeyPairGenerator().getPublicKeySpace().contains(encryptionPK)
			   || kr < 1) {
			throw new IllegalArgumentException();
		}
		CyclicGroup cyclicGroup = (CyclicGroup) independentGenerators.getFirst().getSet();
		if (elgamal.getCyclicGroup().getOrder().compareTo(cyclicGroup.getOrder()) != 0
			   || sigmaChallengeGenerator.getChallengeSpace().getOrder().compareTo(cyclicGroup.getOrder()) > 0) {
			throw new IllegalArgumentException();
		}

		Set cs = eValuesGenerator.getChallengeSpace();
		int size = independentGenerators.getArity() - 1;
		if (!cs.isProduct()
			   || ((ProductSet) cs).getArity() != size
			   || ((ProductSet) cs).getFirst().getOrder().compareTo(cyclicGroup.getOrder()) > 0
			   || !((ImmutableArray<Set>) cs).isUniform()) {
			throw new IllegalArgumentException();
		}

		return new ReEncryptionShuffleProofSystem(sigmaChallengeGenerator, eValuesGenerator,
												  cyclicGroup, size, kr, independentGenerators, elgamal, encryptionPK);
	}

}
