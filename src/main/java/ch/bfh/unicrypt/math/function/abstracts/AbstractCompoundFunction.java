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
package ch.bfh.unicrypt.math.function.abstracts;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.ImmutableArray;
import ch.bfh.unicrypt.math.helper.compound.Compound;
import java.util.Iterator;

/**
 *
 * @param <CF>
 * @param <F>
 * @param <D>
 * @param <DE>
 * @param <C>
 * @param <CE>
 * @author rolfhaenni
 */
public abstract class AbstractCompoundFunction<CF extends AbstractCompoundFunction<CF, D, DE, C, CE>, D extends Set, DE extends Element, C extends Set, CE extends Element>
	   extends AbstractFunction<D, DE, C, CE>
	   implements Compound<CF, Function>, Iterable<Function> {

	private final ImmutableArray<Function> functions;

	protected AbstractCompoundFunction(D domain, C coDomain, ImmutableArray<Function> functions) {
		super(domain, coDomain);
		this.functions = functions;
	}

	@Override
	public int getArity() {
		return this.functions.getLength();
	}

	@Override
	public final boolean isNull() {
		return this.functions.isEmpty();
	}

	@Override
	public final boolean isUniform() {
		return this.functions.isUniform();
	}

	@Override
	public Function getFirst() {
		return this.functions.getFirst();
	}

	@Override
	public Function getAt(int index) {
		return this.functions.getAt(index);
	}

	@Override
	public Function getAt(int... indices) {
		if (indices == null) {
			throw new IllegalArgumentException();
		}
		Function function = this;
		for (final int index : indices) {
			if (function.isCompound()) {
				function = ((Compound<CF, Function>) function).getAt(index);
			} else {
				throw new IllegalArgumentException();
			}
		}
		return function;
	}

	@Override
	public Function[] getAll() {
		return this.functions.getAll();
	}

	@Override
	public CF removeAt(final int index) {
		return this.abstractGetInstance(this.functions.removeAt(index));
	}

	@Override
	public CF insertAt(int index, Function function) {
		return this.abstractGetInstance(this.functions.insertAt(index, function));
	}

	@Override
	public CF add(Function function) {
		return this.abstractGetInstance(this.functions.add(function));
	}

	@Override
	public Iterator<Function> iterator() {
		return this.functions.iterator();
	}

	@Override
	protected boolean standardIsCompound() {
		return true;
	}

	@Override
	protected boolean standardIsEquivalent(Function function) {
		AbstractCompoundFunction<CF, D, DE, C, CE> other = (AbstractCompoundFunction<CF, D, DE, C, CE>) function;
		if (this.getArity() != other.getArity()) {
			return false;
		}
		for (int i = 0; i < this.getArity(); i++) {
			if (!this.getAt(i).isEquivalent(other.getAt(i))) {
				return false;
			}
		}
		return true;
	}

	protected abstract CF abstractGetInstance(ImmutableArray<Function> functions);

}
