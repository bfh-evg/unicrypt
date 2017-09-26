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
package ch.bfh.unicrypt.math.algebra.general.abstracts;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.aggregator.classes.BigIntegerAggregator;
import ch.bfh.unicrypt.helper.aggregator.classes.ByteArrayAggregator;
import ch.bfh.unicrypt.helper.aggregator.classes.StringAggregator;
import ch.bfh.unicrypt.helper.aggregator.interfaces.Aggregator;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.CompositeConverter;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.BigIntegerToByteArray;
import ch.bfh.unicrypt.helper.converter.classes.string.BigIntegerToString;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.random.hybrid.HybridRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.BigIntegerSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.tree.Leaf;
import ch.bfh.unicrypt.helper.tree.Tree;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.algebra.concatenative.interfaces.ConcatenativeSemiGroup;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Field;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeSemiGroup;
import java.math.BigInteger;

/**
 * This abstract class provides a base implementation for the interface {@link Set}. Non-abstract sub-classes need to
 * specify the two generic types {@code E} and {@code V} and all abstract methods. In some sub-classes, it might be
 * necessary to override some default methods. Every abstract method has a name starting with {@code abstract...} and
 * every default method has a name starting with {@code default...}.
 * <p>
 * @param <E> The generic type of elements of this set
 * @param <V> The generic type of values stored in the elements of this set
 * @see AbstractElement
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractSet<E extends Element<V>, V>
	   extends UniCrypt
	   implements Set<V> {

	private static final long serialVersionUID = 1L;

	// the class of the values used to represent elements of this set
	private final Class<?> valueClass;

	// the order of this set
	private BigInteger order;

	// other variables for storing information about the order of this set
	private BigInteger lowerBound, upperBound, minimum;

	// the default converters used to convert elements into BigInteger, String, and ByteArray
	private Converter<V, BigInteger> bigIntegerConverter;
	private Converter<V, String> stringConverter;
	private Converter<V, ByteArray> byteArrayConverter;

	protected AbstractSet(Class<?> valueClass) {
		this.valueClass = valueClass;
	}

	@Override
	public final boolean isSemiGroup() {
		return this instanceof SemiGroup;
	}

	@Override
	public final boolean isMonoid() {
		return this instanceof Monoid;
	}

	@Override
	public final boolean isGroup() {
		return this instanceof Group;
	}

	@Override
	public final boolean isSemiRing() {
		return this instanceof SemiRing;
	}

	@Override
	public final boolean isRing() {
		return this instanceof Ring;
	}

	@Override
	public final boolean isField() {
		return this instanceof Field;
	}

	@Override
	public final boolean isCyclic() {
		return this instanceof CyclicGroup;
	}

	@Override
	public final boolean isAdditive() {
		return this instanceof AdditiveSemiGroup;
	}

	@Override
	public final boolean isMultiplicative() {
		return this instanceof MultiplicativeSemiGroup;
	}

	@Override
	public final boolean isConcatenative() {
		return this instanceof ConcatenativeSemiGroup;
	}

	@Override
	public final boolean isProduct() {
		return this instanceof ProductSet;
	}

	@Override
	public final boolean isFinite() {
		return !this.getOrder().equals(Set.INFINITE);
	}

	@Override
	public final boolean hasKnownOrder() {
		return !this.getOrder().equals(Set.UNKNOWN);
	}

	@Override
	public final BigInteger getOrder() {
		if (this.order == null) {
			this.order = this.abstractGetOrder();
		}
		return this.order;
	}

	@Override
	public final BigInteger getOrderLowerBound() {
		if (this.lowerBound == null) {
			if (this.hasKnownOrder()) {
				this.lowerBound = this.getOrder();
			} else {
				this.lowerBound = this.defaultGetOrderLowerBound();
			}
		}
		return this.lowerBound;
	}

	@Override
	public final BigInteger getOrderUpperBound() {
		if (this.upperBound == null) {
			if (this.hasKnownOrder()) {
				this.upperBound = this.getOrder();
			} else {
				this.upperBound = this.defaultGetOrderUpperBound();
			}
		}
		return this.upperBound;
	}

	@Override
	public final BigInteger getMinimalOrder() {
		if (this.minimum == null) {
			this.minimum = this.defaultGetMinimalOrder();
		}
		return this.minimum;
	}

	@Override
	public final boolean isSingleton() {
		return this.getOrder().equals(MathUtil.ONE);
	}

	@Override
	public ZMod getZModOrder() {
		if (!(this.isFinite() && this.hasKnownOrder())) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		return ZMod.getInstance(this.getOrder());
	}

	@Override
	public ZStarMod getZStarModOrder() {
		if (!(this.isFinite() && this.hasKnownOrder())) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		return ZStarMod.getInstance(this.getOrder());
	}

	@Override
	public final E getElement(V value) {
		if (!this.contains(value)) {
			throw new UniCryptRuntimeException(ErrorCode.ELEMENT_CONSTRUCTION_FAILURE, this, value);
		}
		return this.abstractGetElement(value);
	}

	@Override
	public final boolean contains(V value) {
		if (value == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.abstractContains(value);
	}

	@Override
	public final boolean contains(Element<?> element) {
		if (element == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (!this.valueClass.isInstance(element.getValue())) {
			return false;
		}
		return this.defaultContains((Element<V>) element);
	}

	@Override
	public final E getRandomElement() {
		return this.getRandomElement(HybridRandomByteSequence.getInstance());
	}

	@Override
	public final E getRandomElement(RandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.getRandomElements(randomByteSequence).get();
	}

	@Override
	public final Sequence<E> getRandomElements() {
		return this.getRandomElements(HybridRandomByteSequence.getInstance());
	}

	@Override
	public final Sequence<E> getRandomElements(RandomByteSequence randomByteSequence) {
		if (randomByteSequence == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.abstractGetRandomElements(randomByteSequence);
	}

	@Override
	public final <W> E getElementFrom(W value, Converter<V, W> converter) throws UniCryptException {
		if (value == null || converter == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this, value, converter);
		}
		try {
			V convertedValue = converter.reconvert(value);
			return this.getElement(convertedValue);
		} catch (Exception exception) {
			throw new UniCryptException(ErrorCode.ELEMENT_CONVERSION_FAILURE, exception);
		}
	}

	@Override
	public final <W> E getElementFrom(W value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator) throws
		   UniCryptException {
		if (value == null || convertMethod == null || aggregator == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this, value, convertMethod, aggregator);
		}
		try {
			Tree<W> tree = aggregator.disaggregate(value);
			return this.defaultGetElementFrom(tree, convertMethod);
		} catch (Exception exception) {
			throw new UniCryptException(ErrorCode.ELEMENT_CONVERSION_FAILURE, exception);
		}
	}

	@Override
	public final <W, X> E getElementFrom(X value, ConvertMethod<W> convertMethod, Aggregator<W> aggregator,
		   Converter<W, X> finalConverter) throws UniCryptException {
		if (value == null || convertMethod == null || aggregator == null || finalConverter == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this, value, convertMethod, aggregator,
											   finalConverter);
		}
		try {
			Tree<W> tree = aggregator.disaggregate(finalConverter.reconvert(value));
			return this.defaultGetElementFrom(tree, convertMethod);
		} catch (Exception exception) {
			throw new UniCryptException(ErrorCode.ELEMENT_CONVERSION_FAILURE, exception);
		}

	}

	@Override
	public final <W> E getElementFrom(Tree<W> tree, ConvertMethod<W> convertMethod) throws UniCryptException {
		if (tree == null || convertMethod == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this, tree, convertMethod);
		}
		return this.defaultGetElementFrom(tree, convertMethod);
	}

	@Override
	public final E getElementFrom(long value) throws UniCryptException {
		return this.getElementFrom(BigInteger.valueOf(value));
	}

	@Override
	public final E getElementFrom(BigInteger value) throws UniCryptException {
		if (value == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (this.isProduct()) {
			return this.getElementFrom(value, ConvertMethod.getInstance(BigInteger.class), BigIntegerAggregator.
									   getInstance());

		} else {
			return this.getElementFrom(value, this.getBigIntegerConverter());
		}
	}

	@Override
	public final E getElementFrom(ByteArray value) throws UniCryptException {
		if (value == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (this.isProduct()) {
			return this.getElementFrom(value, ConvertMethod.getInstance(ByteArray.class), ByteArrayAggregator.
									   getInstance());
		} else {
			return this.getElementFrom(value, this.getByteArrayConverter());

		}
	}

	@Override
	public final E getElementFrom(String value) throws UniCryptException {
		if (value == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (this.isProduct()) {
			return this.getElementFrom(value, ConvertMethod.getInstance(String.class), StringAggregator.getInstance());
		} else {
			return this.getElementFrom(value, this.getStringConverter());
		}
	}

	@Override
	public final boolean isEquivalent(final Set<?> other) {
		if (other == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (this == other) {
			return true;
		}
		// matchAll if this.getClass() is a superclass of other.getClass()
		if (this.getClass().isAssignableFrom(other.getClass())) {
			return this.defaultIsEquivalent(other);
		}
		// vice versa
		if (other.getClass().isAssignableFrom(this.getClass())) {
			return other.isEquivalent(this);
		}
		return false;
	}

	@Override
	public final Sequence<E> getElements() {
		return this.defaultGetElements();
	}

	@Override
	public final Class<?> getValueClass() {
		return this.valueClass;
	}

	@Override
	public final int hashCode() {
		int hash = 7;
		hash = 47 * hash + this.valueClass.hashCode();
		hash = 47 * hash + this.getClass().hashCode();
		hash = 47 * hash + this.abstractHashCode();
		return hash;
	}

	@Override
	public final boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		return this.abstractEquals((Set) other);
	}

	@Override
	public final Converter<V, BigInteger> getBigIntegerConverter() {
		if (this.bigIntegerConverter == null) {
			this.bigIntegerConverter = this.abstractGetBigIntegerConverter();
		}
		return this.bigIntegerConverter;
	}

	@Override
	public final Converter<V, ByteArray> getByteArrayConverter() {
		if (this.byteArrayConverter == null) {
			this.byteArrayConverter = this.defaultGetByteArrayConverter();
		}
		return this.byteArrayConverter;
	}

	@Override
	public final Converter<V, String> getStringConverter() {
		if (this.stringConverter == null) {
			this.stringConverter = this.defaultGetStringConverter();
		}
		return this.stringConverter;
	}

	// helper method for selecting a converter from a given convert method or to return one of the default converters
	// for bigIntgers, strings, or byteArrays if no converter exists
	protected final <W> Converter<V, W> getConverter(ConvertMethod<W> convertMethod) {
		Converter<V, W> converter = (Converter<V, W>) convertMethod.getConverter(this.getValueClass());
		if (converter == null) {
			Class<W> outputClass = convertMethod.getOutputClass();
			if (outputClass == String.class) {
				converter = (Converter<V, W>) this.getStringConverter();
			} else if (outputClass == BigInteger.class) {
				converter = (Converter<V, W>) this.getBigIntegerConverter();
			} else if (outputClass == ByteArray.class) {
				converter = (Converter<V, W>) this.getByteArrayConverter();
			} else {
				throw new UniCryptRuntimeException(ErrorCode.OBJECT_NOT_FOUND, this, convertMethod);
			}
		}
		return converter;
	}

	// this method is only called for sets of unknown order
	protected BigInteger defaultGetOrderLowerBound() {
		return MathUtil.ONE;
	}

	// this method is only called for sets of unknown order
	protected BigInteger defaultGetOrderUpperBound() {
		return Set.INFINITE;
	}

	// this method is different only for ProductSet
	protected BigInteger defaultGetMinimalOrder() {
		return this.getOrderLowerBound();
	}

	// this method is different only for Subset
	protected boolean defaultContains(final Element<V> element) {
		return this.isEquivalent(element.getSet());
	}

	// this method is overridden in ProductSet
	protected <W> E defaultGetElementFrom(Tree<W> tree, ConvertMethod<W> convertMethod) throws UniCryptException {
		if (!tree.isLeaf()) {
			throw new UniCryptException(ErrorCode.ELEMENT_CONVERSION_FAILURE);
		}
		W value = ((Leaf<W>) tree).getValue();
		Converter<V, W> converter = this.getConverter(convertMethod);
		return this.getElement(converter.reconvert(value));
	}

	// this method us usually overriden in classes of value type ByteArray
	protected Converter<V, ByteArray> defaultGetByteArrayConverter() {
		return CompositeConverter.getInstance(this.getBigIntegerConverter(), BigIntegerToByteArray.getInstance());
	}

	// this method us usually overriden in classes of value type String
	protected Converter<V, String> defaultGetStringConverter() {
		return CompositeConverter.getInstance(this.getBigIntegerConverter(), BigIntegerToString.getInstance());
	}

	// isEquivalent is usually the same as equals, but there are a few exceptions
	protected boolean defaultIsEquivalent(Set<?> set) {
		return this.abstractEquals(set);
	}

	// some sets allow a more efficient itertation method than this one
	protected Sequence<E> defaultGetElements() {
		final AbstractSet<E, V> set = this;
		Sequence<E> sequence = BigIntegerSequence.getInstance(MathUtil.ZERO)
			   .map(value -> {
				   try {
					   return set.getElementFrom(value);
				   } catch (UniCryptException exception) {
					   return null;
				   }
			   })
			   .filter(Sequence.NOT_NULL);
		if (set.isFinite()) {
			return sequence.limit(set.getOrderLowerBound());
		}
		return sequence;
	}

	protected abstract BigInteger abstractGetOrder();

	protected abstract boolean abstractContains(V value);

	protected abstract E abstractGetElement(V value);

	protected abstract Sequence<E> abstractGetRandomElements(RandomByteSequence randomByteSequence);

	protected abstract Converter<V, BigInteger> abstractGetBigIntegerConverter();

	protected abstract boolean abstractEquals(Set set);

	protected abstract int abstractHashCode();

}
