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
package ch.bfh.unicrypt.math.algebra.multiplicative;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.random.classes.PseudoRandomOracle;
import ch.bfh.unicrypt.random.classes.ReferenceRandomByteSequence;
import java.math.BigInteger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class GStarModSafePrimeTest {

	@Test
	public void testIteration() {
		GStarModSafePrime set = GStarModSafePrime.getInstance(BigInteger.valueOf(23));
		for (Element element : set.getElements()) {
//			System.out.println(element);
		}
	}

	@Test
	public void testGetIndependentGenerators1() {
		ReferenceRandomByteSequence rrs = ReferenceRandomByteSequence.getInstance();
		GStarModSafePrime set = GStarModSafePrime.getInstance(BigInteger.valueOf(23));
		Element g1 = set.getIndependentGenerator(3, rrs);
		Element g2 = set.getIndependentGenerator(5, rrs);
		Element g3 = set.getIndependentGenerator(2, rrs);
		Tuple gs1 = set.getIndependentGenerators(20, rrs);
		Assert.assertTrue(gs1.getAt(3).isEquivalent(g1));
		Assert.assertTrue(gs1.getAt(5).isEquivalent(g2));
		Assert.assertTrue(gs1.getAt(2).isEquivalent(g3));
		Tuple gs2 = set.getIndependentGenerators(2, 10, rrs);
		Assert.assertTrue(gs2.getAt(1).isEquivalent(g1));
		Assert.assertTrue(gs2.getAt(3).isEquivalent(g2));
		Assert.assertTrue(gs2.getAt(0).isEquivalent(g3));
//		System.out.println(g1);
//		System.out.println(g2);
//		System.out.println(g3);
//		System.out.println("Generators:");
//		for (int i = 0; i < gs1.length; i++) {
//			System.out.println(gs1[i]);
//		}
	}

	@Test
	public void testGetIndependentGenerators2() {
		ReferenceRandomByteSequence rrs = PseudoRandomOracle.getInstance().query(ByteArray.getInstance(new byte[]{2, 5}));
		GStarModSafePrime set = GStarModSafePrime.getInstance(BigInteger.valueOf(23));
		Element g1 = set.getIndependentGenerator(3, rrs);
		Element g2 = set.getIndependentGenerator(5, rrs);
		Element g3 = set.getIndependentGenerator(2, rrs);
		Tuple gs1 = set.getIndependentGenerators(20, rrs);
		Assert.assertTrue(gs1.getAt(3).isEquivalent(g1));
		Assert.assertTrue(gs1.getAt(5).isEquivalent(g2));
		Assert.assertTrue(gs1.getAt(2).isEquivalent(g3));
		Tuple gs2 = set.getIndependentGenerators(2, 10, rrs);
		Assert.assertTrue(gs2.getAt(1).isEquivalent(g1));
		Assert.assertTrue(gs2.getAt(3).isEquivalent(g2));
		Assert.assertTrue(gs2.getAt(0).isEquivalent(g3));
	}

}
