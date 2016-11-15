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
package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.helper.converter.classes.TrivialConverter;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.ConvertFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author R. Haenni
 */
public class ConvertEncoder
	   extends AbstractEncoder<Set, Element, Set, Element> {

	private final Set domain;
	private final Set coDomain;
	private final Converter domainConverter;
	private final Converter coDomainConverter;

	public enum Mode {

		BIG_INTEGER, STRING, BYTE_ARRAY

	};

	private ConvertEncoder(Set domain, Set coDomain, Converter domainConverter, Converter coDomainConverter) {
		this.domain = domain;
		this.coDomain = coDomain;
		this.domainConverter = domainConverter;
		this.coDomainConverter = coDomainConverter;
	}

	public static ConvertEncoder getInstance(Set domain, Set coDomain, Mode mode) {
		if (domain == null || coDomain == null || mode == null) {
			throw new IllegalArgumentException();
		}
		Converter domainConverter, coDomainConverter;
		switch (mode) {
			case BIG_INTEGER:
				domainConverter = domain.getBigIntegerConverter();
				coDomainConverter = coDomain.getBigIntegerConverter();
				break;
			case STRING:
				domainConverter = domain.getStringConverter();
				coDomainConverter = coDomain.getStringConverter();
				break;
			case BYTE_ARRAY:
				domainConverter = domain.getByteArrayConverter();
				coDomainConverter = coDomain.getByteArrayConverter();
				break;
			default:
				throw new IllegalStateException();
		}
		return new ConvertEncoder(domain, coDomain, domainConverter, coDomainConverter);
	}

	public static <V> ConvertEncoder getInstance(Set<V> domain, Set<V> coDomain) {
		if (domain == null || coDomain == null) {
			throw new IllegalArgumentException();
		}
		return new ConvertEncoder(domain, coDomain, TrivialConverter.<V>getInstance(),
								  TrivialConverter.<V>getInstance());
	}

	public static <V, W> ConvertEncoder getInstance(Set<V> domain, Set<W> coDomain, Converter<V, W> converter) {
		if (domain == null || coDomain == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return new ConvertEncoder(domain, coDomain, converter, TrivialConverter.<W>getInstance());
	}

	public static <V, W, X> ConvertEncoder getInstance(Set<V> domain, Set<W> coDomain, Converter<V, X> domainConverter,
		   Converter<W, X> coDomainConverter) {
		if (domain == null || coDomain == null || domainConverter == null || coDomainConverter == null) {
			throw new IllegalArgumentException();
		}
		return new ConvertEncoder(domain, coDomain, domainConverter, coDomainConverter);
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return ConvertFunction.getInstance(this.domain, this.coDomain, this.domainConverter, this.coDomainConverter);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return ConvertFunction.getInstance(this.coDomain, this.domain, this.coDomainConverter, this.domainConverter);
	}

	@Override
	protected String defaultToStringContent() {
		return this.domain.toString() + "," + this.coDomain.toString() + "," + this.domainConverter.toString() + ","
			   + this.coDomainConverter.toString();
	}

}
