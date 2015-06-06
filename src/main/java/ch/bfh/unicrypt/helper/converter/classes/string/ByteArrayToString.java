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
package ch.bfh.unicrypt.helper.converter.classes.string;

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractStringConverter;
import javax.xml.bind.DatatypeConverter;

/**
 * Instance of this class convert byte arrays into strings. Three modes of operation are supporting, depending on the
 * chosen radix for the conversion: {@link Radix#BINARY} (radix=2), {@link Radix#HEX} (radix=16), {@link Radix#BASE64}
 * (radix=64). The resulting strings represent the bytes in increasing order from left to right. In the modes
 * {@code BINARY} and {@code HEX}, a delimiter character can be chosen. The default delimiter character is {@code '|'}.
 * In the mode {@code BINARY}, the bytes are represented by bit strings of length 8 containing characters {@code '0'}
 * and {@code '1'}. In the mode {@code HEX}, both upper-case letters {@code '0',...,'9','A',...,'F'} or lower-case
 * letters {@code '0',...,'9','a',...,'f'} are supported. In the mode {@code BASE64}, the byte array is represented by a
 * string of characters {@code 'A',...,'Z','a',...,'z','0',...,'9','+','/'} (plus suffix {@code "="} or {@code "=="} for
 * making the string length a multiple of 4). The default mode of operation is {@code HEX} with upper-case letters.
 * <p>
 * <p>
 * @author Rolf Haenni
 * @version 2.0
 */
public class ByteArrayToString
	   extends AbstractStringConverter<ByteArray> {

	/**
	 * The enumeration type lists the three supported modes of operation. The default mode is {@code HEX}.
	 */
	public enum Radix {

		BINARY, HEX, BASE64

	};

	private final Radix radix;
	private final String delimiter;
	private final boolean upperCase;
	private String regExp;

	protected ByteArrayToString(Radix radix, String delimiter, boolean upperCase) {
		super(ByteArray.class);
		this.radix = radix;
		this.delimiter = delimiter;
		this.upperCase = upperCase;
		if (radix == Radix.BINARY) {
			if (delimiter.length() == 0) {
				this.regExp = "^([0-1]{8})*$";
			} else {
				this.regExp = "^([0-1]{8}(\\|[0-1]{8})*)?$";
			}
		}
		if (radix == Radix.HEX) {
			String range = upperCase ? "[0-9A-F]" : "[0-9a-f]";
			if (delimiter.length() == 0) {
				this.regExp = "^(" + range + "{2})*$"; // after replacing delimiters
			} else {
				this.regExp = "^(" + range + "{2}(\\|" + range + "{2})*)?$"; // after replacing delimiters
			}
		}
		if (radix == Radix.BASE64) {
			this.regExp = "^(([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==))?$";
		}
	}

	/**
	 *
	 * @return
	 */
	public static ByteArrayToString getInstance() {
		return ByteArrayToString.getInstance(Radix.HEX, "", true);
	}

	/**
	 *
	 * @param upperCase
	 * @return
	 */
	public static ByteArrayToString getInstance(boolean upperCase) {
		return ByteArrayToString.getInstance(Radix.HEX, "", upperCase);
	}

	/**
	 *
	 * @param delimiter
	 * @return
	 */
	public static ByteArrayToString getInstance(String delimiter) {
		return ByteArrayToString.getInstance(Radix.HEX, delimiter, true);
	}

	/**
	 *
	 * @param delimiter
	 * @param upperCase
	 * @return
	 */
	public static ByteArrayToString getInstance(String delimiter, boolean upperCase) {
		return ByteArrayToString.getInstance(Radix.HEX, delimiter, upperCase);
	}

	/**
	 *
	 * @param radix
	 * @return
	 */
	public static ByteArrayToString getInstance(Radix radix) {
		return ByteArrayToString.getInstance(radix, "", true);
	}

	/**
	 *
	 * @param radix
	 * @param delimiter
	 * @return
	 */
	public static ByteArrayToString getInstance(Radix radix, String delimiter) {
		return ByteArrayToString.getInstance(radix, delimiter, true);
	}

	/**
	 *
	 * @param radix
	 * @param upperCase
	 * @return
	 */
	public static ByteArrayToString getInstance(Radix radix, boolean upperCase) {
		return ByteArrayToString.getInstance(radix, "", upperCase);
	}

	/**
	 *
	 * @param radix
	 * @param delimiter
	 * @param upperCase
	 * @return
	 */
	public static ByteArrayToString getInstance(Radix radix, String delimiter, boolean upperCase) {
		if (radix == null || delimiter == null || delimiter.length() > 1
			   || (radix == Radix.BINARY && delimiter.matches("^[0-1]$"))
			   || (radix == Radix.HEX && delimiter.matches(upperCase ? "^[0-9A-F]$" : "^[0-9a-f]$"))
			   || (radix == Radix.BASE64 && delimiter.length() > 0)) {
			throw new IllegalArgumentException();
		}
		return new ByteArrayToString(radix, delimiter, upperCase);
	}

	@Override
	protected boolean defaultIsValidOutput(String string) {
		if (this.delimiter.length() > 0) {
			string = string.replace(this.delimiter.charAt(0), '|');
		}
		return string.matches(this.regExp);
	}

	@Override
	protected String abstractConvert(ByteArray byteArray) {
		switch (this.radix) {
			case BINARY: {
				StringBuilder sb = new StringBuilder(byteArray.getLength() * (Byte.SIZE + 1));
				String delim = "";
				for (Byte b : byteArray) {
					int i = b & 0xFF;
					sb.append(delim);
					sb.append(String.format("%8s", Integer.toBinaryString(i)).replace(' ', '0'));
					delim = this.delimiter;
				}
				return sb.toString();
			}
			case HEX: {
				StringBuilder sb = new StringBuilder(byteArray.getLength() * 3);
				String delim = "";
				for (Byte b : byteArray) {
					int i = b & 0xFF;
					sb.append(delim);
					String nextByte = String.format("%02X", i);
					sb.append(this.upperCase ? nextByte : nextByte.toLowerCase());
					delim = this.delimiter;
				}
				return sb.toString();
			}
			case BASE64:
				return DatatypeConverter.printBase64Binary(byteArray.getBytes());
			default:
				// impossible case
				throw new IllegalStateException();
		}
	}

	@Override
	protected ByteArray abstractReconvert(String string) {
		if (this.delimiter.length() > 0) {
			string = string.replace(this.delimiter.charAt(0), '|');
		}
		switch (this.radix) {
			case BINARY: {
				int subLength = 8 + this.delimiter.length();
				byte[] bytes = new byte[(string.length() + this.delimiter.length()) / subLength];
				for (int i = 0; i < bytes.length; i++) {
					bytes[i] = (byte) Integer.parseInt(string.substring(i * subLength, i * subLength + 8), 2);
				}
				ByteArray result = ByteArray.getInstance(bytes);
				return result;
			}
			case HEX: {
				string = string.toUpperCase();
				int subLength = 2 + this.delimiter.length();
				byte[] bytes = new byte[(string.length() + this.delimiter.length()) / subLength];
				for (int i = 0; i < bytes.length; i++) {
					bytes[i] = (byte) Integer.parseInt(string.substring(i * subLength, i * subLength + 2), 16);
				}
				ByteArray result = ByteArray.getInstance(bytes);
				return result;
			}
			case BASE64:
				return ByteArray.getInstance(DatatypeConverter.parseBase64Binary(string));
			default:
				// impossible case
				throw new IllegalStateException();
		}
	}

}
