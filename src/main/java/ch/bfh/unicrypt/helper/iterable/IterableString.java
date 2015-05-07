/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.helper.iterable;

import ch.bfh.unicrypt.helper.UniCrypt;
import java.util.Iterator;

/**
 * Instances of this class offer an inexpensive way of creating iterators over substrings of a given string. The
 * substring separation is determined by a separation character. The default separation character is {@code '|'}.
 * Example: {@code "Hello|World|!"} leads to substrings {@code "Hello"}, {@code "World"}, {@code "!"}.
 * <p>
 * The string may contain escaped separation characters, which are ignored for the separation. The default escape
 * character is {@code '\'}. Example: {@code "Hello|World\|!"} leads to substrings {@code "Hello"}, {@code "World\|!"}.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class IterableString
	   extends UniCrypt
	   implements Iterable<String> {

	private final String string;
	private final char separator;
	private final char escapeChar;

	private IterableString(String string, char separator, char escapeChar) {
		this.string = string;
		this.separator = separator;
		this.escapeChar = escapeChar;
	}

	/**
	 * Returns a new iterable string for the default separator and escape characters.
	 * <p>
	 * @param string The given string
	 * @return The new iterable string
	 */
	public static IterableString getInstance(String string) {
		return IterableString.getInstance(string, '|', '\\');
	}

	/**
	 * Returns a new iterable string for given separator and escape characters.
	 * <p>
	 * @param string     The given string
	 * @param separator  The separation character
	 * @param escapeChar The escape character
	 * @return The new iterable string
	 */
	public static IterableString getInstance(String string, char separator, char escapeChar) {
		if (string == null || separator == escapeChar) {
			throw new IllegalArgumentException();
		}
		return new IterableString(string, separator, escapeChar);
	}

	private boolean isEscaped(int index) {
		return index > 0 && this.isEscapeChar(index - 1);

	}

	private boolean isEscapeChar(int index) {
		return this.string.charAt(index) == this.escapeChar && !this.isEscaped(index);
	}

	private boolean isSeparator(int index) {
		return this.string.charAt(index) == this.separator && !this.isEscaped(index);
	}

	private int findNextSeparator(int index) {
		while (index < 0 || (index < this.string.length() && !this.isSeparator(index))) {
			index++;
		}
		return index;
	}

	@Override
	public Iterator<String> iterator() {
		return new Iterator<String>() {

			int currenIndex = -1;

			@Override
			public boolean hasNext() {
				return string.length() > 0 && this.currenIndex < string.length();
			}

			@Override
			public String next() {
				this.currenIndex++;
				int nextIndex = findNextSeparator(this.currenIndex);
				String result = string.substring(this.currenIndex, nextIndex);
				this.currenIndex = nextIndex;
				return result;
			}

		};
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 59 * hash + this.string.hashCode();
		hash = 59 * hash + this.separator;
		hash = 59 * hash + this.escapeChar;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final IterableString other = (IterableString) obj;
		return this.string.equals(other.string) && this.separator == other.separator && this.escapeChar == other.escapeChar;
	}

	@Override
	protected String defaultToStringContent() {
		return this.string;
	}

}
