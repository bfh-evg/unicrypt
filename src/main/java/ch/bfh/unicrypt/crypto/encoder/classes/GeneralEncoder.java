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
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.helper.converter.classes.TrivialConverter;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;

/**
 *
 * @author rolfhaenni
 */
public class GeneralEncoder
	   extends AbstractEncoder<Set, Element, Set, Element> {

	private final Set domain;
	private final Set coDomain;
	private final Converter converter;

	private GeneralEncoder(Set domain, Set coDomain, Converter converter) {
		this.domain = domain;
		this.coDomain = coDomain;
		this.converter = converter;
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new AbstractFunction(this.domain, this.coDomain) {

			@Override
			protected Element abstractApply(Element element, RandomByteSequence randomByteSequence) {
				Object value = element.convertTo(converter);
				if (coDomain.contains(value)) {
					return coDomain.getElement(value);
				}
				throw new IllegalArgumentException();
			}
		};
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new AbstractFunction(this.coDomain, this.domain) {

			@Override
			protected Element abstractApply(Element element, RandomByteSequence randomByteSequence) {
				Object value = element.convertTo(converter.invert());
				if (domain.contains(value)) {
					return domain.getElement(value);
				}
				throw new IllegalArgumentException();
			}
		};
	}

	public static <V> GeneralEncoder getInstance(Set<V> domain, Set<V> coDomain) {
		if (domain == null | coDomain == null) {
			throw new IllegalArgumentException();
		}
		return new GeneralEncoder(domain, coDomain, TrivialConverter.<V>getInstance());
	}

	public static <V, W> GeneralEncoder getInstance(Set<V> domain, Set<W> coDomain, Converter<V, W> converter) {
		if (domain == null | coDomain == null || converter == null) {
			throw new IllegalArgumentException();
		}
		return new GeneralEncoder(domain, coDomain, converter);
	}

	public static void main(String[] args) {
		ZMod zmod = ZMod.getInstance(10);
		StringMonoid monoid = StringMonoid.getInstance(Alphabet.PRINTABLE_ASCII);

		System.out.println(monoid.getElement("11111").convertToString());
		System.out.println(monoid.getElementFrom(monoid.getElement("11111").convertToString()));

		System.out.println(monoid.getElement("11111").convertToBigInteger());
		System.out.println(monoid.getElementFrom(monoid.getElement("11111").convertToBigInteger()));

		System.out.println(monoid.getElement("11111").convertToByteArray());
		System.out.println(monoid.getElementFrom(monoid.getElement("11111").convertToByteArray()));

		Encoder encoder = GeneralByteArrayEncoder.getInstance(zmod, monoid);

		System.out.println(encoder.encode(zmod.getElement(9)));
		System.out.println(encoder.decode(encoder.encode(zmod.getElement(9))));

	}

}
