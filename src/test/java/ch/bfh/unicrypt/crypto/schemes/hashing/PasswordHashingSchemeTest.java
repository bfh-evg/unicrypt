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

import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.hash.HashAlgorithm;
import ch.bfh.unicrypt.helper.hash.HashMethod;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import org.junit.Assert;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author rolfhaenni
 */
public class PasswordHashingSchemeTest {

	@Test
	public void testPasswordHashingScheme() {

		PasswordHashingScheme phs1 = PasswordHashingScheme.getInstance(StringMonoid.getInstance());
		PasswordHashingScheme phs2 = PasswordHashingScheme.getInstance(StringMonoid.getInstance(), HashMethod.getInstance(HashAlgorithm.SHA1));

		StringElement se1 = StringElement.getInstance("");
		StringElement se2 = StringElement.getInstance("test");

		ByteArrayElement be1 = ByteArrayElement.getInstance(ByteArray.getInstance());
		ByteArrayElement be2 = ByteArrayElement.getInstance(ByteArray.getInstance(0));
		ByteArrayElement be3 = ByteArrayElement.getInstance(ByteArray.getInstance(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));

		for (PasswordHashingScheme phs : new PasswordHashingScheme[]{phs1, phs2}) {
			for (StringElement se : new StringElement[]{se1, se2}) {
				for (ByteArrayElement be : new ByteArrayElement[]{be1, be2, be3}) {
					FiniteByteArrayElement hash = phs.hash(se, be);
					assertTrue(phs.check(se, be, hash).getValue());
					for (PasswordHashingScheme phs0 : new PasswordHashingScheme[]{phs1, phs2}) {
						for (StringElement se0 : new StringElement[]{se1, se2}) {
							for (ByteArrayElement be0 : new ByteArrayElement[]{be1, be2, be3}) {
								FiniteByteArrayElement hash0 = phs0.hash(se0, be0);
								if (phs != phs0 || se != se0 || be != be0) {
									Assert.assertNotEquals(hash, hash0);
								}
							}
						}
					}
				}

			}

		}

	}

}
