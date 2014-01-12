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
import ch.bfh.unicrypt.keygen.interfaces.DDHGroupKeyPairGenerator;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;

public class ElGamalExampleHeavy {
  public static void main(final String[] args) {
    final GStarSave g_q = new GStarSaveClass(BigInteger.valueOf(23));
    final ElGamalEncryptionClass ecs = new ElGamalEncryptionClass(g_q);
    final DDHGroupKeyPairGenerator keyGen = ecs.getKeyGenerator();

    final AtomicElement message = ecs.getPlaintextSpace().createElement(BigInteger.valueOf(9));

    final TupleElement keyPair = keyGen.generateKeyPair();
    final AtomicElement randomization = ecs.getRandomizationSpace().createElement(BigInteger.valueOf(7));
    final TupleElement cipherText = ecs.encrypt(keyGen.getPublicKey(keyPair), message, randomization);
    System.out.println(keyPair);
    System.out.println(cipherText);

    final Element newMessage = ecs.decrypt(keyGen.getPrivateKey(keyPair), cipherText);
    System.out.println(newMessage);

    final AtomicElement reRandomization = ecs.getRandomizationSpace().createElement(BigInteger.valueOf(3));
    final TupleElement reEncryptedCipherText = ecs.reEncrypt(keyGen.getPublicKey(keyPair), cipherText, reRandomization);
    final Element reEncMessage = ecs.decrypt(keyGen.getPrivateKey(keyPair), reEncryptedCipherText);
    System.out.println("ciphertext: " + cipherText);
    System.out.println("reEnc text: " + reEncryptedCipherText);
    System.out.println(reEncMessage);

  }
}
