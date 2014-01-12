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
package ch.bfh.unicrypt.encryption;

import java.math.BigInteger;

import ch.bfh.unicrypt.encryption.classes.ElGamalEncryptionClass;
import ch.bfh.unicrypt.keygen.interfaces.DDHGroupDistributedKeyPairGenerator;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;

public class MultiPartyElGamalExample {
  public static void main(final String[] args) {
    final GStarSave g_q = new GStarSaveClass(BigInteger.valueOf(23));
    final ElGamalEncryptionClass ecs = new ElGamalEncryptionClass(g_q);
    final DDHGroupDistributedKeyPairGenerator keyGen = ecs.getKeyGenerator();

    final AtomicElement message = ecs.getPlaintextSpace().createElement(BigInteger.valueOf(9));

    final TupleElement keyPair1 = keyGen.generateKeyPair();
    System.out.println(keyPair1);
    final TupleElement keyPair2 = keyGen.generateKeyPair();
    System.out.println(keyPair2);
    final TupleElement keyPair3 = keyGen.generateKeyPair();
    System.out.println(keyPair3);
    final AtomicElement publicKey = keyGen.combinePublicKeys(keyGen.getPublicKey(keyPair1), keyGen.getPublicKey(keyPair2), keyGen.getPublicKey(keyPair3));

    final AtomicElement randomization = ecs.getRandomizationSpace().createElement(BigInteger.valueOf(7));
    final TupleElement cipherText = ecs.encrypt(publicKey, message, randomization);

    final Element partialMessage1 = ecs.partialDecrypt(keyGen.getPrivateKey(keyPair1), cipherText);
    final Element partialMessage2 = ecs.partialDecrypt(keyGen.getPrivateKey(keyPair2), cipherText);
    final Element partialMessage3 = ecs.partialDecrypt(keyGen.getPrivateKey(keyPair3), cipherText);

    final Element newMessage = ecs.combinePartialDecryptions(cipherText, partialMessage1, partialMessage2, partialMessage3);
    System.out.println(message);
    System.out.println(newMessage);

  }
}
