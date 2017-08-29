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
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Triple;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 *
 * @see "[BG13] Zero-Knowledge Argument for Polynomial Evaluation with Application to Blacklists"
 * <p>
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

	private PolynomialEvaluationProofSystem(final SigmaChallengeGenerator challengeGenerator,
		   final PolynomialElement polynomial, final PedersenCommitmentScheme pedersenCS) {
		super(challengeGenerator);
		this.pedersenCS = pedersenCS;
		this.polynomial = polynomial;

		this.cyclicGroup = pedersenCS.getCyclicGroup();
		this.zModPrime = (ZModPrime) cyclicGroup.getZModOrder();

		this.d = (int) Math.floor(Math.log(polynomial.getValue().getDegree()) / Math.log(2));
	}

	public static final PolynomialEvaluationProofSystem getInstance(final PolynomialElement polynomial,
		   final PedersenCommitmentScheme pedersenCS) {
		if (pedersenCS == null) {
			throw new IllegalArgumentException();
		}
		SigmaChallengeGenerator challengeGenerator
			   = FiatShamirSigmaChallengeGenerator.getInstance(pedersenCS.getMessageSpace());
		return PolynomialEvaluationProofSystem.getInstance(challengeGenerator, polynomial, pedersenCS);
	}

	public static final PolynomialEvaluationProofSystem
		   getInstance(final SigmaChallengeGenerator challengeGenerator, final PolynomialElement polynomial,
				  final PedersenCommitmentScheme pedersenCS) {

		if (challengeGenerator == null || polynomial == null || pedersenCS == null) {
			throw new IllegalArgumentException();
		}
		// TBD: Check instance of ZModPrime or check whether order is prime and store a ZMod instead of ZModPrime
		if (!(polynomial.getSet().getSemiRing() instanceof ZModPrime)
			   || pedersenCS.getCyclicGroup().getOrder() != polynomial.getSet().getSemiRing().getOrder()) {
			throw new IllegalArgumentException();
		}
		if (!challengeGenerator.getChallengeSpace().isEquivalent(pedersenCS.getMessageSpace())) {
			throw new IllegalArgumentException();
		}

		return new PolynomialEvaluationProofSystem(challengeGenerator, polynomial, pedersenCS);
	}

	public ZModPrime getZModPrime() {
		return this.zModPrime;
	}

	public CyclicGroup getCyclicGroup() {
		return this.cyclicGroup;
	}

	public PedersenCommitmentScheme getPedersenCommitmentScheme() {
		return this.pedersenCS;
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
			xiBarVs[i] = rV.getAt(i + 1).selfApply(challenge)
				   .apply(fBarV.getAt(i).selfApply(rV.getAt(i)).invert()).apply(xiV.getAt(i));
		}
		Tuple xiBarV = Tuple.getInstance(xiBarVs);

		Tuple response = Tuple.getInstance(fBarV, rBarV, tBar, xiBarV);

		return Triple.getInstance(commitment, challenge, response);
	}

	@Override
	protected boolean abstractVerify(Triple proof, Pair publicInput) {

		Tuple commitment = (Tuple) proof.getFirst();
		Tuple response = (Tuple) proof.getThird();
		Tuple cV = ((Tuple) commitment.getAt(0)).insert(publicInput.getFirst());
		Tuple cfV = (Tuple) commitment.getAt(1);
		Tuple cdV = (Tuple) commitment.getAt(2);
		Tuple cfuV = (Tuple) commitment.getAt(3);

		Tuple fBarV = (Tuple) response.getAt(0);
		Tuple rBarV = (Tuple) response.getAt(1);
		Element tBar = response.getAt(2);
		Tuple xiBarV = (Tuple) response.getAt(3);

		ZModElement challenge = this.getChallengeGenerator().generate(publicInput, commitment);
		boolean v = true;

		// Precompute c_j^x
		Element[] cxVs = new Element[cV.getLength()];
		for (int i = 0; i < cV.getLength(); i++) {
			cxVs[i] = cV.getAt(i).selfApply(challenge);
		}

		for (int i = 0; i < this.d + 1; i++) {
			v = v && cxVs[i].apply(cfV.getAt(i)).isEquivalent(this.pedersenCS.commit(fBarV.getAt(i), rBarV.getAt(i)));
		}

		for (int i = 0; i < this.d; i++) {
			v = v && cxVs[i + 1].apply(cV.getAt(i).selfApply(fBarV.getAt(i).invert())
				   .apply(cfuV.getAt(i))).isEquivalent(
				   this.pedersenCS.commit(this.zModPrime.getZeroElement(), xiBarV.getAt(i)));
		}

		Element left = publicInput.getSecond().selfApply(challenge.power(this.d + 1));
		ZModElement x_i = challenge.power(0);
		for (int i = 0; i <= this.d; i++) {
			left = left.apply(cdV.getAt(i).selfApply(x_i));
			x_i = x_i.multiply(challenge);
		}

		Element deltaBar = this.computeDeltaBar(fBarV, challenge);
		v = v && left.isEquivalent(this.pedersenCS.commit(deltaBar, tBar));

		return v;
	}

	//================================================================
	// Compute deltas in a binary tree fashion
	//-----------------------------------------
	private PolynomialElement xPoly;

	private Tuple computeDeltas(Tuple uV, Tuple fV) {
		PolynomialSemiRing polynomialSemiRing = this.polynomial.getSet();
		PolynomialElement[] result = new PolynomialElement[1];
		this.xPoly = polynomialSemiRing.getElement(polynomialSemiRing.getSemiRing().getZeroElement(),
												   polynomialSemiRing.getSemiRing().getOneElement());
		createTree1(this.d + 1, 0, this.polynomial.getSet().getOneElement(), uV, fV, result);

		Element[] dVs = new Element[this.d + 1];
		for (int i = 0; i < dVs.length; i++) {
			dVs[i] = result[0].getValue().getCoefficient(i);
		}
		return Tuple.getInstance(dVs);
	}

	public void createTree1(int level, int degree, PolynomialElement poly, Tuple uV, Tuple fV,
		   PolynomialElement[] result) {
		if (level == 0) {
			if (degree == 0) {
				result[0] = poly;
			}
			result[0] = result[0].add(poly.times(polynomial.getValue().getCoefficient(degree)));
		} else {
			// right node
			createTree1(level - 1, degree, poly.multiply(xPoly), uV, fV, result);

			// left node
			int nextDegree = degree + (int) Math.pow(2, level - 1);
			if (nextDegree <= polynomial.getValue().getDegree()) {
				PolynomialElement yPoly
					   = polynomial.getSet().getElement(Tuple.getInstance(fV.getAt(level - 1), uV.getAt(level - 1)));
				createTree1(level - 1, nextDegree, poly.multiply(yPoly), uV, fV, result);
			}
		}
	}

	//================================================================
	// Compute deltaBar in a binary tree fashion
	//-------------------------------------------
	private Element computeDeltaBar(Tuple fBarV, Element x) {
		DualisticElement[] result = new DualisticElement[]{this.zModPrime.getZeroElement()};
		createTree2(this.d + 1, 0, this.zModPrime.getOneElement(), fBarV, x, result);
		return result[0];
	}

	public void createTree2(int level, int degree, DualisticElement value, Tuple fBarV, Element x,
		   DualisticElement[] result) {
		if (level == 0) {
			result[0] = result[0].add(value.multiply(polynomial.getValue().getCoefficient(degree)));
		} else {
			// right node
			createTree2(level - 1, degree, value.multiply(x), fBarV, x, result);

			// left node
			int nextDegree = degree + (int) Math.pow(2, level - 1);
			if (nextDegree <= polynomial.getValue().getDegree()) {
				createTree2(level - 1, nextDegree, value.multiply(fBarV.getAt(level - 1)), fBarV, x, result);
			}
		}
	}

}
