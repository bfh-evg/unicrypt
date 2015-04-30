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
package ch.bfh.unicrypt.helper.hash;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.BigIntegerToByteArray;
import ch.bfh.unicrypt.helper.hash.HashMethod.Mode;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class HashMethodTest {

	@Test
	public void HashMethodTest() {
		HashMethod h1 = HashMethod.getInstance();
		HashMethod h2 = HashMethod.getInstance(Mode.RECURSIVE);
		HashMethod h3 = HashMethod.getInstance(HashAlgorithm.SHA256);
		HashMethod h4 = HashMethod.getInstance(ConvertMethod.<ByteArray>getInstance());
		HashMethod h5 = HashMethod.getInstance(HashAlgorithm.SHA256, Mode.RECURSIVE);
		HashMethod h6 = HashMethod.getInstance(ConvertMethod.<ByteArray>getInstance(), Mode.RECURSIVE);
		HashMethod h7 = HashMethod.getInstance(HashAlgorithm.SHA256, ConvertMethod.<ByteArray>getInstance());
		HashMethod h8 = HashMethod.getInstance(HashAlgorithm.SHA256, ConvertMethod.<ByteArray>getInstance(), Mode.RECURSIVE
		);
		assertEquals(h8, h1);
		assertEquals(h8, h2);
		assertEquals(h8, h3);
		assertEquals(h8, h4);
		assertEquals(h8, h5);
		assertEquals(h8, h6);
		assertEquals(h8, h7);
		assertEquals(h8, h8);

		assertEquals(ConvertMethod.getInstance(), h1.getConvertMethod());
		assertEquals(Mode.RECURSIVE, h1.getMode());
		assertEquals(HashAlgorithm.SHA256, h1.getHashAlgorithm());

		ConvertMethod<ByteArray> convertMethod = ConvertMethod.getInstance(BigIntegerToByteArray.getInstance());
		HashMethod h = HashMethod.getInstance(HashAlgorithm.MD5, convertMethod, Mode.BYTETREE);
		assertEquals(convertMethod, h.getConvertMethod());
		assertEquals(Mode.BYTETREE, h.getMode());
		assertEquals(HashAlgorithm.MD5, h.getHashAlgorithm());

	}

	@Test
	public void testGetHashAlgorithm() {
	}

	@Test
	public void testGetConvertMethod() {
	}

	@Test
	public void testGetMode() {
	}

	@Test
	public void testDefaultToStringContent() {
	}

	@Test
	public void testHashCode() {
	}

	@Test
	public void testEquals() {
	}

	@Test
	public void testGetInstance_0args() {
	}

	@Test
	public void testGetInstance_HashAlgorithm() {
	}

	@Test
	public void testGetInstance_ConvertMethod() {
	}

	@Test
	public void testGetInstance_HashMethodMode() {
	}

	@Test
	public void testGetInstance_HashAlgorithm_ConvertMethod() {
	}

	@Test
	public void testGetInstance_ConvertMethod_HashMethodMode() {
	}

	@Test
	public void testGetInstance_HashAlgorithm_HashMethodMode() {
	}

	@Test
	public void testGetInstance_3args() {
	}

}
