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
package ch.bfh.unicrypt.math.algebra.additive.parameters;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import java.math.BigInteger;

/**
 * This interface defines the capabilities of the helper classes dealing with the parameters of various elliptic curve
 * standards. The parameters include the generator of the subgroup.
 * <p>
 * @param <F>  The generic type of the underlying finite field
 * @param <DE> The generic type of the dualistic elements of the underlying finite field
 * <p>
 * @author C. Lutz
 * @author R. Haenni
 */
public interface ECParameters<F extends FiniteField, DE extends DualisticElement> {

	/**
	 * Returns the underlying finite field of the elliptic curve.
	 * <p>
	 * @return The finite field of the elliptic curve
	 */
	public F getFiniteField();

	/**
	 * Returns the first coefficient of the elliptic curve (usually denoted by {@code a}).
	 * <p>
	 * @return The first coefficient {@code a}
	 */
	public DE getA();

	/**
	 * Returns the second coefficient of the elliptic curve (usually denoted by {@code b}).
	 * <p>
	 * @return The second coefficient {@code b}
	 */
	public DE getB();

	/**
	 * Returns the x-coordinate of the default generator.
	 * <p>
	 * @return The x-coordinate of the default generator
	 */
	public DE getGx();

	/**
	 * Returns the y-coordinate of the default generator.
	 * <p>
	 * @return The y-coordinate of the default generator
	 */
	public DE getGy();

	/**
	 * Returns the order of the EC group.
	 * <p>
	 * @return The group order
	 */
	public BigInteger getOrder();

	/**
	 * Returns the cofactor of the EC group. This is the fraction of the total number of points on the curve and the
	 * number of points in the actual subgroup.
	 * <p>
	 * @return The cofactor of the EC group
	 */
	public BigInteger getCoFactor();

}
