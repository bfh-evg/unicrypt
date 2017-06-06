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
package ch.bfh.unicrypt.crypto.schemes.signature;

import ch.bfh.unicrypt.crypto.schemes.signature.classes.SchnorrSignatureScheme;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.helper.prime.SafePrime;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author R. Haenni <rolf.haenni@bfh.ch>
 */
public class SchnorrSignatureSchemeTest {

	public SchnorrSignatureSchemeTest() {
	}

	@Test
	public void testSignVerify1() {
		GStarModSafePrime g_q = GStarModSafePrime.getInstance(23);
		GStarModElement g = g_q.getElement(4);

		SchnorrSignatureScheme<StringMonoid> schnorr = SchnorrSignatureScheme.getInstance(StringMonoid.getInstance(Alphabet.BASE64), g);

		Pair keyPair = schnorr.getKeyPairGenerator().generateKeyPair();
		Element privateKey = keyPair.getFirst();
		Element publicKey = keyPair.getSecond();

		StringElement message = schnorr.getMessageSpace().getElement("Message");
		Element randomization = schnorr.getRandomizationSpace().getRandomElement();

		Pair signature = schnorr.sign(privateKey, message, randomization);

		BooleanElement result = schnorr.verify(publicKey, message, signature);
		Assert.assertTrue(result.getValue());

	}

	@Test
	public void testSignVerify2() {
		GStarModSafePrime g_q = GStarModSafePrime.getInstance(SafePrime.getRandomInstance(128));
		GStarModElement g = g_q.getDefaultGenerator();

		SchnorrSignatureScheme<StringMonoid> schnorr = SchnorrSignatureScheme.getInstance(StringMonoid.getInstance(Alphabet.BASE64), g);

		Pair keyPair = schnorr.getKeyPairGenerator().generateKeyPair();
		Element privateKey = keyPair.getFirst();
		Element publicKey = keyPair.getSecond();

		StringElement message = schnorr.getMessageSpace().getElement("Message");
		Element randomization = schnorr.getRandomizationSpace().getRandomElement();

		Pair signature = schnorr.sign(privateKey, message, randomization);

		BooleanElement result = schnorr.verify(publicKey, message, signature);
		Assert.assertTrue(result.getValue());

		// Generate and verify the 100 first pairs from the signature space
		for (Tuple falseSignature : schnorr.getSignatureSpace().getElements().limit(100)) {
			result = schnorr.verify(publicKey, message, falseSignature);
			Assert.assertFalse(result.getValue());
		}

	}

}
