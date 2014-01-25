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
package ch.bfh.unicrypt.crypto.schemes.sharing.abstracts;

import ch.bfh.unicrypt.crypto.random.classes.RandomNumberGenerator;
import ch.bfh.unicrypt.crypto.schemes.scheme.abstracts.AbstractScheme;
import ch.bfh.unicrypt.crypto.schemes.sharing.interfaces.SecretSharingScheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

public abstract class AbstractSecretSharingScheme<MS extends Set, ME extends Element, SS extends Set, SE extends Element>
			 extends AbstractScheme<MS>
			 implements SecretSharingScheme {

	private final int size;

	protected AbstractSecretSharingScheme(int size) {
		this.size = size;
	}

	@Override
	public final SS getShareSpace() {
		return this.abstractGetShareSpace();
	}

	@Override
	public final int getSize() {
		return this.size;
	}

	@Override
	public final SE[] share(Element message) {
		return this.share(message, (RandomNumberGenerator) null);
	}

	@Override
	public final SE[] share(Element message, RandomNumberGenerator randomGenerator) {
		if (message == null || !this.getMessageSpace().contains(message)) {
			throw new IllegalArgumentException();
		}
		return this.abstractShare(message, randomGenerator);
	}

	@Override
	public final ME recover(Element... shares) {
		if (shares == null || shares.length < this.getThreshold() || shares.length > this.getSize()) {
			throw new IllegalArgumentException();
		}
		for (Element share : shares) {
			if (share == null || !this.getShareSpace().contains(share)) {
				throw new IllegalArgumentException();
			}
		}
		return this.abstractRecover(shares);
	}

	protected int getThreshold() { // this method is not really needed here, but it simplifies the method recover
		return this.getSize();
	}

	protected abstract SS abstractGetShareSpace();

	protected abstract SE[] abstractShare(Element message, RandomNumberGenerator randomGenerator);

	protected abstract ME abstractRecover(Element[] shares);

}
