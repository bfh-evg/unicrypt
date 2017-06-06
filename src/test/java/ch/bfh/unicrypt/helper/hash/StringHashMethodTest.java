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
import ch.bfh.unicrypt.helper.aggregator.classes.StringAggregator;
import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.StringToByteArray;
import ch.bfh.unicrypt.helper.tree.Tree;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class StringHashMethodTest {

	String string1 = "";
	String string2 = "61|62|63";

	Tree<String> t1 = Tree.getInstance(string1);
	Tree<String> t2 = Tree.getInstance(string2);

	Tree<String> t3 = Tree.getInstance(t1, t2);
	Tree<String> t4 = Tree.getInstance();
	Tree<String> t5 = Tree.getInstance(t3, t4);

	HashAlgorithm hashAlgorithm = HashAlgorithm.SHA256;
	StringToByteArray converter = StringToByteArray.getInstance();

	@Test
	public void ByteArrayHashMethodTest1() {

		// CRH
		ByteArray hash1 = hashAlgorithm.getHashValue(converter.convert(string1));
		ByteArray hash2 = hashAlgorithm.getHashValue(converter.convert(string2));
		ByteArray hash3 = hashAlgorithm.getHashValue(hash1.append(hash2));
		ByteArray hash4 = hashAlgorithm.getHashValue(ByteArray.getInstance());
		ByteArray hash5 = hashAlgorithm.getHashValue(hash3.append(hash4));

		HashMethod<String> hashMethod = HashMethod.getInstance(hashAlgorithm, converter);

		Assert.assertEquals(hash1, hashMethod.getHashValue(t1));
		Assert.assertEquals(hash2, hashMethod.getHashValue(t2));
		Assert.assertEquals(hash3, hashMethod.getHashValue(t3));
		Assert.assertEquals(hash4, hashMethod.getHashValue(t4));
		Assert.assertEquals(hash5, hashMethod.getHashValue(t5));

	}

	@Test
	public void ByteArrayHashMethodTest2() {

		// CAH
		Aggregator<ByteArray> aggregator = ByteArrayAggregator.getInstance();
		HashMethod<String> hashMethod = HashMethod.getInstance(hashAlgorithm, converter, aggregator);

		ByteArray b1 = converter.convert(string1);
		ByteArray b2 = converter.convert(string2);

		Tree<ByteArray> bt1 = Tree.getInstance(b1);
		Tree<ByteArray> bt2 = Tree.getInstance(b2);

		Tree<ByteArray> bt3 = Tree.getInstance(bt1, bt2);
		Tree<ByteArray> bt4 = Tree.getInstance();
		Tree<ByteArray> bt5 = Tree.getInstance(bt3, bt4);

		Assert.assertEquals(hashAlgorithm.getHashValue(aggregator.aggregate(bt1)), hashMethod.getHashValue(t1));
		Assert.assertEquals(hashAlgorithm.getHashValue(aggregator.aggregate(bt2)), hashMethod.getHashValue(t2));
		Assert.assertEquals(hashAlgorithm.getHashValue(aggregator.aggregate(bt3)), hashMethod.getHashValue(t3));
		Assert.assertEquals(hashAlgorithm.getHashValue(aggregator.aggregate(bt4)), hashMethod.getHashValue(t4));
		Assert.assertEquals(hashAlgorithm.getHashValue(aggregator.aggregate(bt5)), hashMethod.getHashValue(t5));

	}

	@Test
	public void ByteArrayHashMethodTest3() {

		// ACH
		StringAggregator aggregator = StringAggregator.getInstance();
		HashMethod<String> hashMethod = HashMethod.getInstance(hashAlgorithm, aggregator, converter);

		String b1 = aggregator.aggregate(t1);
		String b2 = aggregator.aggregate(t2);
		String b3 = aggregator.aggregate(t3);
		String b4 = aggregator.aggregate(t4);
		String b5 = aggregator.aggregate(t5);

		Assert.assertEquals(hashAlgorithm.getHashValue(converter.convert(b1)), hashMethod.getHashValue(t1));
		Assert.assertEquals(hashAlgorithm.getHashValue(converter.convert(b2)), hashMethod.getHashValue(t2));
		Assert.assertEquals(hashAlgorithm.getHashValue(converter.convert(b3)), hashMethod.getHashValue(t3));
		Assert.assertEquals(hashAlgorithm.getHashValue(converter.convert(b4)), hashMethod.getHashValue(t4));
		Assert.assertEquals(hashAlgorithm.getHashValue(converter.convert(b5)), hashMethod.getHashValue(t5));

	}

}
