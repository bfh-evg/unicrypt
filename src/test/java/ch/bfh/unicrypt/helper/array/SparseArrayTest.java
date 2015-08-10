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
package ch.bfh.unicrypt.helper.array;

import ch.bfh.unicrypt.helper.array.classes.SparseArray;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author R. Haenni <rolf.haenni@bfh.ch>
 */
public class SparseArrayTest {

	@Test
	public void generalTest() {

		SparseArray<Integer> sparseArray = SparseArray.getInstance(0, new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
		List<SparseArray<Integer>> sparseArrays = new ArrayList<>();
		sparseArrays.add(SparseArray.getInstance(0, new Integer[]{0, 0, 0, 8, 7, 5, 4, 3, 255, 255}));
		sparseArrays.add(sparseArray.reverse().extract(1, 6).shiftRight(3).removeAt(5).append(SparseArray.getInstance(0, new Integer[]{255, 255})));
		sparseArrays.add(sparseArray.reverse().extract(1, 6).shiftRight(3).removeAt(5).append(SparseArray.getInstance(255, 2)));
		sparseArrays.add(sparseArray.extract(3, 6).removeAt(3).reverse().append(SparseArray.getInstance(0, new Integer[]{255, 255})).shiftRight(3));
		sparseArrays.add(SparseArray.getInstance(0, new Integer[]{0, 0, 0, 8, 7, 5, 4, 3, 255, 254}).replaceAt(9, 255));
		sparseArrays.add(SparseArray.getInstance(0, new Integer[]{0, 0, 0, 8, 7, 5, 4, 255, 255}).insertAt(7, 3));

		for (SparseArray<Integer> s1 : sparseArrays) {
			for (SparseArray<Integer> s2 : sparseArrays) {
				assertEquals(s1, s2);
				assertEquals(s1.getLength(), s2.getLength());
				assertEquals(s1.append(s1), s2.append(s1));
				assertEquals(s1.append(s1), s2.append(s2));
				assertEquals(s1.append(s2), s2.append(s1));
				assertEquals(s1.append(s2), s2.append(s2));
				assertEquals(s1.append(s1), s2.append(s2));
				assertEquals(s1.countSuffix(), s2.countSuffix());
				assertEquals(s1.countPrefix(), s2.countPrefix());
				for (int i = 0; i < s1.getLength(); i++) {
					assertEquals(s1.removeAt(i), s2.removeAt(i));
				}
				assertEquals(s1.reverse(), s2.reverse());
				for (int i = 0; i <= s1.getLength() + 2; i++) {
					assertEquals(s1.shiftLeft(i), s2.shiftLeft(i));
					assertEquals(s1.shiftRight(i), s2.shiftRight(i));
				}
				assertEquals(s1.removeSuffix(), s2.removeSuffix());
				assertEquals(s1.removePrefix(), s2.removePrefix());
				for (int i = 0; i < s1.getLength(); i++) {
					assertEquals(s1.removePrefix(i), s2.removePrefix(i));
					assertEquals(s1.removeSuffix(i), s2.removeSuffix(i));
				}
				for (int i = 0; i < s1.getLength(); i++) {
					for (int j = 0; j < s1.getLength() - i + 1; j++) {
						assertEquals(s1.extract(i, j), s2.extract(i, j));
					}
				}

			}
		}
	}

	@Test
	public void testSparseArray() {
		SparseArray<String> a = SparseArray.getInstance("", 12, "Test");
		Assert.assertEquals(13, a.getLength());
		Assert.assertEquals("", a.getAt(0));
		Assert.assertEquals("Test", a.getAt(12));

		SparseArray<String> b = SparseArray.getInstance("", a.getSequence());
		Assert.assertEquals(a, b);
	}

	@Test
	public void testCount() {
		SparseArray<String> sa = SparseArray.getInstance("", "a", "b", "", "c", "", "", "e", "a");
		Assert.assertEquals(3, sa.count());
		Assert.assertEquals(2, sa.count("a"));
		Assert.assertEquals(0, sa.count("x"));
		Assert.assertEquals(5, sa.countExcept());
		Assert.assertEquals(6, sa.countExcept("a"));
		{
			int i = 0;
			for (Integer index : sa.getAllIndices()) {
				i++;
			}
			Assert.assertEquals(8, i);
		}
	}

	@Test
	public void testIndices() {
		SparseArray<String> sa = SparseArray.getInstance("", "a", "b", "", "c", "", "", "e", "a");
		{
			int i = 0;
			for (Integer index : sa.getAllIndices()) {
				i++;
			}
			Assert.assertEquals(8, i);
		}
		{
			int i = 0;
			for (Integer index : sa.getIndices()) {
				i++;
			}
			Assert.assertEquals(3, i);
		}
		{
			int i = 0;
			for (Integer index : sa.getIndices("a")) {
				i++;
			}
			Assert.assertEquals(2, i);
		}
		{
			int i = 0;
			for (Integer index : sa.getIndicesExcept()) {
				i++;
			}
			Assert.assertEquals(5, i);
		}
		{
			int i = 0;
			for (Integer index : sa.getIndicesExcept("a")) {
				i++;
			}
			Assert.assertEquals(6, i);
		}
	}

}
