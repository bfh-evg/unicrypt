/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
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

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.aggregator.classes.BigIntegerAggregator;
import ch.bfh.unicrypt.helper.aggregator.classes.ByteArrayAggregator;
import ch.bfh.unicrypt.helper.aggregator.classes.StringAggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.array.classes.DenseArray;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.array.interfaces.NestedArray;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.tree.Tree;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.math.BigInteger;
import java.util.Iterator;

/**
 *
 * @author R. Haenni
 */
public class Tuple
	   extends AbstractElement<ProductSet, Tuple, DenseArray<Element>>
	   implements NestedArray<Element> {

	private static final long serialVersionUID = 1L;

	protected Tuple(final ProductSet set, final DenseArray<? extends Element> elements) {
		super(set, (DenseArray<Element>) elements);
	}

	public int getArity() {
		return this.getLength();
	}

	@Override
	public int getLength() {
		return this.value.getLength();
	}

	@Override
	public final boolean isEmpty() {
		return this.value.isEmpty();
	}

	@Override
	public final boolean isUniform() {
		return this.value.isUniform();
	}

	@Override
	public Sequence<Integer> getAllIndices() {
		return this.value.getAllIndices();
	}

	@Override
	public Sequence<Integer> getIndices(Element element) {
		return this.value.getIndices(element);
	}

	@Override
	public Sequence<Integer> getIndicesExcept(Element element) {
		return this.value.getIndicesExcept(element);
	}

	@Override
	public int count(Element element) {
		return this.value.count(element);
	}

	@Override
	public int countExcept(Element element) {
		return this.value.countExcept(element);
	}

	@Override
	public int countPrefix(Element element) {
		return this.value.countPrefix(element);
	}

	@Override
	public int countSuffix(Element element) {
		return this.value.countSuffix(element);
	}

	@Override
	public Element getFirst() {
		return this.value.getFirst();
	}

	@Override
	public Element getLast() {
		return this.value.getLast();
	}

	@Override
	public Element getAt(int index) {
		return this.value.getAt(index);
	}

	@Override
	public Element getAt(int... indices) {
		if (indices == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		Element element = this;
		for (final int index : indices) {
			if (element.isTuple()) {
				element = ((Tuple) element).getAt(index);
			} else {
				throw new UniCryptRuntimeException(ErrorCode.INVALID_INDEX, this, indices, index);
			}
		}
		return element;
	}

	@Override
	public Tuple extract(int offset, int length) {
		return Tuple.getInstance(this.getSet().extract(offset, length), this.value.extract(offset, length));
	}

	@Override
	public Tuple extractPrefix(int length) {
		return Tuple.getInstance(this.getSet().extractPrefix(length), this.value.extractPrefix(length));
	}

	@Override
	public Tuple extractSuffix(int length) {
		return Tuple.getInstance(this.getSet().extractSuffix(length), this.value.extractSuffix(length));
	}

	@Override
	public Tuple extractRange(int fromIndex, int toIndex) {
		return Tuple.getInstance(this.getSet().extractRange(fromIndex, toIndex),
								 this.value.extractRange(fromIndex, toIndex));
	}

	@Override
	public Tuple remove(int offset, int length) {
		return Tuple.getInstance(this.getSet().remove(offset, length), this.value.remove(offset, length));
	}

	@Override
	public Tuple removePrefix(int length) {
		return Tuple.getInstance(this.getSet().removePrefix(length), this.value.removePrefix(length));
	}

	@Override
	public Tuple removeSuffix(int length) {
		return Tuple.getInstance(this.getSet().removeSuffix(length), this.value.removeSuffix(length));
	}

	@Override
	public Tuple removeRange(int fromIndex, int toIndex) {
		return Tuple.getInstance(this.getSet().removeRange(fromIndex, toIndex),
								 this.value.removeRange(fromIndex, toIndex));
	}

	@Override
	public Tuple removeAt(final int index) {
		return Tuple.getInstance(this.getSet().removeAt(index), this.value.removeAt(index));
	}

	@Override
	public Tuple removeFirst() {
		return Tuple.getInstance(this.getSet().removeFirst(), this.value.removeFirst());
	}

	@Override
	public Tuple removeLast() {
		return Tuple.getInstance(this.getSet().removeLast(), this.value.removeLast());
	}

	@Override
	public Tuple insertAt(int index, Element element) {
		return Tuple.getInstance(this.getSet().insertAt(index, element.getSet()),
								 this.value.insertAt(index, element));
	}

	@Override
	public Tuple insert(Element element) {
		return Tuple.getInstance(this.getSet().insert(element.getSet()), this.value.insert(element));
	}

	@Override
	public Tuple add(Element element) {
		return Tuple.getInstance(this.getSet().add(element.getSet()), this.value.add(element));
	}

	@Override
	public Tuple append(ImmutableArray<Element> other) {
		return Tuple.getInstance(this.value.append(other));
	}

	@Override
	public Tuple replaceAt(int index, Element element) {
		return Tuple.getInstance(this.getSet().replaceAt(index, element.getSet()),
								 this.value.replaceAt(index, element));
	}

	@Override
	public Tuple reverse() {
		return Tuple.getInstance(this.getSet().reverse(), this.value.reverse());
	}

	@Override
	public Tuple[] split(int... indices) {
		DenseArray<Element>[] elementArray = this.value.split(indices);
		Tuple[] result = new Tuple[elementArray.length];
		for (int i = 0; i < elementArray.length; i++) {
			result[i] = Tuple.getInstance(elementArray[i]);
		}
		return result;
	}

	@Override
	public final Sequence<Element> getSequence() {
		return this.value.getSequence();
	}

	@Override
	public Iterator<Element> iterator() {
		return this.value.iterator();
	}

	@Override
	protected final <W> Tree<W> defaultConvertTo(final ConvertMethod<W> convertMethod) {
		Sequence<Tree<W>> trees = this.getSequence().map(element -> element.convertTo(convertMethod));
		return Tree.getInstance(trees);
	}

	@Override
	protected BigInteger defaultConvertToBigInteger() {
		return this.convertTo(ConvertMethod.getInstance(BigInteger.class), BigIntegerAggregator.getInstance());
	}

	@Override
	protected ByteArray defaultConvertToByteArray() {
		return this.convertTo(ConvertMethod.getInstance(), ByteArrayAggregator.getInstance());
	}

	@Override
	protected String defaultConvertToString() {
		return this.convertTo(ConvertMethod.getInstance(String.class), StringAggregator.getInstance());
	}

	@Override
	protected String defaultToStringContent() {
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
	 * @param elements The array of input elements
	 * @return The corresponding tuple element
	 */
	public static Tuple getInstance(DenseArray<? extends Element> elements) {
		if (elements == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
		}
		ProductSet productSet;
		if (elements.isUniform() && !elements.isEmpty()) {
			productSet = ProductSet.getInstance(elements.getFirst().getSet(), elements.getLength());
		} else {
			Set[] sets = new Set[elements.getLength()];
			for (int i : elements.getAllIndices()) {
				sets[i] = elements.getAt(i).getSet();
			}
			productSet = ProductSet.getInstance(sets);
		}
		return Tuple.getInstance(productSet, elements);
	}

	public static Tuple getInstance(Sequence<? extends Element> elements) {
		return Tuple.getInstance(DenseArray.getInstance(elements));
	}

	public static Tuple getInstance(Element... elements) {
		return Tuple.getInstance(DenseArray.getInstance(elements));
	}

	public static Tuple getInstance(Element element, int arity) {
		return Tuple.getInstance(DenseArray.getInstance(element, arity));
	}

	// helper method to distinguish between pairs, triples and tuples
	private static Tuple getInstance(ProductSet productSet, DenseArray<? extends Element> elements) {
		if (elements.getLength() == 1) {
			return new Singleton(productSet, elements);
		}
		if (elements.getLength() == 2) {
			return new Pair(productSet, elements);
		}
		if (elements.getLength() == 3) {
			return new Triple(productSet, elements);
		}
		return new Tuple(productSet, elements);
	}

}
