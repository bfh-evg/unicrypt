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

import ch.bfh.unicrypt.crypto.schemes.signature.classes.RSASignatureScheme;
import ch.bfh.unicrypt.helper.math.Alphabet;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrimePair;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;
import java.security.SecureRandom;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Philémon von Bergen &lt;philemon.vonbergen@bfh.ch&gt;
 */
public class RSASignatureTest {

	@Test
	public void SignatureTest1() {
		BigInteger p = BigInteger.probablePrime(512, new SecureRandom());
		BigInteger q = BigInteger.probablePrime(512, new SecureRandom());
		RSASignatureScheme<ZMod> rsa = RSASignatureScheme.getInstance(ZModPrimePair.getInstance(p, q));
		Element prKey = rsa.getKeyPairGenerator().generatePrivateKey();
		Element puKey = rsa.getKeyPairGenerator().generatePublicKey(prKey);

		Element message = rsa.getMessageSpace().getElement(5);
		Element signature = rsa.sign(prKey, message);

		BigInteger n = p.multiply(q);
		rsa = RSASignatureScheme.getInstance(ZMod.getInstance(n));
		assertTrue(rsa.verify(puKey, message, signature).getValue());
		assertFalse(rsa.verify(puKey, message, message).getValue());

	}

	@Test
	public void SignatureTest2() {
		BigInteger p = BigInteger.probablePrime(512, new SecureRandom());
		BigInteger q = BigInteger.probablePrime(512, new SecureRandom());

		RSASignatureScheme<StringMonoid> rsa = RSASignatureScheme.getInstance(StringMonoid.getInstance(Alphabet.ALPHANUMERIC), ZModPrimePair.getInstance(p, q));
		Element prKey = rsa.getKeyPairGenerator().generatePrivateKey();
		Element puKey = rsa.getKeyPairGenerator().generatePublicKey(prKey);

		Element message = rsa.getMessageSpace().getElement("Hallo");
		Element signature = rsa.sign(prKey, message);

		assertTrue(rsa.verify(puKey, message, signature).getValue());
		assertFalse(rsa.verify(puKey, rsa.getMessageSpace().getElement("World"), signature).getValue());

	}

}
