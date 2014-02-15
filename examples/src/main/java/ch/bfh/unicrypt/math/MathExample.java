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
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.Alphabet;
import ch.bfh.unicrypt.math.helper.numerical.ResidueClass;
import java.math.BigInteger;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class MathExample {

	public static void example1() {
		ZMod zMod = ZMod.getInstance(23);

		Element e1 = zMod.getElement(5);
		ResidueClass v1 = (ResidueClass) e1.getValue();
		BigInteger b1 = v1.getBigInteger();
		BigInteger m1 = v1.getModulus();
		Example.printLine(e1, v1, b1, m1);

		Element<ResidueClass> e2 = zMod.getElement(7);
		ResidueClass v2 = e2.getValue();
		BigInteger b2 = v2.getBigInteger();
		BigInteger m2 = v2.getModulus();
		Example.printLine(e2, v2, b2, m2);

		ZModElement e3 = zMod.getElement(7);
		ResidueClass v3 = e3.getValue();
		BigInteger b3 = v3.getBigInteger();
		BigInteger m3 = v3.getModulus();
		Example.printLine(e3, v3, b3, m3);

		StringMonoid strM = StringMonoid.getInstance(Alphabet.DECIMAL);
		Element f1 = strM.getElement("1234");
		String str1 = (String) f1.getValue();
		Element<String> f2 = strM.getElement("1234");
		String str2 = f2.getValue();
		StringElement f3 = strM.getElement("1234");
		String str3 = f3.getValue();
		Example.printLine(f1, f2, f3);

	}

	public static void main(final String[] args) {
		example1();
	}

}
