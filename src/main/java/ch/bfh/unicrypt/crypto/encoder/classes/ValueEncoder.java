/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographic framework allowing the implementation of cryptographic protocols, e.g. e-voting
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
package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.helper.converter.classes.TrivialConverter;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;

/**
 *
 * @author rolfhaenni
 */
public class ValueEncoder
	   extends AbstractEncoder<Set, Element, Set, Element> {

	private final Set domain;
	private final Set coDomain;
	private final Converter converter1;
	private final Converter converter2;

	public enum DefaultConverter {

		BIG_INTEGER, STRING, BYTE_ARRAY

	};

	private ValueEncoder(Set domain, Set coDomain, Converter converter1, Converter converter2) {
		this.domain = domain;
		this.coDomain = coDomain;
		this.converter1 = converter1;
		this.converter2 = converter2;
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new AbstractFunction(this.domain, this.coDomain) {

			@Override
			protected Element abstractApply(Element element, RandomByteSequence randomByteSequence) {
				Object value = element.convertTo(converter1);
				Element result = coDomain.getElementFrom(value, converter2);
				if (result == null) {
					throw new IllegalArgumentException();
				}
				return result;
			}
		};
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new AbstractFunction(this.coDomain, this.domain) {

			@Override
			protected Element abstractApply(Element element, RandomByteSequence randomByteSequence) {
				Object value = element.convertTo(converter2);
				Element result = domain.getElementFrom(value, converter1);
				if (result == null) {
					throw new IllegalArgumentException();
				}
				return result;
			}
		};
	}

	public static ValueEncoder getInstance(Set domain, Set coDomain, DefaultConverter mode) {
		if (domain == null || coDomain == null || mode == null) {
			throw new IllegalArgumentException();
		}
		Converter converter1;
		Converter converter2;
		switch (mode) {
			case BIG_INTEGER:
				converter1 = domain.getBigIntegerConverter();
				converter2 = coDomain.getBigIntegerConverter();
				break;
			case STRING:
				converter1 = domain.getStringConverter();
				converter2 = coDomain.getStringConverter();
				break;
			case BYTE_ARRAY:
				converter1 = domain.getByteArrayConverter();
				converter2 = coDomain.getByteArrayConverter();
				break;
			default:
				throw new IllegalStateException();
		}
		return new ValueEncoder(domain, coDomain, converter1, converter2);
	}

	public static <V> ValueEncoder getInstance(Set<V> domain, Set<V> coDomain) {
		if (domain == null || coDomain == null) {
			throw new IllegalArgumentException();
		}
		return new ValueEncoder(domain, coDomain, TrivialConverter.<V>getInstance(), TrivialConverter.<V>getInstance());
	}

	public static <V, W> ValueEncoder getInstance(Set<V> domain, Set<W> coDomain, Converter<V, W> converter) {
		if (domain == null || coDomain == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return new ValueEncoder(domain, coDomain, converter, TrivialConverter.<W>getInstance());
	}

	public static <V, W, X> ValueEncoder getInstance(Set<V> domain, Set<W> coDomain, Converter<V, X> converter1, Converter<W, X> converter2) {
		if (domain == null || coDomain == null || converter1 == null || converter1 == null) {
			throw new IllegalArgumentException();
		}
		return new ValueEncoder(domain, coDomain, converter1, converter2);
	}

}
