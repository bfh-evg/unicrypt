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
package ch.bfh.unicrypt.helper.converter.classes.string;

import ch.bfh.unicrypt.helper.array.classes.BitArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractStringConverter;

/**
 * Instances of this class convert bit strings into Java strings consisting of characters {@code '0'} and {@code '1'}.
 * There are two modes of operations. In the default mode, the output strings are constructed from left-to-right,
 * whereas in the 'reverse' mode, strings are constructed from right-to-left.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class BitArrayToString
	   extends AbstractStringConverter<BitArray> {

	// used to check strings before reconverting
	private static final String REGEXP = "^[0-1]*$";
	private static final long serialVersionUID = 1L;

	// a flag indicating the mode of operation (left-to-right / right-to-left)
	private final boolean reverse;

	protected BitArrayToString(boolean reverse) {
		super(BitArray.class);
		this.reverse = reverse;
	}

	/**
	 * Returns the default {@code BitArrayToString} converter, which constructs the string from left-to-right.
	 * <p>
	 * @return The default converter
	 */
	public static BitArrayToString getInstance() {
		return new BitArrayToString(false);
	}

	/**
	 * Returns a new {@code BitArrayToString} converter for one of the two modes of operation.
	 * <p>
	 * @param reverse A flag indicating the mode of operation
	 * @return The new converter
	 */
	public static BitArrayToString getInstance(boolean reverse) {
		return new BitArrayToString(reverse);
	}

	@Override
	protected boolean defaultIsValidOutput(String string) {
		return string.matches(REGEXP);
	}

	@Override
	protected String abstractConvert(BitArray bitArray) {
		StringBuilder stringBuilder = new StringBuilder(bitArray.getLength());
		for (Boolean bit : this.reverse ? bitArray.reverse() : bitArray) {
			stringBuilder.append(bit ? "1" : "0");
		}
		return stringBuilder.toString();
	}

	@Override
	protected BitArray abstractReconvert(String string) {
		boolean[] bits = new boolean[string.length()];
		for (int i = 0; i < bits.length; i++) {
			bits[i] = string.charAt(i) == '1';
		}
		BitArray result = BitArray.getInstance(bits);
		return this.reverse ? result.reverse() : result;
	}

}
