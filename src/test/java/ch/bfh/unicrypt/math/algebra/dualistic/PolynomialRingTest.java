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

import ch.bfh.unicrypt.helper.numerical.ResidueClass;
import ch.bfh.unicrypt.helper.numerical.WholeNumber;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialRing;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author philipp
 */
public class PolynomialRingTest {

	private static PolynomialRing<WholeNumber> ring0;  // Z
	private static PolynomialRing<ResidueClass> ring1;  // ZMod
	private static PolynomialRing<ResidueClass> ring2;  // ZMod Binary

	public PolynomialRingTest() {
		ring0 = PolynomialRing.getInstance(Z.getInstance());
		ring1 = PolynomialRing.getInstance(ZMod.getInstance(7));
		ring2 = PolynomialRing.getInstance(ZMod.getInstance(2));
	}

	@Test
	public void testInvert() {
		Z z = Z.getInstance();
		PolynomialElement<WholeNumber> p = ring0.getElement(Tuple.getInstance(z.getElement(0), z.getElement(1), z.getElement(2), z.getElement(3)));
		assertEquals(ring0.getZeroElement(), p.add(p.invert()));

		ZMod zmod = (ZMod) ring1.getSemiRing();
		PolynomialElement<ResidueClass> p1 = ring1.getElement(Tuple.getInstance(zmod.getElement(5), zmod.getElement(0), zmod.getElement(6), zmod.getElement(3)));
		assertEquals(ring1.getZeroElement(), p1.add(p1.invert()));

		zmod = (ZMod) ring2.getSemiRing();
		PolynomialElement<ResidueClass> p2 = ring2.getElement(Tuple.getInstance(zmod.getElement(0), zmod.getElement(1), zmod.getElement(1), zmod.getElement(1)));
		assertEquals(ring2.getZeroElement(), p2.add(p2.invert()));
	}

}
