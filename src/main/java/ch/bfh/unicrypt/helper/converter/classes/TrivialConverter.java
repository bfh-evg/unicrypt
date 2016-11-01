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
package ch.bfh.unicrypt.helper.converter.classes;

import ch.bfh.unicrypt.helper.converter.abstracts.AbstractConverter;

/**
 * This generic class implements a trivial converter from a input type to the same output type {@code V}. The conversion
 * forth and back passes the values unchanged.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The input and output type
 */
public class TrivialConverter<V>
	   extends AbstractConverter<V, V> {

	private static final long serialVersionUID = 1L;

	protected TrivialConverter(Class<V> valueClass) {
		super(valueClass, valueClass);
	}

	@Override
	protected V abstractConvert(V value) {
		return value;
	}

	@Override
	protected V abstractReconvert(V value) {
		return value;
	}

	/**
	 * Creates a new trivial converter for instances of a given class of type {@code V} without indicating the class of
	 * the input and output values. For technical reasons, trivial converters created in this way are not compatible
	 * with {@link ConvertMethod}.
	 * <p>
	 * @param <V> The input and output type
	 * @return The trivial converter
	 * @see ConvertMethod
	 */
	public static <V> TrivialConverter<V> getInstance() {
		return new TrivialConverter<>(null);
	}

	/**
	 * Creates a new trivial converter for instances of a given class of type {@code V}. Indicating the class is needed
	 * in {@link ConvertMethod} for technical reasons.
	 * <p>
	 * @param <V>        The input and output type
	 * @param valueClass The class of the input and output type
	 * @return The trivial converter
	 * @see ConvertMethod
	 */
	public static <V> TrivialConverter<V> getInstance(Class<V> valueClass) {
		return new TrivialConverter<>(valueClass);
	}

}
