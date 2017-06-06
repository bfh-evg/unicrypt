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
package ch.bfh.unicrypt.helper.converter.classes.biginteger;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractBigIntegerConverter;
import ch.bfh.unicrypt.helper.math.MathUtil;
import java.math.BigInteger;

/**
 * Instances of this class convert byte arrays into non-negative {@code BigInteger} values 0, 1, 2, ... The input byte
 * arrays can be restricted in two ways. First, it is possible to define a block length. For example, if the block
 * length equals 2, then only byte arrays of length 0, 2, 4, ... are valid inputs. Second, a minimal number of blocks
 * can be specified. Again, for blocks of length 2 and with a minimal number of blocks of 3, then only byte arrays of
 * length 6, 8, 10, ... are valid inputs. An unrestricted input corresponds to block length 1 and minimal number of
 * blocks 0.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class ByteArrayToBigInteger
	   extends AbstractBigIntegerConverter<ByteArray> {

	private static final long serialVersionUID = 1L;

	private final int blockLength;
	private final int minBlocks;

	protected ByteArrayToBigInteger(int blockLength, int minBlocks) {
		super(ByteArray.class);
		this.blockLength = blockLength;
		this.minBlocks = minBlocks;
	}

	/**
	 * Creates a new default {@link ByteArrayToBigInteger} converter with the block length set to 1 and the minimal
	 * number of blocks set to 0. This is the default converter for byte arrays of arbitrary length.
	 * <p>
	 * @return The new converter
	 */
	public static ByteArrayToBigInteger getInstance() {
		return ByteArrayToBigInteger.getInstance(1, 0);
	}

	/**
	 * Creates a new {@link ByteArrayToBigInteger} converter for a given block length. The minimal number of blocks is
	 * set to 1. The is the default converter for blocks of a fixed length.
	 * <p>
	 * @param blockLength The block length
	 * @return The new converter
	 */
	public static ByteArrayToBigInteger getInstance(int blockLength) {
		return ByteArrayToBigInteger.getInstance(blockLength, 1);
	}

	/**
	 * Creates a new {@link ByteArrayToBigInteger} converter for a given block length and minimal number of blocks. This
	 * is the general factory method for this class.
	 * <p>
	 * @param blockLength The block length
	 * @param minBlocks   The minimal number of blocks
	 * @return The new converter
	 */
	public static ByteArrayToBigInteger getInstance(int blockLength, int minBlocks) {
		if (blockLength < 1 || minBlocks < 0) {
			throw new IllegalArgumentException();
		}
		return new ByteArrayToBigInteger(blockLength, minBlocks);
	}

	@Override
	protected boolean defaultIsValidInput(ByteArray byteArray) {
		return (byteArray.getLength() % this.blockLength) == 0
			   && (byteArray.getLength() / this.blockLength) >= minBlocks;
	}

	@Override
	protected boolean defaultIsValidOutput(BigInteger value) {
		return value.signum() >= 0;
	}

	// For blocklLength=1, there is 1 byte array of length 0, 256 of length 1, 65536 of length 2, etc. For
	// blocklLength=2, there is 1 byte array of length 0, 65536 of length 2, etc. To convert an input byte array, the
	// number of all byte arrays shorter than the input array is computed and added to the integer represented by the
	// byte array (unsigned, big endian).
	@Override
	protected BigInteger abstractConvert(ByteArray byteArray) {
		BigInteger blockSize = MathUtil.powerOfTwo(this.blockLength * Byte.SIZE);

		// compute the total number of shorter byte arrays
		BigInteger result = MathUtil.ZERO;
		BigInteger multipleBlockSize = blockSize.pow(this.minBlocks);
		for (int blocks = this.minBlocks; blocks < byteArray.getLength() / this.blockLength; blocks++) {
			result = result.add(multipleBlockSize);
			multipleBlockSize = multipleBlockSize.multiply(blockSize);
		}
		// convert byte array to BigInteger and add it to the result
		return result.add(new BigInteger(1, byteArray.getBytes()));
	}

	@Override
	protected ByteArray abstractReconvert(BigInteger value) {
		BigInteger blockSize = MathUtil.powerOfTwo(this.blockLength * Byte.SIZE);

		// subtract the total number of shorter byte arrays
		int blocks = this.minBlocks;
		BigInteger multipleBlockSize = blockSize.pow(this.minBlocks);
		while (value.compareTo(multipleBlockSize) >= 0) {
			value = value.subtract(multipleBlockSize);
			multipleBlockSize = multipleBlockSize.multiply(blockSize);
			blocks++;
		}
		// convert the resulting value to ByteArray and remove leading 0 byte for sign
		ByteArray result = ByteArray.getInstance(value.toByteArray()).removePrefix();
		// adjust length of result
		return result.addPrefix(blocks * this.blockLength - result.getLength());
	}

}
