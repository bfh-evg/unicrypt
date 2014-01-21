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
package ch.bfh.unicrypt.math.helper;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArrayExample {

	public static void example1() {
		ByteArray byteArray = ByteArray.getInstance(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});

		System.out.println(byteArray);
		System.out.println("Length : " + byteArray.getLength());
//		for (Byte b: byteArray) {
		System.out.println("Byte 0 : " + byteArray.getByte(0));
		System.out.println("Byte 1 : " + byteArray.getByte(1));
		System.out.println("Byte 2 : " + byteArray.getByte(2));
		System.out.println("Byte 9 : " + byteArray.getByte(9));

		System.out.println("Extract: " + byteArray.extract(2, 4));

		ByteArray[] byteArrays = byteArray.split(2, 4, 7);
		System.out.println("Split:  ");
		for (ByteArray ba : byteArrays) {
			System.out.println(ba);
		}
		System.out.println("Conc: " + byteArray.concatenate(byteArray));

	}

	public static void main(final String[] args) {
		example1();
	}

}
