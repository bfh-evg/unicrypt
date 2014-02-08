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
package ch.bfh.unicrypt.math.helper.numerical;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class NaturalNumberTest {

	private final NaturalNumber n0 = NaturalNumber.getInstance(0);
	private final NaturalNumber n1 = NaturalNumber.getInstance(1);
	private final NaturalNumber n2 = NaturalNumber.getInstance(2);
	private final NaturalNumber n3 = NaturalNumber.getInstance(3);
	private final NaturalNumber n9 = NaturalNumber.getInstance(9);
	private final WholeNumber w_5 = WholeNumber.getInstance(-5);

	public NaturalNumberTest() {
	}

	@Test
	public void testAdd() {
		assertEquals(0, n0.add(n0).getBigInteger().intValue());
		assertEquals(1, n0.add(n1).getBigInteger().intValue());
		assertEquals(5, n2.add(n3).getBigInteger().intValue());
		assertEquals(18, n9.add(n9).getBigInteger().intValue());
		assertEquals(4, n9.add(w_5).getBigInteger().intValue());
		assertEquals(NaturalNumber.class, n9.add(w_5).getClass());
		assertEquals(NaturalNumber.class, w_5.add(n9).getClass());
	}

	@Test
	public void testMultiply() {
		assertEquals(0, n0.multiply(n0).getBigInteger().intValue());
		assertEquals(0, n0.multiply(n1).getBigInteger().intValue());
		assertEquals(6, n2.multiply(n3).getBigInteger().intValue());
		assertEquals(81, n9.multiply(n9).getBigInteger().intValue());
		assertEquals(-45, n9.multiply(w_5).getBigInteger().intValue());
		assertEquals(25, w_5.multiply(w_5).getBigInteger().intValue());
		assertEquals(NaturalNumber.class, w_5.multiply(w_5).getClass());
		assertFalse(NaturalNumber.class.equals(n9.multiply(w_5).getClass()));
		assertFalse(NaturalNumber.class.equals(w_5.multiply(n9).getClass()));
	}

	@Test
	public void testSquare() {
		assertEquals(0, n0.square().getBigInteger().intValue());
		assertEquals(1, n1.square().getBigInteger().intValue());
		assertEquals(4, n2.square().getBigInteger().intValue());
		assertEquals(81, n9.square().getBigInteger().intValue());
	}

	@Test
	public void testPower() {
		NaturalNumber e0 = NaturalNumber.getInstance(0);
		NaturalNumber e1 = NaturalNumber.getInstance(1);
		NaturalNumber e2 = NaturalNumber.getInstance(2);
		NaturalNumber e3 = NaturalNumber.getInstance(3);
		assertEquals(1, n0.power(e0).getBigInteger().intValue());
		assertEquals(0, n0.power(e1).getBigInteger().intValue());
		assertEquals(0, n0.power(e1).getBigInteger().intValue());
		assertEquals(1, n1.power(e0).getBigInteger().intValue());
		assertEquals(1, n1.power(e1).getBigInteger().intValue());
		assertEquals(1, n1.power(e1).getBigInteger().intValue());
		assertEquals(1, n2.power(e0).getBigInteger().intValue());
		assertEquals(2, n2.power(e1).getBigInteger().intValue());
		assertEquals(4, n2.power(e2).getBigInteger().intValue());
		assertEquals(8, n2.power(e3).getBigInteger().intValue());
		assertEquals(8, n2.power(NaturalNumber.getInstance(3)).getBigInteger().intValue());
	}

	@Test
	public void testMinus() {
		assertEquals(0, n0.negate().getBigInteger().intValue());
		assertEquals(-1, n1.negate().getBigInteger().intValue());
		assertEquals(-2, n2.negate().getBigInteger().intValue());
		assertEquals(NaturalNumber.class, n0.negate().getClass());
		assertEquals(WholeNumber.class, n2.negate().getClass());
	}

	@Test
	public void testSubtract() {
		assertEquals(0, n0.subtract(n0).getBigInteger().intValue());
		assertEquals(-1, n0.subtract(n1).getBigInteger().intValue());
		assertEquals(1, n1.subtract(n0).getBigInteger().intValue());
		assertEquals(0, n9.subtract(n9).getBigInteger().intValue());
		assertEquals(-14, w_5.subtract(n9).getBigInteger().intValue());
		assertEquals(14, n9.subtract(w_5).getBigInteger().intValue());
	}

	@Test
	public void testIsEqual() {
		assertTrue(n0.isEqual(n0));
		assertFalse(n0.isEqual(n1));
		assertTrue(n0.isEqual(n1.subtract(n1)));
	}

	@Test
	public void testIsSmallerThan() {
		assertFalse(n0.isSmallerThan(n0));
		assertTrue(n0.isSmallerThan(n1));
		assertFalse(n1.isSmallerThan(n0));
	}

	@Test
	public void testIsGreaterThan() {
		assertFalse(n0.isGreaterThan(n0));
		assertFalse(n0.isGreaterThan(n1));
		assertTrue(n1.isGreaterThan(n0));
	}

}
