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
package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.util.HashMap;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class PseudoRandomReferenceString
	   extends PseudoRandomGenerator
	   implements RandomReferenceString {

	private HashMap<Integer, byte[]> randomByteBufferMap;
	private int javaHashValue;

	public PseudoRandomReferenceString(HashMethod hashMethod, Element seed) {
		super(hashMethod, seed);
	}

	protected byte[] getRandomByteBuffer(int counter) {
		if (randomByteBufferMap == null) {
			randomByteBufferMap = new HashMap<Integer, byte[]>();
		}
		if (!randomByteBufferMap.containsKey(counter)) {
			randomByteBufferMap.put(counter, super.getRandomByteBuffer(counter));
		}
		return randomByteBufferMap.get(counter);
	}

	@Override
	public void reset() {
		super.reset();
	}

	@Override
	public boolean isReset() {
		return super.isReset();
	}

	public static PseudoRandomReferenceString getInstance(byte[] query) {
		if (query == null) {
			throw new IllegalArgumentException();
		}
		return getInstance(ByteArrayMonoid.getInstance().getElement(query));
	}

	public static PseudoRandomReferenceString getInstance() {
		return PseudoRandomReferenceString.getInstance(HashMethod.DEFAULT, PseudoRandomGenerator.DEFAULT_SEED);
	}

	public static PseudoRandomReferenceString getInstance(Element seed) {
		return PseudoRandomReferenceString.getInstance(HashMethod.DEFAULT, seed);
	}

	public static PseudoRandomReferenceString getInstance(HashMethod hashMethod) {
		return PseudoRandomReferenceString.getInstance(hashMethod, PseudoRandomGenerator.DEFAULT_SEED);
	}

	public static PseudoRandomReferenceString getInstance(HashMethod hashMethod, Element seed) {
		if (hashMethod == null || seed == null) {
			throw new IllegalArgumentException();
		}
		return new PseudoRandomReferenceString(hashMethod, seed);
	}

	@Override
	public int hashCode() {
		if (javaHashValue == 0) {
			javaHashValue = getHashMethod().hashCode() + getSeed().getValue().hashCode();
		}
		return javaHashValue;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final PseudoRandomReferenceString other = (PseudoRandomReferenceString) obj;
		if (getHashMethod() != getHashMethod() && (!this.getHashMethod().equals(other.getHashMethod()))) {
			return false;
		}

		if (this.getSeed().getValue() != other.getSeed().getValue()) {
			return false;
		}
		return true;
	}

}
