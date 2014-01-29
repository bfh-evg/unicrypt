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
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.ImmutableArray;
import ch.bfh.unicrypt.math.helper.bytetree.ByteTree;
import ch.bfh.unicrypt.math.helper.compound.Compound;
import ch.bfh.unicrypt.math.helper.compound.RecursiveCompound;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Iterator;

/**
 *
 * @author rolfhaenni
 */
public class Tuple
	   extends AbstractElement<ProductSet, Tuple, ImmutableArray<Element>>
	   implements RecursiveCompound<Tuple, Element>, Iterable<Element> {

	protected Tuple(final ProductSet set, final ImmutableArray<Element> elements) {
		super(set, elements);
	}

	@Override
	public int getArity() {
		return this.getValue().getLength();
	}

	@Override
	public final boolean isEmpty() {
		return this.getValue().isEmpty();
	}

	@Override
	public final boolean isUniform() {
		return this.getValue().isUniform();
	}

	@Override
	public Element getFirst() {
		return this.getValue().getFirst();
	}

	@Override
	public Element getLast() {
		return this.getValue().getLast();
	}

	@Override
	public Element getAt(int index) {
		return this.getValue().getAt(index);
	}

	@Override
	public Element getAt(int... indices) {
		if (indices == null) {
			throw new IllegalArgumentException();
		}
		Element element = this;
		for (final int index : indices) {
			if (element.isTuple()) {
				element = ((Tuple) element).getAt(index);
			} else {
				throw new IllegalArgumentException();
			}
		}
		return element;
	}

	@Override
	public Element[] getAll() {
		return this.getValue().getAll();
	}

	@Override
	public Tuple removeAt(final int index) {
		return Tuple.getInstance(this.getSet().removeAt(index), this.getValue().removeAt(index));
	}

	@Override
	public Tuple insertAt(int index, Element element) {
		return Tuple.getInstance(this.getSet().insertAt(index, element.getSet()), this.getValue().insertAt(index, element));

	}

	@Override
	public Tuple add(Element element) {
		return this.insertAt(this.getArity(), element);
	}

	@Override
	public Tuple append(Compound<Tuple, Element> compound) {
		if (compound instanceof Tuple) {
			Tuple other = (Tuple) compound;
			return Tuple.getInstance(this.getSet().append(other.getSet()), this.getValue().append(other.getValue()));
		}
		throw new IllegalArgumentException();
	}

	@Override
	public Iterator<Element> iterator() {
		return this.getValue().iterator();
	}

	@Override
	protected BigInteger abstractGetBigInteger() {
		BigInteger[] values = new BigInteger[this.getArity()];
		int i = 0;
		for (Element element : this) {
			values[i] = element.getBigInteger();
			i++;
		}
		return MathUtil.foldAndPair(values);
	}

	@Override
	protected ByteTree abstractGetByteTree() {
		ByteTree[] byteTrees = new ByteTree[this.getArity()];
		for (int i = 0; i < this.getArity(); i++) {
			byteTrees[i] = this.getValue().getAt(i).getByteTree();
		}
		return ByteTree.getInstance(byteTrees);
	}

	@Override
	protected String standardToStringContent() {
		String result = "";
		String separator = "";
		for (Element element : this) {
			result = result + separator + element.toString();
			separator = ", ";
		}
		return result;
	}

	/**
	 * This is a static factory method to construct a composed element without the need of constructing the
	 * corresponding product or power group beforehand. The input elements are given as an array.
	 * <p>
	 * <p/>
	 * @param elements The array of input elements
	 * @return The corresponding tuple element
	 * @throws IllegalArgumentException if {@literal elements} is null or contains null
	 */
	public static Tuple getInstance(ImmutableArray<Element> elements) {
		if (elements == null || elements.getLength() < 0) {
			throw new IllegalArgumentException();
		}
		ProductSet productSet;
		if (elements.isUniform() && !elements.isEmpty()) {
			productSet = ProductSet.getInstance(elements.getFirst().getSet(), elements.getLength());
		} else {
			Set[] sets = new Set[elements.getLength()];
			for (int i = 0; i < elements.getLength(); i++) {
				sets[i] = elements.getAt(i).getSet();
			}
			productSet = ProductSet.getInstance(sets);
		}
		return Tuple.getInstance(productSet, elements);
	}

	public static Tuple getInstance(Element... elements) {
		return Tuple.getInstance(ImmutableArray.getInstance(elements));
	}

	public static Tuple getInstance(Element element, int arity) {
		return Tuple.getInstance(ImmutableArray.getInstance(element, arity));
	}

	// helper method to distinguish between pairs, triples and tuples
	private static Tuple getInstance(ProductSet productSet, ImmutableArray<Element> elements) {
		if (elements.getLength() == 2) {
			return new Pair(productSet, elements);
		}
		if (elements.getLength() == 3) {
			return new Triple(productSet, elements);
		}
		return new Tuple(productSet, elements);
	}

}
