package ch.bfh.unicrypt.helper;

import ch.bfh.unicrypt.Example;
import ch.bfh.unicrypt.helper.array.ByteArray;
import ch.bfh.unicrypt.helper.converter.ConverterMap;
import ch.bfh.unicrypt.helper.converter.ResidueClassConverter;
import ch.bfh.unicrypt.helper.numerical.NaturalNumber;
import ch.bfh.unicrypt.helper.numerical.ResidueClass;
import ch.bfh.unicrypt.helper.numerical.WholeNumber;
import java.math.BigInteger;

/*
 * UniCrypt
 *
 * UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 * Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for Security in the Information
 * Society (RISIS), E-Voting Group (EVG) Quellgasse 21, CH-2501 Biel, Switzerland
 *
 * Licensed under Dual License consisting of: 1. GNU Affero General Public License (AGPL) v3 and 2. Commercial license
 *
 *
 * 1. This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General
 * Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program. If not, see
 * <http://www.gnu.org/licenses/>.
 *
 *
 * 2. Licensees holding valid commercial licenses for UniCrypt may use this file in accordance with the commercial
 * license agreement provided with the Software or, alternatively, in accordance with the terms contained in a written
 * agreement between you and Bern University of Applied Sciences (BFH), Research Institute for Security in the
 * Information Society (RISIS), E-Voting Group (EVG) Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 * For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ConverterMapExample {

	public static void example1() {
		ConverterMap cMap = ConverterMap.getInstance();
		Example.printLine(cMap.convertToByteArray(ByteArray.getInstance(10, true)));
		Example.printLine(cMap.convertToByteArray(BigInteger.valueOf(123456)));
		Example.printLine(cMap.convertToByteArray(NaturalNumber.getInstance(BigInteger.valueOf(123))));
		Example.printLine(cMap.convertToByteArray(Permutation.getInstance(10)));
		Example.printLine(cMap.convertToByteArray(ResidueClass.getInstance(BigInteger.valueOf(123), BigInteger.valueOf(1234))));
		Example.printLine(cMap.convertToByteArray("Hallo"));
		Example.printLine(cMap.convertToByteArray(WholeNumber.getInstance(BigInteger.valueOf(-123))));

		cMap.addConverter(ResidueClassConverter.getInstance());
		Example.printLine(cMap.convertToByteArray(ResidueClass.getInstance(BigInteger.valueOf(123), BigInteger.valueOf(1234))));

	}

	public static void main(final String[] args) {
		Example.runExamples();
	}

}
