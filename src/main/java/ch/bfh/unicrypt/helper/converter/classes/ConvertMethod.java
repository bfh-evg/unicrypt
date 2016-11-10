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

import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import java.util.HashMap;
import java.util.Map;

/**
 * Instances of this generic class provide multiple converters for the same output type {@code W}. The purpose of this
 * class is to declare the conversion into values of type {@code W} in one central place, without limiting the
 * flexibility. Note that {@link ConvertMethod} itself is not a {@link Converter}, it only allows the selection of the
 * converter to be used for instances of a specific class.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <W> The output type
 */
public class ConvertMethod<W>
	   extends UniCrypt {

	private static final long serialVersionUID = 1L;

	private final Class<W> outputClass;

	// a map for storing the converters
	private final Map<Class<?>, Converter<?, W>> converterMap;

	private ConvertMethod(Class<W> outputClass) {
		this.outputClass = outputClass;
		this.converterMap = new HashMap<>();
	}

	/**
	 * Creates a new empty converter method of output type {@code ByteArray}.
	 * <p>
	 * @return The new converter method
	 */
	public static ConvertMethod<ByteArray> getInstance() {
		return new ConvertMethod(ByteArray.class);
	}

	/**
	 * Creates a new empty converter method of output type {@code W} from a given output class of type type {@code W}.
	 * <p>
	 * @param <W>         The output type
	 * @param outputClass The class of the output values
	 * @return The new converter method
	 */
	public static <W> ConvertMethod<W> getInstance(Class<W> outputClass) {
		if (outputClass == null) {
			throw new IllegalArgumentException();
		}
		return new ConvertMethod(outputClass);
	}

	/**
	 * Creates a new converter method of output type {@code W} from a given list of converters of output type {@code W}.
	 * Each of the given converters must know the class of the input values, and these classes must be distinct.
	 * <p>
	 * @param <W>        The output type
	 * @param converters The given converters
	 * @return The new converter method
	 */
	public static <W> ConvertMethod<W> getInstance(Converter<?, W>... converters) {
		if (converters == null || converters.length == 0 || converters[0] == null || converters[0].getOutputClass() ==
			   null) {
			throw new IllegalArgumentException();
		}
		Class<W> outputClass = converters[0].getOutputClass();
		ConvertMethod convertMethod = new ConvertMethod(outputClass);
		for (Converter<?, W> converter : converters) {
			if (converter == null) {
				throw new IllegalArgumentException();
			}
			Class<?> inputClass = converter.getInputClass();
			if (inputClass == null || convertMethod.converterMap.containsKey(inputClass) || !outputClass.equals(
				   converter.getOutputClass())) {
				throw new IllegalArgumentException();
			}
			convertMethod.converterMap.put(inputClass, converter);
		}
		return convertMethod;
	}

	/**
	 * Selects and returns the converter for input values of a given class. Returns {@code null} if no such converter
	 * exists.
	 * <p>
	 * @param valueClass The class of the input values
	 * @return The corresponding converter (or {@code null} if no such converter exists)
	 */
	public Converter<?, W> getConverter(Class<?> valueClass) {
		return this.converterMap.get(valueClass);
	}

	/**
	 * Selects and returns the converter for input values of a given class. Returns the given default converter, if no
	 * suitable converter exists.
	 * <p>
	 * @param <V>              The generic type of the input class
	 * @param valueClass       The class of the input values
	 * @param defaultConverter The given default converter
	 * @return The corresponding converter (or the default converter if no such converter exists)
	 */
	public <V> Converter<V, W> getConverter(Class<V> valueClass, Converter<V, W> defaultConverter) {
		Converter<V, W> converter = (Converter<V, W>) this.getConverter(valueClass);
		if (converter == null) {
			if (defaultConverter == null) {
				throw new IllegalArgumentException();
			}
			return defaultConverter;
		}
		return converter;
	}

	/**
	 * Returns the class of type {@code W} of the output values, or {@code null} if the class is unknown. This method is
	 * needed in {@link ConvertMethod} for technical reasons.
	 * <p>
	 * @return The output class
	 * @see ConvertMethod
	 */
	public Class<W> getOutputClass() {
		return this.outputClass;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 37 * hash + (this.converterMap != null ? this.converterMap.hashCode() : 0);
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
		final ConvertMethod<?> other = (ConvertMethod<?>) obj;
		if (this.converterMap.size() != other.converterMap.size()) {
			return false;
		}
		for (Class c : this.converterMap.keySet()) {
			if (!this.getConverter(c).equals(other.getConverter(c))) {
				return false;
			}
		}
		return true;
	}

}
