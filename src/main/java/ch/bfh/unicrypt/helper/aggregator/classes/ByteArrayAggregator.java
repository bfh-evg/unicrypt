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

import ch.bfh.unicrypt.helper.aggregator.abstracts.AbstractInvertibleAggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import java.nio.ByteBuffer;

/**
 *
 * @author rolfhaenni
 */
public class ByteArrayAggregator
	   extends AbstractInvertibleAggregator<ByteArray> {

	private static final byte LEAF_IDENTIFIER = (byte) 0x01;
	private static final byte NODE_IDENTIFIER = (byte) 0x00;
	private static final int PREFIX_LENGTH = 5;

	@Override
	protected ByteArray abstractAggregateLeaf(ByteArray value) {
		ByteBuffer buffer = ByteBuffer.allocate(PREFIX_LENGTH + value.getLength());
		buffer.put(LEAF_IDENTIFIER);
		buffer.putInt(value.getLength());
		buffer.put(value.getBytes());
		return new SafeByteArray(buffer.array());
	}

	@Override
	protected ByteArray abstractAggregateNode(Iterable<ByteArray> values, int length) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	protected Iterable<ByteArray> abstractDisaggregateNode(ByteArray value) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
// this local class allows creating instances of ByteArray without copying the array

class SafeByteArray
	   extends ByteArray {

	protected SafeByteArray(byte[] bytes) {
		super(bytes);
	}

}
