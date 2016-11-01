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

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import java.math.BigInteger;

/**
 * This abstract class provides a basis implementation for objects of type {@link FiniteField}.
 * <p>
 * @param <E> The generic type of the elements of this finite field
 * @param <V> The generic type of the values stored in the elements of this finite field
 * @param <M> The generic type of the multiplicative group of this finite field
 * <p>
 * @author R. Haenni
 */
public abstract class AbstractFiniteField<E extends DualisticElement<V>, M extends MultiplicativeGroup<V>, V>
	   extends AbstractField<E, M, V>
	   implements FiniteField<V> {

	private static final long serialVersionUID = 1L;

	// the charcteristic of this finite field
	private BigInteger characteristic;

	protected AbstractFiniteField(Class<?> valueClass) {
		super(valueClass);
	}

	@Override
	public final BigInteger getCharacteristic() {
		if (this.characteristic == null) {
			this.characteristic = this.abstractGetCharacteristic();
		}
		return this.characteristic;
	}

	protected abstract BigInteger abstractGetCharacteristic();

}
