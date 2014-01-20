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
package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.compound.Compound;
import ch.bfh.unicrypt.math.helper.compound.CompoundIterator;
import java.util.Iterator;

public class CompositeEncoder
			 extends AbstractEncoder<Set, Element, Set, Element>
			 implements Compound<CompositeEncoder, Encoder>, Iterable<Encoder> {

	private final Encoder[] encoders;

	protected CompositeEncoder(Encoder[] encoders) {
		this.encoders = encoders;
	}

	@Override
	public Encoder[] getAll() {
		return this.encoders.clone();
	}

	@Override
	public int getArity() {
		return this.encoders.length;
	}

	@Override
	public Encoder getAt(int index) {
		if (index < 0 || index >= this.getArity()) {
			throw new IndexOutOfBoundsException();
		}
		return this.encoders[index];
	}

	@Override
	public Encoder getAt(int... indices) {
		if (indices == null) {
			throw new IllegalArgumentException();
		}
		Encoder encoder = this;
		for (final int index : indices) {
			if (encoder instanceof CompositeEncoder) {
				encoder = ((CompositeEncoder) encoder).getAt(index);
			} else {
				throw new IllegalArgumentException();
			}
		}
		return encoder;
	}

	@Override
	public Encoder getFirst() {
		return this.getAt(0);
	}

	@Override
	public boolean isNull() {
		return this.getArity() == 0;
	}

	@Override
	public boolean isUniform() {
		return this.encoders.length <= 1;
	}

	@Override
	public CompositeEncoder removeAt(int index) {
		int arity = this.getArity();
		if (index < 0 || index >= arity) {
			throw new IndexOutOfBoundsException();
		}
		final Encoder[] remaining = new Encoder[arity - 1];
		for (int i = 0; i < arity - 1; i++) {
			if (i < index) {
				remaining[i] = this.getAt(i);
			} else {
				remaining[i] = this.getAt(i + 1);
			}
		}
		return CompositeEncoder.getInstance(remaining);
	}

	@Override
	public Iterator<Encoder> iterator() {
		return new CompoundIterator<Encoder>(this);
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		int length = this.encoders.length;
		Function[] encodingFunctions = new Function[length];
		for (int i = 0; i < length; i++) {
			encodingFunctions[i] = this.encoders[i].getEncodingFunction();
		}
		return CompositeFunction.getInstance(encodingFunctions);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		int length = this.encoders.length;
		Function[] decodingFunctions = new Function[length];
		for (int i = 0; i < length; i++) {
			decodingFunctions[length - i - 1] = this.encoders[i].getDecodingFunction();
		}
		return CompositeFunction.getInstance(decodingFunctions);
	}

	public static CompositeEncoder getInstance(Encoder... encoders) {
		if (encoders == null || encoders.length == 0) {
			throw new IllegalArgumentException();
		}
		for (Encoder encoder : encoders) {
			if (encoder == null) {
				throw new IllegalArgumentException();
			}
		}
		return new CompositeEncoder(encoders);
	}

}
