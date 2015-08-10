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
 * @author R. Haenni
 */
public class HashAlgorithmTest {

	@Test
	public void HashAlgorithmTest() {

		assertSame(HashAlgorithm.SHA1, HashAlgorithm.getInstance("SHA-1"));
		assertSame(HashAlgorithm.SHA256, HashAlgorithm.getInstance("SHA-256"));
		assertSame(HashAlgorithm.SHA384, HashAlgorithm.getInstance("SHA-384"));
		assertSame(HashAlgorithm.SHA512, HashAlgorithm.getInstance("SHA-512"));
		try {
			HashAlgorithm.getInstance("");
			fail();
		} catch (Exception e) {
		}
		Assert.assertEquals(20, HashAlgorithm.SHA1.getByteLength());
		Assert.assertEquals(32, HashAlgorithm.SHA256.getByteLength());
		Assert.assertEquals(48, HashAlgorithm.SHA384.getByteLength());
		Assert.assertEquals(64, HashAlgorithm.SHA512.getByteLength());
		Assert.assertEquals(160, HashAlgorithm.SHA1.getBitLength());
		Assert.assertEquals(256, HashAlgorithm.SHA256.getBitLength());
		Assert.assertEquals(384, HashAlgorithm.SHA384.getBitLength());
		Assert.assertEquals(512, HashAlgorithm.SHA512.getBitLength());
		Assert.assertEquals("SHA-1", HashAlgorithm.SHA1.getAlgorithmName());
		Assert.assertEquals("SHA-256", HashAlgorithm.SHA256.getAlgorithmName());
		Assert.assertEquals("SHA-384", HashAlgorithm.SHA384.getAlgorithmName());
		Assert.assertEquals("SHA-512", HashAlgorithm.SHA512.getAlgorithmName());
	}

	// test vectors taken from http://www.di-mgt.com.au/sha_testvectors.html
	@Test
	public void HashAlgorithmTest_HashValue_SHA1() {
		byte[] bytes1 = ByteArray.getInstance("".toUpperCase()).getBytes();
		byte[] bytes2 = ByteArray.getInstance("61|62|63".toUpperCase()).getBytes();
		byte[] hash1 = ByteArray.getInstance("da|39|a3|ee|5e|6b|4b|0d|32|55|bf|ef|95|60|18|90|af|d8|07|09".toUpperCase()).getBytes();
		byte[] hash2 = ByteArray.getInstance("a9|99|3e|36|47|06|81|6a|ba|3e|25|71|78|50|c2|6c|9c|d0|d8|9d".toUpperCase()).getBytes();
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.SHA1.getHashValue(bytes1)));
		Assert.assertTrue(Arrays.equals(hash2, HashAlgorithm.SHA1.getHashValue(bytes2)));
	}

	@Test
	public void HashAlgorithmTest_HashValue_SHA224() {
		byte[] bytes1 = ByteArray.getInstance("".toUpperCase()).getBytes();
		byte[] hash1 = ByteArray.getInstance("d1|4a|02|8c|2a|3a|2b|c9|47|61|02|bb|28|82|34|c4|15|a2|b0|1f|82|8e|a6|2a|c5|b3|e4|2f".toUpperCase()).getBytes();
		Assert.assertArrayEquals(hash1, HashAlgorithm.SHA224.getHashValue(bytes1));
	}

	@Test
	public void HashAlgorithmTest_HashValue_SHA256() {
		byte[] bytes1 = ByteArray.getInstance("".toUpperCase()).getBytes();
		byte[] bytes2 = ByteArray.getInstance("61|62|63".toUpperCase()).getBytes();
		byte[] hash1 = ByteArray.getInstance("e3|b0|c4|42|98|fc|1c|14|9a|fb|f4|c8|99|6f|b9|24|27|ae|41|e4|64|9b|93|4c|a4|95|99|1b|78|52|b8|55".toUpperCase()).getBytes();
		byte[] hash2 = ByteArray.getInstance("ba|78|16|bf|8f|01|cf|ea|41|41|40|de|5d|ae|22|23|b0|03|61|a3|96|17|7a|9c|b4|10|ff|61|f2|00|15|ad".toUpperCase()).getBytes();
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.SHA256.getHashValue(bytes1)));
		Assert.assertTrue(Arrays.equals(hash2, HashAlgorithm.SHA256.getHashValue(bytes2)));
	}

	@Test
	public void HashAlgorithmTest_HashValue_SHA384() {
		byte[] bytes1 = ByteArray.getInstance("".toUpperCase()).getBytes();
		byte[] bytes2 = ByteArray.getInstance("61|62|63".toUpperCase()).getBytes();
		byte[] hash1 = ByteArray.getInstance("38|b0|60|a7|51|ac|96|38|4c|d9|32|7e|b1|b1|e3|6a|21|fd|b7|11|14|be|07|43|4c|0c|c7|bf|63|f6|e1|da|27|4e|de|bf|e7|6f|65|fb|d5|1a|d2|f1|48|98|b9|5b".toUpperCase()).getBytes();
		byte[] hash2 = ByteArray.getInstance("cb|00|75|3f|45|a3|5e|8b|b5|a0|3d|69|9a|c6|50|07|27|2c|32|ab|0e|de|d1|63|1a|8b|60|5a|43|ff|5b|ed|80|86|07|2b|a1|e7|cc|23|58|ba|ec|a1|34|c8|25|a7".toUpperCase()).getBytes();
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.SHA384.getHashValue(bytes1)));
		Assert.assertTrue(Arrays.equals(hash2, HashAlgorithm.SHA384.getHashValue(bytes2)));
	}

	@Test
	public void HashAlgorithmTest_HashValue_SHA512() {
		byte[] bytes1 = ByteArray.getInstance("".toUpperCase()).getBytes();
		byte[] bytes2 = ByteArray.getInstance("61|62|63".toUpperCase()).getBytes();
		byte[] hash1 = ByteArray.getInstance("cf|83|e1|35|7e|ef|b8|bd|f1|54|28|50|d6|6d|80|07|d6|20|e4|05|0b|57|15|dc|83|f4|a9|21|d3|6c|e9|ce|47|d0|d1|3c|5d|85|f2|b0|ff|83|18|d2|87|7e|ec|2f|63|b9|31|bd|47|41|7a|81|a5|38|32|7a|f9|27|da|3e".toUpperCase()).getBytes();
		byte[] hash2 = ByteArray.getInstance("dd|af|35|a1|93|61|7a|ba|cc|41|73|49|ae|20|41|31|12|e6|fa|4e|89|a9|7e|a2|0a|9e|ee|e6|4b|55|d3|9a|21|92|99|2a|27|4f|c1|a8|36|ba|3c|23|a3|fe|eb|bd|45|4d|44|23|64|3c|e8|0e|2a|9a|c9|4f|a5|4c|a4|9f".toUpperCase()).getBytes();
		Assert.assertTrue(Arrays.equals(hash1, HashAlgorithm.SHA512.getHashValue(bytes1)));
		Assert.assertTrue(Arrays.equals(hash2, HashAlgorithm.SHA512.getHashValue(bytes2)));
	}

	@Test
	public void HashAlgorithmTest_HashValue_SHA512_ByteArray() {
		ByteArray bytes1 = ByteArray.getInstance("".toUpperCase());
		ByteArray bytes2 = ByteArray.getInstance("61|62|63".toUpperCase());
		ByteArray hash1 = ByteArray.getInstance("cf|83|e1|35|7e|ef|b8|bd|f1|54|28|50|d6|6d|80|07|d6|20|e4|05|0b|57|15|dc|83|f4|a9|21|d3|6c|e9|ce|47|d0|d1|3c|5d|85|f2|b0|ff|83|18|d2|87|7e|ec|2f|63|b9|31|bd|47|41|7a|81|a5|38|32|7a|f9|27|da|3e".toUpperCase());
		ByteArray hash2 = ByteArray.getInstance("dd|af|35|a1|93|61|7a|ba|cc|41|73|49|ae|20|41|31|12|e6|fa|4e|89|a9|7e|a2|0a|9e|ee|e6|4b|55|d3|9a|21|92|99|2a|27|4f|c1|a8|36|ba|3c|23|a3|fe|eb|bd|45|4d|44|23|64|3c|e8|0e|2a|9a|c9|4f|a5|4c|a4|9f".toUpperCase());
		Assert.assertEquals(hash1, HashAlgorithm.SHA512.getHashValue(bytes1));
		Assert.assertEquals(hash2, HashAlgorithm.SHA512.getHashValue(bytes2));
	}

	// test vectors taken from RFC 4231
	@Test
	public void HashAlgorithmTest_KeyedHashValue_SHA256() {
		{
			ByteArray key = ByteArray.getInstance("0b|0b|0b|0b|0b|0b|0b|0b|0b|0b|0b|0b|0b|0b|0b|0b|0b|0b|0b|0b".toUpperCase());
			ByteArray data = ByteArray.getInstance("48|69|20|54|68|65|72|65".toUpperCase());
			ByteArray hash = ByteArray.getInstance("b0|34|4c|61|d8|db|38|53|5c|a8|af|ce|af|0b|f1|2b|88|1d|c2|00|c9|83|3d|a7|26|e9|37|6c|2e|32|cf|f7".toUpperCase());

			Assert.assertEquals(hash, HashAlgorithm.SHA256.getHashValue(data, key));
		}
		{
			ByteArray key = ByteArray.getInstance("4a|65|66|65".toUpperCase());
			ByteArray data = ByteArray.getInstance("77|68|61|74|20|64|6f|20|79|61|20|77|61|6e|74|20|66|6f|72|20|6e|6f|74|68|69|6e|67|3f".toUpperCase());
			ByteArray hash = ByteArray.getInstance("5b|dc|c1|46|bf|60|75|4e|6a|04|24|26|08|95|75|c7|5a|00|3f|08|9d|27|39|83|9d|ec|58|b9|64|ec|38|43".toUpperCase());

			Assert.assertEquals(hash, HashAlgorithm.SHA256.getHashValue(data, key));
		}
		{
			ByteArray key = ByteArray.getInstance("aa|aa|aa|aa|aa|aa|aa|aa|aa|aa|aa|aa|aa|aa|aa|aa|aa|aa|aa|aa".toUpperCase());
			ByteArray data = ByteArray.getInstance("dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd|dd".toUpperCase());
			ByteArray hash = ByteArray.getInstance("77|3e|a9|1e|36|80|0e|46|85|4d|b8|eb|d0|91|81|a7|29|59|09|8b|3e|f8|c1|22|d9|63|55|14|ce|d5|65|fe".toUpperCase());

			Assert.assertEquals(hash, HashAlgorithm.SHA256.getHashValue(data, key));
		}
		{
			ByteArray key = ByteArray.getInstance("01|02|03|04|05|06|07|08|09|0a|0b|0c|0d|0e|0f|10|11|12|13|14|15|16|17|18|19".toUpperCase());
			ByteArray data = ByteArray.getInstance("cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd|cd".toUpperCase());
			ByteArray hash = ByteArray.getInstance("82|55|8a|38|9a|44|3c|0e|a4|cc|81|98|99|f2|08|3a|85|f0|fa|a3|e5|78|f8|07|7a|2e|3f|f4|67|29|66|5b".toUpperCase());

			Assert.assertEquals(hash, HashAlgorithm.SHA256.getHashValue(data, key));
		}
		{
			ByteArray key = ByteArray.getInstance("0c|0c|0c|0c|0c|0c|0c|0c|0c|0c|0c|0c|0c|0c|0c|0c|0c|0c|0c|0c".toUpperCase());
			ByteArray data = ByteArray.getInstance("54|65|73|74|20|57|69|74|68|20|54|72|75|6e|63|61|74|69|6f|6e".toUpperCase());
			ByteArray hash = ByteArray.getInstance("a3|b6|16|74|73|10|0e|e0|6e|0c|79|6c|29|55|55|2b".toUpperCase());

			Assert.assertEquals(hash, HashAlgorithm.SHA256.getHashValue(data, key, 16));

		}
		// Test from NIST test vector file (not compatible with FIPS 198, but ok for RFC 2104!!!)
		{
			ByteArray key = ByteArray.getInstance("ad|44|5d|a4|8d|46|ab|fe|f1|03|f9|c6|c5|47|34|44|ff|bb|ae|90|27|5c|c4|a8|16|2b|be|c0|fe|26|f6|d9".toUpperCase());
			ByteArray data = ByteArray.getInstance("81|c9|4b|e4|26|ea|f0|18|64|e8|13|a0|3e|46|74|49|1b|61|51|6b|c9|5d|8a|77|c1|5f|03|d0|ad|fc|4a|dc|27|f2|7a|5a|c4|16|5f|f6|51|8e|da|1a|5c|40|87|08|f7|8a|9e|26|b8|34|17|98|04|a3|12|14|8d|4f|75|f2|1a|77|d7|83|87|13|9d|a4|0c|0a|62|93|c2|a5|9d|01|62|43|7d|68|50|4f|18|9e|d9|70|c5|ab|b9|ff|c6|d8|e1|be|2b|08|77|c7|f2|4b|1d|c2|73|b1|76|5b|fc|5c|e6|f4|b8|d9|9a|96|d5|b1|c9|2e|e5|3a|39|f6|85|b3".toUpperCase());
			ByteArray hash = ByteArray.getInstance("91|bc|35|5f|b0|22|18|25|30|7a|f8|76|d1|14|04|b4|73|22|2d|5a".toUpperCase());

			Assert.assertEquals(hash, HashAlgorithm.SHA1.getHashValue(data, key, 20));

		}

	}

}
