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
package ch.bfh.unicrypt;

import java.io.PrintStream;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class Example {

	private static final PrintStream OUT = System.out;
	private static final String LABEL_SEP = ": ";
	private static final String ITEM_SEP = ", ";

	public static void print(Object object) {
		OUT.print(object);
	}

	public static void printLine() {
		OUT.println();
	}

	public static void printTitle(String title) {
		Example.printLine(title);
		for (int i = 0; i < title.length(); i++) {
			Example.print("-");
		}
		Example.printLine();
	}

	public static void printLine(Object object) {
		Example.print(object);
		Example.printLine();
	}

	public static void printLine(String label, Object object) {
		Example.print(label + Example.LABEL_SEP);
		Example.printLine(object);
	}

	public static void printLine(Object... objects) {
		String sep = "";
		for (Object object : objects) {
			Example.print(sep + object);
			sep = Example.ITEM_SEP;
		}
		Example.printLine();
	}

	public static void printLine(String label, Object... objects) {
		Example.print(label + Example.LABEL_SEP);
		Example.printLine(objects);
	}

	public static void printLines(Object... objects) {
		for (Object object : objects) {
			Example.printLine(object);
		}
	}

	public static void printLines(String label, Object... objects) {
		Example.printLine(label + Example.LABEL_SEP);
		Example.printLines(objects);
	}

}
