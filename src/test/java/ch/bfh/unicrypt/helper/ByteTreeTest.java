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
package ch.bfh.unicrypt.helper;

import ch.bfh.unicrypt.helper.array.ByteArray;
import ch.bfh.unicrypt.helper.bytetree.ByteTree;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public class ByteTreeTest {

	public static ByteTree b0 = ByteTree.getInstance(ByteArray.getInstance("00|00"));
	public static ByteTree b1 = ByteTree.getInstance(ByteArray.getInstance("12|34"));
	public static ByteTree b2 = ByteTree.getInstance(ByteArray.getInstance("56|78|90"));
	public static ByteTree b12 = ByteTree.getInstance(b1, b2);
	public static ByteTree b012 = ByteTree.getInstance(b0, b12);

	public ByteTreeTest() {
	}

	@Test
	public void testGetByteArray() {
		Assert.assertEquals(ByteArray.getInstance("01|00|00|00|02|00|00"), b0.getByteArray());
		Assert.assertEquals(ByteArray.getInstance("01|00|00|00|02|12|34"), b1.getByteArray());
		Assert.assertEquals(ByteArray.getInstance("01|00|00|00|03|56|78|90"), b2.getByteArray());
		Assert.assertEquals(ByteArray.getInstance("00|00|00|00|02|01|00|00|00|02|12|34|01|00|00|00|03|56|78|90"), b12.getByteArray());
		Assert.assertEquals(ByteArray.getInstance("00|00|00|00|02|01|00|00|00|02|00|00|00|00|00|00|02|01|00|00|00|02|12|34|01|00|00|00|03|56|78|90"), b012.getByteArray());
	}

	@Test
	public void testGetInstance_ByteArray() {
		Assert.assertEquals(b0, ByteTree.getInstanceFrom(b0.getByteArray()));
		Assert.assertEquals(b1, ByteTree.getInstanceFrom(b1.getByteArray()));
		Assert.assertEquals(b2, ByteTree.getInstanceFrom(b2.getByteArray()));
		Assert.assertEquals(b12, ByteTree.getInstanceFrom(b12.getByteArray()));
		Assert.assertEquals(b012, ByteTree.getInstanceFrom(b012.getByteArray()));
	}

	@Test
	public void testGetInstance_GetHashValue() {
		ByteArray h0 = b0.getRecursiveHashValue();
		ByteArray h12 = b12.getRecursiveHashValue();
		Assert.assertEquals(h0.concatenate(h12).getHashValue(), b012.getRecursiveHashValue());
	}

}
