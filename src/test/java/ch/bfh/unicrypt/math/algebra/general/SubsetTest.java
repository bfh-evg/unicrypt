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

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Subset;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni <rolf.haenni@bfh.ch>
 */
public class SubsetTest {

	public SubsetTest() {
	}

	/**
	 * Test of getSuperset method, of class Subset.
	 */
	@Test
	public void testSubset() {
		ZMod zMod = ZMod.getInstance(15);
		ZModElement e1 = zMod.getElement(4);
		ZModElement e2 = zMod.getElement(5);
		ZModElement e3 = zMod.getElement(7);
		ZModElement e4 = zMod.getElement(7);
		ZModElement e5 = zMod.getElement(7);
		ZModElement e6 = zMod.getElement(9);
		Subset subset = Subset.getInstance(zMod, new Element[]{e1, e2, e3, e3, e4});
		Assert.assertTrue(subset.contains(e1));
		Assert.assertTrue(subset.contains(e2));
		Assert.assertTrue(subset.contains(e3));
		Assert.assertTrue(subset.contains(e4));
		Assert.assertTrue(subset.contains(e5));
		Assert.assertFalse(subset.contains(e6));
		Assert.assertEquals(3, subset.getOrder().intValue());
//		for (Element element : subset) {
//			System.out.println(element);
//		}
	}

}
