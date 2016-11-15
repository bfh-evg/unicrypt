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

import ch.bfh.unicrypt.helper.array.classes.DenseArray;
import java.util.ArrayList;
import java.util.Iterator;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author R. Haenni <rolf.haenni@bfh.ch>
 */
public class DenseArrayTest {

	private static DenseArray<String> a0 = DenseArray.getInstance();
	private static DenseArray<String> a1 = DenseArray.getInstance("s1", "s2", "s3");
	private static DenseArray<String> a2 = DenseArray.getInstance("s1", "s1", "s1");
	private static DenseArray<String> a3 = DenseArray.getInstance("s1", 3);

	@Test
	public void testGetLength() {
		assertEquals(0, a0.getLength());
		assertEquals(3, a1.getLength());
		assertEquals(3, a2.getLength());
		assertEquals(3, a3.getLength());
	}

	@Test
	public void testGetAt() {
		assertEquals("s1", a1.getAt(0));
		assertEquals("s2", a1.getAt(1));
		assertEquals("s3", a1.getAt(2));
		assertEquals("s1", a2.getAt(0));
		assertEquals("s1", a2.getAt(1));
		assertEquals("s1", a2.getAt(2));
		assertEquals("s1", a3.getAt(0));
		assertEquals("s1", a3.getAt(1));
		assertEquals("s1", a3.getAt(2));
	}

	@Test
	public void testRemoveAt() {
		assertEquals(a0, a1.removeAt(2).removeAt(0).removeAt(0));
		assertEquals(a0, a2.removeAt(1).removeAt(1).removeAt(0));
		assertEquals(a0, a3.removeAt(0).removeAt(1).removeAt(0));
		assertEquals(a1.removeAt(1).removeAt(1), a2.removeAt(2).removeAt(0));
		assertEquals(a1.removeAt(1).removeAt(1), a3.removeAt(2).removeAt(0));
	}

	@Test
	public void testInsertAt() {
		assertEquals(a1, a0.insertAt(0, "s3").insertAt(0, "s2").insertAt(0, "s1"));
		assertEquals(a1, a0.insertAt(0, "s2").insertAt(1, "s3").insertAt(0, "s1"));
		assertEquals(a1, a0.insertAt(0, "s1").insertAt(1, "s3").insertAt(1, "s2"));
	}

	@Test
	public void testReplaceAt() {
		assertEquals(a2, a1.replaceAt(1, "s1").replaceAt(2, "s1"));
	}

	@Test
	public void testAdd() {
		assertEquals(a1, a0.add("s1").add("s2").add("s3"));
		assertEquals(a2, a0.add("s1").add("s1").add("s1"));
		assertEquals(a3, a0.add("s1").add("s1").add("s1"));
	}

	@Test
	public void testIterator() {
		Iterator<String> it2 = a2.iterator();
		Iterator<String> it3 = a3.iterator();
		while (it2.hasNext()) {
			assertEquals(it2.next(), it3.next());
		}
	}

	@Test
	public void testEquals() {
		assertTrue(a0.equals(a0));
		assertFalse(a0.equals(a1));
		assertFalse(a0.equals(a2));
		assertFalse(a0.equals(a3));
		assertTrue(a1.equals(a1));
		assertFalse(a1.equals(a2));
		assertFalse(a1.equals(a3));
		assertTrue(a2.equals(a2));
		assertTrue(a2.equals(a3));
		assertTrue(a3.equals(a3));

	}

	@Test
	public void testAppend() {
		assertEquals(a0, a0.append(a0));
		assertEquals(a1, a0.append(a1));
		assertEquals(a1, a1.append(a0));
		assertEquals(a2, a0.append(a2));
		assertEquals(a2, a2.append(a0));
		assertEquals(a3, a0.append(a3));
		assertEquals(a3, a3.append(a0));
		assertEquals(a3, a3.append(a0));
		assertEquals(6, a1.append(a1).getLength());
		assertEquals(6, a1.append(a2).getLength());
		assertEquals(a2.append(a2), a3.append(a3));
		assertEquals(a2.append(a3), a3.append(a2));
	}

	@Test
	public void testGetInstanceByCollectionException() {
		ArrayList list1 = new ArrayList();
		list1.add("s1");
		list1.add(null);
		list1.add("s3");
		DenseArray a = DenseArray.getInstance(list1);
		assertEquals(2, a.getLength());
	}

	@Test
	public void testCount() {
		DenseArray<String> da = DenseArray.getInstance("a", "b", "", "c", "", "", "e", "a");
		Assert.assertEquals(2, da.count("a"));
		Assert.assertEquals(0, da.count("x"));
		Assert.assertEquals(6, da.countExcept("a"));
		{
			int i = 0;
			for (Integer index : da.getAllIndices()) {
				i++;
			}
			Assert.assertEquals(8, i);
		}
	}

	@Test
	public void testIndices() {
		DenseArray<String> sa = DenseArray.getInstance("", "a", "b", "", "c", "", "", "e", "a");
		{
			int i = 0;
			for (Integer index : sa.getAllIndices()) {
				i++;
			}
			Assert.assertEquals(9, i);
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
			for (Integer index : sa.getIndicesExcept("a")) {
				i++;
			}
			Assert.assertEquals(7, i);
		}
	}

}
