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
package ch.bfh.unicrypt.helper.math;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class AlphabetTest {

	public AlphabetTest() {
	}

	@Test
	public void generalTest() {
		Alphabet a1 = Alphabet.ALPHANUMERIC;
		Alphabet a2 = Alphabet.getInstance("");
		Alphabet a3 = Alphabet.getInstance("x");
		Alphabet a4 = Alphabet.getInstance("xyz");
		Alphabet a5 = Alphabet.getInstance('a', 'g');
		Alphabet a6 = Alphabet.getInstance("abcdefg");
		Alphabet a7 = Alphabet.UNICODE_BMP;

		for (Alphabet alphabet : new Alphabet[]{a1, a2, a3, a4, a5, a6, a7}) {

			String allChars = "";

			for (int i = 0; i < alphabet.getSize(); i++) {
				char c = alphabet.getCharacter(i);
				Assert.assertTrue(alphabet.contains(c));
				Assert.assertEquals(i, alphabet.getIndex(c));
				allChars = allChars + c;
			}
			Assert.assertTrue(alphabet.containsAll(""));
			Assert.assertTrue(alphabet.containsAll(allChars));
			if (alphabet != a7) {
				Assert.assertFalse(alphabet.containsAll("*"));
			}

		}
		Assert.assertFalse(a5.equals(a1));
		Assert.assertFalse(a5.equals(a2));
		Assert.assertFalse(a5.equals(a3));
		Assert.assertFalse(a5.equals(a4));
		Assert.assertTrue(a5.equals(a5));
		Assert.assertTrue(a5.equals(a6));
		Assert.assertFalse(a5.equals(a7));
		Assert.assertEquals(62, a1.getSize());
		Assert.assertEquals(0, a2.getSize());
		Assert.assertEquals(1, a3.getSize());
		Assert.assertEquals(3, a4.getSize());
		Assert.assertEquals(7, a5.getSize());
		Assert.assertEquals(7, a6.getSize());
		Assert.assertEquals(65536, a7.getSize());
	}

}
