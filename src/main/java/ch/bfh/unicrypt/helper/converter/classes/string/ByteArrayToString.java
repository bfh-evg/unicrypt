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

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractStringConverter;
import javax.xml.bind.DatatypeConverter;

/**
 * Instance of this class convert byte arrays into strings. Three modes of operation are supported, depending on the
 * chosen radix for the conversion: {@link Radix#BINARY} (radix=2), {@link Radix#HEX} (radix=16), {@link Radix#BASE64}
 * (radix=64). The resulting strings represent the bytes in increasing order from left to right. In the modes
 * {@code BINARY} and {@code HEX}, a optional delimiter character can be chosen. In the mode {@code BINARY}, the bytes
 * are represented by bit strings of length 8 containing characters {@code '0'} and {@code '1'}. In the mode
 * {@code HEX}, both upper-case letters {@code '0',...,'9','A',...,'F'} or lower-case letters
 * {@code '0',...,'9','a',...,'f'} are supported. In the mode {@code BASE64}, the byte array is represented by a string
 * of characters {@code 'A',...,'Z','a',...,'z','0',...,'9','+','/'} (plus suffix {@code "="} or {@code "=="} for making
 * the string length a multiple of 4). The default mode of operation is {@code HEX} with upper-case letters and no
 * delimiter.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class ByteArrayToString
	   extends AbstractStringConverter<ByteArray> {

	private static final long serialVersionUID = 1L;

	/**
	 * This enumeration type lists the three supported modes of operation. The default mode is {@code HEX}.
	 */
	public enum Radix {

		BINARY, HEX, BASE64

	};

	// the radix of the converter
	private final Radix radix;

	// the chosen delimiter (only for BINARY and HEX)
	private final String delimiter;

	// a flag indicating whether upper-case or lower-case letters are used in HEX mode
	private final boolean upperCase;

	// the regular expression to check if a string is valid for re-conversion
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
	 * Returns the default {@code ByteArrayToString} converter. Its mode is {@code HEX} using upper-case letters. No
	 * delimiters are added.
	 * <p>
	 * @return The default converter.
	 */
	public static ByteArrayToString getInstance() {
		return ByteArrayToString.getInstance(Radix.HEX, "", true);
	}

	/**
	 * Returns a new {@code ByteArrayToString} converter in {@code HEX} mode. The parameter {@code upperCase} determines
	 * whether upper-case or lower-case letters are used in the conversion. No delimiters are added.
	 * <p>
	 * @param upperCase The flag indicating the use of upper-case or lower-case letters
	 * @return The new converter
	 */
	public static ByteArrayToString getInstance(boolean upperCase) {
		return ByteArrayToString.getInstance(Radix.HEX, "", upperCase);
	}

	/**
	 * Returns a new {@code ByteArrayToString} converter in {@code HEX} mode using upper-case letters. The parameter
	 * {@code delimiter} defines the delimiter symbol. All characters except {@code '0',...,'9','A',...,'F'} are allowed
	 * as delimiter.
	 * <p>
	 * @param delimiter The delimiter symbol
	 * @return The new converter
	 */
	public static ByteArrayToString getInstance(String delimiter) {
		return ByteArrayToString.getInstance(Radix.HEX, delimiter, true);
	}

	/**
	 * Returns a new {@code ByteArrayToString} converter in {@code HEX} mode. The parameter {@code upperCase} determines
	 * whether upper-case or lower-case letters are used in the conversion. The parameter {@code delimiter} defines the
	 * delimiter symbol. All characters except {@code '0',...,'9','A',...,'F'} or {@code '0',...,'9','a',...,'f'} are
	 * allowed as delimiter, depending on the value of {@code upperCase}.
	 * <p>
	 * @param delimiter The delimiter symbol
	 * @param upperCase The flag indicating the use of upper-case or lower-case letters
	 * @return The new converter
	 */
	public static ByteArrayToString getInstance(String delimiter, boolean upperCase) {
		return ByteArrayToString.getInstance(Radix.HEX, delimiter, upperCase);
	}

	/**
	 * Returns a new {@code ByteArrayToString} converter for a given mode (radix). For {@code HEX}, upper-case letters
	 * are used in the conversion. No delimiters are added.
	 * <p>
	 * @param radix The given radix {@code BINARY}, {@code HEX}, or {@code BASE64}
	 * @return The new converter
	 */
	public static ByteArrayToString getInstance(Radix radix) {
		if (radix == Radix.BASE64) {
			return ByteArrayToString.getInstance(radix, "", true);
		}
		return ByteArrayToString.getInstance(radix, "", true);
	}

	/**
	 * Returns a new {@code ByteArrayToString} converter for a given mode (radix). For {@code BINARY} or {@code HEX},
	 * the parameter {@code delimiter} defines the delimiter symbol. For {@code HEX}, upper-case letters are used in the
	 * conversion.
	 * <p>
	 * @param radix     The given radix {@code BINARY}, {@code HEX}, or {@code BASE64}
	 * @param delimiter The delimiter symbol
	 * @return The new converter
	 */
	public static ByteArrayToString getInstance(Radix radix, String delimiter) {
		return ByteArrayToString.getInstance(radix, delimiter, true);
	}

	/**
	 * Returns a new {@code ByteArrayToString} converter for a given mode (radix). For {@code HEX}, the parameter
	 * {@code upperCase} determines whether upper-case or lower-case letters are used in the conversion. No delimiters
	 * are added.
	 * <p>
	 * @param radix     The given radix {@code BINARY}, {@code HEX}, or {@code BASE64}
	 * @param upperCase The flag indicating the use of upper-case or lower-case letters
	 * @return The new converter
	 */
	public static ByteArrayToString getInstance(Radix radix, boolean upperCase) {
		return ByteArrayToString.getInstance(radix, "", upperCase);
	}

	/**
	 * This is the general method of this class for constructing a new {@code ByteArrayToString} converter. The
	 * parameter {@code radix} determines the mode. For {@code BINARY} or {@code HEX}, the parameter {@code delimiter}
	 * defines the delimiter symbol. For {@code HEX}, the parameter {@code upperCase} determines whether upper-case or
	 * lower-case letters are used in the conversion.
	 * <p>
	 * @param radix     The given radix {@code BINARY}, {@code HEX}, or {@code BASE64}
	 * @param delimiter The delimiter symbol
	 * @param upperCase The flag indicating the use of upper-case or lower-case letters
	 * @return The new converter
	 */
	public static ByteArrayToString getInstance(Radix radix, String delimiter, boolean upperCase) {
		if (radix == null || delimiter == null || delimiter.length() > 1
			   || radix == Radix.BINARY && delimiter.matches("^[0-1]$")
			   || radix == Radix.HEX && delimiter.matches(upperCase ? "^[0-9A-F]$" : "^[0-9a-f]$")
			   || radix == Radix.BASE64 && delimiter.length() > 0) {
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
