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
package ch.bfh.unicrypt.helper.converter.classes.string;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArrayToStringTest {

	@Test
	public void testMain() {

		ByteArray b1 = ByteArray.getInstance("");
		ByteArray b2 = ByteArray.getInstance("f8");
		ByteArray b3 = ByteArray.getInstance("00|00");
		ByteArray b4 = ByteArray.getInstance("01|23|45|67|89|AB|CD|EF");

		ByteArray[] bs = {b1, b2, b3, b4};

		String[] delimiters = {"|", " ", "-", "x"};

		for (String delim : delimiters) {
			ByteArrayToString c1 = ByteArrayToString.getInstance(ByteArrayToString.Radix.BASE64);
			ByteArrayToString c2 = ByteArrayToString.getInstance(ByteArrayToString.Radix.BINARY);
			ByteArrayToString c3 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX);
			ByteArrayToString c4 = ByteArrayToString.getInstance(ByteArrayToString.Radix.BINARY, true);
			ByteArrayToString c5 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, true);
			ByteArrayToString c6 = ByteArrayToString.getInstance(ByteArrayToString.Radix.BINARY, delim);
			ByteArrayToString c7 = ByteArrayToString.getInstance(ByteArrayToString.Radix.HEX, delim);
			ByteArrayToString[] cs = {c1, c2, c3, c4, c5, c6, c7};
			for (ByteArrayToString c : cs) {
				for (ByteArray b : bs) {
					Assert.assertEquals(b, c.reconvert(c.convert(b)));
				}
			}
		}
	}

}
