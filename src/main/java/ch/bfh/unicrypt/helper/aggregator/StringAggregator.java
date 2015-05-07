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
package ch.bfh.unicrypt.helper.aggregator;

import ch.bfh.unicrypt.helper.array.classes.DenseArray;
import ch.bfh.unicrypt.helper.iterable.IterableString;
import java.util.regex.Matcher;

/**
 *
 * @author rolfhaenni
 */
public class StringAggregator
	   extends Aggregator<String> {

	private final char escapeCharacter;
	private final char openingParenthesis;
	private final char closingParenthesis;
	private final char separator;

	private StringAggregator(char escapeCharacter, char openingParenthesis, char closingParenthesis, char separator) {
		this.escapeCharacter = escapeCharacter;
		this.openingParenthesis = openingParenthesis;
		this.closingParenthesis = closingParenthesis;
		this.separator = separator;
	}

	public static StringAggregator getInstance() {
		return new StringAggregator('\\', '[', ']', '|');
	}

	public static StringAggregator getInstance(char escapeCharacter, char openingParenthesis, char closingParenthesis, char separator) {
		if (escapeCharacter == openingParenthesis || escapeCharacter == closingParenthesis || escapeCharacter == separator
			   || openingParenthesis == closingParenthesis || openingParenthesis == separator || closingParenthesis == separator) {
			throw new IllegalArgumentException();
		}
		return new StringAggregator(escapeCharacter, openingParenthesis, closingParenthesis, separator);
	}

	@Override
	public String abstractAggregate(String value) {
		// escape character must be escaped first!
		value = this.escape(value, this.escapeCharacter);
		value = this.escape(value, this.openingParenthesis);
		value = this.escape(value, this.closingParenthesis);
		value = this.escape(value, this.separator);
		return value;
	}

	@Override
	public String abstractAggregate(Iterable<String> values, int length) {
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
	protected boolean abstractIsSingle(String value) {
		return value.length() == 0 || value.charAt(0) != this.openingParenthesis;
	}

	@Override
	protected String abstractDisaggregateSingle(String value) {
		// escape character must be de-escaped last!
		value = this.invertEscape(value, this.separator);
		value = this.invertEscape(value, this.closingParenthesis);
		value = this.invertEscape(value, this.openingParenthesis);
		value = this.invertEscape(value, this.escapeCharacter);
		return value;
	}

	@Override
	protected Iterable<String> abstractDisaggregateMultiple(String value) {
		if (value.charAt(value.length() - 1) != this.closingParenthesis) {
			throw new IllegalArgumentException();
		}
		value = value.substring(1, value.length() - 1);
		return IterableString.getInstance(value, this.separator, this.escapeCharacter);
	}

	private String escape(String str, char c) {
		return str.replaceAll("\\" + c, Matcher.quoteReplacement("" + this.escapeCharacter + c));
	}

	private String invertEscape(String str, char c) {
		return str.replaceAll("\\" + this.escapeCharacter + c, Matcher.quoteReplacement("" + c));
	}

	public static void main(String[] args) {
		StringAggregator sa1 = StringAggregator.getInstance();
		System.out.println(sa1.aggregate("Hello\\[]|>()/"));
		System.out.println(sa1.aggregate(DenseArray.getInstance("Hallo", "Velo")));
		StringAggregator sa2 = StringAggregator.getInstance('>', '(', ')', '/');
		System.out.println(sa2.aggregate("Hello\\[]|>()/"));
		System.out.println(sa2.aggregate(DenseArray.getInstance("Hallo", "Velo")));
		SingleOrMultiple<String> result = sa2.disaggregate(sa2.aggregate("Hallo", "Velo"));
		for (String str : result.getValues()) {
			System.out.println(str);
		}
	}

}
