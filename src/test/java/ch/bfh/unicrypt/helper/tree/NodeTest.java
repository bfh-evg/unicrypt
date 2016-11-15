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
import ch.bfh.unicrypt.helper.aggregator.classes.ByteArrayAggregator;
import ch.bfh.unicrypt.helper.aggregator.classes.StringAggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class NodeTest {

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void generalNodeTest() {
		Leaf<String> leaf1 = Tree.getInstance("Hello");
		Leaf<String> leaf2 = Tree.getInstance("World");
		Leaf<String> leaf3 = Tree.getInstance("!");

		Node<String> node1 = Tree.getInstance(leaf1, leaf2, leaf3);
		Node<String> node2 = Tree.getInstance(leaf1, leaf2, leaf3);
		Node<String> node3 = Tree.getInstance(node1, node2);
		Node<String> node4 = Tree.getInstance();
		Node<String> node5 = Tree.getInstance(node3, node4, leaf1);
		Node<String> node6 = Tree.getInstance(node3, node4, leaf1);
		Node<String> node7 = Tree.getInstance(node4, node4);
		Node<String> node8 = Tree.getInstance(node7, leaf1, node4, node7);

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
		Tree<String> l1 = Tree.getInstance("One");
		Tree<String> l2 = Tree.getInstance("Two");
		Tree<String> l3 = Tree.getInstance("Three");
		Tree<String> l4 = Tree.getInstance("Four");
		Tree<String> l5 = Tree.getInstance("Five");
		Tree<String> l6 = Tree.getInstance("Six");
		Tree<String> l7 = Tree.getInstance("Seven");
		Tree<String> l8 = Tree.getInstance("Eight");
		Tree<String> l9 = Tree.getInstance("Nine");
		Tree<String> n1 = Tree.getInstance(l1, l2, l3);
		Tree<String> n2 = Tree.getInstance(l4, l5);
		Tree<String> n3 = Tree.getInstance(l6, l7, l8);
		Tree<String> n4 = Tree.getInstance(n1, n2, l9);
		Tree<String> root = Tree.getInstance(n3, n4);
		StringAggregator sa = StringAggregator.getInstance();
		String aggregatedValue = root.aggregate(sa);
		Assert.assertEquals("[[\"Six\"|\"Seven\"|\"Eight\"]|[[\"One\"|\"Two\"|\"Three\"]|[\"Four\"|\"Five\"]|\"Nine\"]]", aggregatedValue);
		Tree<String> result = Tree.getInstance(aggregatedValue, sa);
		Assert.assertEquals(result, root);
	}

	@Test
	public void generalNodeTest3() {
		Tree<BigInteger> l1 = Tree.getInstance(BigInteger.valueOf(1));
		Tree<BigInteger> l2 = Tree.getInstance(BigInteger.valueOf(2));
		Tree<BigInteger> l3 = Tree.getInstance(BigInteger.valueOf(3));
		Tree<BigInteger> l4 = Tree.getInstance(BigInteger.valueOf(4));
		Tree<BigInteger> l5 = Tree.getInstance(BigInteger.valueOf(5));
		Tree<BigInteger> l6 = Tree.getInstance(BigInteger.valueOf(6));
		Tree<BigInteger> l7 = Tree.getInstance(BigInteger.valueOf(7));
		Tree<BigInteger> l8 = Tree.getInstance(BigInteger.valueOf(8));
		Tree<BigInteger> l9 = Tree.getInstance(BigInteger.valueOf(9));
		Tree<BigInteger> n1 = Tree.getInstance(l1, l2, l3);
		Tree<BigInteger> n2 = Tree.getInstance(l4, l5);
		Tree<BigInteger> n3 = Tree.getInstance(l6, l7, l8);
		Tree<BigInteger> n4 = Tree.getInstance(n1, n2, l9);
		Tree<BigInteger> n5 = Tree.getInstance();
		Tree<BigInteger> root = Tree.getInstance(n3, n4, n5);
		BigIntegerAggregator aggregator = BigIntegerAggregator.getInstance();
		Tree<BigInteger> result = Tree.getInstance(root.aggregate(aggregator), aggregator);
		Assert.assertEquals(result, root);
		Assert.assertEquals(result.aggregate(aggregator), root.aggregate(aggregator));
		for (int i = 0; i < 1000; i++) {
			Tree<BigInteger> tree = Tree.getInstance(BigInteger.valueOf(i), aggregator);
			Assert.assertEquals(i, tree.aggregate(aggregator).intValue());
		}
	}

	@Test
	public void generalNodeTest4() {
		Tree<ByteArray> l1 = Tree.getInstance(ByteArray.getInstance(1));
		Tree<ByteArray> l2 = Tree.getInstance(ByteArray.getInstance(1, 2));
		Tree<ByteArray> l3 = Tree.getInstance(ByteArray.getInstance(1, 2, 3));
		Tree<ByteArray> l4 = Tree.getInstance(ByteArray.getInstance(1, 2, 3, 4));
		Tree<ByteArray> l5 = Tree.getInstance(ByteArray.getInstance(1, 2, 3, 4, 5));
		Tree<ByteArray> l6 = Tree.getInstance(ByteArray.getInstance(1, 2, 3, 4, 5, 6));
		Tree<ByteArray> l7 = Tree.getInstance(ByteArray.getInstance(1, 2, 3, 4, 5, 6, 7));
		Tree<ByteArray> l8 = Tree.getInstance(ByteArray.getInstance(1, 2, 3, 4, 5, 6, 7, 8));
		Tree<ByteArray> l9 = Tree.getInstance(ByteArray.getInstance(1, 2, 3, 4, 5, 6, 7, 8, 9));
		Tree<ByteArray> n1 = Tree.getInstance(l1, l2, l3);
		Tree<ByteArray> n2 = Tree.getInstance(l4, l5);
		Tree<ByteArray> n3 = Tree.getInstance(l6, l7, l8);
		Tree<ByteArray> n4 = Tree.getInstance(n1, n2, l9);
		Tree<ByteArray> n5 = Tree.getInstance();
		Tree<ByteArray> root = Tree.getInstance(n3, n4, n5);
		ByteArrayAggregator aggregator = ByteArrayAggregator.getInstance();
		Tree<ByteArray> result = Tree.getInstance(root.aggregate(aggregator), aggregator);
		Assert.assertEquals(result, root);
		Assert.assertEquals(result.aggregate(aggregator), root.aggregate(aggregator));
	}

}
