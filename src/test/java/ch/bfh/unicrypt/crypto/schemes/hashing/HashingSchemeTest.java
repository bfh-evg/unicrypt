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
package ch.bfh.unicrypt.crypto.schemes.hashing;

import ch.bfh.unicrypt.UniCryptException;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.math.MathUtil;
import ch.bfh.unicrypt.helper.random.deterministic.DeterministicRandomByteSequence;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.Z;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class HashingSchemeTest {

	@Test
	public void testHashingScheme1() {

		HashingScheme<ByteArrayMonoid> hs = HashingScheme.getInstance();

		ByteArrayElement e1 = ByteArrayElement.getInstance(ByteArray.getInstance());
		ByteArrayElement e2 = ByteArrayElement.getInstance(ByteArray.getInstance(true, 10));
		ByteArrayElement e3 = ByteArrayElement.getInstance(ByteArray.getRandomInstance(100));

		for (ByteArrayElement e : Sequence.getInstance(e1, e2, e3)) {
			FiniteByteArrayElement hash = hs.hash(e);
			assertTrue(hs.check(e, hash).getValue());
			for (ByteArrayElement e0 : Sequence.getInstance(e1, e2, e3)) {
				FiniteByteArrayElement hash0 = hs.hash(e0);
				if (e == e0) {
					assertEquals(hash, hash0);
				} else {
					assertNotEquals(hash, hash0);
				}
			}
		}
	}

	@Test
	public void testHashingScheme2() {

		HashingScheme<Z> hs = HashingScheme.getInstance(Z.getInstance());

		ZElement e1 = ZElement.getInstance(0);
		ZElement e2 = ZElement.getInstance(5);
		ZElement e3 = ZElement.getInstance(-5);

		for (ZElement e : Sequence.getInstance(e1, e2, e3)) {
			FiniteByteArrayElement hash = hs.hash(e);
			assertTrue(hs.check(e, hash).getValue());
			for (ZElement e0 : Sequence.getInstance(e1, e2, e3)) {
				FiniteByteArrayElement hash0 = hs.hash(e0);
				if (e == e0) {
					assertEquals(hash, hash0);
				} else {
					assertNotEquals(hash, hash0);
				}
			}
		}
	}

	@Test
	public void testHashingScheme3() {

		HashingScheme<ProductGroup> hs = HashingScheme.getInstance(ProductGroup.getInstance(ZMod.getInstance(25), 3));

		Tuple e1;
		try {
			e1 = hs.getMessageSpace().getElementFrom(MathUtil.ZERO, MathUtil.ONE, MathUtil.TWO);
		} catch (UniCryptException ex) {
			fail();
			return;
		}
		Tuple e2 = hs.getMessageSpace().getIdentityElement();
		Tuple e3 = hs.getMessageSpace().getRandomElement(DeterministicRandomByteSequence.getInstance());

		for (Tuple e : Sequence.getInstance(e1, e2, e3)) {
			FiniteByteArrayElement hash = hs.hash(e);
			assertTrue(hs.check(e, hash).getValue());
			for (Tuple e0 : Sequence.getInstance(e1, e2, e3)) {
				FiniteByteArrayElement hash0 = hs.hash(e0);
				if (e == e0) {
					assertEquals(hash, hash0);
				} else {
					assertNotEquals(hash, hash0);
				}
			}
		}
	}

}
