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
package ch.bfh.unicrypt.crypto.schemes.hashing;

import ch.bfh.unicrypt.crypto.schemes.scheme.abstracts.AbstractScheme;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.hash.HashMethod;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FixedByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.HashFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SharedDomainFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class HashingScheme<MS extends Set>
	   extends AbstractScheme<MS> {

	private final ConvertMethod convertMethod;
	private final HashMethod hashMethod;
	protected FixedByteArraySet hashSpace;
	protected Function hashFunction;
	protected Function checkFunction;

	protected HashingScheme(MS messageSpace, ConvertMethod convertMethod, HashMethod hashMethod) {
		super(messageSpace);
		this.convertMethod = convertMethod;
		this.hashMethod = hashMethod;
	}

	public final ConvertMethod getConvertMethod() {
		return this.convertMethod;
	}

	public final HashMethod getHashMethod() {
		return this.hashMethod;
	}

	public final FixedByteArraySet getHashSpace() {
		if (this.hashSpace == null) {
			this.hashSpace = FixedByteArraySet.getInstance(hashMethod.getHashAlgorithm().getByteLength());
		}
		return this.hashSpace;
	}

	public final Function getHashFunction() {
		if (this.hashFunction == null) {
			this.hashFunction = HashFunction.getInstance(this.messageSpace, this.convertMethod, this.hashMethod);
		}
		return this.hashFunction;
	}

	public final Function getCheckFunction() {
		if (this.checkFunction == null) {
			ProductSet checkDomain = ProductSet.getInstance(this.messageSpace, this.getHashSpace());
			this.checkFunction = CompositeFunction.getInstance(
				   SharedDomainFunction.getInstance(
						  CompositeFunction.getInstance(
								 SelectionFunction.getInstance(checkDomain, 0),
								 this.getHashFunction()),
						  SelectionFunction.getInstance(checkDomain, 1)),
				   EqualityFunction.getInstance(this.getHashSpace()));
		}
		return this.checkFunction;
	}

	public final FiniteByteArrayElement hash(Element message) {
		return (FiniteByteArrayElement) this.getHashFunction().apply(message);
	}

	public final BooleanElement check(Element message, FiniteByteArrayElement hashValue) {
		return (BooleanElement) this.getCheckFunction().apply(message, hashValue);
	}

	public static HashingScheme<ByteArrayMonoid> getInstance() {
		return HashingScheme.getInstance(ByteArrayMonoid.getInstance());
	}

	public static <MS extends Set> HashingScheme<MS> getInstance(MS messageSpace) {
		return HashingScheme.getInstance(messageSpace, ConvertMethod.getInstance(), HashMethod.getInstance());
	}

	public static HashingScheme<ByteArrayMonoid> getInstance(ConvertMethod<ByteArrayMonoid> convertMethod,
		   HashMethod<ByteArrayMonoid> hashMethod) {
		return HashingScheme.getInstance(ByteArrayMonoid.getInstance(), convertMethod, hashMethod);
	}

	public static <MS extends Set, W> HashingScheme<MS> getInstance(MS messageSpace, ConvertMethod<W> convertMethod,
		   HashMethod<W> hashMethod) {
		if (messageSpace == null || convertMethod == null || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new HashingScheme(messageSpace, convertMethod, hashMethod);
	}

}
