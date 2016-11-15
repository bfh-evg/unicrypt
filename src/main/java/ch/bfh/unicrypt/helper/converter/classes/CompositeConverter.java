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
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;

/**
 * This generic class connects two existing converters into a single converter. For this, the output type of the first
 * and the input type of the second converter must match. An input value to a composite converter is first processed by
 * the first and then by the second converter. In the reverse process, the value is first processed by the second and
 * then by the first converter.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The input type (of the first converter)
 * @param <X> The common output/input type of the two converters
 * @param <W> The output type (of the second converter)
 */
public class CompositeConverter<V, X, W>
	   extends AbstractConverter<V, W> {

	// the first converter
	private final Converter<V, X> converter1;

	// the second converter
	private final Converter<X, W> converter2;

	protected CompositeConverter(Converter<V, X> converter1, Converter<X, W> converter2) {
		super(converter1.getInputClass(), converter2.getOutputClass());
		this.converter1 = converter1;
		this.converter2 = converter2;
	}

	/**
	 * This method construct a new composite converter from two existing converter.
	 * <p>
	 * @param <V>        The input type (of the first converter)
	 * @param <X>        The common output/input type of the two converters
	 * @param <W>        The output type (of the second converter)
	 * @param converter1 The first converter
	 * @param converter2 The second converter
	 * @return The new composite converter
	 */
	public static <V, X, W> CompositeConverter<V, X, W> getInstance(Converter<V, X> converter1,
		   Converter<X, W> converter2) {
		if (converter1 == null || converter2 == null) {
			throw new IllegalArgumentException();
		}
		return new CompositeConverter(converter1, converter2);
	}

	/**
	 * Returns the first of the two converters.
	 * <p>
	 * @return The first converter
	 */
	public Converter<V, X> getFirstConverter() {
		return this.converter1;
	}

	/**
	 * Returns the second of the two converters.
	 * <p>
	 * @return The second converter
	 */
	public Converter<X, W> getSecondConverter() {
		return this.converter2;
	}

	@Override
	protected boolean defaultIsValidInput(V value) {
		// incomplete, but more efficient test
		return this.converter1.isValidInput(value);
	}

	@Override
	protected boolean defaultIsValidOutput(W value) {
		// incomplete, but more efficient test
		return this.converter2.isValidOutput(value);
	}

	@Override
	protected W abstractConvert(V value) {
		return this.converter2.convert(this.converter1.convert(value));
	}

	@Override
	protected V abstractReconvert(W value) {
		return this.converter1.reconvert(this.converter2.reconvert(value));
	}

}
