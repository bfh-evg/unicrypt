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
package ch.bfh.unicrypt.helper.aggregator.classes;

import ch.bfh.unicrypt.helper.MathUtil;
import ch.bfh.unicrypt.helper.aggregator.abstracts.AbstractAggregator;
import ch.bfh.unicrypt.helper.iterable.IterableArray;
import java.math.BigInteger;

/**
 * The single instance of this class specifies the invertible aggregation of a tree of non-negative {@code BigInteger}
 * values. Leaves are marked by setting the least significant bit to 0 (by multiplying the value by 2). Similarly, nodes
 * are marked by setting the least significant bit to 1 (by multiplying the value by 2 and adding 1). The values
 * obtained from the children of a node are aggregated using {@link MathUtil#pairWithSize(java.math.BigInteger...)}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class BigIntegerAggregator
	   extends AbstractAggregator<BigInteger> {

	private static BigIntegerAggregator instance = null;
	private static final long serialVersionUID = 1L;

	private BigIntegerAggregator() {
		super(BigInteger.class);
	}

	/**
	 * Return the single instance of this class.
	 * <p>
	 * @return The single instance of this class
	 */
	public static BigIntegerAggregator getInstance() {
		if (BigIntegerAggregator.instance == null) {
			BigIntegerAggregator.instance = new BigIntegerAggregator();
		}
		return BigIntegerAggregator.instance;
	}

	// leaves are marked with 0 bit
	@Override
	protected BigInteger abstractAggregateLeaf(BigInteger value) {
		if (value.signum() < 0) {
			throw new IllegalArgumentException();
		}
		return value.multiply(MathUtil.TWO);
	}

	// nodes are marked with 1 bit
	@Override
	protected BigInteger abstractAggregateNode(Iterable<BigInteger> values, int length) {
		BigInteger[] valueArray = new BigInteger[length];
		int i = 0;
		for (BigInteger value : values) {
			valueArray[i] = value;
			i++;
		}
		return MathUtil.pairWithSize(valueArray).multiply(MathUtil.TWO).add(MathUtil.ONE);
	}

	@Override
	protected boolean abstractIsLeaf(BigInteger value) {
		return value.mod(MathUtil.TWO).equals(MathUtil.ZERO);
	}

	@Override
	protected boolean abstractIsNode(BigInteger value) {
		return value.mod(MathUtil.TWO).equals(MathUtil.ONE);
	}

	@Override
	protected BigInteger abstractDisaggregateLeaf(BigInteger value) {
		return value.divide(MathUtil.TWO);
	}

	@Override
	protected Iterable<BigInteger> abstractDisaggregateNode(BigInteger value) {
		return IterableArray.getInstance(MathUtil.unpairWithSize(value.subtract(MathUtil.ONE).divide(MathUtil.TWO)));
	}

}