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
package ch.bfh.unicrypt.random.abstracts;

import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.random.classes.ReferenceRandomByteSequence;
import ch.bfh.unicrypt.random.interfaces.RandomOracle;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractRandomOracle
	   extends UniCrypt
	   implements RandomOracle {

	private static final long serialVersionUID = 1L;

	private final Converter<BigInteger, ByteArray> bigIntegerConverter;
	private final Converter<String, ByteArray> stringConverter;

	protected AbstractRandomOracle(Converter<BigInteger, ByteArray> bigIntegerConverter, Converter<String, ByteArray> stringConverter) {
		this.bigIntegerConverter = bigIntegerConverter;
		this.stringConverter = stringConverter;
	}

	@Override
	public final ReferenceRandomByteSequence query() {
		return this.query(ReferenceRandomByteSequence.DEFAULT_SEED);
	}

	@Override
	public final ReferenceRandomByteSequence query(long input) {
		return this.query(BigInteger.valueOf(input));
	}

	@Override
	public final ReferenceRandomByteSequence query(BigInteger input) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		return this.query(this.bigIntegerConverter.convert(input));
	}

	@Override
	public final ReferenceRandomByteSequence query(String input) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		return this.query(this.stringConverter.convert(input));
	}

	@Override
	public final ReferenceRandomByteSequence query(ByteArray input) {
		if (input == null) {
			throw new IllegalArgumentException();
		}
		return this.abstractQuery(input);
	}

	protected abstract ReferenceRandomByteSequence abstractQuery(ByteArray query);

}
