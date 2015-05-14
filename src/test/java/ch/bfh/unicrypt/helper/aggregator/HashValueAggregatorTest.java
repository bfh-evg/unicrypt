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
package ch.bfh.unicrypt.helper.aggregator;

import ch.bfh.unicrypt.helper.aggregator.classes.HashValueAggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class HashValueAggregatorTest {

	@Test
	public void test() {

		ByteArray b0 = ByteArray.getInstance();
		ByteArray b1 = ByteArray.getInstance(0);
		ByteArray b2 = ByteArray.getInstance(0, 1);

		ByteArray h0 = b0.getHashValue();
		ByteArray h1 = b1.getHashValue();
		ByteArray h2 = b2.getHashValue();

		HashValueAggregator agr1 = HashValueAggregator.getInstance();
		HashValueAggregator agr2 = HashValueAggregator.getInstance(HashAlgorithm.MD5);

		Assert.assertEquals(agr1.getHashAlgorithm().getByteLength(), agr1.aggregateLeaf(b1).getLength());
		Assert.assertEquals(agr2.getHashAlgorithm().getByteLength(), agr2.aggregateLeaf(b1).getLength());

		Assert.assertEquals(agr1.getHashAlgorithm().getByteLength(), agr1.aggregateNode(b0, b1, b2).getLength());
		Assert.assertEquals(agr2.getHashAlgorithm().getByteLength(), agr2.aggregateNode(b0, b1, b2).getLength());

		Assert.assertEquals(h0.append(h1).append(h2).getHashValue(), agr1.aggregateNode(h0, h1, h2));

	}

}
