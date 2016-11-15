/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
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
package ch.bfh.unicrypt.helper.converter.bytearray;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.ByteArrayToByteArray;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class ByteArrayToByteArrayTest {

	ByteArray ba00 = ByteArray.getInstance("");
	ByteArray ba10 = ByteArray.getInstance("00");
	ByteArray ba11 = ByteArray.getInstance("01");
	ByteArray ba12 = ByteArray.getInstance("FE");
	ByteArray ba13 = ByteArray.getInstance("FF");
	ByteArray ba20 = ByteArray.getInstance("00|00");
	ByteArray ba21 = ByteArray.getInstance("00|01");
	ByteArray ba22 = ByteArray.getInstance("FF|FE");
	ByteArray ba23 = ByteArray.getInstance("FF|FF");
	ByteArray ba5 = ByteArray.getInstance((byte) 10, 5);

	@Test
	public void bitArrayToByteArrayTest1() {

		ByteArrayToByteArray c1 = ByteArrayToByteArray.getInstance();
		ByteArrayToByteArray c2 = ByteArrayToByteArray.getInstance(true);
		ByteArrayToByteArray c3 = ByteArrayToByteArray.getInstance(true, true);
		ByteArrayToByteArray c4 = ByteArrayToByteArray.getInstance(false, true);

		for (ByteArray ba1 : new ByteArray[]{ba00, ba10, ba11, ba12, ba13, ba20, ba21, ba22, ba23}) {
			for (ByteArrayToByteArray converter : new ByteArrayToByteArray[]{c1, c2, c3, c4}) {
				ByteArray ba2 = converter.convert(ba1);
				Assert.assertEquals(ba1, converter.reconvert(ba2));
			}
			Assert.assertEquals(ba1, c1.convert(c2.convert(c3.convert(c4.convert(ba1)))));
			Assert.assertEquals(ba1, c1.reconvert(c2.reconvert(c3.reconvert(c4.reconvert(ba1)))));
		}

	}

}
