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
package ch.bfh.unicrypt.math.algebra.additive.abstracts;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.math.Point;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.EC;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.ECElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;

/**
 *
 * @param <V>  Type finite field of EC
 * @param <DE> Type of FiniteFieldElement
 * @param <EE> x
 * <p>
 * @author C. Lutz
 * @author R. Haenni
 */
public class AbstractECElement<V, DE extends DualisticElement<V>, EE extends ECElement<V, DE>>
	   extends AbstractAdditiveElement<EC<V, DE>, EE, Point<DE>>
	   implements ECElement<V, DE> {

	private static final long serialVersionUID = 1L;

	// flag to distinguish the point of infinity from normal curve points
	private final boolean infinity;

	protected AbstractECElement(AbstractSet<EE, Point<DE>> ecGroup, Point<DE> value) {
		super(ecGroup, value);
		this.infinity = false;
	}

	protected AbstractECElement(AbstractSet<EE, Point<DE>> ecGroup) {
		super(ecGroup, Point.<DE>getInstance());
		this.infinity = true;
	}

	@Override
	public DE getX() {
		if (this.infinity) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		return this.value.getX();
	}

	@Override
	public DE getY() {
		if (this.infinity) {
			throw new UniCryptRuntimeException(ErrorCode.UNSUPPORTED_OPERATION, this);
		}
		return this.value.getY();
	}

	@Override
	protected String defaultToStringContent() {
		if (this.infinity) {
			return "Infinity";
		} else {
			return "(" + this.getX().getValue() + "," + this.getY().getValue() + ")";
		}
	}

}
