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
package ch.bfh.unicrypt.bytetree;

import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public class ByteTreeExample {

	public static void main(String[] args) {
		ByteTree b1 = ByteTree.getInstance("Hallo".getBytes());
		byte[] value1 = b1.getSerializedByteTree();
		System.out.println(Arrays.toString(value1));
		ByteTree bb1 = ByteTree.getDeserializedInstance(value1);
		byte[] value11 = ((ByteTreeLeaf) bb1).getValue();
		System.out.println(new String(value11));

		System.out.println("----");

		ByteTree b2 = ByteTree.getInstance("Welt".getBytes());
		byte[] value2 = b2.getSerializedByteTree();
		System.out.println(Arrays.toString(value2));

		System.out.println("----");

		ByteTree b3 = ByteTree.getInstance(b1, b2);

		byte[] value3 = b3.getSerializedByteTree();
		System.out.println(Arrays.toString(value3));
		ByteTree value31 = ByteTree.getDeserializedInstance(value3);
		ByteTree[] byteTrees = ((ByteTreeNode) value31).getChildren();

		byte[] value32 = ((ByteTreeLeaf) byteTrees[0]).getValue();
		byte[] value33 = ((ByteTreeLeaf) byteTrees[1]).getValue();

		System.out.println(new String(value32) + " " + new String(value33));

		System.out.println("----");

		ByteTree b4 = ByteTree.getInstance(".".getBytes());
		byte[] value4 = b4.getSerializedByteTree();
		System.out.println(Arrays.toString(value4));

		System.out.println("----");

		ByteTree b5 = ByteTree.getInstance(b3, b4);

		byte[] value5 = b5.getSerializedByteTree();
		System.out.println(Arrays.toString(value5));

		ByteTree byteTree5 = ByteTree.getDeserializedInstance(value5);
		ByteTree[] byteTrees51 = ((ByteTreeNode) byteTree5).getChildren();

		ByteTree[] byteTrees52 = ((ByteTreeNode) byteTrees51[0]).getChildren();

		byte[] value51 = ((ByteTreeLeaf) byteTrees52[0]).getValue();
		byte[] value52 = ((ByteTreeLeaf) byteTrees52[1]).getValue();

		byte[] value53 = ((ByteTreeLeaf) byteTrees51[1]).getValue();

		System.out.println(new String(value51) + " " + new String(value52) + new String(value53));

		System.out.println("----");

		ByteTree b6 = ByteTree.getInstance(b1, b2, b4);

		byte[] value6 = b6.getSerializedByteTree();
		System.out.println(Arrays.toString(value6));

		ByteTree byteTree6 = ByteTree.getDeserializedInstance(value6);
		ByteTree[] byteTrees6 = ((ByteTreeNode) byteTree6).getChildren();

		byte[] value61 = ((ByteTreeLeaf) byteTrees6[0]).getValue();
		byte[] value62 = ((ByteTreeLeaf) byteTrees6[1]).getValue();
		byte[] value63 = ((ByteTreeLeaf) byteTrees6[2]).getValue();

		System.out.println(new String(value61) + " " + new String(value62) + new String(value63));

		System.out.println("----");

		ByteTree b7 = ByteTree.getDeserializedInstance(value6);

		byte[] value7 = b7.getSerializedByteTree();
		System.out.println(Arrays.toString(value7));

		System.out.println("----");

		ByteTree example = ByteTree.getInstance(
					 ByteTree.getInstance(
									ByteTree.getInstance(BigInteger.valueOf(1).toByteArray()), ByteTree.getInstance(BigInteger.valueOf(23).toByteArray())
					 ), ByteTree.getInstance(BigInteger.valueOf(45).toByteArray()));

		System.out.println(Arrays.toString(example.getSerializedByteTree()));

	}

}
