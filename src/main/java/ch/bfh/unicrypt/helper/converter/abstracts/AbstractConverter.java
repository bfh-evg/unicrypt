/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.helper.converter.abstracts;

import ch.bfh.unicrypt.helper.UniCrypt;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;

/**
 * This abstract class serves as a base implementation of the {@link Converter} interface.
 * <p>
 * @author Rolf Haenni
 * @version 2.0
 * @param <V> The input type
 * @param <W> The output type
 */
public abstract class AbstractConverter<V extends Object, W extends Object>
	   extends UniCrypt
	   implements Converter<V, W> {

	private final Class<V> inputClass;
	private final Class<W> outputClass;

	protected AbstractConverter(Class<V> inputClass, Class<W> outputClass) {
		this.inputClass = inputClass;
		this.outputClass = outputClass;
	}

	@Override
	public Class<V> getInputClass() {
		return this.inputClass;
	}

	@Override
	public Class<W> getOutputClass() {
		return this.outputClass;
	}

	@Override
	public final W convert(V value) {
		if (this.isValidInput(value)) {
			return this.abstractConvert(value);
		}
		throw new IllegalArgumentException();
	}

	@Override
	public final V reconvert(W value) {
		if (this.isValidOutput(value)) {
			return this.abstractReconvert(value);
		}
		throw new IllegalArgumentException();
	}

	@Override
	public final boolean isValidInput(V value) {
		return (value != null) && this.defaultIsValidInput(value);
	}

	@Override
	public final boolean isValidOutput(W value) {
		return (value != null) && this.defaultIsValidOutput(value);
	}

	protected boolean defaultIsValidInput(V value) {
		return true;
	}

	protected boolean defaultIsValidOutput(W value) {
		return true;
	}

	protected abstract W abstractConvert(V value);

	protected abstract V abstractReconvert(W value);

}
