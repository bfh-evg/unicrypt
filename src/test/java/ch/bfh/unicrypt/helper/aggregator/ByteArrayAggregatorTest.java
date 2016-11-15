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

import ch.bfh.unicrypt.helper.aggregator.classes.ByteArrayAggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.tree.Leaf;
import ch.bfh.unicrypt.helper.tree.Node;
import ch.bfh.unicrypt.helper.tree.Tree;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class ByteArrayAggregatorTest {

	@Test
	public void ByteArrayAggregatorTestLeaf() {

		ByteArray b0 = ByteArray.getInstance();
		ByteArray b1 = ByteArray.getInstance(1);
		ByteArray b2 = ByteArray.getInstance(1, 2);
		ByteArray b3 = ByteArray.getInstance(1, 2, 3);

		ByteArrayAggregator agr = ByteArrayAggregator.getInstance();

		ByteArray l0 = agr.aggregate(Tree.getInstance(b0));
		ByteArray l1 = agr.aggregate(Tree.getInstance(b1));
		ByteArray l2 = agr.aggregate(Tree.getInstance(b2));
		ByteArray l3 = agr.aggregate(Tree.getInstance(b3));

		Assert.assertEquals(ByteArray.getInstance(0, 0, 0, 0, 0), l0);
		Assert.assertEquals(ByteArray.getInstance(0, 0, 0, 0, 1, 1), l1);
		Assert.assertEquals(ByteArray.getInstance(0, 0, 0, 0, 2, 1, 2), l2);
		Assert.assertEquals(ByteArray.getInstance(0, 0, 0, 0, 3, 1, 2, 3), l3);

		Assert.assertEquals(Tree.getInstance(b0), agr.disaggregate(l0));
		Assert.assertEquals(Tree.getInstance(b1), agr.disaggregate(l1));
		Assert.assertEquals(Tree.getInstance(b2), agr.disaggregate(l2));
		Assert.assertEquals(Tree.getInstance(b3), agr.disaggregate(l3));

	}

	@Test
	public void ByteArrayAggregatorTestNode() {

		ByteArrayAggregator agr = ByteArrayAggregator.getInstance();

		Leaf<ByteArray> b0 = Tree.getInstance(ByteArray.getInstance());
		Leaf<ByteArray> b1 = Tree.getInstance(ByteArray.getInstance(1));
		Leaf<ByteArray> b2 = Tree.getInstance(ByteArray.getInstance(1, 2));
		Leaf<ByteArray> b3 = Tree.getInstance(ByteArray.getInstance(1, 2, 3));

		Node<ByteArray> c1 = Tree.getInstance();
		Node<ByteArray> c2 = Tree.getInstance(b0);
		Node<ByteArray> c3 = Tree.getInstance(b1);
		Node<ByteArray> c4 = Tree.getInstance(b0, b1);
		Node<ByteArray> c5 = Tree.getInstance(b0, b1, b2);
		Node<ByteArray> c6 = Tree.getInstance(b0, b1, b2, b0);
		Node<ByteArray> c7 = Tree.getInstance(c1, c2, c3, c4, c5, c6);
		Node<ByteArray> c8 = Tree.getInstance(c7, c6, c1);

		ByteArray l0 = agr.aggregate(b0);
		ByteArray l1 = agr.aggregate(b1);
		ByteArray l2 = agr.aggregate(b2);
		ByteArray l3 = agr.aggregate(b3);

		ByteArray n1 = agr.aggregate(c1);
		ByteArray n2 = agr.aggregate(c2);
		ByteArray n3 = agr.aggregate(c3);
		ByteArray n4 = agr.aggregate(c4);
		ByteArray n5 = agr.aggregate(c5);
		ByteArray n6 = agr.aggregate(c6);
		ByteArray n7 = agr.aggregate(c7);
		ByteArray n8 = agr.aggregate(c8);

		Assert.assertEquals(ByteArray.getInstance(1, 0, 0, 0, 0), n1);
		Assert.assertEquals(ByteArray.getInstance(1, 0, 0, 0, 5, 0, 0, 0, 0, 0), n2);
		Assert.assertEquals(ByteArray.getInstance(1, 0, 0, 0, 6, 0, 0, 0, 0, 1, 1), n3);
		Assert.assertEquals(ByteArray.getInstance(1, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1), n4);
		Assert.assertEquals(ByteArray.getInstance(1, 0, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 2, 1, 2), n5);

		Assert.assertEquals(n1, agr.aggregate(agr.disaggregate(n1)));
		Assert.assertEquals(n2, agr.aggregate(agr.disaggregate(n2)));
		Assert.assertEquals(n3, agr.aggregate(agr.disaggregate(n3)));
		Assert.assertEquals(n4, agr.aggregate(agr.disaggregate(n4)));
		Assert.assertEquals(n5, agr.aggregate(agr.disaggregate(n5)));
		Assert.assertEquals(n6, agr.aggregate(agr.disaggregate(n6)));
		Assert.assertEquals(n7, agr.aggregate(agr.disaggregate(n7)));
		Assert.assertEquals(n8, agr.aggregate(agr.disaggregate(n8)));
	}

}
