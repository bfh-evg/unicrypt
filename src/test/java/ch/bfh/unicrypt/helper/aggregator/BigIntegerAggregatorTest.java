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

import ch.bfh.unicrypt.helper.aggregator.classes.BigIntegerAggregator;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.tree.Tree;
import java.math.BigInteger;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class BigIntegerAggregatorTest {

	@Test
	public void BigIntegerAggregatorTestLeaf() {
		BigIntegerAggregator aggregator = BigIntegerAggregator.getInstance();
		for (int i = 0; i < 100; i++) {
			BigInteger value = BigInteger.valueOf(i);
			BigInteger aggregatedValue = aggregator.aggregate(Tree.getInstance(value));
			Assert.assertEquals(value.multiply(MathUtil.TWO), aggregatedValue);
			Assert.assertEquals(Tree.getInstance(value), aggregator.disaggregate(aggregatedValue));
		}
	}

	@Test
	public void BigIntegerAggregatorTestNode() {
		BigIntegerAggregator aggregator = BigIntegerAggregator.getInstance();
		for (int i = 0; i < 100; i++) {
			BigInteger aggregatedValue = BigInteger.valueOf(i);
			Assert.assertEquals(aggregatedValue, aggregator.aggregate(aggregator.disaggregate(aggregatedValue)));
		}
		try {
			aggregator.disaggregate(BigInteger.valueOf(-1));
			fail();
		} catch (Exception e) {
		}
	}

}
