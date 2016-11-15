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

import ch.bfh.unicrypt.helper.aggregator.classes.StringAggregator;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.tree.Tree;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni
 */
public class StringAggregatorTest {

	@Test
	public void stringAggregatorTest() {
		Tree<String> l1 = Tree.getInstance("Hello");
		Tree<String> l2 = Tree.getInstance("World");
		Tree<String> l3 = Tree.getInstance("Universe");
		Tree<String> n1 = Tree.getInstance(l1, l2);
		Tree<String> n2 = Tree.getInstance(n1, l2);
		Tree<String> n3 = Tree.getInstance();
		Tree<String> n4 = Tree.getInstance(n1, n2, n3, l3);
		Tree<String> n5 = Tree.getInstance(n4);

		StringAggregator aggregator1 = StringAggregator.getInstance();
		StringAggregator aggregator2 = StringAggregator.getInstance('\'', '<', '>', '*', '-');

		for (StringAggregator aggregator : new StringAggregator[]{aggregator1, aggregator2}) {
			for (Tree<String> tree : new Tree[]{l1, l2, l3, n1, n2, n3, n4, n5}) {
				Assert.assertEquals(tree, aggregator.disaggregate(aggregator.aggregate(tree)));
			}
		}
	}

	@Test
	public void stringAggregatorTestLeaf() {

		StringAggregator aggregator1 = StringAggregator.getInstance();
		StringAggregator aggregator2 = StringAggregator.getInstance('\'', '<', '>', '*', '-');

		String[] strings = new String[]{"", "x", "Hello", "[", "[]", "\\", "\\\\", "[j]|\\h|\\[\\\\|]\\]"};

		for (String value : strings) {
			for (StringAggregator aggregator : new StringAggregator[]{aggregator1, aggregator2}) {
				String aggregatedValue = aggregator.aggregate(Tree.getInstance(value));
				Assert.assertEquals(Tree.getInstance(value), aggregator.disaggregate(aggregatedValue));
			}
		}
	}

	@Test
	public void stringAggregatorTestNode() {
		StringAggregator aggregator1 = StringAggregator.getInstance();
		StringAggregator aggregator2 = StringAggregator.getInstance('\'', '<', '>', '*', '-');

		String[] strings = new String[]{"\"\"", "[]", "[\"Hello\"|\"World\"]"};

		for (Set<String> values : getStringSets(strings)) {
			List<Tree<String>> trees = new ArrayList<>();
			for (String value : values) {
				trees.add(Tree.getInstance(value));
			}
			for (StringAggregator aggregator : new StringAggregator[]{aggregator1, aggregator2}) {
				String aggregatedValue = aggregator.aggregate(Tree.getInstance(Sequence.getInstance(trees)));
				Tree<String> result = aggregator.disaggregate(aggregatedValue);
				int i = 0;
				for (String x : result) {
					Assert.assertTrue(values.contains(x));
					i++;
				}
				Assert.assertEquals(values.size(), i);
			}
		}
	}

	public static List<Set<String>> getStringSets(String[] inputSet) {
		List<Set<String>> subSets = new ArrayList<>();
		for (String addToSets : inputSet) {
			List<Set<String>> newSets = new ArrayList<>();
			for (Set<String> curSet : subSets) {
				Set<String> copyPlusNew = new HashSet<>();
				copyPlusNew.addAll(curSet);
				copyPlusNew.add(addToSets);
				newSets.add(copyPlusNew);
			}
			Set<String> newValSet = new HashSet<>();
			newValSet.add(addToSets);
			newSets.add(newValSet);
			subSets.addAll(newSets);
		}
		subSets.add(new HashSet<String>());
		return subSets;
	}

}
