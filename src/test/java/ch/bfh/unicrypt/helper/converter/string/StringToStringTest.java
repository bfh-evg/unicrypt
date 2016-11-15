/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographic framework allowing the implementation of cryptographic protocols, e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.helper.converter.string;

import ch.bfh.unicrypt.helper.converter.classes.string.StringToString;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class StringToStringTest {

	String string0 = "";
	String string10 = "0";
	String string11 = "1";
	String string20 = "00";
	String string21 = "01";
	String string22 = "10";
	String string23 = "11";
	String string30 = "000";
	String string31 = "001";
	String string32 = "010";
	String string33 = "011";
	String string34 = "100";
	String string35 = "101";
	String string36 = "110";
	String string37 = "111";
	String string40 = "0000";

	@Test
	public void stringToStringTest1() {

		StringToString converter = StringToString.getInstance();
		for (String string : new String[]{string0, string10, string11, string20, string21, string22, string23, string30, string31, string32, string33, string34, string35, string36, string37, string40}) {
			Assert.assertEquals(string, converter.convert(string));
			Assert.assertEquals(string, converter.reconvert(string));
		}

	}

	@Test
	public void bitArrayToStringTest2() {

		StringToString converter = StringToString.getInstance(true); // reverse
		for (String string : new String[]{string0, string10, string11, string20, string21, string22, string23, string30, string31, string32, string33, string34, string35, string36, string37, string40}) {
			Assert.assertEquals(new StringBuilder(string).reverse().toString(), converter.convert(string));
			Assert.assertEquals(new StringBuilder(string).reverse().toString(), converter.reconvert(string));
		}

	}

}
