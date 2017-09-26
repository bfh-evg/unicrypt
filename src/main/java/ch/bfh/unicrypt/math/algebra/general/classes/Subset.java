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
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.math.BigInteger;
import java.util.LinkedHashSet;

/**
 *
 * @author R. Haenni <rolf.haenni@bfh.ch>
 */
public class Subset
	   extends AbstractSet<Element<Object>, Object> {

	private static final long serialVersionUID = 1L;

	private final Set<?> superSet;
	private final java.util.Set<Element<Object>> elementSet;

	protected Subset(Set<?> superSet, java.util.Set<Element<Object>> elements) {
		super(superSet.getValueClass());
		this.superSet = superSet;
		this.elementSet = elements;
	}

	public Set getSuperset() {
		return this.superSet;
	}

	@Override
	protected Sequence<Element<Object>> defaultGetElements() {
		return Sequence.getInstance(elementSet);
	}

	@Override
	protected boolean defaultContains(final Element<Object> element) {
		return this.elementSet.contains(element);
	}

	@Override
	protected boolean defaultIsEquivalent(Set set) {
		Subset other = (Subset) set;
		return this.superSet.isEquivalent(other.superSet) && this.elementSet.equals(other.elementSet);
	}

	@Override
	protected boolean abstractContains(Object value) {
		for (Element element : this.elementSet) {
			if (element.getValue().equals(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Element abstractGetElement(Object value) {
		for (Element element : this.elementSet) {
			if (element.getValue().equals(value)) {
				return element;
			}
		}
		throw new UniCryptRuntimeException(ErrorCode.OBJECT_NOT_FOUND, this, value);
	}

	@Override
	protected Converter<Object, BigInteger> abstractGetBigIntegerConverter() {
		// TODO: return proper converter
		// return this.superSet.getBigIntegerConverter();
		return null;
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return BigInteger.valueOf(this.elementSet.size());
	}

	@Override
	protected Sequence<Element<Object>> abstractGetRandomElements(RandomByteSequence randomByteSequence) {
		return randomByteSequence.getRandomIntegerSequence(this.elementSet.size() - 1)
			   .map(index -> {
				   int i = 0;
				   for (Element<Object> element : getElements()) {
					   if (i == index) {
						   return element;
					   }
					   i++;
				   }
				   throw new UniCryptRuntimeException(ErrorCode.INVALID_INDEX, this, index);
			   });
	}

	@Override
	protected boolean abstractEquals(Set set) {
		Subset other = (Subset) set;
		return this.superSet.equals(other.superSet) && this.elementSet.equals(other.elementSet);
	}

	@Override
	protected int abstractHashCode() {
		int hash = 7;
		hash = 47 * hash + this.superSet.hashCode();
		hash = 47 * hash + this.elementSet.hashCode();
		return hash;
	}

	public static Subset getInstance(Set superSet, Element... elements) {
		if (superSet == null || elements == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, superSet, elements);
		}
		// A LinkedHashSet retains the order
		LinkedHashSet<Element<Object>> hashSet = new LinkedHashSet<>();
		for (Element element : elements) {
			if (element == null) {
				throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER);
			}
			if (!superSet.contains(element)) {
				throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, superSet, element);
			}
			hashSet.add(element);
		}
		return new Subset(superSet, hashSet);
	}

}
