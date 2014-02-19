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
package ch.bfh.unicrypt.math.helper;

import ch.bfh.unicrypt.Example;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.N;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.bytetree.ByteTree;

/**
 *
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public class ByteTreeExample {

	public static void example1() {

		// Define multiple byte tree leaves
		ByteTree b1 = ByteTree.getInstance(1);
		ByteTree b2 = ByteTree.getInstance(2);
		ByteTree b3 = ByteTree.getInstance(3);
		ByteTree b4 = ByteTree.getInstance(4);
		ByteTree b5 = ByteTree.getInstance(256);

		// Combine b1 to b3
		ByteTree b123 = ByteTree.getInstance(b1, b2, b3);
		Example.printLine(b123);
		Example.printLine(b123.getByteArray());

		// Combine b4 and b5
		ByteTree b45 = ByteTree.getInstance(b4, b5);
		Example.printLine(b45);
		Example.printLine(b45.getByteArray());

		// Combine b123 and b45
		ByteTree b123_45 = ByteTree.getInstance(b123, b45);
		Example.printLine(b123_45);
		Example.printLine(b123_45.getByteArray());
	}

	public static void example2() {

		// The same as Example 1, but using UniCrypt elements
		N naturalNumber = N.getInstance();

		// Define multiple natural numbers
		Element e1 = naturalNumber.getElement(1);
		Element e2 = naturalNumber.getElement(2);
		Element e3 = naturalNumber.getElement(3);
		Element e4 = naturalNumber.getElement(4);
		Element e5 = naturalNumber.getElement(256);

		// Combine e1 to e3
		Tuple t123 = Tuple.getInstance(e1, e2, e3);
		ByteTree b123 = t123.getByteTree();
		Example.printLine(b123);
		Example.printLine(b123.getByteArray());

		// Combine e4 and e5
		Tuple t45 = Tuple.getInstance(e4, e5);
		ByteTree b45 = t45.getByteTree();
		Example.printLine(b45);
		Example.printLine(b45.getByteArray());

		// Combine t123 and t45
		Tuple t123_45 = Tuple.getInstance(t123, t45);
		ByteTree b123_45 = t123_45.getByteTree();
		Example.printLine(b123_45);
		Example.printLine(b123_45.getByteArray());
	}

	public static void example3() {

		// Same as Example 1
		ByteTree b1 = ByteTree.getInstance(1);
		ByteTree b2 = ByteTree.getInstance(2);
		ByteTree b3 = ByteTree.getInstance(3);
		ByteTree b4 = ByteTree.getInstance(4);
		ByteTree b5 = ByteTree.getInstance(256);
		ByteTree b123 = ByteTree.getInstance(b1, b2, b3);
		ByteTree b45 = ByteTree.getInstance(b4, b5);
		ByteTree b123_45 = ByteTree.getInstance(b123, b45);
		Example.printLine(b123_45);

		// Generate byte array from byte tree
		ByteArray byteArray = b123_45.getByteArray();
		Example.printLine(byteArray);

		// Reconstruct byte tree from byte array
		ByteTree reconstructedTree = ByteTree.getInstanceFrom(byteArray);
		Example.printLine(reconstructedTree);
	}

	public static void example4() {

		// Same as Example 1
		N naturalNumber = N.getInstance();
		Element e1 = naturalNumber.getElement(1);
		Element e2 = naturalNumber.getElement(2);
		Element e3 = naturalNumber.getElement(3);
		Element e4 = naturalNumber.getElement(4);
		Element e5 = naturalNumber.getElement(256);
		Tuple t123 = Tuple.getInstance(e1, e2, e3);
		Tuple t45 = Tuple.getInstance(e4, e5);
		Tuple t123_45 = Tuple.getInstance(t123, t45);
		Set set = t123_45.getSet();

		// Generate byte array from tuple
		ByteArray byteArray = t123_45.getByteTree().getByteArray();
		Example.printLine(byteArray);

		// Reconstruct tuple from byte array
		ByteTree reconstructedTree = ByteTree.getInstanceFrom(byteArray);
		Element reconstructedElement = set.getElementFrom(reconstructedTree);
		Example.printLine(reconstructedElement);
	}

	public static void main(String[] args) {
		Example.runExamples();
	}

}

//		ByteTree b1 = ByteTree.getInstance(ByteArray.getInstance("Hallo".getBytes()));
//		ByteArray value1 = b1.getByteArray();
//		System.out.println((value1));
//		ByteTree bb1 = ByteTree.getInstanceFrom(value1);
//		ByteArray value11 = ((ByteTreeLeaf) bb1).getValue();
//		System.out.println(new String(value11.getAll()));
//
//		System.out.println("----");
//
//		ByteTree b2 = ByteTree.getInstance(ByteArray.getInstance("Welt".getBytes()));
//		ByteArray value2 = b2.getByteArray();
//		System.out.println(value2);
//
//		System.out.println("----");
//
//		ByteTree b3 = ByteTree.getInstance(b1, b2);
//
//		ByteArray value3 = b3.getByteArray();
//		System.out.println(value3);
//		ByteTree value31 = ByteTree.getInstanceFrom(value3);
//		ByteTree[] byteTrees = ((ByteTreeNode) value31).getChildren();
//
//		ByteArray value32 = ((ByteTreeLeaf) byteTrees[0]).getValue();
//		ByteArray value33 = ((ByteTreeLeaf) byteTrees[1]).getValue();
//
//		System.out.println(new String(value32.getAll()) + " " + new String(value33.getAll()));
//
//		System.out.println("----");
//
//		ByteTree b4 = ByteTree.getInstance(ByteArray.getInstance(".".getBytes()));
//		ByteArray value4 = b4.getByteArray();
//		System.out.println(value4);
//
//		System.out.println("----");
//
//		ByteTree b5 = ByteTree.getInstance(b3, b4);
//
//		ByteArray value5 = b5.getByteArray();
//		System.out.println(value5);
//
//		ByteTree byteTree5 = ByteTree.getInstanceFrom(value5);
//		ByteTree[] byteTrees51 = ((ByteTreeNode) byteTree5).getChildren();
//
//		ByteTree[] byteTrees52 = ((ByteTreeNode) byteTrees51[0]).getChildren();
//
//		ByteArray value51 = ((ByteTreeLeaf) byteTrees52[0]).getValue();
//		ByteArray value52 = ((ByteTreeLeaf) byteTrees52[1]).getValue();
//
//		ByteArray value53 = ((ByteTreeLeaf) byteTrees51[1]).getValue();
//
//		System.out.println(new String(value51.getAll()) + " " + new String(value52.getAll()) + new String(value53.getAll()));
//
//		System.out.println("----");
//
//		ByteTree b6 = ByteTree.getInstance(b1, b2, b4);
//
//		ByteArray value6 = b6.getByteArray();
//		System.out.println(value6);
//
//		ByteTree byteTree6 = ByteTree.getInstanceFrom(value6);
//		ByteTree[] byteTrees6 = ((ByteTreeNode) byteTree6).getChildren();
//
//		ByteArray value61 = ((ByteTreeLeaf) byteTrees6[0]).getValue();
//		ByteArray value62 = ((ByteTreeLeaf) byteTrees6[1]).getValue();
//		ByteArray value63 = ((ByteTreeLeaf) byteTrees6[2]).getValue();
//
//		System.out.println(new String(value61.getAll()) + " " + new String(value62.getAll()) + new String(value63.getAll()));
//
//		System.out.println("----");
//
//		ByteTree b7 = ByteTree.getInstanceFrom(value6);
//
//		ByteArray value7 = b7.getByteArray();
//		System.out.println(value7);
//
//		System.out.println("----");
//
//		ByteTree example = ByteTree.getInstance(
//			   ByteTree.getInstance(
//					  ByteTree.getInstance(ByteArray.getInstance(BigInteger.valueOf(1).toByteArray())), ByteTree.getInstance(ByteArray.getInstance(BigInteger.valueOf(23).toByteArray()))
//			   ), ByteTree.getInstance(ByteArray.getInstance(BigInteger.valueOf(45).toByteArray())));
//
//		System.out.println(example.getByteArray());
