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
package ch.bfh.unicrypt.math.helper.polynomial;

import ch.bfh.unicrypt.math.helper.ByteArray;
import ch.bfh.unicrypt.math.helper.UniCrypt;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <C>
 */
public class BinaryPolynomial
	   extends UniCrypt
	   implements Polynomial<Boolean> {

	private final int degree;
	private final ByteArray coefficients;

	private BinaryPolynomial(ByteArray coefficients) {
		this.coefficients = coefficients;
		int byteIndex = 0;
		for (int i = 0; i < this.coefficients.getLength(); i++) {
			if (this.coefficients.getAt(i) > 0) {
				byteIndex = i;
			}
		}
		byte b = this.coefficients.getAt(byteIndex);
		int bitIndex = Byte.SIZE - Integer.numberOfLeadingZeros(b);
		this.degree = byteIndex * Byte.SIZE + bitIndex;
	}

	@Override
	public int getDegree() {
		return this.degree;
	}

	@Override
	public Boolean getCoefficient(int index) {
		if (index < 0) {
			throw new IllegalArgumentException();
		}
		int byteIndex = index / Byte.SIZE;
		int bitIndex = index % Byte.SIZE;
		if (byteIndex >= this.coefficients.getLength()) {
			return false;
		}
		return ((this.coefficients.getAt(byteIndex) >> bitIndex) & 1) == 1;
	}

	public ByteArray getCoefficients() {
		return this.coefficients;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 29 * hash + (this.coefficients != null ? this.coefficients.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final BinaryPolynomial other = (BinaryPolynomial) obj;
		if (this.coefficients != other.coefficients && (this.coefficients == null || !this.coefficients.equals(other.coefficients))) {
			return false;
		}
		return true;
	}

	@Override
	public String defaultToStringValue() {
		return "f(x)=" + this.coefficients;
	}

}
