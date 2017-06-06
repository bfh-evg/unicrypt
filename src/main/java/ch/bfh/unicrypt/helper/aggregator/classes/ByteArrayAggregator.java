/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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

import ch.bfh.unicrypt.helper.aggregator.abstracts.AbstractAggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.sequence.SequenceIterator;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * The single instance of this class specifies the invertible aggregation of a tree of {@code ByteArray} values. Leaves
 * are prefixed with 5 additional bytes: a marker byte {@code 0x00}, plus four bytes for representing the length of the
 * byte array (big-endian). For example, {@code "00|00|00|00|05|A0|BC|89|C1|09"} is the result of the aggregating a leaf
 * storing the byte array {@code "A0|BC|89|C1|09"} of length {@code 0x05=5}. Nodes are treated similarly. The
 * concatenation of the byte arrays obtained from all children is prefixed with 5 additional bytes: a marker byte
 * {@code 0x01}, plus four bytes for representing the total length of the concatenated children byte arrays. For
 * example, a node with two identical children {@code "00|00|00|00|05|A0|BC|89|C1|09"} results in
 * {@code "01|00|00|00|14|00|00|00|05|A0|BC|89|C1|0900|00|00|00|05|A0|BC|89|C1|09"}, where {@code 0x14=20} is the length
 * of the concatenation of the two inputs of length {@code 0x0A=10}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class ByteArrayAggregator
	   extends AbstractAggregator<ByteArray> {

	private static final byte LEAF_IDENTIFIER = (byte) 0x00;
	private static final byte NODE_IDENTIFIER = (byte) 0x01;
	private static final int PREFIX_LENGTH = 1 + Integer.SIZE / Byte.SIZE;

	private static ByteArrayAggregator instance = null;
	private static final long serialVersionUID = 1L;

	/**
	 * Return the single instance of this class.
	 * <p>
	 * @return The single instance of this class
	 */
	public static ByteArrayAggregator getInstance() {
		if (ByteArrayAggregator.instance == null) {
			ByteArrayAggregator.instance = new ByteArrayAggregator();
		}
		return ByteArrayAggregator.instance;
	}

	@Override
	protected ByteArray abstractAggregateLeaf(ByteArray value) {
		ByteBuffer buffer = ByteBuffer.allocate(PREFIX_LENGTH + value.getLength());
		buffer.put(LEAF_IDENTIFIER);
		buffer.putInt(value.getLength());
		buffer.put(value.getBytes());
		return new SafeByteArray(buffer.array());
	}

	@Override
	protected ByteArray abstractAggregateNode(Sequence<ByteArray> values) {
		int length = values.getLength().intValue();
		ByteArray[] byteArrays = new ByteArray[length + 1];
		int i = 1;
		int byteLength = 0;
		for (ByteArray value : values) {
			byteArrays[i] = value;
			byteLength = byteLength + value.getLength();
			i++;
		}
		ByteBuffer buffer = ByteBuffer.allocate(PREFIX_LENGTH);
		buffer.put(NODE_IDENTIFIER);
		buffer.putInt(byteLength);
		byteArrays[0] = new SafeByteArray(buffer.array());
		return ByteArray.getInstance(byteArrays);
	}

	@Override
	protected boolean abstractIsLeaf(ByteArray value) {
		return value.getLength() >= PREFIX_LENGTH && value.getAt(0) == LEAF_IDENTIFIER;
	}

	@Override
	protected boolean abstractIsNode(ByteArray value) {
		return value.getLength() >= PREFIX_LENGTH && value.getAt(0) == NODE_IDENTIFIER;
	}

	@Override
	protected ByteArray abstractDisaggregateLeaf(ByteArray value) {
		return value.removePrefix(PREFIX_LENGTH);
	}

	@Override
	protected Sequence<ByteArray> abstractDisaggregateNode(final ByteArray value) {
		return new Sequence<ByteArray>() {

			@Override
			public SequenceIterator<ByteArray> iterator() {
				return new SequenceIterator<ByteArray>() {

					private int currentIndex = PREFIX_LENGTH;

					@Override
					public boolean hasNext() {
						return this.currentIndex < value.getLength();
					}

					@Override
					public ByteArray abstractNext() {
						ByteArray prefix = value.extract(this.currentIndex, PREFIX_LENGTH);
						int byteLength = new BigInteger(1, prefix.removePrefix(1).getBytes()).intValue();
						ByteArray next = value.extract(this.currentIndex, PREFIX_LENGTH + byteLength);
						this.currentIndex = this.currentIndex + PREFIX_LENGTH + byteLength;
						return next;
					}
				};
			}

		};
	}

}
