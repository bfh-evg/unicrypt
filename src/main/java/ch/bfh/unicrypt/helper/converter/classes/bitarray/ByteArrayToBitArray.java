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
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractBitArrayConverter;

/**
 * The single instance of this class converts {@code ByteArray} values into {@code BitArray} values. The bits contained
 * in the byte array and the order of the bits remain unchanged.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class ByteArrayToBitArray
	   extends AbstractBitArrayConverter<ByteArray> {

	private static ByteArrayToBitArray instance = null;
	private static final long serialVersionUID = 1L;

	protected ByteArrayToBitArray() {
		super(ByteArray.class);
	}

	/**
	 * Returns the single instance of this class.
	 * <p>
	 * @return The single instance
	 */
	public static ByteArrayToBitArray getInstance() {
		if (instance == null) {
			instance = new ByteArrayToBitArray();
		}
		return instance;
	}

	@Override
	protected boolean defaultIsValidOutput(BitArray bitArray) {
		return bitArray.getLength() % Byte.SIZE == 0;
	}

	@Override
	public BitArray abstractConvert(ByteArray byteArray) {
		return BitArray.getInstance(byteArray);
	}

	@Override
	public ByteArray abstractReconvert(BitArray bitArray) {
		return bitArray.getByteArray();
	}

}
