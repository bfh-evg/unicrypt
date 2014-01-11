/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 ???
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
 *   a written agreement between you and ???.
 *
 *
 *   For further information contact ???
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.util;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * This class represents the ... Noch nicht fertig.
 * http://www.csc.kth.se/utbildning/kth/kurser/SA104X/fkand13/vmnv-1.1.0.pdf
 * <p>
 * Even though NIO is in use {@link ByteBuffer} this class does not (yet) work with direct buffers, as
 * allocation/deallocation handling costs more than indirectly used (See API)
 * <p>
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public abstract class ByteTree {

	public final static int BYTES_USED_FOR_IDENTIFIER = 1;
	public final static int BYTES_USED_FOR_AMOUNT = 4;
	public final static int BYTES_USED_FOR_PREAMBLE = BYTES_USED_FOR_IDENTIFIER + BYTES_USED_FOR_AMOUNT;

	private byte[] value;

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
	public static ByteTree getInstance(byte[] bytes) {
		return new ByteTreeLeaf(bytes);
	}

	/**
	 * Returns the ByteTree representation of a given serialized ByteTree.
	 * <p>
	 * @param bytes the serialized ByteTree
	 * @return ByteTree representation
	 */
	public static ByteTree getDeserializedInstance(byte[] bytes) {
		if (bytes == null || bytes.length < BYTES_USED_FOR_PREAMBLE) {
			throw new IllegalArgumentException();
		}
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
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
	public byte[] getSerializedByteTree() {
		if (this.value == null) {
			byte[] internalValue = new byte[this.defaultGetSize()];
			ByteBuffer buffer = ByteBuffer.wrap(internalValue);
			defaultSerialize(buffer);
			value = internalValue;
		}
		return this.value;
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
		if (value != null) {
			buffer.put(value);
		} else {
			abstractSerialize(buffer);
		}
	}

	protected int defaultGetSize() {
		if (value != null) {
			return value.length;
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

	private final static class ByteTreeNode
		   extends ByteTree {

		public static final byte IDENTIFIER = 0;
		private final ByteTree[] elements;

		private ByteTreeNode(ByteBuffer buffer) {
			int amountOfElements = buffer.getInt();
			elements = new ByteTree[amountOfElements];

			for (int i = 0; i < elements.length; i++) {
				byte identifier = buffer.get();
				switch (identifier) {
					case ByteTreeLeaf.IDENTIFIER:
						elements[i] = new ByteTreeLeaf(buffer);
						break;
					case ByteTreeNode.IDENTIFIER:
						elements[i] = new ByteTreeNode(buffer);
						break;
					default:
						throw new IllegalArgumentException();
				}
			}
		}

		private ByteTreeNode(ByteTree... elements) {
			if (elements == null) {
				throw new IllegalArgumentException();
			}
			for (ByteTree element : elements) {
				if (element == null) {
					throw new IllegalArgumentException();
				}
			}
			this.elements = elements;
		}

		@Override
		protected void abstractSerialize(ByteBuffer buffer) {
			buffer.put(IDENTIFIER);
			buffer.putInt(this.elements.length);
			for (ByteTree element : elements) {
				element.defaultSerialize(buffer);
			}
		}

		@Override
		protected int abstractGetSize() {
			int size = 0;
			for (ByteTree element : elements) {
				size += element.defaultGetSize();
			}
			return size;

		}

	}

	private final static class ByteTreeLeaf
		   extends ByteTree {

		public static final byte IDENTIFIER = 1;
		private final byte[] bytes;

		private ByteTreeLeaf(ByteBuffer buffer) {
			int amountOfBytes = buffer.getInt();
			bytes = new byte[amountOfBytes];

			buffer.get(bytes);
		}

		private ByteTreeLeaf(byte[] bytes) {
			this.bytes = bytes;
		}

		@Override
		protected void abstractSerialize(ByteBuffer buffer) {
			buffer.put(IDENTIFIER);
			buffer.putInt(this.bytes.length);
			buffer.put(bytes);
		}

		@Override
		protected int abstractGetSize() {
			return this.bytes.length;
		}

	}

	public static void main(String[] args) {
		ByteTree b1 = ByteTree.getInstance("Hallo".getBytes());
		byte[] value1 = b1.getSerializedByteTree();
		System.out.println(Arrays.toString(value1));

		ByteTree b2 = ByteTree.getInstance("Welt".getBytes());
		byte[] value2 = b2.getSerializedByteTree();
		System.out.println(Arrays.toString(value2));

		ByteTree b3 = ByteTree.getInstance(b1, b2);

		byte[] value3 = b3.getSerializedByteTree();
		System.out.println(Arrays.toString(value3));

		ByteTree b4 = ByteTree.getInstance(".".getBytes());
		byte[] value4 = b4.getSerializedByteTree();
		System.out.println(Arrays.toString(value4));

		ByteTree b5 = ByteTree.getInstance(b3, b4);

		byte[] value5 = b5.getSerializedByteTree();
		System.out.println(Arrays.toString(value5));

		ByteTree b6 = ByteTree.getInstance(b1, b2, b4);

		byte[] value6 = b6.getSerializedByteTree();
		System.out.println(Arrays.toString(value6));

		ByteTree b7 = ByteTree.getDeserializedInstance(value6);

		byte[] value7 = b7.getSerializedByteTree();
		System.out.println(Arrays.toString(value7));

	}

}
