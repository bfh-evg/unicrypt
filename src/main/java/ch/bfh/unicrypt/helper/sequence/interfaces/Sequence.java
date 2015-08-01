/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographic framework allowing the implementation of cryptographic protocols, e.g. e-voting
 *  Copyright (C) 2015 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.helper.sequence.interfaces;

import ch.bfh.unicrypt.helper.array.interfaces.ImmutableArray;
import java.math.BigInteger;

/**
 * This interface represents the concept of an iterable sequence of values similar to streams in Java 8. No means are
 * provided to directly access or manipulate the values. Computational operations can be described declaratively as a
 * pipeline. The execution of the pipeline is lazy. The length of a sequence is either finite and known, finite but
 * unknown, or infinite.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The generic type of the values contained in the sequence
 */
public interface Sequence<V>
	   extends Iterable<V> {

	public static final BigInteger INFINITE = BigInteger.valueOf(-1);
	public static final BigInteger UNKNOWN = BigInteger.valueOf(-2);

	public boolean isEmpty();

	public BigInteger getLength();

	public long count();

	public long count(Predicate<? super V> predicate);

	public V find();

	public V find(long n);

	public V find(Predicate<? super V> predicate);

	public V find(Predicate<? super V> predicate, long n);

	public <W> Sequence<W> map(Mapping<? super V, ? extends W> mapping);

	public Sequence<V> filter(Predicate<? super V> predicate);

	public Sequence<V> shorten(long maxLength);

	public Sequence<V> shorten(BigInteger maxLength);

	public Sequence<V> skip();

	public Sequence<V> skip(long n);

	public Sequence<? extends ImmutableArray<V>> group(long groupLength);

}
