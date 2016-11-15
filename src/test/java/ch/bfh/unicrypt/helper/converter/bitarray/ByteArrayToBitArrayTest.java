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
package ch.bfh.unicrypt.helper.converter.bitarray;

import ch.bfh.unicrypt.helper.array.classes.BitArray;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bitarray.ByteArrayToBitArray;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class ByteArrayToBitArrayTest {

	ByteArray ba0 = ByteArray.getInstance("");
	ByteArray ba1 = ByteArray.getInstance("00");
	ByteArray ba2 = ByteArray.getInstance("FF");
	ByteArray ba3 = ByteArray.getInstance("00|00");
	ByteArray ba4 = ByteArray.getInstance("FF|FF");

	@Test
	public void byteArrayToBitArrayTest1() {

		ByteArrayToBitArray converter = ByteArrayToBitArray.getInstance();
		for (ByteArray ba : new ByteArray[]{ba0, ba1, ba2, ba3, ba4}) {
			Assert.assertEquals(BitArray.getInstance(ba), converter.convert(ba));
			Assert.assertEquals(ba, converter.reconvert(converter.convert(ba)));
		}

		try {
			converter.reconvert(BitArray.getInstance("0"));
			fail();
		} catch (Exception e) {
		}

	}

}
