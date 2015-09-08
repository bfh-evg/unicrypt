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
package ch.bfh.unicrypt.helper.math;

import java.math.BigInteger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class PermutationTest {

	public PermutationTest() {
	}

	@Test
	public void generalTest() {
		Permutation p0 = Permutation.getInstance(0);
		Assert.assertEquals(0, p0.getSize());
		Assert.assertEquals(p0, p0);
		Assert.assertEquals(p0, p0.invert());
		Assert.assertEquals(p0, p0.compose(p0));
		Assert.assertEquals(p0, Permutation.getInstance(new int[]{}));

		Permutation p1 = Permutation.getInstance(new int[]{1, 3, 5, 2, 0, 4});
		Assert.assertEquals(6, p1.getSize());
		Assert.assertEquals(1, p1.permute(0));
		Assert.assertEquals(3, p1.permute(1));
		Assert.assertEquals(5, p1.permute(2));
		Assert.assertEquals(2, p1.permute(3));
		Assert.assertEquals(0, p1.permute(4));
		Assert.assertEquals(4, p1.permute(5));
		Assert.assertEquals(p1, Permutation.getInstance(6, p1.getRank()));

		Permutation p2 = p1.invert();
		Assert.assertEquals(6, p2.getSize());
		Assert.assertEquals(4, p2.permute(0));
		Assert.assertEquals(0, p2.permute(1));
		Assert.assertEquals(3, p2.permute(2));
		Assert.assertEquals(1, p2.permute(3));
		Assert.assertEquals(5, p2.permute(4));
		Assert.assertEquals(2, p2.permute(5));
		Permutation p3 = p1.compose(p1.invert());
		Assert.assertEquals(6, p3.getSize());
		Assert.assertEquals(0, p3.permute(0));
		Assert.assertEquals(1, p3.permute(1));
		Assert.assertEquals(2, p3.permute(2));
		Assert.assertEquals(3, p3.permute(3));
		Assert.assertEquals(4, p3.permute(4));
		Assert.assertEquals(5, p3.permute(5));
		Permutation p4 = p1.invert().compose(p1);
		Assert.assertEquals(6, p4.getSize());
		Assert.assertEquals(0, p4.permute(0));
		Assert.assertEquals(1, p4.permute(1));
		Assert.assertEquals(2, p4.permute(2));
		Assert.assertEquals(3, p4.permute(3));
		Assert.assertEquals(4, p4.permute(4));
		Assert.assertEquals(5, p4.permute(5));

		Permutation p5 = Permutation.getInstance(6);
		Assert.assertEquals(6, p5.getSize());
		Assert.assertEquals(0, p5.permute(0));
		Assert.assertEquals(1, p5.permute(1));
		Assert.assertEquals(2, p5.permute(2));
		Assert.assertEquals(3, p5.permute(3));
		Assert.assertEquals(4, p5.permute(4));
		Assert.assertEquals(5, p5.permute(5));
	}

	@Test
	public void testRank() {
		Permutation[] ps = new Permutation[24];
		for (int i = 0; i < 24; i++) {
			Permutation p = Permutation.getInstance(4, BigInteger.valueOf(i));
			Assert.assertEquals(i, p.getRank().intValue());
			ps[i] = p;
		}
		for (Permutation p1 : ps) {
			for (Permutation p2 : ps) {
				if (p1 != p2) {
					Assert.assertFalse(p1.equals(p2));
				}
			}
		}

	}

}
