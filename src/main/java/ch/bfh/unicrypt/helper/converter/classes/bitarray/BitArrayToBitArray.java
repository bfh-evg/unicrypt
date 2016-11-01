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
package ch.bfh.unicrypt.helper.converter.classes.bitarray;

import ch.bfh.unicrypt.helper.array.classes.BitArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractBitArrayConverter;

/**
 * Instance of this class convert {@code BitArray} values into {@code BitArray} values. There are two operating modes,
 * one in which the bit arrays remain unchanged and one in which the bit arrays are reversed.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class BitArrayToBitArray
	   extends AbstractBitArrayConverter<BitArray> {
	private static final long serialVersionUID = 1L;

	private final boolean reverse;

	private BitArrayToBitArray(boolean reverse) {
		super(BitArray.class);
		this.reverse = reverse;
	}

	/**
	 * Returns a new default {@link BitArrayToBitArray} converter, which leaves the bit arrays unchanged.
	 * <p>
	 * @return The default converter
	 */
	public static BitArrayToBitArray getInstance() {
		return new BitArrayToBitArray(false);
	}

	/**
	 * Returns a new {@link BitArrayToBitArray} converter for a given flag {@code reverse} indicating if the byte arrays
	 * are reversed.
	 * <p>
	 * @param reverse The flag indicating if the byte arrays are reversed
	 * @return The new converter
	 */
	public static BitArrayToBitArray getInstance(boolean reverse) {
		return new BitArrayToBitArray(reverse);
	}

	@Override
	protected BitArray abstractConvert(BitArray bitArray) {
		if (this.reverse) {
			return bitArray.reverse();
		}
		return bitArray;
	}

	@Override
	protected BitArray abstractReconvert(BitArray bitArray) {
		if (this.reverse) {
			return bitArray.reverse();
		}
		return bitArray;
	}

}
