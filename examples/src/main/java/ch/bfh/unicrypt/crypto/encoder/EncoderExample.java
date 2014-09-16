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
package ch.bfh.unicrypt.crypto.encoder;

import java.math.BigInteger;

import ch.bfh.unicrypt.Example;
import ch.bfh.unicrypt.crypto.encoder.classes.CompositeEncoder;
import ch.bfh.unicrypt.crypto.encoder.classes.FiniteStringToZModEncoder;
import ch.bfh.unicrypt.crypto.encoder.classes.ProbabilisticECGroupF2mEncoder;
import ch.bfh.unicrypt.crypto.encoder.classes.ZModToGStarModSafePrimeEncoder;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.StandardECPolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.algebra.params.classes.SECECCParamsF2m;
import ch.bfh.unicrypt.helper.Alphabet;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class EncoderExample {

	public static void example1() {

		// Define underlying groups
		GStarModSafePrime group = GStarModSafePrime.getRandomInstance(64);
		ZMod zMod = group.getZModOrder();

		// Create encoders
		Encoder encoder1 = FiniteStringToZModEncoder.getInstance(zMod, Alphabet.LOWER_CASE);
		Encoder encoder2 = ZModToGStarModSafePrimeEncoder.getInstance(group);

		// Create composite encoder
		Encoder encoder12 = CompositeEncoder.getInstance(encoder1, encoder2);

		// Encode and decode message
		Element message = encoder12.getDomain().getElement("hello");
		Element encodedMessage = encoder12.encode(message);
		Element decodedMessage = encoder12.decode(encodedMessage);

		Example.printLines("Groups", group, zMod);
		Example.printLines("Encoders", encoder1, encoder2, encoder12);
		Example.printLines("Messages", message, encodedMessage, decodedMessage);
	}
	
	/**
	 * Example shows how to encode an element from ZMod into an elliptic curve over F2m
	 */
	public static void example2() {
		
		// Define underlying groups
		StandardECPolynomialField ecf2m = StandardECPolynomialField
				.getInstance(SECECCParamsF2m.sect113r1);
		ZMod z=ZMod.getInstance(ecf2m.getOrder());
		
		// Create encoders
		Encoder encoder1 = FiniteStringToZModEncoder.getInstance(z, Alphabet.LOWER_CASE);
		ProbabilisticECGroupF2mEncoder encoder2=ProbabilisticECGroupF2mEncoder.getInstance(z, ecf2m);
		
		// Create composite encoder
		Encoder encoder12 = CompositeEncoder.getInstance(encoder1, encoder2);
		
		// Encode and decode message
		Element message = encoder12.getDomain().getElement("hello");
		Element encodedMessage = encoder12.encode(message);
		Element decodedMessage = encoder12.decode(encodedMessage);

		Example.printLines("Groups", ecf2m, z);
		Example.printLines("Encoders", encoder1, encoder2, encoder12);
		Example.printLines("Messages", message, encodedMessage, decodedMessage);
		
	}

	public static void main(final String[] args) {
		Example.runExamples();
	}

}
