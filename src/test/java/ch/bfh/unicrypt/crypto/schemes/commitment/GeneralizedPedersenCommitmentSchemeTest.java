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
package ch.bfh.unicrypt.crypto.schemes.commitment;

import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.crypto.schemes.commitment.classes.GeneralizedPedersenCommitmentScheme;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.random.RandomOracle;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class GeneralizedPedersenCommitmentSchemeTest {

	final static int P = 23;
	final private GStarModSafePrime G_q;
	final private ZMod Z_q;

	public GeneralizedPedersenCommitmentSchemeTest() {
		this.G_q = GStarModSafePrime.getInstance(this.P);
		this.Z_q = G_q.getZModOrder();
	}

	@Test
	public void testGeneralizedPedersenCommitment2() {

		DeterministicRandomByteSequence rrs = RandomOracle.getInstance().query(ByteArray.getInstance("X".getBytes()));
		// System.out.println("-->g0: " + this.G_q.getIndependentGenerator(0, rrs));   //  2  4
		// System.out.println("-->g1: " + this.G_q.getIndependentGenerator(1, rrs));   // 16  3
		// System.out.println("-->g2: " + this.G_q.getIndependentGenerator(2, rrs));   //  4  9

		GeneralizedPedersenCommitmentScheme gpcs = GeneralizedPedersenCommitmentScheme.getInstance(G_q, 2, rrs);
		Tuple messages = Tuple.getInstance(Z_q.getElement(1), Z_q.getElement(3));
		Element r = Z_q.getElement(2);
		Element c = gpcs.commit(messages, r);   // c = g1^m1 * g2^m2 * g0^r = 3^1 * 9^3 * 4^2 = 3 * 16 * 16 = 9
		Element verification = G_q.getIndependentGenerators(rrs).get(1).selfApply(Z_q.getElement(1)).apply(
			   G_q.getIndependentGenerators(rrs).get(2).selfApply(Z_q.getElement(3))).apply(
					  G_q.getIndependentGenerators(rrs).get(0).selfApply(Z_q.getElement(2)));
		assertTrue(c.isEquivalent(verification));

		Element randomizationGenerator = G_q.getIndependentGenerators(rrs).get(0);
		Tuple messageGenerators = Tuple.getInstance(G_q.getIndependentGenerators(rrs).get(1), G_q.getIndependentGenerators(rrs).get(2));
		gpcs = GeneralizedPedersenCommitmentScheme.getInstance(randomizationGenerator, messageGenerators);
		Element cc = gpcs.commit(messages, r);
		assertEquals(c, cc);
	}

	@Test
	public void testGeneralizedPedersenCommitment4() {

		DeterministicRandomByteSequence rrs = RandomOracle.getInstance().query(ByteArray.getInstance("X".getBytes()));
		// System.out.println("-->g0: " + this.G_q.getIndependentGenerator(0, rrs));   //  2  4
		// System.out.println("-->g1: " + this.G_q.getIndependentGenerator(1, rrs));   // 16  3
		// System.out.println("-->g2: " + this.G_q.getIndependentGenerator(2, rrs));   //  4  9
		// System.out.println("-->g3: " + this.G_q.getIndependentGenerator(3, rrs));   //  6  8
		// System.out.println("-->g4: " + this.G_q.getIndependentGenerator(4, rrs));   //  8 18

		GeneralizedPedersenCommitmentScheme gpcs = GeneralizedPedersenCommitmentScheme.getInstance(G_q, 4, rrs);
		Tuple messages = Tuple.getInstance(Z_q.getElement(1), Z_q.getElement(3), Z_q.getElement(4), Z_q.getElement(5));
		Element r = Z_q.getElement(3);
		Element c = gpcs.commit(messages, r); // c = 3^1 * 9^3 * 8^4 * 18^5 * 4^3 = 3 * 16 * 2 * 3 * 18 = 9
		Element verification = G_q.getIndependentGenerators(rrs).get(1).selfApply(Z_q.getElement(1)).apply(
			   G_q.getIndependentGenerators(rrs).get(2).selfApply(Z_q.getElement(3))).apply(
					  G_q.getIndependentGenerators(rrs).get(3).selfApply(Z_q.getElement(4))).apply(
					  G_q.getIndependentGenerators(rrs).get(4).selfApply(Z_q.getElement(5))).apply(
					  G_q.getIndependentGenerators(rrs).get(0).selfApply(Z_q.getElement(3)));
		assertTrue(c.isEquivalent(verification));

		Element randomizationGenerator = G_q.getIndependentGenerators(rrs).get(0);
		Tuple messageGenerators = Tuple.getInstance(G_q.getIndependentGenerators(rrs).get(1), G_q.getIndependentGenerators(rrs).get(2), G_q.getIndependentGenerators(rrs).get(3), G_q.getIndependentGenerators(rrs).get(4));
		gpcs = GeneralizedPedersenCommitmentScheme.getInstance(randomizationGenerator, messageGenerators);
		Element cc = gpcs.commit(messages, r);
		assertEquals(c, cc);
	}

	@Test(expected = UniCryptRuntimeException.class)
	public void testGeneralizedPedersenCommitment_Exception() {
		// Commitment size does not match with number of messages
		GeneralizedPedersenCommitmentScheme gpcs = GeneralizedPedersenCommitmentScheme.getInstance(G_q, 3);
		Tuple messages = Tuple.getInstance(Z_q.getElement(1), Z_q.getElement(3));
		Element r = Z_q.getElement(2);
		Element c = gpcs.commit(messages, r);

	}

}
