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
package ch.bfh.unicrypt.helper.sequence;

/**
 * Instances of this class offer an inexpensive way of creating iterators over substrings of a given string. The
 * substring separation is determined by a separation character. The default separation character is {@code '|'}.
 * Example: {@code "Hello|World|!"} leads to substrings {@code "Hello"}, {@code "World"}, {@code "!"}.
 * <p>
 * The string may contain nested sub-strings surrounded by parentheses. If this is the case, only top-level separation
 * characters are considered. Example: {@code "Hello|[World|!]"} leads to substrings
 * {@code "Hello"}, {@code "[World|!]"}, if {@code '['} and {@code ']'} are the opening and closing parenthesis
 * characters. The parenthesis characters can be chosen freely. When no parenthesis characters are specified, all
 * separation characters are considered.
 * <p>
 * The string may contain escaped separation and parenthesis characters, which are ignored for determining the
 * separation. The default escape character is {@code '\'}. Example: {@code "Hello|World\|!"} leads to substrings
 * {@code "Hello"}, {@code "World\|!"}. If no escape character is specified, escaping is turned off.
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class StringSequence
	   extends Sequence<String> {

	private static final long serialVersionUID = 1L;

	private final String string;
	private final char separator;

	private final boolean nested;
	private char openingParenthesis;
	private char closingParenthesis;

	private final boolean escaped;
	private char escapeChar;

	protected StringSequence(String string, char separator) {
		super(Sequence.UNKNOWN);
		this.string = string;
		this.separator = separator;
		this.nested = false;
		this.escaped = false;
	}

	protected StringSequence(String string, char separator, char escapeChar) {
		super(Sequence.UNKNOWN);
		this.string = string;
		this.separator = separator;
		this.nested = false;
		this.escaped = true;
		this.escapeChar = escapeChar;
	}

	protected StringSequence(String string, char separator, char openingParentesis, char closingParenthesis) {
		super(Sequence.UNKNOWN);
		this.string = string;
		this.separator = separator;
		this.nested = true;
		this.openingParenthesis = openingParentesis;
		this.closingParenthesis = closingParenthesis;
		this.escaped = false;
	}

	protected StringSequence(String string, char separator, char openingParentesis, char closingParenthesis,
		   char escapeChar) {
		super(Sequence.UNKNOWN);
		this.string = string;
		this.separator = separator;
		this.nested = true;
		this.openingParenthesis = openingParentesis;
		this.closingParenthesis = closingParenthesis;
		this.escaped = true;
		this.escapeChar = escapeChar;
	}

	/**
	 * Returns a new string sequence for the default separator. Escaping is turned on for the default escape character.
	 * <p>
	 * @param string The given string
	 * @return The new string sequence
	 */
	public static StringSequence getInstance(String string) {
		return StringSequence.getInstance(string, '|', '\\');
	}

	/**
	 * Returns a new string sequence for a given separator. Escaping is turned off.
	 * <p>
	 * @param string    The given string
	 * @param separator The given separation character
	 * @return The new string sequence
	 */
	public static StringSequence getInstance(String string, char separator) {
		if (string == null) {
			throw new IllegalArgumentException();
		}
		return new StringSequence(string, separator);
	}

	/**
	 * Returns a new string sequence for a given separator. Escaping is turned on for the given escape character.
	 * <p>
	 * @param string     The given string
	 * @param separator  The separation character
	 * @param escapeChar The escape character
	 * @return The new string sequence
	 */
	public static StringSequence getInstance(String string, char separator, char escapeChar) {
		if (string == null || separator == escapeChar) {
			throw new IllegalArgumentException();
		}
		return new StringSequence(string, separator, escapeChar);
	}

	/**
	 * Returns a new string sequence for given separator and parenthesis characters. Escaping is turned off.
	 * <p>
	 * @param string             The given string
	 * @param separator          The given separation character
	 * @param openingParenthesis The given opening parenthesis
	 * @param closingParenthesis The given closing parenthesis
	 * @return The new string sequence
	 */
	public static StringSequence getInstance(String string, char separator, char openingParenthesis,
		   char closingParenthesis) {
		if (string == null || separator == openingParenthesis || separator == closingParenthesis ||
			   openingParenthesis == closingParenthesis) {
			throw new IllegalArgumentException();
		}
		return new StringSequence(string, separator, openingParenthesis, closingParenthesis);
	}

	/**
	 * Returns a new string sequence for given separator and parenthesis characters. Escaping is turned on for the given
	 * escape character.
	 * <p>
	 * @param string             The given string
	 * @param separator          The given separation character
	 * @param openingParenthesis The given opening parenthesis
	 * @param closingParenthesis The given closing parenthesis
	 * @param escapeChar         The given escape character
	 * @return The new string sequence
	 */
	public static StringSequence getInstance(String string, char separator, char openingParenthesis,
		   char closingParenthesis, char escapeChar) {
		if (string == null || separator == openingParenthesis || separator == closingParenthesis ||
			   separator == escapeChar ||
			   openingParenthesis == closingParenthesis || openingParenthesis == escapeChar ||
			   closingParenthesis == escapeChar) {
			throw new IllegalArgumentException();
		}
		return new StringSequence(string, separator, openingParenthesis, closingParenthesis, escapeChar);
	}

	private boolean isEscaped(int index) {
		return this.escaped && index > 0 && this.isEscapeChar(index - 1);
	}

	private boolean isEscapeChar(int index) {
		return this.string.charAt(index) == this.escapeChar && !this.isEscaped(index);
	}

	private boolean isSeparator(int index) {
		return this.string.charAt(index) == this.separator && !this.isEscaped(index);
	}

	private boolean isOpeningParanthesis(int index) {
		return this.string.charAt(index) == this.openingParenthesis && !this.isEscaped(index);
	}

	private boolean isClosingParanthesis(int index) {
		return this.string.charAt(index) == this.closingParenthesis && !this.isEscaped(index);
	}

	private int findNextSeparator(int index) {
		index = Math.max(index, 0);
		if (this.nested) {
			int depth = 0;
			while (index < this.string.length() && (depth > 0 || !this.isSeparator(index))) {
				if (this.isOpeningParanthesis(index)) {
					depth++;
				}
				if (this.isClosingParanthesis(index)) {
					depth--;
					if (depth < 0) {
						throw new IllegalArgumentException();
					}
				}
				index++;
			}
			if (index == this.string.length() && depth > 0) {
				throw new IllegalArgumentException();
			}
			return index;
		} else {
			while (index < this.string.length() && !this.isSeparator(index)) {
				index++;
			}
			return index;
		}
	}

	@Override
	public SequenceIterator<String> iterator() {
		return new SequenceIterator<String>() {

			private int currenIndex = -1;

			@Override
			public boolean hasNext() {
				return string.length() > 0 && this.currenIndex < string.length();
			}

			@Override
			public String abstractNext() {
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
		hash = 59 * hash + this.openingParenthesis;
		hash = 59 * hash + this.closingParenthesis;
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
		final StringSequence other = (StringSequence) obj;
		return this.string.equals(other.string) && this.separator == other.separator &&
			   this.openingParenthesis == other.openingParenthesis &&
			   this.closingParenthesis == other.closingParenthesis && this.escapeChar == other.escapeChar;
	}

	@Override
	protected String defaultToStringContent() {
		return this.string;
	}

}
