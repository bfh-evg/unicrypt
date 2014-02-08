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
package ch.bfh.unicrypt.math.helper.numerical;

import ch.bfh.unicrypt.math.helper.UniCrypt;
import java.math.BigInteger;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <N>
 */
public abstract class Numerical<N extends Numerical<N>>
	   extends UniCrypt {

	protected BigInteger bigInteger;

	protected Numerical(BigInteger bigInteger) {
		this.bigInteger = bigInteger;
	}

	public BigInteger getBigInteger() {
		return this.bigInteger;
	}

	public N add(N other) {
		if (!this.isCompatible(other)) {
			throw new IllegalArgumentException();
		}
		return this.abstractAdd(other);
	}

	public N multiply(N other) {
		if (!this.isCompatible(other)) {
			throw new IllegalArgumentException();
		}
		return this.abstractMultiply(other);
	}

	public N square() {
		return this.multiply((N) this);
	}

	public N negate() {
		return this.abstractNegate();
	}

	public N subtract(N other) {
		if (!this.isCompatible(other)) {
			throw new IllegalArgumentException();
		}
		return this.abstractSubtract(other);
	}

	public N power(NaturalNumber exponent) {
		if (exponent == null) {
			throw new IllegalArgumentException();
		}
		return this.abstractPower(exponent.bigInteger);
	}

	public final boolean isCompatible(N other) {
		if (other == null) {
			throw new IllegalArgumentException();
		}
		return abstractIsCompatible(other);

	}

	public final boolean isGreaterThan(N other) {
		if (!this.isCompatible(other)) {
			throw new IllegalArgumentException();
		}
		return this.bigInteger.compareTo(other.bigInteger) > 0;
	}

	public final boolean isSmallerThan(N other) {
		if (!this.isCompatible(other)) {
			throw new IllegalArgumentException();
		}
		return this.bigInteger.compareTo(other.bigInteger) < 0;
	}

	public final boolean isEqual(N other) {
		if (!this.isCompatible(other)) {
			throw new IllegalArgumentException();
		}
		return this.bigInteger.equals(other.bigInteger);
	}

	@Override
	protected String defaultToStringName() {
		return "";
	}

	@Override
	protected String defaultToStringValue() {
		return this.bigInteger.toString();
	}

	protected abstract boolean abstractIsCompatible(N other);

	protected abstract N abstractAdd(N other);

	protected abstract N abstractMultiply(N other);

	protected abstract N abstractSubtract(N other);

	protected abstract N abstractNegate();

	protected abstract N abstractTimes(BigInteger factor);

	protected abstract N abstractPower(BigInteger exponent);

}
