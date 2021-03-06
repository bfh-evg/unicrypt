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
package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;

/**
 * This interface represents the the concept of a function f:X->X, which computes the inverse of the given input
 * element.
 * <p>
 * @see Group#invert(Element)
 * @see Element#invert()
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class OneOverFunction
	   extends AbstractFunction<OneOverFunction, MultiplicativeGroup, MultiplicativeElement, MultiplicativeGroup, MultiplicativeElement> {

	private static final long serialVersionUID = 1L;

	private OneOverFunction(final MultiplicativeGroup domain, MultiplicativeGroup coDomain) {
		super(domain, coDomain);
	}

	@Override
	protected MultiplicativeElement abstractApply(final MultiplicativeElement element,
		   final RandomByteSequence randomByteSequence) {
		return element.oneOver();
	}

	/**
	 * This is the default constructor for this class. It creates an invert function for a given group.
	 * <p>
	 * @param multiplicativeGroup The given Group
	 * @return Returns an instance of this class
	 */
	public static OneOverFunction getInstance(final MultiplicativeGroup multiplicativeGroup) {
		return new OneOverFunction(multiplicativeGroup, multiplicativeGroup);
	}

}
