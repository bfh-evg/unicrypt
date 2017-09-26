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
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import java.math.BigInteger;

/**
 * This abstract class provides a base implementation for the interface {@link SemiGroup}.
 * <p>
 * @param <E> The generic type of elements of this semigroup
 * @param <V> The generic type of values stored in the elements of this semigroup
 * @see AbstractElement
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractSemiGroup<E extends Element<V>, V>
	   extends AbstractSet<E, V>
	   implements SemiGroup<V> {

	private static final long serialVersionUID = 1L;

	protected AbstractSemiGroup(Class<?> valueClass) {
		super(valueClass);
	}

	@Override
	public final E apply(final Element element1, final Element element2) {
		if (!this.contains(element1) || !this.contains(element2)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element1, element2);
		}
		return this.abstractApply((E) element1, (E) element2);
	}

	@Override
	public final E apply(final Element... elements) {
		if (elements == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.defaultApply(Sequence.getInstance(elements).filter(Sequence.NOT_NULL));
	}

	@Override
	public final E apply(final ImmutableArray<Element> elements) {
		if (elements == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		// no filtering of null value required
		return this.defaultApply(Sequence.getInstance(elements));
	}

	@Override
	public final E apply(final Sequence<Element> elements) {
		if (elements == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.defaultApply(elements.filter(Sequence.NOT_NULL));
	}

	@Override
	public final E selfApply(final Element element, final long amount) {
		return this.selfApply(element, BigInteger.valueOf(amount));
	}

	@Override
	public final E selfApply(final Element element, final Element<BigInteger> amount) {
		if (amount == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		return this.selfApply(element, amount.getValue());
	}

	@Override
	public final E selfApply(final Element element, final BigInteger amount) {
		if (amount == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this);
		}
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		return this.defaultSelfApply((E) element, amount);
	}

	@Override
	public final E selfApply(final Element element) {
		return this.apply(element, element);
	}

	@Override
	public final E multiSelfApply(final Element[] elements, final BigInteger[] amounts) {
		if (elements == null || amounts == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, this, elements, amounts);
		}
		if (elements.length != amounts.length) {
			throw new UniCryptRuntimeException(ErrorCode.INCOMPATIBLE_ARGUMENTS, this, elements, amounts);
		}
		return this.defaultMultiSelfApply(elements, amounts);
	}

	// this method is overriden in AbstractMonoid
	protected E defaultApply(final Sequence<Element> elements) {
		final SemiGroup<V> semiGroup = this;
		return (E) elements.reduce((element1, element2) -> semiGroup.apply(element1, element2));
	}

	// this method is overriden in AbstractMonoid
	protected E defaultSelfApply(E element, BigInteger amount) {
		if (amount.signum() <= 0) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_AMOUNT, this, amount);
		}
		return this.defaultSelfApplyAlgorithm(element, amount);
	}

	// this method is overriden in GStarMod, ZStarMod, ZMod, and PolynomialSemiRing
	protected E defaultSelfApplyAlgorithm(E element, BigInteger positiveAmount) {
		E result = element;
		for (int i = positiveAmount.bitLength() - 2; i >= 0; i--) {
			result = this.abstractApply(result, result);
			if (positiveAmount.testBit(i)) {
				result = this.abstractApply(result, element);
			}
		}
		return result;
	}

	// this method is overriden in AbstractMonoid
	protected E defaultMultiSelfApply(final Element[] elements, final BigInteger[] amounts) {
		if (elements.length == 0) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_LENGTH, this, elements, amounts);
		}
		Element[] results = new Element[elements.length];
		for (int i = 0; i < elements.length; i++) {
			results[i] = this.selfApply(elements[i], amounts[i]);
		}
		return this.apply(results);
	}

	protected abstract E abstractApply(E element1, E element2);

}
