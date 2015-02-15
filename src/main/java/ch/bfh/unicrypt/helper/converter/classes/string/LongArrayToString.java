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

import ch.bfh.unicrypt.helper.array.classes.LongArray;
import ch.bfh.unicrypt.helper.converter.abstracts.AbstractStringConverter;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class LongArrayToString
	   extends AbstractStringConverter<LongArray> {

	public enum Radix {

		BINARY, HEX;

	};

	private final Radix radix;
	private final boolean reverse;
	private final String delimiter;
	private final String binaryRegExp;
	private final String hexRegExp;

	protected LongArrayToString(Radix radix, boolean reverse) {
		super(LongArray.class);
		this.radix = radix;
		this.reverse = reverse;
		this.delimiter = "";
		this.binaryRegExp = "^([0-1]{8}([0-1]{8})*)?$";
		this.hexRegExp = "^([0-9A-F]{2}([0-9A-F]{2})*)?$";
	}

	protected LongArrayToString(Radix radix, boolean reverse, String delimiter) {
		super(LongArray.class);
		this.radix = radix;
		this.reverse = reverse;
		this.delimiter = delimiter;
		this.binaryRegExp = "^([0-1]{8}(\\|[0-1]{8})*)?$";
		this.hexRegExp = "^([0-9A-F]{2}(\\|[0-9A-F]{2})*)?$";
	}

	@Override
	public String abstractConvert(LongArray longArray) {
		switch (this.radix) {
			case BINARY: {
				StringBuilder sb = new StringBuilder(longArray.getLength() * (Long.SIZE + 1));
				String delim = "";
				for (Long l : this.reverse ? longArray.reverse() : longArray) {
					sb.append(delim);
					sb.append(String.format("%64s", Long.toBinaryString(l)).replace(' ', '0'));
					delim = this.delimiter;
				}
				return sb.toString();
			}
			case HEX: {
				StringBuilder sb = new StringBuilder(longArray.getLength() * 3);
				String delim = "";
				for (Long b : this.reverse ? longArray.reverse() : longArray) {
					sb.append(delim);
					sb.append(String.format("%016X", b));
					delim = this.delimiter;
				}
				return sb.toString();
			}
			default:
				// impossible case
				throw new IllegalStateException();
		}
	}

	@Override
	public LongArray abstractReconvert(String string) {
		if (this.delimiter.length() > 0) {
			string = string.replace(this.delimiter.charAt(0), '|');
		}
		switch (this.radix) {
			case BINARY: {
				if (!string.matches(this.binaryRegExp)) {
					throw new IllegalArgumentException();
				}
				int subLength = 8 + this.delimiter.length();
				long[] longs = new long[(string.length() + this.delimiter.length()) / subLength];
				for (int i = 0; i < longs.length; i++) {
					longs[i] = (long) Integer.parseInt(string.substring(i * subLength, i * subLength + 8), 2);
				}
				LongArray result = LongArray.getInstance(longs);
				return this.reverse ? result.reverse() : result;
			}
			case HEX: {
				string = string.toUpperCase();
				if (!string.matches(this.hexRegExp)) {
					throw new IllegalArgumentException();
				}
				int subLength = 2 + this.delimiter.length();
				long[] longs = new long[(string.length() + this.delimiter.length()) / subLength];
				for (int i = 0; i < longs.length; i++) {
					longs[i] = (long) Integer.parseInt(string.substring(i * subLength, i * subLength + 2), 16);
				}
				LongArray result = LongArray.getInstance(longs);
				return this.reverse ? result.reverse() : result;
			}
			default:
				// impossible case
				throw new IllegalStateException();
		}
	}

	public static LongArrayToString getInstance() {
		return LongArrayToString.getInstance(LongArrayToString.Radix.HEX, false);
	}

	public static LongArrayToString getInstance(Radix radix) {
		return LongArrayToString.getInstance(radix, false);
	}

	public static LongArrayToString getInstance(boolean reverse) {
		return LongArrayToString.getInstance(LongArrayToString.Radix.HEX, reverse);
	}

	public static LongArrayToString getInstance(Radix radix, boolean reverse) {
		if (radix == null) {
			throw new IllegalArgumentException();
		}
		return new LongArrayToString(radix, reverse);
	}

	public static LongArrayToString getInstance(String delimiter) {
		return LongArrayToString.getInstance(Radix.HEX, delimiter, false);
	}

	public static LongArrayToString getInstance(String delimiter, boolean reverse) {
		return LongArrayToString.getInstance(Radix.HEX, delimiter, reverse);
	}

	public static LongArrayToString getInstance(Radix radix, String delimiter) {
		return LongArrayToString.getInstance(radix, delimiter, false);
	}

	public static LongArrayToString getInstance(Radix radix, String delimiter, boolean reverse) {
		if (radix == null || delimiter == null || delimiter.length() != 1
			   || (radix == Radix.BINARY && "01".indexOf(delimiter.charAt(0)) >= 0)
			   || (radix == Radix.HEX && "0123456789ABCDEFabcdef".indexOf(delimiter.charAt(0)) >= 0)) {
			throw new IllegalArgumentException();
		}
		return new LongArrayToString(radix, reverse, delimiter);
	}

}
