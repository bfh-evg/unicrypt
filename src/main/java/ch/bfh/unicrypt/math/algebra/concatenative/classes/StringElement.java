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
package ch.bfh.unicrypt.math.algebra.concatenative.classes;

import ch.bfh.unicrypt.math.algebra.concatenative.abstracts.AbstractConcatenativeElement;
import ch.bfh.unicrypt.math.helper.ByteArray;
import ch.bfh.unicrypt.math.helper.bytetree.ByteTreeLeaf;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class StringElement
	   extends AbstractConcatenativeElement<StringMonoid, StringElement, String> {

	protected StringElement(final StringMonoid monoid, final String string) {
		super(monoid, string);
	}

	@Override
	public int getLength() {
		return this.getValue().length();
	}

	@Override
	protected BigInteger abstractGetBigInteger() {
		BigInteger value1 = BigInteger.ZERO;
		BigInteger alphabetSize = BigInteger.valueOf(this.getSet().getAlphabet().getSize());
		for (int i = 0; i < this.getLength(); i++) {
			int charIndex = this.getSet().getAlphabet().getIndex(this.getValue().charAt(i));
			value1 = value1.multiply(alphabetSize).add(BigInteger.valueOf(charIndex));
		}
		BigInteger value2 = BigInteger.ZERO;
		int blockLength = this.getSet().getBlockLength();
		BigInteger blockSize = alphabetSize.pow(blockLength);
		for (int i = 0; i < this.getLength() / blockLength; i++) {
			value2 = value2.multiply(blockSize).add(BigInteger.ONE);
		}
		return value1.add(value2);
	}

	@Override
	protected ByteTreeLeaf abstractGetByteTree() {
		return ByteTreeLeaf.getInstance(ByteArray.getInstance(getBigInteger().toByteArray()));
	}

	@Override
	public String defaultToStringValue() {
		return "\"" + this.getValue() + "\"";
	}

}
