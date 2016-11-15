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
package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.ErrorCode;
import ch.bfh.unicrypt.UniCryptRuntimeException;
import ch.bfh.unicrypt.helper.converter.classes.ConvertMethod;
import ch.bfh.unicrypt.helper.hash.HashMethod;
import ch.bfh.unicrypt.helper.random.RandomByteSequence;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FixedByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;

/**
 * This class represents the concept of a hash function, which maps an arbitrarily long input element into an element of
 * a given co-domain. The mapping itself is defined by some cryptographic hash function such as SHA-256. The co-domain
 * is always an instance of {@link FixedByteArraySet}.
 * <p>
 * @see Element#getHashValue()
 * @see Element#getHashValue(ConvertMethod, HashMethod)
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class HashFunction
	   extends AbstractFunction<HashFunction, Set, Element, FixedByteArraySet, FiniteByteArrayElement> {

	private static final long serialVersionUID = 1L;

	private final ConvertMethod convertMethod;
	private final HashMethod hashMethod;

	private HashFunction(Set domain, FixedByteArraySet coDomain, ConvertMethod convertMethod, HashMethod hashMethod) {
		super(domain, coDomain);
		this.convertMethod = convertMethod;
		this.hashMethod = hashMethod;
	}

	public ConvertMethod getConvertMethod() {
		return this.convertMethod;
	}

	public HashMethod getHashMethod() {
		return this.hashMethod;
	}

	@Override
	protected boolean defaultIsEquivalent(HashFunction other) {
		return this.getHashMethod().equals(other.getHashMethod());
	}

	@Override
	protected FiniteByteArrayElement abstractApply(final Element element, final RandomByteSequence randomByteSequence) {
		return this.getCoDomain().getElement(element.getHashValue(this.convertMethod, this.hashMethod));
	}

	/**
	 * This constructor generates a default SHA-256 hash function. The order of the co-domain is 2^256.
	 * <p>
	 * @param domain
	 * @return Returns an instance of this class
	 */
	public static HashFunction getInstance(Set domain) {
		return HashFunction.getInstance(domain, ConvertMethod.getInstance(), HashMethod.getInstance());
	}

	/**
	 * This constructor generates a default hash function for a given hash algorithm name. The co-domain is chosen
	 * accordingly.
	 * <p>
	 * @param <V>
	 * @param domain
	 * @param convertMethod
	 * @param hashMethod    The name of the hash algorithm
	 * @return Returns an instance of this class
	 */
	public static <V> HashFunction getInstance(Set domain, ConvertMethod<V> convertMethod,
		   final HashMethod<V> hashMethod) {
		if (domain == null || convertMethod == null || hashMethod == null) {
			throw new UniCryptRuntimeException(ErrorCode.NULL_POINTER, domain, convertMethod, hashMethod);
		}
		FixedByteArraySet set = FixedByteArraySet.getInstance(hashMethod.getHashAlgorithm().getByteLength());
		return new HashFunction(domain, set, convertMethod, hashMethod);
	}

}
