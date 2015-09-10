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

import ch.bfh.unicrypt.crypto.proofsystem.classes.PolynomialMembershipProofSystem;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import java.math.BigInteger;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author philipp
 */
public class PolynomialMembershipProofSystemTest {

	final static int P1 = 167;
	final static String P2 = "88059184022561109274134540595138392753102891002065208740257707896840303297223";

	@Test
	public void testPolynomialMembershipProofSystem() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(P1);
		final ZModPrime Z_q = (ZModPrime) G_q.getZModOrder();

		PedersenCommitmentScheme pedersenCS = PedersenCommitmentScheme.getInstance(G_q);

		Subset members = Subset.getInstance(Z_q, Z_q.getElement(BigInteger.valueOf(15)), Z_q.getElement(BigInteger.valueOf(45)), Z_q.getElement(BigInteger.valueOf(37)), Z_q.getElement(BigInteger.valueOf(75)));

		ZModElement u = Z_q.getElement(BigInteger.valueOf(15));

		Element r0 = Z_q.getElement(BigInteger.valueOf(56));
		Element cu = pedersenCS.commit(u, r0);

		Tuple secretInput = Tuple.getInstance(u, r0);
		Element publicInput = cu;

		PolynomialMembershipProofSystem pmps = PolynomialMembershipProofSystem.getInstance(members, pedersenCS);
		Tuple proof = pmps.generate(secretInput, publicInput);
		boolean verify = pmps.verify(proof, publicInput);

		assertTrue(verify);
	}

	@Test
	public void testPolynomialMembershipProofSystem2() {

		final CyclicGroup G_q = GStarModSafePrime.getInstance(new BigInteger(P2, 10));
		final ZModPrime Z_q = (ZModPrime) G_q.getZModOrder();

		PedersenCommitmentScheme pedersenCS = PedersenCommitmentScheme.getInstance(G_q);

		int size = 10;
		Tuple members = ProductGroup.getInstance(Z_q, size).getRandomElement();
		Element[] mem = new Element[size];
		for (int i = 0; i < members.getArity(); i++) {
			mem[i] = members.getAt(i);
		}
		PolynomialMembershipProofSystem pmps = PolynomialMembershipProofSystem.getInstance(Subset.getInstance(Z_q, mem), pedersenCS);

		for (int i = 0; i < members.getArity(); i++) {
			Element u = members.getAt(i);

			Element r0 = Z_q.getRandomElement();
			Element cu = pedersenCS.commit(u, r0);

			Tuple secretInput = Tuple.getInstance(u, r0);
			Element publicInput = cu;

			Tuple proof = pmps.generate(secretInput, publicInput);
			boolean verify = pmps.verify(proof, publicInput);

			assertTrue(verify);
		}
	}

	@Test
	public void testPolynomialMembershipProofSystem3() {

		final RandomByteSequence deterministicRandomByteSequence = DeterministicRandomByteSequence.getInstance();

		final CyclicGroup G_q = GStarModSafePrime.getInstance(P1);
		final ZModPrime Z_q = (ZModPrime) G_q.getZModOrder();

		PedersenCommitmentScheme pedersenCS = PedersenCommitmentScheme.getInstance(G_q);

		Subset members = Subset.getInstance(Z_q, Z_q.getElement(BigInteger.valueOf(15)), Z_q.getElement(BigInteger.valueOf(45)), Z_q.getElement(BigInteger.valueOf(37)), Z_q.getElement(BigInteger.valueOf(75)));
		PolynomialMembershipProofSystem pmps = PolynomialMembershipProofSystem.getInstance(members, pedersenCS);

		Element[] nonMem = new Element[]{Z_q.getElement(BigInteger.valueOf(21)), Z_q.getElement(BigInteger.valueOf(43)), Z_q.getElement(BigInteger.valueOf(18))};

		for (Element u : nonMem) {
			Element r0 = Z_q.getRandomElement(deterministicRandomByteSequence);
			Element cu = pedersenCS.commit(u, r0);

			Tuple secretInput = Tuple.getInstance(u, r0);
			Element publicInput = cu;

			Tuple proof = pmps.generate(secretInput, publicInput, deterministicRandomByteSequence);
			boolean verify = pmps.verify(proof, publicInput);

			assertTrue(!verify);
		}
	}

}
