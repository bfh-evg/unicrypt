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

import ch.bfh.unicrypt.crypto.proofsystem.abstracts.AbstractSigmaProofSystem;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.classes.FiatShamirSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.GeneralizedPedersenCommitmentScheme;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractCompoundFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;

/**
 *
 * @see "[ASM10] Proof-of-Knowledge of Representation of Committed Value and Its Applications"
 * <p>
 * @author philipp
 */
public class DoubleDiscreteLogProofSystem
	   extends AbstractSigmaProofSystem<ProductGroup, Tuple, ProductGroup, Pair> {

	private final PedersenCommitmentScheme pedersenCS;
	private final GeneralizedPedersenCommitmentScheme generalizedPedersenCS;

	private final CyclicGroup G_p;
	private final CyclicGroup G_q;
	private final ZModPrime Z_p;
	private final ZModPrime Z_q;

	private final int k;

	private DoubleDiscreteLogProofSystem(final SigmaChallengeGenerator challengeGenerator,
		   final PedersenCommitmentScheme pedersenCS, final GeneralizedPedersenCommitmentScheme generalizedPedersenCS,
		   final int k) {
		super(challengeGenerator);
		this.pedersenCS = pedersenCS;
		this.generalizedPedersenCS = generalizedPedersenCS;

		this.G_p = this.pedersenCS.getCyclicGroup();
		this.Z_p = (ZModPrime) this.G_p.getZModOrder();

		this.G_q = this.generalizedPedersenCS.getCyclicGroup();
		this.Z_q = (ZModPrime) this.G_q.getZModOrder();

		this.k = k;
	}

	public static DoubleDiscreteLogProofSystem getInstance(final PedersenCommitmentScheme pedersenCS,
		   final GeneralizedPedersenCommitmentScheme generalizedPedersenCS, final int k) {
		if (pedersenCS == null) {
			throw new IllegalArgumentException();
		}
		SigmaChallengeGenerator challengeGenerator
			   = FiatShamirSigmaChallengeGenerator.getInstance(pedersenCS.getMessageSpace());
		return DoubleDiscreteLogProofSystem.getInstance(challengeGenerator, pedersenCS, generalizedPedersenCS, k);
	}

	public static DoubleDiscreteLogProofSystem getInstance(final SigmaChallengeGenerator challengeGenerator,
		   final PedersenCommitmentScheme pedersenCS, final GeneralizedPedersenCommitmentScheme generalizedPedersenCS,
		   final int k) {
		if (challengeGenerator == null || pedersenCS == null || generalizedPedersenCS == null || k <= 1) {
			throw new IllegalArgumentException();
		}
		// TBD: Check instance of ZModPrime or check whether order is prime and store a Z_p and Z_q as ZMod
		// instead of ZModPrime
		if (!(pedersenCS.getCyclicGroup().getZModOrder() instanceof ZModPrime)
			   || !(generalizedPedersenCS.getCyclicGroup().getZModOrder() instanceof ZModPrime)) {
			throw new IllegalArgumentException();
		}
		BigInteger p = pedersenCS.getCyclicGroup().getOrder();
		BigInteger q = generalizedPedersenCS.getCyclicGroup().getOrder();
		// p = γq+1
		if (!p.subtract(MathUtil.ONE).gcd(q).equals(q)) {
			throw new IllegalArgumentException();
		}
		// 2^k < p
		if (p.compareTo(MathUtil.powerOfTwo(k)) < 1) {
			throw new IllegalArgumentException();
		}
		if (!challengeGenerator.getChallengeSpace().isEquivalent(pedersenCS.getMessageSpace())) {
			throw new IllegalArgumentException();
		}

		return new DoubleDiscreteLogProofSystem(challengeGenerator, pedersenCS, generalizedPedersenCS, k);
	}

	@Override
	protected ProductGroup abstractGetPrivateInputSpace() {
		// x, r \in Z_p and s, m1....mL \in Z_q
		return ProductGroup.getInstance(this.Z_p, this.Z_p, this.Z_q,
										ProductGroup.getInstance(this.Z_q, this.generalizedPedersenCS.getSize()));
	}

	@Override
	protected ProductGroup abstractGetPublicInputSpace() {
		// C \in G_p and D \in G_q
		return ProductGroup.getInstance(this.G_p, this.G_q);
	}

	@Override
	protected ProductSet abstractGetProofSpace() {
		return ProductGroup.getInstance(this.getCommitmentSpace(), this.getChallengeSpace(), this.getResponseSpace());
	}

	@Override
	public ProductGroup getCommitmentSpace() {
		// T \in G_p, T1_1...T1_k \in G_p and T2_1 ... T2_k \in G_q
		return ProductGroup.getInstance(this.G_p, ProductGroup.getInstance(this.G_p, this.k),
										ProductGroup.getInstance(this.G_q, this.k));
	}

	@Override
	public ZMod getChallengeSpace() {
		// c \in {0,1}^k => Z_{2^k} \subset Z_p
		return this.Z_p;
	}

	@Override
	public ProductGroup getResponseSpace() {
		// z_x, z_r \in Z_p
		// z_m1,1 ... z_mL,k \in Z_q (Tuple X Tuple, k X L),
		// z_s_1 ... z_s_k \in Z_q
		// z_r_1 ... z_r_k \in Z_p
		return ProductGroup.getInstance(
			   ProductGroup.getInstance(this.Z_p, 2),
			   ProductGroup.getInstance(this.generalizedPedersenCS.getMessageSpace(), this.k),
			   ProductGroup.getInstance(this.Z_q, this.k),
			   ProductGroup.getInstance(this.Z_p, this.k));
	}

	private Function getRepresentationFunction() {
		return ((AbstractCompoundFunction<ProductFunction, ProductSet, Tuple, ProductSet, Tuple>) ((ImmutableArray<Function>) this.generalizedPedersenCS.getCommitmentFunction())
			   .getAt(0)).getAt(0);
	}

	@Override
	protected Triple abstractGenerate(Tuple secretInput, Pair publicInput, RandomByteSequence randomByteSequence) {

		// 1. Create commitment
		Element rhoX = this.Z_p.getRandomElement(randomByteSequence);
		Element rhoR = this.Z_p.getRandomElement(randomByteSequence);
		Element T = this.pedersenCS.commit(rhoX, rhoR);

		Tuple[] rhoMVs = new Tuple[this.k];
		Element[] rhoSVs = new Element[this.k];
		Element[] rhoRVs = new Element[this.k];
		Element[] T1Vs = new Element[this.k];
		Element[] T2Vs = new Element[this.k];
		Function repF = this.getRepresentationFunction();

		for (int i = 0; i < this.k; i++) {
			rhoMVs[i] = this.generalizedPedersenCS.getMessageSpace().getRandomElement(randomByteSequence);
			rhoSVs[i] = this.Z_q.getRandomElement(randomByteSequence);
			rhoRVs[i] = this.Z_p.getRandomElement(randomByteSequence);

			T1Vs[i] = this.pedersenCS.commit(this.Z_p.getElement(repF.apply(rhoMVs[i]).convertToBigInteger()),
											 rhoRVs[i]);
			T2Vs[i] = this.generalizedPedersenCS.commit(rhoMVs[i], rhoSVs[i]);
		}

		Tuple commitment = Tuple.getInstance(T, Tuple.getInstance(T1Vs), Tuple.getInstance(T2Vs));

		// 2. Create challenge
		Element c = this.getChallengeGenerator().generate(publicInput, commitment);

		// 3. Create response
		Element x = secretInput.getAt(0);
		Element r = secretInput.getAt(1);
		Element s = secretInput.getAt(2);
		Tuple mV = (Tuple) secretInput.getAt(3);
		BigInteger ci = c.convertToBigInteger();

		Element zX = rhoX.apply(x.selfApply(c).invert());
		Element zR = rhoR.apply(r.selfApply(c).invert());

		Tuple[] zMVs = new Tuple[this.k];
		Element[] zSVs = new Element[this.k];
		Element[] zRVs = new Element[this.k];

		for (int i = 0; i < this.k; i++) {
			Element[] zMiVs = new Element[mV.getArity()];
			int bit = ci.testBit(i) ? 1 : 0;
			for (int j = 0; j < mV.getArity(); j++) {
				zMiVs[j] = rhoMVs[i].getAt(j).apply(mV.getAt(j).selfApply(bit).invert());
			}
			zMVs[i] = Tuple.getInstance(zMiVs);
			zSVs[i] = rhoSVs[i].apply(s.selfApply(bit).invert());
			zRVs[i] = rhoRVs[i].apply(r.selfApply(repF.apply(zMVs[i])).selfApply(bit).invert());
		}

		Tuple response = Tuple.getInstance(
			   Pair.getInstance(zX, zR),
			   Tuple.getInstance(zMVs),
			   Tuple.getInstance(zSVs),
			   Tuple.getInstance(zRVs));

		return Triple.getInstance(commitment, c, response);
	}

	@Override
	protected boolean abstractVerify(Triple proof, Pair publicInput) {

		Tuple commitment = (Tuple) proof.getFirst();
		Tuple response = (Tuple) proof.getThird();

		Element C = publicInput.getFirst();
		Element D = publicInput.getSecond();

		Element T = commitment.getAt(0);
		Tuple T1 = (Tuple) commitment.getAt(1);
		Tuple T2 = (Tuple) commitment.getAt(2);

		Pair zXR = (Pair) response.getAt(0);
		Tuple zMV = (Tuple) response.getAt(1);
		Tuple zSV = (Tuple) response.getAt(2);
		Tuple zRV = (Tuple) response.getAt(3);

		Element c = this.getChallengeGenerator().generate(publicInput, commitment);
		BigInteger ci = c.convertToBigInteger();
		Function repF = this.getRepresentationFunction();

		boolean v = T.isEquivalent(C.selfApply(c).apply(this.pedersenCS.commit(zXR.getFirst(), zXR.getSecond())));

		for (int i = 0; i < this.k; i++) {
			int bit = ci.testBit(i) ? 1 : 0;
			v = v && T2.getAt(i).isEquivalent(D.selfApply(bit).apply(this.generalizedPedersenCS.commit(zMV.getAt(i),
																									   zSV.getAt(i))));
			Element x = this.Z_p.getElement(repF.apply(zMV.getAt(i)).convertToBigInteger());
			if (bit == 0) {
				v = v && T1.getAt(i).isEquivalent(this.pedersenCS.commit(x, zRV.getAt(i)));
			} else {
				v = v && T1.getAt(i).isEquivalent(C.selfApply(x).apply(this.pedersenCS.getRandomizationGenerator()
					   .selfApply(zRV.getAt(i))));
			}
		}

		return v;
	}

}
