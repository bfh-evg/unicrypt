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

import ch.bfh.unicrypt.helper.converter.abstracts.AbstractBigIntegerConverter;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.helper.math.MathUtil;
import java.math.BigInteger;

/**
 * Instances of this class convert strings of a given alphabet into non-negative {@code BigInteger} values 0, 1, 2, ...
 * The input strings can be restricted in two ways. First, it is possible to define a block length. For example, if the
 * block length equals 2, then only strings of length 0, 2, 4, ... are valid inputs. Second, a minimal number of blocks
 * can be specified. Again, for blocks of length 2 and with a minimal number of blocks of 3, then only strings of length
 * 6, 8, 10, ... are valid inputs. An unrestricted input corresponds to block length 1 and minimal number of blocks 0.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class StringToBigInteger
	   extends AbstractBigIntegerConverter<String> {

	private static final long serialVersionUID = 1L;

	private final Alphabet alphabet;
	private final int blockLength;
	private final int minBlocks;

	protected StringToBigInteger(Alphabet alphabet, int blockLength, int minBlocks) {
		super(String.class);
		this.alphabet = alphabet;
		this.blockLength = blockLength;
		this.minBlocks = minBlocks;
	}

	/**
	 * For a given alphabet, this method creates a new default {@link StringToBigInteger} converter with the block
	 * length set to 1 and the minimal number of blocks set to 0.
	 * <p>
	 * @param alphabet The given alphabet
	 * @return The new converter
	 */
	public static StringToBigInteger getInstance(Alphabet alphabet) {
		return StringToBigInteger.getInstance(alphabet, 1, 0);
	}

	/**
	 * For a given alphabet and a given block length, this method creates a new {@link StringToBigInteger} converter.
	 * The minimal number of blocks is set to 0.
	 * <p>
	 * @param alphabet    The given alphabet
	 * @param blockLength The block length
	 * @return The new converter
	 */
	public static StringToBigInteger getInstance(Alphabet alphabet, int blockLength) {
		return StringToBigInteger.getInstance(alphabet, blockLength, 0);
	}

	/**
	 * Creates a new {@link StringToBigInteger} converter for a given alphabet, block length, and minimal number of
	 * blocks. This is the general factory method for this class.
	 * <p>
	 * @param alphabet    The given alphabet
	 * @param blockLength The block length
	 * @param minBlocks   The minimal number of blocks
	 * @return The new converter
	 */
	public static StringToBigInteger getInstance(Alphabet alphabet, int blockLength, int minBlocks) {
		if (alphabet == null || blockLength < 1 || minBlocks < 0) {
			throw new IllegalArgumentException();
		}
		return new StringToBigInteger(alphabet, blockLength, minBlocks);
	}

	@Override
	protected boolean defaultIsValidInput(String string) {
		return this.alphabet.containsAll(string) && (string.length() % this.blockLength) == 0
			   && (string.length() / this.blockLength) >= minBlocks;
	}

	@Override
	protected boolean defaultIsValidOutput(BigInteger value) {
		return value.signum() >= 0;
	}

	@Override
	protected BigInteger abstractConvert(String string) {
		BigInteger alphabetSize = BigInteger.valueOf(this.alphabet.getSize());
		BigInteger blockSize = alphabetSize.pow(this.blockLength);

		// compute the total number of shorter strings
		BigInteger result1 = MathUtil.ZERO;
		BigInteger multipleBlockSize = blockSize.pow(this.minBlocks);
		for (int i = this.minBlocks; i < string.length() / this.blockLength; i++) {
			result1 = result1.add(multipleBlockSize);
			multipleBlockSize = multipleBlockSize.multiply(blockSize);
		}
		// compute the rank of the string among all string of its length
		BigInteger result2 = MathUtil.ZERO;
		for (int i = 0; i < string.length(); i++) {
			int charIndex = this.alphabet.getIndex(string.charAt(i));
			result2 = result2.multiply(alphabetSize).add(BigInteger.valueOf(charIndex));
		}
		return result1.add(result2);
	}

	@Override
	protected String abstractReconvert(BigInteger value) {
		BigInteger alphabetSize = BigInteger.valueOf(this.alphabet.getSize());
		BigInteger blockSize = alphabetSize.pow(this.blockLength);

		// subtract the total number of shorter strings
		int blocks = this.minBlocks;
		BigInteger multipleBlockSize = blockSize.pow(this.minBlocks);
		while (value.compareTo(multipleBlockSize) >= 0) {
			value = value.subtract(multipleBlockSize);
			multipleBlockSize = multipleBlockSize.multiply(blockSize);
			blocks++;
		}
		// convert the resulting value to string
		String result = "";
		for (int i = 0; i < blocks * blockLength; i++) {
			result = this.alphabet.getCharacter(value.mod(alphabetSize).intValue()) + result;
			value = value.divide(alphabetSize);
		}
		return result;
	}

	@Override
	protected String defaultToStringContent() {
		return this.alphabet.toString() + "," + this.blockLength + "," + this.minBlocks;
	}

}
