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
import java.util.Arrays;
import org.junit.Assert;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class HashAlgorithmTest {

	@Test
	public void HashAlgorithmTest() {

		assertSame(HashAlgorithm.MD5, HashAlgorithm.getInstance("MD5"));
		assertSame(HashAlgorithm.SHA1, HashAlgorithm.getInstance("SHA-1"));
		assertSame(HashAlgorithm.SHA256, HashAlgorithm.getInstance("SHA-256"));
		assertSame(HashAlgorithm.SHA384, HashAlgorithm.getInstance("SHA-384"));
		assertSame(HashAlgorithm.SHA512, HashAlgorithm.getInstance("SHA-512"));
		try {
			HashAlgorithm.getInstance("");
			fail();
		} catch (Exception e) {
		}
		Assert.assertEquals(16, HashAlgorithm.MD5.getByteLength());
		Assert.assertEquals(20, HashAlgorithm.SHA1.getByteLength());
		Assert.assertEquals(32, HashAlgorithm.SHA256.getByteLength());
		Assert.assertEquals(48, HashAlgorithm.SHA384.getByteLength());
		Assert.assertEquals(64, HashAlgorithm.SHA512.getByteLength());
		Assert.assertEquals(128, HashAlgorithm.MD5.getBitLength());
		Assert.assertEquals(160, HashAlgorithm.SHA1.getBitLength());
		Assert.assertEquals(256, HashAlgorithm.SHA256.getBitLength());
		Assert.assertEquals(384, HashAlgorithm.SHA384.getBitLength());
		Assert.assertEquals(512, HashAlgorithm.SHA512.getBitLength());
		Assert.assertEquals("MD5", HashAlgorithm.MD5.getAlgorithmName());
		Assert.assertEquals("SHA-1", HashAlgorithm.SHA1.getAlgorithmName());
		Assert.assertEquals("SHA-256", HashAlgorithm.SHA256.getAlgorithmName());
		Assert.assertEquals("SHA-384", HashAlgorithm.SHA384.getAlgorithmName());
		Assert.assertEquals("SHA-512", HashAlgorithm.SHA512.getAlgorithmName());
	}

	@Test
	public void HashAlgorithmTest_HashValue_MD5() {
		byte[] bytes1 = ByteArray.getInstance("".toUpperCase()).getBytes();
		byte[] bytes2 = ByteArray.getInstance("61|62|63".toUpperCase()).getBytes();
		byte[] hash1 = ByteArray.getInstance("d4|1d|8c|d9|8f|00|b2|04|e9|80|09|98|ec|f8|42|7e".toUpperCase()).getBytes();
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.MD5.getHashValue(bytes1)));
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.MD5.getHashValue(bytes2, 0, 0)));
		try {
			HashAlgorithm.MD5.getHashValue(bytes2, -2, 0);
			fail();
		} catch (Exception e) {
		}
		try {
			HashAlgorithm.MD5.getHashValue(bytes2, 1, 5);
			fail();
		} catch (Exception e) {
		}
	}

	// test vectors taken from http://www.di-mgt.com.au/sha_testvectors.html
	@Test
	public void HashAlgorithmTest_HashValue_SHA1() {
		byte[] bytes1 = ByteArray.getInstance("".toUpperCase()).getBytes();
		byte[] bytes2 = ByteArray.getInstance("61|62|63".toUpperCase()).getBytes();
		byte[] bytes3 = ByteArray.getInstance("00|11|61|62|63|22".toUpperCase()).getBytes();
		byte[] hash1 = ByteArray.getInstance("da|39|a3|ee|5e|6b|4b|0d|32|55|bf|ef|95|60|18|90|af|d8|07|09".toUpperCase()).getBytes();
		byte[] hash2 = ByteArray.getInstance("a9|99|3e|36|47|06|81|6a|ba|3e|25|71|78|50|c2|6c|9c|d0|d8|9d".toUpperCase()).getBytes();
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.SHA1.getHashValue(bytes1)));
		Assert.assertTrue(Arrays.equals(hash2, HashAlgorithm.SHA1.getHashValue(bytes2)));
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.SHA1.getHashValue(bytes2, 0, 0)));
		Assert.assertTrue(Arrays.equals(hash2, HashAlgorithm.SHA1.getHashValue(bytes3, 2, 3)));
	}

	@Test
	public void HashAlgorithmTest_HashValue_SHA256() {
		byte[] bytes1 = ByteArray.getInstance("".toUpperCase()).getBytes();
		byte[] bytes2 = ByteArray.getInstance("61|62|63".toUpperCase()).getBytes();
		byte[] bytes3 = ByteArray.getInstance("00|11|61|62|63|22".toUpperCase()).getBytes();
		byte[] hash1 = ByteArray.getInstance("e3|b0|c4|42|98|fc|1c|14|9a|fb|f4|c8|99|6f|b9|24|27|ae|41|e4|64|9b|93|4c|a4|95|99|1b|78|52|b8|55".toUpperCase()).getBytes();
		byte[] hash2 = ByteArray.getInstance("ba|78|16|bf|8f|01|cf|ea|41|41|40|de|5d|ae|22|23|b0|03|61|a3|96|17|7a|9c|b4|10|ff|61|f2|00|15|ad".toUpperCase()).getBytes();
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.SHA256.getHashValue(bytes1)));
		Assert.assertTrue(Arrays.equals(hash2, HashAlgorithm.SHA256.getHashValue(bytes2)));
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.SHA256.getHashValue(bytes2, 0, 0)));
		Assert.assertTrue(Arrays.equals(hash2, HashAlgorithm.SHA256.getHashValue(bytes3, 2, 3)));
	}

	@Test
	public void HashAlgorithmTest_HashValue_SHA384() {
		byte[] bytes1 = ByteArray.getInstance("".toUpperCase()).getBytes();
		byte[] bytes2 = ByteArray.getInstance("61|62|63".toUpperCase()).getBytes();
		byte[] bytes3 = ByteArray.getInstance("00|11|61|62|63|22".toUpperCase()).getBytes();
		byte[] hash1 = ByteArray.getInstance("38|b0|60|a7|51|ac|96|38|4c|d9|32|7e|b1|b1|e3|6a|21|fd|b7|11|14|be|07|43|4c|0c|c7|bf|63|f6|e1|da|27|4e|de|bf|e7|6f|65|fb|d5|1a|d2|f1|48|98|b9|5b".toUpperCase()).getBytes();
		byte[] hash2 = ByteArray.getInstance("cb|00|75|3f|45|a3|5e|8b|b5|a0|3d|69|9a|c6|50|07|27|2c|32|ab|0e|de|d1|63|1a|8b|60|5a|43|ff|5b|ed|80|86|07|2b|a1|e7|cc|23|58|ba|ec|a1|34|c8|25|a7".toUpperCase()).getBytes();
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.SHA384.getHashValue(bytes1)));
		Assert.assertTrue(Arrays.equals(hash2, HashAlgorithm.SHA384.getHashValue(bytes2)));
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.SHA384.getHashValue(bytes2, 0, 0)));
		Assert.assertTrue(Arrays.equals(hash2, HashAlgorithm.SHA384.getHashValue(bytes3, 2, 3)));
	}

	@Test
	public void HashAlgorithmTest_HashValue_SHA512() {
		byte[] bytes1 = ByteArray.getInstance("".toUpperCase()).getBytes();
		byte[] bytes2 = ByteArray.getInstance("61|62|63".toUpperCase()).getBytes();
		byte[] bytes3 = ByteArray.getInstance("00|11|61|62|63|22".toUpperCase()).getBytes();
		byte[] hash1 = ByteArray.getInstance("cf|83|e1|35|7e|ef|b8|bd|f1|54|28|50|d6|6d|80|07|d6|20|e4|05|0b|57|15|dc|83|f4|a9|21|d3|6c|e9|ce|47|d0|d1|3c|5d|85|f2|b0|ff|83|18|d2|87|7e|ec|2f|63|b9|31|bd|47|41|7a|81|a5|38|32|7a|f9|27|da|3e".toUpperCase()).getBytes();
		byte[] hash2 = ByteArray.getInstance("dd|af|35|a1|93|61|7a|ba|cc|41|73|49|ae|20|41|31|12|e6|fa|4e|89|a9|7e|a2|0a|9e|ee|e6|4b|55|d3|9a|21|92|99|2a|27|4f|c1|a8|36|ba|3c|23|a3|fe|eb|bd|45|4d|44|23|64|3c|e8|0e|2a|9a|c9|4f|a5|4c|a4|9f".toUpperCase()).getBytes();
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.SHA512.getHashValue(bytes1)));
		Assert.assertTrue(Arrays.equals(hash2, HashAlgorithm.SHA512.getHashValue(bytes2)));
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.SHA512.getHashValue(bytes2, 0, 0)));
		Assert.assertTrue(Arrays.equals(hash2, HashAlgorithm.SHA512.getHashValue(bytes3, 2, 3)));
	}

	@Test
	public void HashAlgorithmTest_HashValue_SHA512_ByteArray() {
		ByteArray bytes1 = ByteArray.getInstance("".toUpperCase());
		ByteArray bytes2 = ByteArray.getInstance("61|62|63".toUpperCase());
		ByteArray bytes3 = ByteArray.getInstance("00|11|61|62|63|22".toUpperCase());
		ByteArray hash1 = ByteArray.getInstance("cf|83|e1|35|7e|ef|b8|bd|f1|54|28|50|d6|6d|80|07|d6|20|e4|05|0b|57|15|dc|83|f4|a9|21|d3|6c|e9|ce|47|d0|d1|3c|5d|85|f2|b0|ff|83|18|d2|87|7e|ec|2f|63|b9|31|bd|47|41|7a|81|a5|38|32|7a|f9|27|da|3e".toUpperCase());
		ByteArray hash2 = ByteArray.getInstance("dd|af|35|a1|93|61|7a|ba|cc|41|73|49|ae|20|41|31|12|e6|fa|4e|89|a9|7e|a2|0a|9e|ee|e6|4b|55|d3|9a|21|92|99|2a|27|4f|c1|a8|36|ba|3c|23|a3|fe|eb|bd|45|4d|44|23|64|3c|e8|0e|2a|9a|c9|4f|a5|4c|a4|9f".toUpperCase());
		Assert.assertEquals(hash1, HashAlgorithm.SHA512.getHashValue(bytes1));
		Assert.assertEquals(hash2, HashAlgorithm.SHA512.getHashValue(bytes2));
	}

}