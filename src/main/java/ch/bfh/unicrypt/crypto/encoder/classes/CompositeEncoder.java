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
import ch.bfh.unicrypt.math.helper.ImmutableArray;
import ch.bfh.unicrypt.math.helper.compound.Compound;
import java.util.Iterator;

public class CompositeEncoder
	   extends AbstractEncoder<Set, Element, Set, Element>
	   implements Compound<CompositeEncoder, Encoder>, Iterable<Encoder> {

	private final ImmutableArray<Encoder> encoders;

	protected CompositeEncoder(ImmutableArray<Encoder> encoders) {
		this.encoders = encoders;
	}

	@Override
	public Encoder[] getAll() {
		return this.encoders.getAll();
	}

	@Override
	public int getArity() {
		return this.encoders.getLength();
	}

	@Override
	public Encoder getAt(int index) {
		return this.encoders.getAt(index);
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
		return this.encoders.getFirst();
	}

	@Override
	public boolean isNull() {
		return this.encoders.isEmpty();
	}

	@Override
	public boolean isUniform() {
		return this.encoders.isUniform();
	}

	@Override
	public CompositeEncoder removeAt(int index) {
		return new CompositeEncoder(this.encoders.removeAt(index));
	}

	@Override
	public CompositeEncoder insertAt(int index, Encoder encoder) {
		return new CompositeEncoder(this.encoders.insertAt(index, encoder));
	}

	@Override
	public CompositeEncoder add(Encoder encoder) {
		return new CompositeEncoder(this.encoders.add(encoder));
	}

	@Override
	public Iterator<Encoder> iterator() {
		return this.encoders.iterator();
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		int arity = this.getArity();
		Function[] encodingFunctions = new Function[arity];
		for (int i = 0; i < arity; i++) {
			encodingFunctions[i] = this.encoders.getAt(i).getEncodingFunction();
		}
		return CompositeFunction.getInstance(encodingFunctions);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		int arity = this.getArity();
		Function[] decodingFunctions = new Function[arity];
		for (int i = 0; i < arity; i++) {
			decodingFunctions[arity - i - 1] = this.encoders.getAt(i).getDecodingFunction();
		}
		return CompositeFunction.getInstance(decodingFunctions);
	}

	public static CompositeEncoder getInstance(ImmutableArray<Encoder> encoders) {
		if (encoders == null || encoders.getLength() == 0) {
			throw new IllegalArgumentException();
		}
		return new CompositeEncoder(encoders);
	}

	public static CompositeEncoder getInstance(Encoder... encoders) {
		return CompositeEncoder.getInstance(ImmutableArray.getInstance(encoders));
	}

	public static CompositeEncoder getInstance(Encoder encoder, int arity) {
		return CompositeEncoder.getInstance(ImmutableArray.getInstance(encoder, arity));
	}

}
