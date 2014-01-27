/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.math.helper.bytetree;

import ch.bfh.unicrypt.math.helper.ByteArray;
import java.io.Serializable;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * This class represents the ByteTree described in Wikstroms Verifier.
 * http://www.csc.kth.se/utbildning/kth/kurser/SA104X/fkand13/vmnv-1.1.0.pdf
 * <p>
 * This implementation aims for a possible interchange of Wikstroms library.
 * <p>
 * The following description of the ByteTree is mainly adopted from the referenced document:
 * <p>
 * "We use a byte-oriented format to represent objects on file and to turn them into arrays of bytes. The goal of this
 * format is to be as simple as possible."
 * <p>
 * A byte tree is either a leaf containing an array of bytes, or a node containing other byte trees.
 * <p>
 * We use a 8k-bit two’s-complement representation of n in big endian byte order.
 * <p>
 * A byte tree is represented by an array of bytes as follows: • Leaf: Concatenation of 1 byte 01 indicating the leaf 4
 * bytes indicating the number of data bytes data bytes
 * <p>
 *
 * • Node: Concatenation of 1 byte 00 indicating the node 4 bytes bytes indicating the number of children children
 * (either Node / Leaf)
 * <p>
 * Example: node(node(leaf(1), leaf(23)), leaf(45)) is represented as
 * <p>
 * [0, 0, 0, 0, 2, 0, 0, 0, 0, 2, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 23, 1, 0, 0, 0, 1, 45]
 * <p>
 * node1........(..node2.......(..leaf1.............leaf2.............)leaf3............)
 * <p>
 * <p>
 * <p>
 * Even though NIO is in use {@link ByteBuffer} this class does not (yet) work with direct buffers, as
 * allocation/deallocation handling costs more than indirectly used (See API)
 * <p>
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public abstract class ByteTree
	   implements Serializable {

	public final static int BYTES_USED_FOR_IDENTIFIER = Byte.SIZE / 8;
	public final static int BYTES_USED_FOR_AMOUNT = Integer.SIZE / 8;
	public final static int BYTES_USED_FOR_PREAMBLE = BYTES_USED_FOR_IDENTIFIER + BYTES_USED_FOR_AMOUNT;

	private byte[] serializedValue;

	/**
	 * Returns a new instance of a ByteTree given one or more ByteTree-elements.
	 * <p>
	 * @param children ByteTree-elements that will be connected as children to the new instance of ByteTree
	 * @return new Instance of a ByteTree with at least one node-element and one leaf-element.
	 */
	public static ByteTree getInstance(ByteTree... children) {
		return new ByteTreeNode(children);
	}

	/**
	 * Returns a new instance of a ByteTree given a byte[].
	 * <p>
	 * @param bytes The byte[] that will be embedded as leaf-element of a ByteTree
	 * @return new Instance of a ByteTree with exactly one leaf-element.
	 */
	public static ByteTreeLeaf getInstance(ByteArray bytes) {
		return new ByteTreeLeaf(bytes);
	}

	/**
	 * Returns the ByteTree representation of a given serialized ByteTree.
	 * <p>
	 * @param bytes the serialized ByteTree
	 * @return ByteTree representation
	 */
	public static ByteTree getDeserializedInstance(ByteArray bytes) {
		if (bytes == null || bytes.getLength() < BYTES_USED_FOR_PREAMBLE) {
			throw new IllegalArgumentException();
		}
		ByteBuffer buffer = ByteBuffer.wrap(bytes.getAll());
		ByteTree byteTree = null;
		if (buffer.hasRemaining()) {
			try {
				byte identifier = buffer.get();
				switch (identifier) {
					case ByteTreeLeaf.IDENTIFIER:
						byteTree = new ByteTreeLeaf(buffer);
						break;
					case ByteTreeNode.IDENTIFIER:
						byteTree = new ByteTreeNode(buffer);
						break;
					default:
						throw new IllegalArgumentException();
				}
			} catch (BufferUnderflowException ex) {
				throw new IllegalArgumentException();
			}
		}
		return byteTree;
	}

	/**
	 * Calculates the serialized version of a ByteTree.
	 * <p>
	 * @return Serialized ByteTree
	 */
	public ByteArray getSerializedByteTree() {
		if (this.serializedValue == null) {
			byte[] internalValue = new byte[this.defaultGetSize()];
			ByteBuffer buffer = ByteBuffer.wrap(internalValue);
			defaultSerialize(buffer);
			serializedValue = internalValue;
		}
		if (serializedValue == null) {
			return ByteArray.getInstance();
		} else {
			return ByteArray.getInstance(this.serializedValue);
		}
	}

	/**
	 * Only called by a parent node via {@link ByteTree#getDeserializedInstance(byte[]) }. This method calculates the
	 * value for this sub-ByteTree. If this value is already known (If this Element one was a root-node), the known
	 * value will be written to the buffer for getSerializedByteTree and will be sent back. If the value of this Element
	 * is unknown, it will be propagated via {@link ByteTree#abstractSerialize(java.nio.ByteBuffer) } to the subclasses
	 * for further calculation.
	 * <p>
	 * Remark: The value calculated here will not be stored as it does not render a good 'time / memory' ratio.
	 * <p>
	 * @param buffer The target buffer where the value shall be written to.
	 */
	protected void defaultSerialize(ByteBuffer buffer) {
		if (serializedValue != null) {
			buffer.put(serializedValue);
		} else {
			abstractSerialize(buffer);
		}
	}

	protected int defaultGetSize() {
		if (serializedValue != null) {
			return serializedValue.length;
		}
		int size = BYTES_USED_FOR_PREAMBLE;
		size += abstractGetSize();
		return size;
	}

	/**
	 * Only called by {@link ByteTree#defaultSerialize(java.nio.ByteBuffer) ) in order to calculate the value of the ByteTree
	 * <p>
	 * @param buffer
	 */
	protected abstract void abstractSerialize(ByteBuffer buffer);

	/**
	 * Returns the true length of the byte[] that represents this ByteTree.
	 * <p>
	 * @return
	 */
	protected abstract int abstractGetSize();

}
