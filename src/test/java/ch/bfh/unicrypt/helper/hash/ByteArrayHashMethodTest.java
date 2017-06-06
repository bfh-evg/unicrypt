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
package ch.bfh.unicrypt.helper.hash;

import ch.bfh.unicrypt.helper.aggregator.classes.ByteArrayAggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.tree.Tree;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class ByteArrayHashMethodTest {

	ByteArray bytes1 = ByteArray.getInstance("");
	ByteArray bytes2 = ByteArray.getInstance("61|62|63");

	Tree<ByteArray> t1 = Tree.getInstance(bytes1);
	Tree<ByteArray> t2 = Tree.getInstance(bytes2);

	Tree<ByteArray> t3 = Tree.getInstance(t1, t2);
	Tree<ByteArray> t4 = Tree.getInstance();
	Tree<ByteArray> t5 = Tree.getInstance(t3, t4);

	HashAlgorithm hashAlgorithm = HashAlgorithm.SHA256;

	@Test
	public void ByteArrayHashMethodTest1() {

		ByteArray hash1 = ByteArray.getInstance("e3|b0|c4|42|98|fc|1c|14|9a|fb|f4|c8|99|6f|b9|24|27|ae|41|e4|64|9b|93|4c|a4|95|99|1b|78|52|b8|55".toUpperCase());
		ByteArray hash2 = ByteArray.getInstance("ba|78|16|bf|8f|01|cf|ea|41|41|40|de|5d|ae|22|23|b0|03|61|a3|96|17|7a|9c|b4|10|ff|61|f2|00|15|ad".toUpperCase());
		ByteArray hash3 = hashAlgorithm.getHashValue(hash1.append(hash2));
		ByteArray hash4 = hashAlgorithm.getHashValue(ByteArray.getInstance());
		ByteArray hash5 = hashAlgorithm.getHashValue(hash3.append(hash4));

		HashMethod<ByteArray> hashMethod = HashMethod.getInstance(hashAlgorithm);

		Assert.assertEquals(hash1, hashMethod.getHashValue(t1));
		Assert.assertEquals(hash2, hashMethod.getHashValue(t2));
		Assert.assertEquals(hash3, hashMethod.getHashValue(t3));
		Assert.assertEquals(hash4, hashMethod.getHashValue(t4));
		Assert.assertEquals(hash5, hashMethod.getHashValue(t5));

	}

	@Test
	public void ByteArrayHashMethodTest2() {

		ByteArrayAggregator aggregator = ByteArrayAggregator.getInstance();
		HashMethod<ByteArray> hashMethod = HashMethod.getInstance(hashAlgorithm, aggregator);

		ByteArray b1 = aggregator.aggregate(t1);
		ByteArray b2 = aggregator.aggregate(t2);
		ByteArray b3 = aggregator.aggregate(t3);
		ByteArray b4 = aggregator.aggregate(t4);
		ByteArray b5 = aggregator.aggregate(t5);

		Assert.assertEquals(hashAlgorithm.getHashValue(b1), hashMethod.getHashValue(t1));
		Assert.assertEquals(hashAlgorithm.getHashValue(b2), hashMethod.getHashValue(t2));
		Assert.assertEquals(hashAlgorithm.getHashValue(b3), hashMethod.getHashValue(t3));
		Assert.assertEquals(hashAlgorithm.getHashValue(b4), hashMethod.getHashValue(t4));
		Assert.assertEquals(hashAlgorithm.getHashValue(b5), hashMethod.getHashValue(t5));

	}

}
