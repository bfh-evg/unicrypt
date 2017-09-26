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
package ch.bfh.unicrypt.helper.math;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class implements the mathematical concept of an alphabet as a finite set of characters. The characters in the
 * set are numbered from {@code 0} to {@code s-1}, where {@code s} denotes the size of the set. Multiple commonly used
 * alphabets are pre-defined.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class Alphabet
	   extends UniCrypt
	   implements Iterable<Character> {

	public static final Alphabet UNARY = new Alphabet('1', '1');
	public static final Alphabet BINARY = new Alphabet('0', '1');
	public static final Alphabet OCTAL = new Alphabet('0', '7');
	public static final Alphabet DECIMAL = new Alphabet('0', '9');
	public static final Alphabet HEXADECIMAL = new Alphabet("0123456789ABCDEF");
	public static final Alphabet LOWER_CASE = new Alphabet('a', 'z');
	public static final Alphabet UPPER_CASE = new Alphabet('A', 'Z');
	public static final Alphabet LETTERS = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz");
	public static final Alphabet ALPHANUMERIC
		   = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789");
	public static final Alphabet BASE64
		   = new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\\+\\/");
	public static final Alphabet PRINTABLE_ASCII = new Alphabet('\u0020', '\u007E');
	public static final Alphabet UNICODE_BMP = new Alphabet('\u0000', '\uFFFF');
	private static final long serialVersionUID = 1L;

	private final String characters;
	private final Character minChar;
	private final Character maxChar;

	protected Alphabet(String characters) {
		this.characters = characters;
		this.minChar = null;
		this.maxChar = null;
	}

	protected Alphabet(char minChar, char maxChar) {
		this.characters = null;
		this.minChar = minChar;
		this.maxChar = maxChar;
	}

	/**
	 * Returns the default alphabet consisting of all unicode characters
	 * <p>
	 * @return The default alphabet
	 */
	public static Alphabet getInstance() {
		return Alphabet.UNICODE_BMP;
	}

	/**
	 * Creates a new alphabet containing all characters of a given string.
	 * <p>
	 * @param characters The given string
	 * @return The new alphabet
	 */
	public static Alphabet getInstance(String characters) {
		if (characters == null) {
			throw new IllegalArgumentException();
		}
		Set<Character> charSet = new HashSet<>();
		for (char c : characters.toCharArray()) {
			charSet.add(c);
		}
		if (characters.length() != charSet.size()) {
			throw new IllegalArgumentException();
		}
		return new Alphabet(characters);
	}

	/**
	 * Creates a new alphabet containing all characters within the indicated bounds, i.e., larger or equal to
	 * {@code minChar} and smaller or equal to {@code maxChar}.
	 * <p>
	 * @param minChar The lower bound
	 * @param maxChar The upper bound
	 * @return The new alphabet
	 */
	public static Alphabet getInstance(char minChar, char maxChar) {
		if (minChar > maxChar) {
			throw new IllegalArgumentException();
		}
		return new Alphabet(minChar, maxChar);
	}

	/**
	 * Returns the size of the alphabet.
	 * <p>
	 * @return The size of the alphabet
	 */
	public int getSize() {
		if (this.characters == null) {
			return this.maxChar - this.minChar + 1;
		}
		return this.characters.length();
	}

	/**
	 * Checks if a character is in the alphabet.
	 * <p>
	 * @param character The given character
	 * @return {@code true}, if {@code c} is in the alphabet, {@code false} otherwise
	 */
	public boolean contains(char character) {
		if (this.characters == null) {
			return character >= this.minChar && character <= this.maxChar;
		}
		return this.characters.lastIndexOf(character) >= 0;
	}

	/**
	 * Checks if every character of a given string is in the alphabet.
	 * <p>
	 * @param string The given string
	 * @return {@code true}, if every character from {@code string} is in the alphabet, {@code false} otherwise
	 */
	public boolean containsAll(String string) {
		if (string == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < string.length(); i++) {
			if (!this.contains(string.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the character in the alphabet with index {@code i}.
	 * <p>
	 * @param index The index
	 * @return The corresponding character
	 */
	public char getCharacter(int index) {
		if (index < 0 || index >= this.getSize()) {
			throw new UniCryptRuntimeException(ErrorCode.INVALID_INDEX, this, index);
		}
		if (this.characters == null) {
			return (char) (this.minChar + index);
		}
		return this.characters.charAt(index);
	}

	/**
	 * Returns the index of a given character
	 * <p>
	 * @param character The given character
	 * @return The corresponding index
	 */
	public int getIndex(char character) {
		if (!this.contains(character)) {
			throw new IllegalArgumentException();
		}
		if (this.characters == null) {
			return character - this.minChar;
		}
		return this.characters.lastIndexOf(character);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 41 * hash + (this.characters != null ? this.characters.hashCode() : 0);
		hash = 41 * hash + (this.minChar != null ? this.minChar.hashCode() : 0);
		hash = 41 * hash + (this.maxChar != null ? this.maxChar.hashCode() : 0);
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
		final Alphabet other = (Alphabet) obj;
		if (this.getSize() != other.getSize()) {
			return false;
		}
		for (int i = 0; i < this.getSize(); i++) {
			if (this.getCharacter(i) != other.getCharacter(i)) {
				return false;
			}
		}
		return true;
	}

	@Override
	protected String defaultToStringContent() {
		if (this.characters == null) {
			return this.minChar + "..." + this.maxChar;
		}
		return this.characters;
	}

	@Override
	public Iterator<Character> iterator() {
		return new Iterator<Character>() {

			private int currentIndex = 0;

			@Override
			public boolean hasNext() {
				return this.currentIndex < getSize();
			}

			@Override
			public Character next() {
				return getCharacter(this.currentIndex++);
			}

		};
	}

}
