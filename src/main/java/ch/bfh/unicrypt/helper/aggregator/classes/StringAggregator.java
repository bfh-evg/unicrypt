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
package ch.bfh.unicrypt.helper.aggregator.classes;

import ch.bfh.unicrypt.helper.aggregator.abstracts.AbstractAggregator;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.sequence.StringSequence;

/**
 * Instance of this class specify the invertible aggregation of a tree of strings. The aggregation of a node consists in
 * separating the aggregated strings obtained from the children with separator character and surrounding them by an
 * opening and closing parenthesis. Corresponding characters can be chosen freely (the default characters are
 * {@code '|'}, {@code '['}, and {@code ']'}, respectively). To avoid conflicts with the strings contained in the
 * leaves, an escape character needs to be specified (the default escape character is {@code '\'}). The aggregation of a
 * leaf consists in adding the escape character to each conflict and surrounding the string by quotes (the default quote
 * character is {@code '"'}).
 * <p>
 * @author R. Haenni
 * @version 2.0
 */
public class StringAggregator
	   extends AbstractAggregator<String> {

	private static StringAggregator defaultInstance = null;
	private static final long serialVersionUID = 1L;

	private final char quoteCharacter;
	private final char openingParenthesis;
	private final char closingParenthesis;
	private final char separator;
	private final char escapeCharacter;

	private StringAggregator(char quoteCharacter, char openingParenthesis, char closingParenthesis, char separator,
		   char escapeCharacter) {
		this.quoteCharacter = quoteCharacter;
		this.openingParenthesis = openingParenthesis;
		this.closingParenthesis = closingParenthesis;
		this.separator = separator;
		this.escapeCharacter = escapeCharacter;
	}

	/**
	 * Returns the default instance of this class with the default quote, separator, parenthesis, and escape characters.
	 * <p>
	 * @return The default instance of this class
	 */
	public static StringAggregator getInstance() {
		if (StringAggregator.defaultInstance == null) {
			StringAggregator.defaultInstance = new StringAggregator('"', '[', ']', '|', '\\');
		}
		return StringAggregator.defaultInstance;
	}

	/**
	 * Returns a new instance of this class for given quote, separator, parenthesis, and escape characters.
	 * <p>
	 * @param quoteChar          The quote character
	 * @param openingParenthesis The opening parenthesis
	 * @param closingParenthesis The closing parenthesis
	 * @param separator          The separator character
	 * @param escapeCharacter    The escape character
	 * @return The new instance of this class
	 */
	public static StringAggregator getInstance(char quoteChar, char openingParenthesis, char closingParenthesis,
		   char separator, char escapeCharacter) {
		if (quoteChar == openingParenthesis || quoteChar == closingParenthesis || quoteChar == separator
			   || quoteChar == escapeCharacter
			   || escapeCharacter == openingParenthesis || escapeCharacter == closingParenthesis
			   || escapeCharacter == separator
			   || openingParenthesis == closingParenthesis || openingParenthesis == separator
			   || closingParenthesis == separator) {
			throw new IllegalArgumentException();
		}
		return new StringAggregator(quoteChar, openingParenthesis, closingParenthesis, separator, escapeCharacter);
	}

	@Override
	protected String abstractAggregateLeaf(String value) {
		// escape character must be escaped first!
		value = this.escape(value, this.escapeCharacter);
		value = this.escape(value, this.quoteCharacter);
		value = this.escape(value, this.openingParenthesis);
		value = this.escape(value, this.closingParenthesis);
		value = this.escape(value, this.separator);
		return this.quoteCharacter + value + this.quoteCharacter;
	}

	@Override
	protected String abstractAggregateNode(Sequence<String> values) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.openingParenthesis);
		for (String value : values) {
			sb.append(value);
			sb.append(this.separator);
		}
		if (sb.length() > 1) {
			sb.deleteCharAt(sb.length() - 1);
		}
		sb.append(this.closingParenthesis);
		return sb.toString();
	}

	@Override
	protected boolean abstractIsLeaf(String value) {
		return value.length() >= 2 && value.charAt(0) == this.quoteCharacter
			   && value.charAt(value.length() - 1) == this.quoteCharacter;
	}

	@Override
	protected boolean abstractIsNode(String value) {
		return value.length() >= 2 && value.charAt(0) == this.openingParenthesis
			   && value.charAt(value.length() - 1) == this.closingParenthesis;
	}

	@Override
	protected String abstractDisaggregateLeaf(String value) {
		value = value.substring(1, value.length() - 1);
		// escape character must be de-escaped last!
		value = this.invertEscape(value, this.quoteCharacter);
		value = this.invertEscape(value, this.openingParenthesis);
		value = this.invertEscape(value, this.closingParenthesis);
		value = this.invertEscape(value, this.separator);
		value = this.invertEscape(value, this.escapeCharacter);
		return value;
	}

	@Override
	protected Sequence<String> abstractDisaggregateNode(String value) {
		value = value.substring(1, value.length() - 1);
		return StringSequence.getInstance(value, this.separator, this.openingParenthesis, this.closingParenthesis,
										  this.escapeCharacter);
	}

	private String escape(String str, char c) {
		return str.replace("" + c, "" + this.escapeCharacter + c);
	}

	private String invertEscape(String str, char c) {
		return str.replace("" + this.escapeCharacter + c, "" + c);
	}

}
