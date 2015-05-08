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
package ch.bfh.unicrypt.helper.tree;

import ch.bfh.unicrypt.helper.aggregator.classes.BigIntegerAggregator;
import ch.bfh.unicrypt.helper.aggregator.classes.StringAggregator;
import java.math.BigInteger;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class NodeTest {

	@Test
	public void generalNodeTest() {
		Leaf<String> leaf1 = Leaf.getInstance("Hello");
		Leaf<String> leaf2 = Leaf.getInstance("World");
		Leaf<String> leaf3 = Leaf.getInstance("!");

		Node<String> node1 = Node.getInstance(leaf1, leaf2, leaf3);
		Node<String> node2 = Node.getInstance(leaf1, leaf2, leaf3);
		Node<String> node3 = Node.getInstance(node1, node2);
		Node<String> node4 = Node.getInstance();
		Node<String> node5 = Node.getInstance(node3, node4, leaf1);
		Node<String> node6 = Node.getInstance(node3, node4, leaf1);
		Node<String> node7 = Node.getInstance(node4, node4);
		Node<String> node8 = Node.getInstance(node7, leaf1, node4, node7);

		Assert.assertTrue(node1.equals(node1));
		Assert.assertTrue(node1.equals(node2));
		Assert.assertFalse(node1.equals(node3));
		Assert.assertFalse(node1.equals(node4));
		Assert.assertTrue(node2.equals(node2));
		Assert.assertFalse(node2.equals(node3));
		Assert.assertFalse(node2.equals(node4));
		Assert.assertTrue(node3.equals(node3));
		Assert.assertFalse(node3.equals(node4));
		Assert.assertTrue(node4.equals(node4));
		Assert.assertTrue(node5.equals(node6));

		{
			int i = 0;
			Leaf<String> expected = leaf1;
			for (Tree<String> node : node1.getChildren()) {
				Assert.assertEquals(expected, node);
				if (expected == leaf1) {
					expected = leaf2;
				} else {
					if (expected == leaf2) {
						expected = leaf3;
					}
				}
				i++;
			}
			Assert.assertEquals(3, i);
		}
		{
			int i = 0;
			for (String s : node5) {
				i++;
			}
			Assert.assertEquals(7, i);
		}
		{
			int i = 0;
			for (String s : node4) {
				i++;
			}
			Assert.assertEquals(0, i);
		}
		{
			int i = 0;
			for (String s : node8) {
				i++;
			}
			Assert.assertEquals(1, i);
		}
	}

	@Test
	public void generalNodeTest2() {
		Tree<String> l1 = Leaf.getInstance("One");
		Tree<String> l2 = Leaf.getInstance("Two");
		Tree<String> l3 = Leaf.getInstance("Three");
		Tree<String> l4 = Leaf.getInstance("Four");
		Tree<String> l5 = Leaf.getInstance("Five");
		Tree<String> l6 = Leaf.getInstance("Six");
		Tree<String> l7 = Leaf.getInstance("Seven");
		Tree<String> l8 = Leaf.getInstance("Eight");
		Tree<String> l9 = Leaf.getInstance("Nine");
		Tree<String> n1 = Node.getInstance(l1, l2, l3);
		Tree<String> n2 = Node.getInstance(l4, l5);
		Tree<String> n3 = Node.getInstance(l6, l7, l8);
		Tree<String> n4 = Node.getInstance(n1, n2, l9);
		Tree<String> root = Node.getInstance(n3, n4);
		StringAggregator sa = StringAggregator.getInstance();
		System.out.println(root);
		System.out.println(root.aggregate(sa));
		Assert.assertEquals("[[Six|Seven|Eight]|[[One|Two|Three]|[Four|Five]|Nine]]", root.aggregate(sa));
		Tree<String> result = Tree.getInstance(root.aggregate(sa), sa);
		Assert.assertEquals(result, root);
	}

	@Test
	public void generalNodeTest3() {
		Tree<BigInteger> l1 = Leaf.getInstance(BigInteger.valueOf(1));
		Tree<BigInteger> l2 = Leaf.getInstance(BigInteger.valueOf(2));
		Tree<BigInteger> l3 = Leaf.getInstance(BigInteger.valueOf(3));
		Tree<BigInteger> l4 = Leaf.getInstance(BigInteger.valueOf(4));
		Tree<BigInteger> l5 = Leaf.getInstance(BigInteger.valueOf(5));
		Tree<BigInteger> l6 = Leaf.getInstance(BigInteger.valueOf(6));
		Tree<BigInteger> l7 = Leaf.getInstance(BigInteger.valueOf(7));
		Tree<BigInteger> l8 = Leaf.getInstance(BigInteger.valueOf(8));
		Tree<BigInteger> l9 = Leaf.getInstance(BigInteger.valueOf(9));
		Tree<BigInteger> n1 = Node.getInstance(l1, l2, l3);
		Tree<BigInteger> n2 = Node.getInstance(l4, l5);
		Tree<BigInteger> n3 = Node.getInstance(l6, l7, l8);
		Tree<BigInteger> n4 = Node.getInstance(n1, n2, l9);
		Tree<BigInteger> n5 = Node.getInstance();
		Tree<BigInteger> root = Node.getInstance(n3, n4, n5);
		BigIntegerAggregator aggregator = BigIntegerAggregator.getInstance();
		Tree<BigInteger> result = Tree.getInstance(root.aggregate(aggregator), aggregator);
		Assert.assertEquals(result, root);
		Assert.assertEquals(result.aggregate(aggregator), root.aggregate(aggregator));
		for (int i = 0; i < 1000; i++) {
			Tree<BigInteger> tree = Tree.getInstance(BigInteger.valueOf(i), aggregator);
			Assert.assertEquals(i, tree.aggregate(aggregator).intValue());
		}
	}

}
