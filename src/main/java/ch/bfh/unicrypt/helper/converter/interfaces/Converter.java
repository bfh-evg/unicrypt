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
package ch.bfh.unicrypt.helper.converter.interfaces;

import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.tree.Tree;

/**
 * This generic interface provides two methods for converting instances of an input type {@code V} into instances of an
 * output type {@code W}, forth and back. Instances of the input type for which
 * {@link Converter#isValidInput(java.lang.Object)} returns {@code false} can not be converted. Similarly, instances of
 * the output type for which {@link Converter#isValidOutput(java.lang.Object)} returns {@code false} can not be
 * converted back.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * <p>
 * @param <V> The input type
 * @param <W> The output type
 */
public interface Converter<V, W> {

	/**
	 * Converts the given input value into an output value. An exception is thrown if the input value is invalid.
	 * <p>
	 * @param value The input value
	 * @return The output value
	 * @see Converter#isValidInput(java.lang.Object)
	 */
	public W convert(V value);

	/**
	 * Converts the given output value back into an input value. An exception is thrown if the output value is invalid.
	 * <p>
	 * @param value The input value
	 * @return The output value
	 * @see Converter#isValidOutput(java.lang.Object)
	 */
	public V reconvert(W value);

	/**
	 * Converts the given tree of input values into a tree of output values. An exception is thrown if one of the input
	 * values is invalid.
	 * <p>
	 * @param tree The tree of input values
	 * @return The tree of output values
	 */
	public Tree<W> convert(Tree<V> tree);

	/**
	 * Converts the given tree of output values back into a tree of input values. An exception is thrown if one of the
	 * output values is invalid.
	 * <p>
	 * @param tree The input value
	 * @return The output value
	 * @see Converter#isValidOutput(java.lang.Object)
	 */
	public Tree<V> reconvert(Tree<W> tree);

	/**
	 * Checks if a given input value is valid for the conversion.
	 * <p>
	 * @param value The input value
	 * @return {@code true}, if the value is valid, {@code false} otherwise
	 */
	public boolean isValidInput(V value);

	/**
	 * Checks if a given output value is valid for the re-conversion.
	 * <p>
	 * @param value The output value
	 * @return {@code true}, if the output is valid, {@code false} otherwise
	 */
	public boolean isValidOutput(W value);

	/**
	 * Returns the class of type {@code V} of the input values, or {@code null} if the class is unknown. This method is
	 * needed in {@link ConvertMethod} for technical reasons.
	 * <p>
	 * @return The input class
	 * @see ConvertMethod
	 */
	public Class<V> getInputClass();

	/**
	 * Returns the class of type {@code W} of the output values, or {@code null} if the class is unknown. This method is
	 * needed in {@link ConvertMethod} for technical reasons.
	 * <p>
	 * @return The output class
	 * @see ConvertMethod
	 */
	public Class<W> getOutputClass();

	public Converter<W, V> invert();

}
