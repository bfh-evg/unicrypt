/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
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
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.classes.RandomOracleSigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.proofsystem.challengegenerator.interfaces.SigmaChallengeGenerator;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;

/**
 *
 * @author philipp
 */
public class PolynomialEvaluationProofSystem
	   extends AbstractSigmaProofSystem<ProductGroup, Tuple, ProductGroup, Pair> {

	private final PedersenCommitmentScheme pedersenCS;
	private final PolynomialElement polynomial;

	private final CyclicGroup cyclicGroup;
	private final ZModPrime zModPrime;

	// D = 2^(d+1) - 1 ==> d = ceil(log(D + 1)) - 1 = floor(log(D))
	private final int d;

	protected PolynomialEvaluationProofSystem(final SigmaChallengeGenerator challengeGenerator, final PolynomialElement polynomial, final PedersenCommitmentScheme pedersenCS) {
		super(challengeGenerator);
		this.pedersenCS = pedersenCS;
		this.polynomial = polynomial;

		this.cyclicGroup = pedersenCS.getCyclicGroup();
		this.zModPrime = (ZModPrime) cyclicGroup.getZModOrder();

		this.d = (int) Math.floor(Math.log(polynomial.getValue().getDegree()) / Math.log(2));
	}

	// Just TMP
	public static final PolynomialEvaluationProofSystem getInstance(final PolynomialElement polynomial, final PedersenCommitmentScheme pedersenCS) {

		int d = (int) Math.floor(Math.log(polynomial.getValue().getDegree()) / Math.log(2));
		CyclicGroup cyclicGroup = pedersenCS.getCyclicGroup();
		ZModPrime zModPrime = (ZModPrime) cyclicGroup.getZModOrder();
		SigmaChallengeGenerator challengeGenerator = RandomOracleSigmaChallengeGenerator.getInstance(ProductGroup.getInstance(cyclicGroup, 2), ProductGroup.getInstance(
																									 ProductGroup.getInstance(cyclicGroup, d),
																									 ProductGroup.getInstance(cyclicGroup, d + 1),
																									 ProductGroup.getInstance(cyclicGroup, d + 1),
																									 ProductGroup.getInstance(cyclicGroup, d)), zModPrime);

		return PolynomialEvaluationProofSystem.getInstance(challengeGenerator, polynomial, pedersenCS);
	}

	public static final PolynomialEvaluationProofSystem getInstance(final SigmaChallengeGenerator challengeGenerator, final PolynomialElement polynomial, final PedersenCommitmentScheme pedersenCS) {

		if (challengeGenerator == null || polynomial == null || pedersenCS == null) {
			throw new IllegalArgumentException();
		}
		if (!(polynomial.getSet().getSemiRing() instanceof ZModPrime)) {
			throw new IllegalArgumentException();
		}
		if (pedersenCS.getCyclicGroup().getOrder() != polynomial.getSet().getSemiRing().getOrder()) {
			throw new IllegalArgumentException();
		}
		//TODO Check challenge, commitment and public input spaces of challengeGenerator

		return new PolynomialEvaluationProofSystem(challengeGenerator, polynomial, pedersenCS);
	}

	@Override
	protected ProductGroup abstractGetPrivateInputSpace() {
		// u, v, r_0, t \in Z_p
		return ProductGroup.getInstance(this.zModPrime, 4);
	}

	@Override
	protected ProductGroup abstractGetPublicInputSpace() {
		// c_u, c_v \in G
		return ProductGroup.getInstance(this.cyclicGroup, 2);
	}

	@Override
	protected ProductGroup abstractGetProofSpace() {
		return ProductGroup.getInstance(this.getCommitmentSpace(), this.getChallengeSpace(), this.getResponseSpace());
	}

	@Override
	public ProductGroup getCommitmentSpace() {
		// c_1 ... c_d \in G
		// c_f_0 ... c_f_d \in G
		// c_detla_0 ... c_delta_d \in G
		// c_fu_0 ... c_fu_d-1 \in G
		return ProductGroup.getInstance(
			   ProductGroup.getInstance(this.cyclicGroup, this.d),
			   ProductGroup.getInstance(this.cyclicGroup, this.d + 1),
			   ProductGroup.getInstance(this.cyclicGroup, this.d + 1),
			   ProductGroup.getInstance(this.cyclicGroup, this.d));
	}

	@Override
	public ZModPrime getChallengeSpace() {
		return this.zModPrime;
	}

	@Override
	public ProductGroup getResponseSpace() {
		// f'_0 ... f'_d \in Z_p
		// r'_0 ... r'_d \in Z_p
		// t' \in Z_p
		// xi'_0 ... xi'_d-1 in Z_p
		return ProductGroup.getInstance(
			   ProductGroup.getInstance(this.zModPrime, this.d + 1),
			   ProductGroup.getInstance(this.zModPrime, this.d + 1),
			   this.zModPrime,
			   ProductGroup.getInstance(this.zModPrime, this.d));
	}

	@Override
	protected Triple abstractGenerate(Tuple secretInput, Pair publicInput, RandomByteSequence randomByteSequence) {

		// 0. Preparation
		ProductGroup zpd = ProductGroup.getInstance(this.zModPrime, this.d + 1);
		Tuple rV = zpd.getRandomElement(randomByteSequence);
		rV = rV.replaceAt(0, secretInput.getAt(2));
		Tuple sV = zpd.getRandomElement(randomByteSequence);
		Tuple tV = zpd.getRandomElement(randomByteSequence);
		Tuple fV = zpd.getRandomElement(randomByteSequence);
		Tuple xiV = ProductGroup.getInstance(this.zModPrime, this.d).getRandomElement(randomByteSequence);

		ZModElement[] uVs = new ZModElement[this.d + 1];
		uVs[0] = (ZModElement) secretInput.getAt(0);
		for (int i = 1; i < uVs.length; i++) {
			uVs[i] = uVs[i - 1].multiply(uVs[i - 1]);
		}
		Tuple uV = Tuple.getInstance(uVs);

		// 1. Create Commitment
		// a) c_1 ... c_d
		Element[] cVs = new Element[this.d + 1];
		cVs[0] = this.cyclicGroup.getIdentityElement();
		for (int i = 1; i < cVs.length; i++) {
			cVs[i] = this.pedersenCS.commit(uV.getAt(i), rV.getAt(i));
		}
		Tuple cV = Tuple.getInstance(cVs).removeAt(0);

		// b) c_f_0 ... c_f_d
		Element[] cfVs = new Element[this.d + 1];
		for (int i = 0; i < cfVs.length; i++) {
			cfVs[i] = this.pedersenCS.commit(fV.getAt(i), sV.getAt(i));
		}
		Tuple cfV = Tuple.getInstance(cfVs);

		// c) c_delta_0 ... c_delta_d
		Tuple dV = this.computeDeltas(uV, fV);
		Element[] cdVs = new Element[this.d + 1];
		for (int i = 0; i < cdVs.length; i++) {
			cdVs[i] = this.pedersenCS.commit(dV.getAt(i), tV.getAt(i));
		}
		Tuple cdV = Tuple.getInstance(cdVs);

		// d) c_fu_0 ... c_fu_d-1
		Element[] cfuVs = new Element[this.d];
		for (int i = 0; i < cfuVs.length; i++) {
			cfuVs[i] = this.pedersenCS.commit(fV.getAt(i).selfApply(uV.getAt(i)), xiV.getAt(i));
		}
		Tuple cfuV = Tuple.getInstance(cfuVs);

		Tuple commitment = Tuple.getInstance(cV, cfV, cdV, cfuV);

		// 2. Get Challenge
		ZModElement challenge = this.getChallengeGenerator().generate(publicInput, commitment);

		// 3. Compute Response
		Element[] fBarVs = new Element[this.d + 1];
		Element[] rBarVs = new Element[this.d + 1];
		for (int i = 0; i < fBarVs.length; i++) {
			fBarVs[i] = uV.getAt(i).selfApply(challenge).apply(fV.getAt(i));
			rBarVs[i] = rV.getAt(i).selfApply(challenge).apply(sV.getAt(i));
		}
		Tuple fBarV = Tuple.getInstance(fBarVs);
		Tuple rBarV = Tuple.getInstance(rBarVs);

		ZModElement tBar = challenge.power(this.d + 1).multiply(secretInput.getAt(3));
		ZModElement x_i = challenge.power(0);
		for (int i = 0; i <= this.d; i++) {
			tBar = tBar.add(tV.getAt(i).selfApply(x_i));
			x_i = x_i.multiply(challenge);
		}

		Element[] xiBarVs = new Element[this.d];
		for (int i = 0; i < xiBarVs.length; i++) {
			xiBarVs[i] = rV.getAt(i + 1).selfApply(challenge).apply(fBarV.getAt(i).selfApply(rV.getAt(i)).invert()).apply(xiV.getAt(i));
		}
		Tuple xiBarV = Tuple.getInstance(xiBarVs);

		Tuple response = Tuple.getInstance(fBarV, rBarV, tBar, xiBarV);

		return Triple.getInstance(commitment, challenge, response);
	}

	@Override
	protected boolean abstractVerify(Triple proof, Pair publicInput) {

		Tuple commitment = (Tuple) proof.getFirst();
		Tuple response = (Tuple) proof.getThird();
		Tuple cV = ((Tuple) commitment.getAt(0)).insertAt(0, publicInput.getFirst());
		Tuple cfV = (Tuple) commitment.getAt(1);
		Tuple cdV = (Tuple) commitment.getAt(2);
		Tuple cfuV = (Tuple) commitment.getAt(3);

		Tuple fBarV = (Tuple) response.getAt(0);
		Tuple rBarV = (Tuple) response.getAt(1);
		Element tBar = response.getAt(2);
		Tuple xiBarV = (Tuple) response.getAt(3);

		ZModElement challenge = this.getChallengeGenerator().generate(publicInput, commitment);
		boolean v = true;

		for (int i = 0; i < this.d + 1; i++) {
			v = v && cV.getAt(i).selfApply(challenge).apply(cfV.getAt(i)).isEquivalent(this.pedersenCS.commit(fBarV.getAt(i), rBarV.getAt(i)));
		}

		for (int i = 0; i < this.d; i++) {
			v = v && cV.getAt(i + 1).selfApply(challenge).apply(cV.getAt(i).selfApply(fBarV.getAt(i).invert()).apply(cfuV.getAt(i))).isEquivalent(this.pedersenCS.commit(zModPrime.getZeroElement(), xiBarV.getAt(i)));
		}

		Element left = publicInput.getSecond().selfApply(challenge.power(this.d + 1));
		ZModElement x_i = challenge.power(0);
		for (int i = 0; i <= this.d; i++) {
			left = left.apply(cdV.getAt(i).selfApply(x_i));
			x_i = x_i.multiply(challenge);
		}

		ZModElement right = this.zModPrime.getZeroElement();
		for (int i = 0; i <= this.polynomial.getValue().getDegree(); i++) {
			BigInteger ib = BigInteger.valueOf(i);
			ZModElement ai = (ZModElement) this.polynomial.getValue().getCoefficient(i);
			for (int j = 0; j <= this.d; j++) {
				int ij = ib.testBit(j) ? 1 : 0;
				ai = ai.multiply(((ZModElement) fBarV.getAt(j)).power(ij).selfApply(challenge.power(1 - ij)));
			}
			right = right.add(ai);
		}
		v = v && left.isEquivalent(this.pedersenCS.commit(right, tBar));

		return v;
	}

	private Tuple computeDeltas(Tuple uV, Tuple fV) {
		PolynomialElement[] result = new PolynomialElement[this.polynomial.getValue().getDegree() + 1];
		Node root = new Node(this.d + 1, 0, this.polynomial.getSet().getOneElement(), uV, fV, result);

		for (int i = 1; i < result.length; i++) {
			result[0] = result[0].add(result[i].times(this.polynomial.getValue().getCoefficient(i)));
		}

		Element[] dVs = new Element[this.d + 1];
		for (int i = 0; i < dVs.length; i++) {
			dVs[i] = result[0].getValue().getCoefficient(i);
		}
		return Tuple.getInstance(dVs);
	}

	private class Node {

		public Node(int level, int degree, PolynomialElement poly, Tuple uV, Tuple fV, PolynomialElement[] result) {
			if (level == 0) {
				result[degree] = poly;
			} else {
				// right node
				PolynomialElement xPoly = polynomial.getSet().getElement(BigInteger.ZERO, BigInteger.ONE);
				Node right = new Node(level - 1, degree, poly.multiply(xPoly), uV, fV, result);

				// left node
				int nextDegree = degree + (int) Math.pow(2, level - 1);
				if (nextDegree <= polynomial.getValue().getDegree()) {
					PolynomialElement yPoly = polynomial.getSet().getElement(Tuple.getInstance(fV.getAt(level - 1), uV.getAt(level - 1)));
					Node left = new Node(level - 1, nextDegree, poly.multiply(yPoly), uV, fV, result);
				}
			}
		}

	}

}
