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
package ch.bfh.unicrypt.helper.converter.classes.bytearray;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractByteArrayConverter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Instances of this class convert strings into {@code ByteArray} values using the standard Java conversion method
 * implemented in {@link String#getBytes(Charset)} and {@link String#String(byte[], Charset)}. The default converter
 * uses Java's default character set {@link Charset#defaultCharset()}. A converter for any other standard character set
 * defined in {@link StandardCharsets} can be created.
 * <p>
 * @author Rolf Haenni
 * @version 2.0
 * @see StandardCharsets
 */
public class StringToByteArray
	   extends AbstractByteArrayConverter<String> {

	private final Charset charset;

	private StringToByteArray(Charset charset) {
		super(String.class);
		this.charset = charset;
	}

	/**
	 * Returns the default instance of this class using {@link Charset#defaultCharset()} as character set.
	 * <p>
	 * @return The default instance
	 */
	public static StringToByteArray getInstance() {
		return new StringToByteArray(Charset.defaultCharset());
	}

	private static boolean isStandardCharset(Charset charset) {
		return charset == StandardCharsets.ISO_8859_1
			   || charset == StandardCharsets.US_ASCII
			   || charset == StandardCharsets.UTF_16
			   || charset == StandardCharsets.UTF_16BE
			   || charset == StandardCharsets.UTF_16LE
			   || charset == StandardCharsets.UTF_8;
	}

	/**
	 * Returns a new instance for a given standard character set.
	 * <p>
	 * @param standardCharset The given standard character set
	 * @return The new instance
	 */
	public static StringToByteArray getInstance(Charset standardCharset) {
		if (standardCharset == null || !isStandardCharset(standardCharset)) {
			throw new IllegalArgumentException();
		}
		return new StringToByteArray(standardCharset);
	}

	@Override
	protected boolean defaultIsValidOutput(ByteArray byteArray) {
		// not all byte arrays are valid for re-conversion in a given character set,
		// but performing corresponding tests is too expensive
		return true;
	}

	@Override
	protected ByteArray abstractConvert(String string) {
		return SafeByteArray.getInstance(string.getBytes(this.charset));
	}

	@Override
	protected String abstractReconvert(ByteArray byteArray) {
		return new String(byteArray.getBytes(), charset);
	}

}
