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
package ch.bfh.unicrypt.helper.numerical;

import ch.bfh.unicrypt.helper.numerical.WholeNumber;
import ch.bfh.unicrypt.helper.numerical.NaturalNumber;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class WholeNumberTest {

	private final WholeNumber w0 = WholeNumber.getInstance(0);
	private final WholeNumber w1 = WholeNumber.getInstance(1);
	private final WholeNumber w2 = WholeNumber.getInstance(2);
	private final WholeNumber w3 = WholeNumber.getInstance(3);
	private final WholeNumber w9 = WholeNumber.getInstance(9);
	private final WholeNumber w_5 = WholeNumber.getInstance(-5);
	private final WholeNumber w_1 = WholeNumber.getInstance(-1);

	public WholeNumberTest() {
	}

	@Test
	public void testAdd() {
		assertEquals(0, w0.add(w0).getBigInteger().intValue());
		assertEquals(1, w0.add(w1).getBigInteger().intValue());
		assertEquals(5, w2.add(w3).getBigInteger().intValue());
		assertEquals(18, w9.add(w9).getBigInteger().intValue());
		assertEquals(4, w9.add(w_5).getBigInteger().intValue());
		assertEquals(NaturalNumber.class, w9.add(w_5).getClass());
		assertEquals(NaturalNumber.class, w_5.add(w9).getClass());
	}

	@Test
	public void testMultiply() {
		assertEquals(0, w0.multiply(w0).getBigInteger().intValue());
		assertEquals(0, w0.multiply(w1).getBigInteger().intValue());
		assertEquals(6, w2.multiply(w3).getBigInteger().intValue());
		assertEquals(81, w9.multiply(w9).getBigInteger().intValue());
		assertEquals(-45, w9.multiply(w_5).getBigInteger().intValue());
		assertEquals(25, w_5.multiply(w_5).getBigInteger().intValue());
		assertEquals(NaturalNumber.class, w_5.multiply(w_5).getClass());
		assertFalse(NaturalNumber.class.equals(w9.multiply(w_5).getClass()));
		assertFalse(NaturalNumber.class.equals(w_5.multiply(w9).getClass()));
	}

	@Test
	public void testSquare() {
		assertEquals(0, w0.square().getBigInteger().intValue());
		assertEquals(1, w1.square().getBigInteger().intValue());
		assertEquals(4, w2.square().getBigInteger().intValue());
		assertEquals(81, w9.square().getBigInteger().intValue());
	}

	@Test
	public void testPower() {
		NaturalNumber e0 = NaturalNumber.getInstance(0);
		NaturalNumber e1 = NaturalNumber.getInstance(1);
		NaturalNumber e2 = NaturalNumber.getInstance(2);
		NaturalNumber e3 = NaturalNumber.getInstance(3);
		NaturalNumber e_big1 = NaturalNumber.getInstance(new BigInteger("10000000000000"));
		NaturalNumber e_big2 = NaturalNumber.getInstance(new BigInteger("100000000000001"));

		assertEquals(1, w0.power(e0).getBigInteger().intValue());
		assertEquals(0, w0.power(e1).getBigInteger().intValue());
		assertEquals(0, w0.power(e1).getBigInteger().intValue());
		assertEquals(1, w1.power(e0).getBigInteger().intValue());
		assertEquals(1, w1.power(e1).getBigInteger().intValue());
		assertEquals(1, w1.power(e1).getBigInteger().intValue());
		assertEquals(1, w2.power(e0).getBigInteger().intValue());
		assertEquals(2, w2.power(e1).getBigInteger().intValue());
		assertEquals(4, w2.power(e2).getBigInteger().intValue());
		assertEquals(8, w2.power(e3).getBigInteger().intValue());
		assertEquals(0, w0.power(e_big1).getBigInteger().intValue());
		assertEquals(1, w1.power(e_big1).getBigInteger().intValue());
		assertEquals(1, w_1.power(e_big1).getBigInteger().intValue());
		assertEquals(-1, w_1.power(e_big2).getBigInteger().intValue());
		assertEquals(8, w2.power(e3).getBigInteger().intValue());
		assertEquals(8, w2.power(NaturalNumber.getInstance(3)).getBigInteger().intValue());
	}

	@Test
	public void testMinus() {
		assertEquals(0, w0.negate().getBigInteger().intValue());
		assertEquals(-1, w1.negate().getBigInteger().intValue());
		assertEquals(-2, w2.negate().getBigInteger().intValue());
		assertEquals(NaturalNumber.class, w0.negate().getClass());
		assertEquals(WholeNumber.class, w2.negate().getClass());
	}

	@Test
	public void testSubtract() {
		assertEquals(0, w0.subtract(w0).getBigInteger().intValue());
		assertEquals(-1, w0.subtract(w1).getBigInteger().intValue());
		assertEquals(1, w1.subtract(w0).getBigInteger().intValue());
		assertEquals(0, w9.subtract(w9).getBigInteger().intValue());
		assertEquals(-14, w_5.subtract(w9).getBigInteger().intValue());
		assertEquals(14, w9.subtract(w_5).getBigInteger().intValue());
	}

	@Test
	public void testIsEqual() {
		assertTrue(w0.isEqual(w0));
		assertFalse(w0.isEqual(w1));
		assertTrue(w0.isEqual(w1.subtract(w1)));
	}

	@Test
	public void testIsSmallerThan() {
		assertFalse(w0.isSmallerThan(w0));
		assertTrue(w0.isSmallerThan(w1));
		assertFalse(w1.isSmallerThan(w0));
	}

	@Test
	public void testIsGreaterThan() {
		assertFalse(w0.isGreaterThan(w0));
		assertFalse(w0.isGreaterThan(w1));
		assertTrue(w1.isGreaterThan(w0));
	}

}
