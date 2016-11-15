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
package ch.bfh.unicrypt.helper.sequence.functions;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author rolfhaenni
 */
public class MappingTest {

	public MappingTest() {
	}

	@Test
	public void testCompose() {

		Mapping<Integer, String> map1 = new Mapping<Integer, String>() {

			@Override
			public String apply(Integer value) {
				return "" + value;
			}
		};
		Mapping<String, Integer> map2 = new Mapping<String, Integer>() {

			@Override
			public Integer apply(String value) {
				return value.length();
			}
		};

		Mapping<Integer, Integer> map12 = map1.compose(map2);
		Mapping<String, String> map21 = map2.compose(map1);

		assertEquals(1, (int) map12.apply(5));
		assertEquals(2, (int) map12.apply(15));
		assertEquals(3, (int) map12.apply(-15));
		assertEquals("4", map21.apply("1234"));
		assertEquals("5", map21.apply("-1234"));

	}

}
