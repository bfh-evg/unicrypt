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
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class ByteArrayAggregatorTest {

	@Test
	public void ByteArrayAggregatorTestLeaf() {

		ByteArray b0 = ByteArray.getInstance();
		ByteArray b1 = ByteArray.getInstance(1);
		ByteArray b2 = ByteArray.getInstance(1, 2);
		ByteArray b3 = ByteArray.getInstance(1, 2, 3);

		ByteArrayAggregator agr = ByteArrayAggregator.getInstance();

		ByteArray l0 = agr.aggregateLeaf(b0);
		ByteArray l1 = agr.aggregateLeaf(b1);
		ByteArray l2 = agr.aggregateLeaf(b2);
		ByteArray l3 = agr.aggregateLeaf(b3);

		Assert.assertEquals(ByteArray.getInstance(0, 0, 0, 0, 0), l0);
		Assert.assertEquals(ByteArray.getInstance(0, 0, 0, 0, 1, 1), l1);
		Assert.assertEquals(ByteArray.getInstance(0, 0, 0, 0, 2, 1, 2), l2);
		Assert.assertEquals(ByteArray.getInstance(0, 0, 0, 0, 3, 1, 2, 3), l3);

		Assert.assertEquals(b0, agr.disaggregateLeaf(l0));
		Assert.assertEquals(b1, agr.disaggregateLeaf(l1));
		Assert.assertEquals(b2, agr.disaggregateLeaf(l2));
		Assert.assertEquals(b3, agr.disaggregateLeaf(l3));

		Assert.assertTrue(agr.isLeaf(l0));
		Assert.assertTrue(agr.isLeaf(l1));
		Assert.assertTrue(agr.isLeaf(l2));
		Assert.assertTrue(agr.isLeaf(l3));

		Assert.assertFalse(agr.isNode(l0));
		Assert.assertFalse(agr.isNode(l1));
		Assert.assertFalse(agr.isNode(l2));
		Assert.assertFalse(agr.isNode(l3));
	}

	@Test
	public void ByteArrayAggregatorTestNode() {

		ByteArray b0 = ByteArray.getInstance();
		ByteArray b1 = ByteArray.getInstance(1);
		ByteArray b2 = ByteArray.getInstance(1, 2);
		ByteArray b3 = ByteArray.getInstance(1, 2, 3);

		ByteArrayAggregator agr = ByteArrayAggregator.getInstance();

		ByteArray l0 = agr.aggregateLeaf(b0);
		ByteArray l1 = agr.aggregateLeaf(b1);
		ByteArray l2 = agr.aggregateLeaf(b2);
		ByteArray l3 = agr.aggregateLeaf(b3);

		ByteArray n1 = agr.aggregateNode();
		ByteArray n2 = agr.aggregateNode(l0);
		ByteArray n3 = agr.aggregateNode(l1);
		ByteArray n4 = agr.aggregateNode(l0, l1);
		ByteArray n5 = agr.aggregateNode(l0, l1, l2);
		ByteArray n6 = agr.aggregateNode(l0, l1, l2, l0);
		ByteArray n7 = agr.aggregateNode(n1, n2, n3, n4, n5, n6);
		ByteArray n8 = agr.aggregateNode(n7, n6, n1);

		Assert.assertTrue(agr.isNode(n1));
		Assert.assertTrue(agr.isNode(n2));
		Assert.assertTrue(agr.isNode(n3));
		Assert.assertTrue(agr.isNode(n4));
		Assert.assertTrue(agr.isNode(n5));
		Assert.assertTrue(agr.isNode(n6));
		Assert.assertTrue(agr.isNode(n7));
		Assert.assertTrue(agr.isNode(n8));

		Assert.assertFalse(agr.isLeaf(n1));
		Assert.assertFalse(agr.isLeaf(n2));
		Assert.assertFalse(agr.isLeaf(n3));
		Assert.assertFalse(agr.isLeaf(n4));
		Assert.assertFalse(agr.isLeaf(n5));
		Assert.assertFalse(agr.isLeaf(n6));
		Assert.assertFalse(agr.isLeaf(n7));
		Assert.assertFalse(agr.isLeaf(n8));

		Assert.assertEquals(ByteArray.getInstance(1, 0, 0, 0, 0), n1);
		Assert.assertEquals(ByteArray.getInstance(1, 0, 0, 0, 5, 0, 0, 0, 0, 0), n2);
		Assert.assertEquals(ByteArray.getInstance(1, 0, 0, 0, 6, 0, 0, 0, 0, 1, 1), n3);
		Assert.assertEquals(ByteArray.getInstance(1, 0, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1), n4);
		Assert.assertEquals(ByteArray.getInstance(1, 0, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 2, 1, 2), n5);

		Assert.assertEquals(n1, agr.aggregateNode(agr.disaggregateNode(n1)));
		Assert.assertEquals(n2, agr.aggregateNode(agr.disaggregateNode(n2)));
		Assert.assertEquals(n3, agr.aggregateNode(agr.disaggregateNode(n3)));
		Assert.assertEquals(n4, agr.aggregateNode(agr.disaggregateNode(n4)));
		Assert.assertEquals(n5, agr.aggregateNode(agr.disaggregateNode(n5)));
		Assert.assertEquals(n6, agr.aggregateNode(agr.disaggregateNode(n6)));
		Assert.assertEquals(n7, agr.aggregateNode(agr.disaggregateNode(n7)));
		Assert.assertEquals(n8, agr.aggregateNode(agr.disaggregateNode(n8)));
	}

}
