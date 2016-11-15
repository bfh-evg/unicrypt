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

import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PermutationCommitmentScheme;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.math.Permutation;
import ch.bfh.unicrypt.helper.random.RandomOracle;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class PermutationCommitmentTest {

	final static int P = 23;
	final private GStarModSafePrime G_q;
	final private ZMod Z_q;
	final private DeterministicRandomByteSequence rrs;

	public PermutationCommitmentTest() {
		this.G_q = GStarModSafePrime.getInstance(PermutationCommitmentTest.P);
		this.Z_q = G_q.getZModOrder();
		this.rrs = RandomOracle.getInstance().query(ByteArray.getInstance("X".getBytes()));
		// System.out.println("g0: " + this.G_q.getIndependentGenerator(0, rrs));   //  2  4
		// System.out.println("g1: " + this.G_q.getIndependentGenerator(1, rrs));   // 16  3
		// System.out.println("g2: " + this.G_q.getIndependentGenerator(2, rrs));   //  4  9
		// System.out.println("g3: " + this.G_q.getIndependentGenerator(3, rrs));   //  6  8
		// System.out.println("g4: " + this.G_q.getIndependentGenerator(4, rrs));   //  8 18
	}

	@Test
	public void testPermuationCommitment1() {

		Permutation pi = Permutation.getInstance(new int[]{0});

		PermutationElement permutation = PermutationGroup.getInstance(pi.getSize()).getElement(pi);
		Tuple randomizations = Tuple.getInstance(this.Z_q.getElement(2));

		PermutationCommitmentScheme cp = PermutationCommitmentScheme.getInstance(this.G_q, pi.getSize(), this.rrs);
		Tuple commitment = cp.commit(permutation, randomizations);
		Element verification = this.G_q.getIndependentGenerators(this.rrs).get(0).selfApply(2).apply(
			   this.G_q.getIndependentGenerators(this.rrs).get(1));
		assertTrue(commitment.getAt(0).isEquivalent(verification));   // 4^2 * 3 = 2
	}

	@Test
	public void testPermuationCommitment3() {

		Permutation pi = Permutation.getInstance(new int[]{2, 0, 1});   // invert: [1, 2, 0]
		assertTrue(pi.permute(1) == 0);
		assertTrue(pi.permute(0) == 2);

		PermutationElement permutation = PermutationGroup.getInstance(pi.getSize()).getElement(pi);
		Tuple randomizations = Tuple.getInstance(this.Z_q.getElement(1), this.Z_q.getElement(2), this.Z_q.getElement(3));

		PermutationCommitmentScheme cp = PermutationCommitmentScheme.getInstance(this.G_q, pi.getSize(), this.rrs);
		Tuple commitment = cp.commit(permutation, randomizations);
		Element verification1 = this.G_q.getIndependentGenerators(rrs).get(0).selfApply(1).apply(
			   this.G_q.getIndependentGenerators(rrs).get(2));
		Element verification2 = this.G_q.getIndependentGenerators(rrs).get(0).selfApply(2).apply(
			   this.G_q.getIndependentGenerators(rrs).get(3));
		Element verification3 = this.G_q.getIndependentGenerators(rrs).get(0).selfApply(3).apply(
			   this.G_q.getIndependentGenerators(rrs).get(1));

		assertTrue(commitment.getAt(0).isEquivalent(verification1));    // 4^1 * 9 =  13
		assertTrue(commitment.getAt(1).isEquivalent(verification2));    // 4^2 * 8 =  13
		assertTrue(commitment.getAt(2).isEquivalent(verification3));   // 4^3 * 3 = 8
	}

	@Test
	public void testPermuationCommitment4() {

		Permutation pi = Permutation.getInstance(new int[]{2, 3, 1, 0});  // invert: [3, 2, 0, 1]

		PermutationElement permutation = PermutationGroup.getInstance(pi.getSize()).getElement(pi);
		Tuple randomizations = Tuple.getInstance(this.Z_q.getElement(1), this.Z_q.getElement(2), this.Z_q.getElement(3), this.Z_q.getElement(4));

		PermutationCommitmentScheme cp = PermutationCommitmentScheme.getInstance(this.G_q, pi.getSize(), this.rrs);
		Tuple commitment = cp.commit(permutation, randomizations);
//TODO:		assertTrue(commitment.getByteAt(0).isEquivalent(this.G_q.getElement(3)));  // 4^1 * 18 =  3
//TODO:		assertTrue(commitment.getByteAt(1).isEquivalent(this.G_q.getElement(13))); // 4^2 *  8 = 13
//TODO:		assertTrue(commitment.getByteAt(2).isEquivalent(this.G_q.getElement(8)));  // 4^3 *  3 =  8
//TODO:		assertTrue(commitment.getByteAt(3).isEquivalent(this.G_q.getElement(4)));  // 4^4 *  9 =  4
	}

}
