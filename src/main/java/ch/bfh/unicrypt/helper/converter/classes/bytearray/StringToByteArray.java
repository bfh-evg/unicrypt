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
package ch.bfh.unicrypt.helper.converter.classes.bytearray;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractByteArrayConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Instances of this class convert strings into {@code ByteArray} values using the standard Java conversion method
 * implemented in {@link String#getBytes(Charset)} and {@link String#String(byte[], Charset)}. The default converter
 * uses Java's default character set {@link Charset#defaultCharset()}. A converter for any of the following Unicode
 * character sets can be created: {@code UTF-8}, {@code UTF-16}, {@code UTF-16BE}, {@code UTF-16LE},
 * {@code UTF-32}, {@code UTF-32BE}, {@code UTF-32LE}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @see StandardCharsets
 */
public class StringToByteArray
	   extends AbstractByteArrayConverter<String> {

	static final String[] UNICODE_CHARSETS
		   = {"UTF-8", "UTF-16", "UTF-16BE", "UTF-16LE", "UTF-32", "UTF-32BE", "UTF-32LE"};
	private static final long serialVersionUID = 1L;

	private final Charset charset;
	private final int minBytesPerCharacter; // the minimal number of bytes needed to encode a single character

	private StringToByteArray(Charset charset, int minBytesPerCharacter) {
		super(String.class);
		this.charset = charset;
		this.minBytesPerCharacter = minBytesPerCharacter;
	}

	/**
	 * Returns the default instance of this class using {@link Charset#defaultCharset()} as character set.
	 * <p>
	 * @return The default instance
	 */
	public static StringToByteArray getInstance() {
		return StringToByteArray.getInstance("UTF-8");
	}

	/**
	 * Returns a new instance for a given name of a Unicode character set.
	 * <p>
	 * @param charsetName The given name of the Unicode character set
	 * @return The new instance
	 */
	public static StringToByteArray getInstance(String charsetName) {
		if (charsetName == null || !isUnicodeCharset(charsetName)) {
			throw new IllegalArgumentException();
		}
		return new StringToByteArray(Charset.forName(charsetName), getMinBytes(charsetName));

	}

	/**
	 * Returns a new instance for a given Unicode character set.
	 * <p>
	 * @param charset The given Unicode character set
	 * @return The new instance
	 */
	public static StringToByteArray getInstance(Charset charset) {
		if (charset == null || !isUnicodeCharset(charset.name())) {
			throw new IllegalArgumentException();
		}
		return new StringToByteArray(charset, getMinBytes(charset.name()));
	}

	private static boolean isUnicodeCharset(String charsetName) {
		for (String unicodeCharset : UNICODE_CHARSETS) {
			if (unicodeCharset.equals(charsetName)) {
				return true;
			}
		}
		return false;
	}

	private static int getMinBytes(String charsetName) {
		if (charsetName.equals("UTF-8")) {
			return 1;
		}
		if (charsetName.startsWith("UTF-16")) {
			return 2;
		}
		if (charsetName.startsWith("UTF-32")) {
			return 4;
		}
		throw new IllegalArgumentException();
	}

	@Override
	protected boolean defaultIsValidOutput(ByteArray byteArray) {
		// not all byte arrays are valid for re-conversion, but performing corresponding tests is too expensive
		return byteArray.getLength() % this.minBytesPerCharacter == 0;
	}

	@Override
	protected ByteArray abstractConvert(String string) {
		return new SafeByteArray(string.getBytes(this.charset));
	}

	@Override
	protected String abstractReconvert(ByteArray byteArray) {
		return new String(byteArray.getBytes(), charset);
	}

}
