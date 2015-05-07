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

import ch.bfh.unicrypt.helper.MathUtil;
import ch.bfh.unicrypt.helper.iterable.IterableArray;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class BigIntegerAggregator
	   extends Aggregator<BigInteger> {

	public static BigIntegerAggregator getInstance() {
		return new BigIntegerAggregator();
	}

	// leaves are marked with 0 bit
	@Override
	public BigInteger abstractAggregate(BigInteger value) {
		return value.multiply(MathUtil.TWO);
	}

	// nodes are marked with 1 bit
	@Override
	public BigInteger abstractAggregate(Iterable<BigInteger> values, int length) {
		BigInteger[] valueArray = new BigInteger[length];
		int i = 0;
		for (BigInteger value : values) {
			valueArray[i] = value;
			i++;
		}
		return MathUtil.pairWithSize(valueArray).multiply(MathUtil.TWO).add(MathUtil.ONE);
	}

	@Override
	protected boolean abstractIsSingle(BigInteger value) {
		return value.mod(MathUtil.TWO).equals(MathUtil.ZERO);
	}

	@Override
	protected BigInteger abstractDisaggregateSingle(BigInteger value) {
		return value.divide(MathUtil.TWO);
	}

	@Override
	protected Iterable<BigInteger> abstractDisaggregateMultiple(BigInteger value) {
		return IterableArray.getInstance(MathUtil.unpairWithSize(value.subtract(MathUtil.ONE).divide(MathUtil.TWO)));
	}

	public static void main(String[] args) {
		BigInteger b1 = BigInteger.valueOf(15);
		BigInteger b2 = BigInteger.valueOf(20);
		BigInteger b3 = BigInteger.valueOf(4);
		BigIntegerAggregator aggregator = BigIntegerAggregator.getInstance();
		SingleOrMultiple<BigInteger> result = aggregator.disaggregate(aggregator.aggregate(b1, b2, b3));
		for (BigInteger value : result.getValues()) {
			System.out.println(value);
		}
	}

}
