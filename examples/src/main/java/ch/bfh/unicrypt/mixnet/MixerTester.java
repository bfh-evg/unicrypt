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
package ch.bfh.unicrypt.mixnet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.unicrypt.blinding.classes.BlindingSchemeClass;
import ch.bfh.unicrypt.blinding.interfaces.BlindingScheme;
import ch.bfh.unicrypt.encryption.classes.ElGamalEncryptionClass;
import ch.bfh.unicrypt.encryption.interfaces.RandomizedAsymmetricHomomorphicEncryptionScheme;
import ch.bfh.unicrypt.keygen.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.PermutationElement;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.interfaces.DDHGroup;
import ch.bfh.unicrypt.mixnet.classes.BlindingMixerClass;
import ch.bfh.unicrypt.mixnet.classes.ReEncryptionMixerClass;
import ch.bfh.unicrypt.mixnet.interfaces.BlindingMixer;
import ch.bfh.unicrypt.mixnet.interfaces.ReEncryptionMixer;

public class MixerTester {

  public static void main(final String[] args) {

    final DDHGroup ddhGroup = new GStarSaveClass(BigInteger.valueOf(23));
    final RandomizedAsymmetricHomomorphicEncryptionScheme elGamal = new ElGamalEncryptionClass(ddhGroup);

    final KeyPairGenerator keygen = elGamal.getKeyGenerator();
    final TupleElement keyPair = keygen.generateKeyPair();
    final Element privateKey = keygen.getPrivateKey(keyPair);
    final Element publicKey = keygen.getPublicKey(keyPair);

    final List<Element> encryptions = new ArrayList<Element>();
    Element message;
    for (int i = 1; i <= 10; i++) {
      message = elGamal.getPlaintextSpace().createRandomElement();
      System.out.println(message);
      encryptions.add(elGamal.encrypt(publicKey, message));
    }
    System.out.println(encryptions);

    final ReEncryptionMixer mixer = new ReEncryptionMixerClass(elGamal, publicKey);
    final PermutationElement permutation = mixer.getPermutationSpace(encryptions.size()).createRandomElement();
    System.out.println(permutation);

    final List<Element> mixedEncryptions = mixer.shuffle(encryptions, permutation);
    System.out.println(mixedEncryptions);

    Element plaintext;
    for (final Element encryption : mixedEncryptions) {
      plaintext = elGamal.decrypt(privateKey, encryption);
      System.out.println(plaintext);
    }

    final BlindingScheme blindingScheme = new BlindingSchemeClass(elGamal.getCiphertextSpace());
    final AdditiveElement blindingFactor = blindingScheme.getBlindingValueSpace().createElement(2);
    final BlindingMixer mixer2 = new BlindingMixerClass(blindingScheme);
    final List<Element> squaredEncryptions = mixer2.shuffle(mixedEncryptions, permutation.invert(), blindingFactor);
    System.out.println(squaredEncryptions);

    for (final Element encryption : squaredEncryptions) {
      plaintext = elGamal.decrypt(privateKey, encryption);
      System.out.println(plaintext);
    }
  }

}
