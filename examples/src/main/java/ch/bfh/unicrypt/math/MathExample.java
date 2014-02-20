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
package ch.bfh.unicrypt.math;

import ch.bfh.unicrypt.Example;
import ch.bfh.unicrypt.helper.Alphabet;
import ch.bfh.unicrypt.helper.numerical.ResidueClass;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.math.BigInteger;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class MathExample {

	public static void example1() {
		// Generate Z_23 (specific type)
		ZMod zMod = ZMod.getInstance(23);

		// Option 1: Non-Generic Type (casting required)
		Element e1 = zMod.getElement(5);
		ResidueClass v1 = (ResidueClass) e1.getValue();
		BigInteger b1 = v1.getBigInteger();
		BigInteger m1 = v1.getModulus();
		Example.printLine(e1, v1, b1, m1);

		// Option 2: Generic Type
		Element<ResidueClass> e2 = zMod.getElement(7);
		ResidueClass v2 = e2.getValue();
		BigInteger b2 = v2.getBigInteger();
		BigInteger m2 = v2.getModulus();
		Example.printLine(e2, v2, b2, m2);

		// Option 3: Specific Type
		ZModElement e3 = zMod.getElement(9);
		ResidueClass v3 = e3.getValue();
		BigInteger b3 = v3.getBigInteger();
		BigInteger m3 = v3.getModulus();
		Example.printLine(e3, v3, b3, m3);
	}

	public static void example2() {
		// Generate String Monoid (specific type)
		StringMonoid strMonoid = StringMonoid.getInstance(Alphabet.DECIMAL);

		// Option 1: Non-Generic Type (casting required)
		Element e1 = strMonoid.getElement("123");
		String str1 = (String) e1.getValue();
		Example.printLine(e1, str1);

		// Option 2: Generic Type
		Element<String> e2 = strMonoid.getElement("1234");
		String str2 = e2.getValue();
		Example.printLine(e2, str2);

		// Option 3: Specific Type
		StringElement e3 = strMonoid.getElement("12345");
		String str3 = e3.getValue();
		Example.printLine(e3, str3);
	}

	public static void example3() {
		// Generate Z_23 (non-generic type)
		Set zMod = ZMod.getInstance(23);

		// Option 1: Non-Generic Type
		Element e1 = zMod.getElementFrom(5);
		Example.printLine(e1);

		// Option 2: Generic Type
		Element<ResidueClass> e2 = zMod.getElementFrom(7);
		Example.printLine(e2);

		// Option 3: Specific Type (casting required)
		ZModElement e3 = (ZModElement) zMod.getElementFrom(9);
		Example.printLine(e3);
	}

	public static void example4() {
		// Generate Z_23 (generic type)
		Set<ResidueClass> zMod = ZMod.getInstance(23);

		// Option 1: Non-Generic Type
		Element e1 = zMod.getElementFrom(5);
		Example.printLine(e1);

		// Option 2: Generic Type
		Element<ResidueClass> e2 = zMod.getElementFrom(7);
		Example.printLine(e2);

		// Option 3: Specific Type (casting required)
		ZModElement e3 = (ZModElement) zMod.getElementFrom(9);
		Example.printLine(e3);
	}

	public static void main(final String[] args) {
		Example.runExamples();
	}

}
