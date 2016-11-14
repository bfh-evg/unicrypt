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
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.PrimeField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeCyclicGroup;
import java.math.BigInteger;

/**
 * This abstract class provides a basis implementation for objects of type {@link PrimeField}.
 * <p>
 * @param <E> Generic type of the elements of this prime field
 * @param <M> Generic type of the {@link MultiplicativeCyclicGroup} of this prime field
 * @param <V> Generic type of values stored in the elements of this prime field
 * @author R. Haenni
 */
public abstract class AbstractPrimeField<E extends DualisticElement<V>, M extends MultiplicativeCyclicGroup, V>
	   extends AbstractCyclicRing<E, V>
	   implements PrimeField<V> {

	private static final long serialVersionUID = 1L;

	private M multiplicativeGroup;

	protected AbstractPrimeField(Class<?> valueClass) {
		super(valueClass);
	}

	@Override
	public final BigInteger getCharacteristic() {
		return this.getOrder();
	}

	@Override
	public final M getMultiplicativeGroup() {
		if (this.multiplicativeGroup == null) {
			this.multiplicativeGroup = this.abstractGetMultiplicativeGroup();
		}
		return this.multiplicativeGroup;
	}

	@Override
	public final E divide(Element element1, Element element2) {
		return this.multiply(element1, this.oneOver(element2));
	}

	@Override
	public final E oneOver(Element element) {
		if (!this.contains(element)) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_ELEMENT, this, element);
		}
		if (((E) element).isZero()) {
			throw new UniCryptRuntimeException(ErrorCode.DIVISION_BY_ZERO, this, element);
		}
		return this.abstractOneOver((E) element);
	}

	protected abstract E abstractOneOver(E element);

	protected abstract M abstractGetMultiplicativeGroup();

}
