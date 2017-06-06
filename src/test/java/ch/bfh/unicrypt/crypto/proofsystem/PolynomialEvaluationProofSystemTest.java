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
package ch.bfh.unicrypt.crypto.proofsystem;

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.crypto.proofsystem.classes.PolynomialEvaluationProofSystem;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author philipp
 */
public class PolynomialEvaluationProofSystemTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";

	@Test
	public void testPolynomialEvalutaionProofSystem() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(467);
		final ZModPrime Z_q = (ZModPrime) G_q.getZModOrder();
		final Element g = G_q.getElement(BigInteger.valueOf(3));
		final Element h = G_q.getElement(BigInteger.valueOf(266));

		PedersenCommitmentScheme pedersenCS = PedersenCommitmentScheme.getInstance(h, g);
		PolynomialRing pr = PolynomialRing.getInstance(Z_q);
		PolynomialElement poly;
		try {
			poly = pr.getElementFrom(51, 115, 3, 0, 93);
		} catch (UniCryptException ex) {
			fail();
			return;
		}

		ZModElement u = Z_q.getElement(BigInteger.valueOf(5));
		Element v = poly.evaluate(u);

		Element r0 = Z_q.getElement(BigInteger.valueOf(201));
		Element t = Z_q.getElement(BigInteger.valueOf(189));
		Element cu = pedersenCS.commit(u, r0);
		Element cv = pedersenCS.commit(v, t);

		Tuple secretInput = Tuple.getInstance(u, v, r0, t);
		Tuple publicInput = Tuple.getInstance(cu, cv);

		PolynomialEvaluationProofSystem peps = PolynomialEvaluationProofSystem.getInstance(poly, pedersenCS);
		Tuple proof = peps.generate(secretInput, publicInput);
		boolean verify = peps.verify(proof, publicInput);

		assertTrue(verify);
	}

	@Test
	public void testPolynomialEvalutaionProofSystem2() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(P1);
		final ZModPrime Z_q = (ZModPrime) G_q.getZModOrder();

		PedersenCommitmentScheme pedersenCS = PedersenCommitmentScheme.getInstance(G_q);
		PolynomialRing pr = PolynomialRing.getInstance(Z_q);
		PolynomialElement poly;
		try {
			poly = pr.getElementFrom(1, 15, 23, 45, 33, 11);
		} catch (UniCryptException ex) {
			fail();
			return;
		}

		ZModElement u = Z_q.getElement(BigInteger.valueOf(15));
		Element v = poly.evaluate(u);

		Element r0 = Z_q.getElement(BigInteger.valueOf(56));
		Element t = Z_q.getElement(BigInteger.valueOf(47));
		Element cu = pedersenCS.commit(u, r0);
		Element cv = pedersenCS.commit(v, t);

		Tuple secretInput = Tuple.getInstance(u, v, r0, t);
		Tuple publicInput = Tuple.getInstance(cu, cv);

		PolynomialEvaluationProofSystem peps = PolynomialEvaluationProofSystem.getInstance(poly, pedersenCS);
		Tuple proof = peps.generate(secretInput, publicInput);
		boolean verify = peps.verify(proof, publicInput);

		assertTrue(verify);
	}

	@Test
	public void testPolynomialEvalutaionProofSystem3() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(P1);
		final ZModPrime Z_q = (ZModPrime) G_q.getZModOrder();

		PedersenCommitmentScheme pedersenCS = PedersenCommitmentScheme.getInstance(G_q);

		for (int i = 2; i < 33; i++) {

			PolynomialRing pr = PolynomialRing.getInstance(Z_q);
			PolynomialElement poly = pr.getElement(ProductGroup.getInstance(Z_q, i + 1).getRandomElement());

			ZModElement u = Z_q.getRandomElement();
			Element v = poly.evaluate(u);

			Element r0 = Z_q.getRandomElement();
			Element t = Z_q.getRandomElement();
			Element cu = pedersenCS.commit(u, r0);
			Element cv = pedersenCS.commit(v, t);

			Tuple secretInput = Tuple.getInstance(u, v, r0, t);
			Tuple publicInput = Tuple.getInstance(cu, cv);

			PolynomialEvaluationProofSystem peps = PolynomialEvaluationProofSystem.getInstance(poly, pedersenCS);
			Tuple proof = peps.generate(secretInput, publicInput);
			boolean verify = peps.verify(proof, publicInput);

			assertTrue(verify);
		}

	}

	@Test
	public void testPolynomialEvalutaionProofSystem4() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final ZModPrime Z_q = (ZModPrime) G_q.getZModOrder();

		PedersenCommitmentScheme pedersenCS = PedersenCommitmentScheme.getInstance(G_q);

		int size = 100;

		PolynomialRing pr = PolynomialRing.getInstance(Z_q);
		PolynomialElement poly = pr.getElement(ProductGroup.getInstance(Z_q, size + 1).getRandomElement());

		ZModElement u = Z_q.getRandomElement();
		Element v = poly.evaluate(u);

		Element r0 = Z_q.getRandomElement();
		Element t = Z_q.getRandomElement();
		Element cu = pedersenCS.commit(u, r0);
		Element cv = pedersenCS.commit(v, t);

		Tuple secretInput = Tuple.getInstance(u, v, r0, t);
		Tuple publicInput = Tuple.getInstance(cu, cv);

		PolynomialEvaluationProofSystem peps = PolynomialEvaluationProofSystem.getInstance(poly, pedersenCS);

		Tuple proof = peps.generate(secretInput, publicInput);
		boolean verify = peps.verify(proof, publicInput);

		assertTrue(verify);
	}

}
