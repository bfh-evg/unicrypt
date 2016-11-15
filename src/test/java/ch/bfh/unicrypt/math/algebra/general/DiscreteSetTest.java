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
package ch.bfh.unicrypt.math.algebra.general;

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.math.algebra.general.classes.EnumeratedSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni <rolf.haenni@bfh.ch>
 */
public class DiscreteSetTest {

	private static EnumeratedSet<String> ds = EnumeratedSet.getInstance("John", "Mary", "Bob");

	@Test
	public void testStandardToStringContent() {
		// System.out.println(ds);
	}

	@Test
	public void testContains() {
		assertTrue(ds.contains("John"));
		assertTrue(ds.contains("Mary"));
		assertTrue(ds.contains("Bob"));
		assertFalse(ds.contains("Paul"));
		assertFalse(ds.contains(""));
	}

	@Test
	public void testGetOrder() {
		assertEquals(3, ds.getOrder().intValue());
	}

	@Test
	public void testGetElement() {
		assertEquals("John", ds.getElement("John").getValue());
		assertEquals("Bob", ds.getElement("Bob").getValue());
	}

	@Test
	public void testGetBigInteger() {
		try {
			assertEquals("John", ds.getElementFrom(ds.getElement("John").convertToBigInteger()).getValue());
		} catch (UniCryptException ex) {
			fail();
		}
	}

}
