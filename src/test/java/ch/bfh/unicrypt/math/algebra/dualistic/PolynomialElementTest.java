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
package ch.bfh.unicrypt.math.algebra.dualistic;

import ch.bfh.unicrypt.helper.array.classes.BitArray;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.math.Polynomial;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialSemiRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import java.math.BigInteger;
import java.util.HashMap;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author philipp
 */
public class PolynomialElementTest {

	private static final Z z = Z.getInstance();
	private static final ZModPrime zmod2 = ZModPrime.getInstance(2);

	private static final PolynomialSemiRing ring0 = PolynomialSemiRing.getInstance(z);
	private static final PolynomialSemiRing ring2 = PolynomialSemiRing.getInstance(zmod2);

	@Test
	public void testEvaluate() {
		PolynomialElement p = ring0.getElement(Tuple.getInstance(z.getElement(-1), z.getElement(2), z.getElement(-6), z.getElement(2)));
		assertEquals(z.getElement(5), p.evaluate(z.getElement(3)));

		p = ring0.getElement(Tuple.getInstance(z.getElement(0)));
		assertEquals(z.getElement(0), p.evaluate(z.getElement(3)));

		p = ring0.getElement(Tuple.getInstance(z.getElement(2)));
		assertEquals(z.getElement(2), p.evaluate(z.getElement(3)));

		p = ring0.getElement(Tuple.getInstance(z.getElement(0), z.getElement(2)));
		assertEquals(z.getElement(6), p.evaluate(z.getElement(3)));

		HashMap map = new HashMap<>();
		map.put(0, z.getElement(1));
		map.put(1001, z.getElement(48));
		p = ring0.getElement(Polynomial.getInstance(map, z.getZeroElement(), z.getOneElement()));
		assertEquals(BigInteger.valueOf(3).pow(1001).multiply(BigInteger.valueOf(48)).add(MathUtil.ONE), p.evaluate(z.getElement(3)).getValue());

		PolynomialElement p2 = ring2.getElement(Tuple.getInstance(zmod2.getElement(0), zmod2.getElement(1), zmod2.getElement(1), zmod2.getElement(1)));
		assertEquals(zmod2.getZeroElement(), p2.evaluate(zmod2.getElement(0)));
		assertEquals(zmod2.getOneElement(), p2.evaluate(zmod2.getElement(1)));

		p2 = ring2.getElement(Tuple.getInstance(zmod2.getElement(1), zmod2.getElement(1), zmod2.getElement(1), zmod2.getElement(1)));
		assertEquals(zmod2.getOneElement(), p2.evaluate(zmod2.getElement(0)));
		assertEquals(zmod2.getZeroElement(), p2.evaluate(zmod2.getElement(1)));

		p2 = ring2.getElement(Tuple.getInstance(zmod2.getElement(1), zmod2.getElement(0), zmod2.getElement(1), zmod2.getElement(1)));
		assertEquals(zmod2.getOneElement(), p2.evaluate(zmod2.getElement(0)));
		assertEquals(zmod2.getOneElement(), p2.evaluate(zmod2.getElement(1)));

		p2 = ring2.getElement(Polynomial.getInstance(BitArray.getInstance(), zmod2.getZeroElement(), zmod2.getOneElement()));
		assertEquals(zmod2.getZeroElement(), p2.evaluate(zmod2.getElement(0)));
		assertEquals(zmod2.getZeroElement(), p2.evaluate(zmod2.getElement(1)));

	}

}
