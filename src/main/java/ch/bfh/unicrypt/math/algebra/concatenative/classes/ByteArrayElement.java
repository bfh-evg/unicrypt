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
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author rolfhaenni
 */
public class ByteArrayElement
	   extends AbstractConcatenativeElement<ByteArrayMonoid, ByteArrayElement> {

	private final byte[] bytes;

	protected ByteArrayElement(final ByteArrayMonoid monoid, final byte[] bytes) {
		super(monoid);
		this.bytes = bytes;
	}

	public byte[] getByteArray() {
		return this.bytes.clone();
	}

	@Override
	public int getLength() {
		return this.bytes.length;
	}

	@Override
	protected BigInteger standardGetValue() {

		BigInteger value1 = new BigInteger(1, this.bytes);
		BigInteger value2 = BigInteger.ZERO;
		int blockLength = this.getSet().getBlockLength();

		//As I do not know what this code really should do, I just tuned it... but the variable names I have chosen ... are stupid.
		//Futher tuning by removing the for loop and do it mathematically!
		//TODO: Describe what it does!
		if (this.getLength() > 0) {
			byte[] oneOone = new byte[this.getLength()];
			int amount = oneOone.length / blockLength;
			for (int i = 0; i < amount; i++) {
				oneOone[(oneOone.length - 1) - (i * blockLength)] = 1; //It does something like: 100000000100000000100000000 ->MSB
			}
			value2 = new BigInteger(1, oneOone);
		}
		return value1.add(value2);
	}

	@Override
	protected boolean standardIsEquivalent(ByteArrayElement element) {
		return Arrays.equals(this.bytes, element.bytes);
	}

	@Override
	public String standardToStringContent() {
		String str = "";
		String delimiter = "";
		for (int i = 0; i < this.getLength(); i++) {
			str = str + delimiter + String.format("%02X", BigInteger.valueOf(bytes[i] & 0xFF));
			if ((i + 1) % this.getSet().getBlockLength() == 0) {
				delimiter = "|";

			} else {
				delimiter = "-";
			}
		}
		return "\"" + str + "\"";
	}

}
