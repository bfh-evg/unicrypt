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
package ch.bfh.unicrypt.crypto.mixer.classes;

public class ReEncryptionMixerClass {
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.ReEncryptionScheme;
//import ch.bfh.unicrypt.crypto.mixer.interfaces.ReEncryptionMixer;
//import ch.bfh.unicrypt.math.algebra.concatenative.classes.PermutationElement;
//import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
//
//public class ReEncryptionMixerClass extends MixerClass implements ReEncryptionMixer {
//
//  ReEncryptionScheme encryptionScheme;
//  Element publicKey;
//
//  public ReEncryptionMixerClass(final ReEncryptionScheme encryptionScheme, final Element publicKey) {
//    super(encryptionScheme.getEncryptionSpace());
//    this.encryptionScheme = encryptionScheme;
//    this.publicKey = publicKey;
//  }
//
//  @Override
//  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation) {
//    return this.shuffle(elements, permutation, (Random) null);
//  }
//
//  @Override
//  public List<Element> shuffle(final List<Element> elements, final List<Element> randomizations) {
//    return this.shuffle(elements, randomizations, (Random) null);
//  }
//
//  @Override
//  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation, final Random random) {
//    if (elements == null) {
//      throw new IllegalArgumentException();
//    }
//    final List<Element> randomizations = new ArrayList<Element>();
//    for (int i = 1; i <= elements.size(); i++) {
//      randomizations.add(this.getRandomizationSpace().getRandomElement(random));
//    }
//    return this.shuffle(elements, permutation, randomizations);
//  }
//
//  @Override
//  public List<Element> shuffle(final List<Element> elements, final List<Element> randomizations, final Random random) {
//    if (elements == null) {
//      throw new IllegalArgumentException();
//    }
//    final PermutationElement permutation = this.getPermutationSpace(elements.size()).getRandomElement(random);
//    return this.shuffle(elements, permutation, randomizations);
//  }
//
//  @Override
//  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation, final List<Element> randomizations) {
//    if ((elements == null) || (randomizations == null) || (elements.size() != randomizations.size())) {
//      throw new IllegalArgumentException();
//    }
//    final List<Element> reEncryptions = new ArrayList<Element>();
//    Element randomization;
//    final int i = 0;
//    for (final Element encryption : elements) {
//      randomization = randomizations.get(i);
//      reEncryptions.add(this.getEncryptionScheme().reEncrypt(this.getPublicKey(), encryption, randomization));
//    }
//    return super.shuffle(reEncryptions, permutation);
//  }
//
//  @Override
//  public ReEncryptionScheme getEncryptionScheme() {
//    return this.encryptionScheme;
//  }
//
//  @Override
//  public Group getRandomizationSpace() {
//    return this.getEncryptionScheme().getRandomizationSpace();
//  }
//
//  @Override
//  public Element getPublicKey() {
//    return this.publicKey;
//  }
//
}
