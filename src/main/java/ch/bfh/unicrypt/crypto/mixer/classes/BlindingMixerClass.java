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

public class BlindingMixerClass {
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Random;
//
//import ch.bfh.unicrypt.crypto.schemes.blinding.interfaces.BlindingScheme;
//import ch.bfh.unicrypt.crypto.mixer.interfaces.BlindingMixer;
//import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
//import ch.bfh.unicrypt.math.algebra.concatenative.classes.PermutationElement;
//
//public class BlindingMixerClass extends MixerClass implements BlindingMixer {
//
//  BlindingScheme blindingScheme;
//
//  public BlindingMixerClass(final BlindingScheme blindingScheme) {
//    super(blindingScheme.getBlindingSpace());
//    this.blindingScheme = blindingScheme;
//  }
//
//  @Override
//  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation) {
//    return this.shuffle(elements, permutation, (Random) null);
//  }
//
//  @Override
//  public List<Element> shuffle(final List<Element> elements, final AdditiveElement blindingFactor) {
//    return this.shuffle(elements, blindingFactor, (Random) null);
//  }
//
//  @Override
//  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation, final Random random) {
//    final AdditiveElement blindingFactor = this.getBlindingScheme().getBlindingValueSpace().getRandomElement(random);
//    return this.shuffle(elements, permutation, blindingFactor);
//  }
//
//  @Override
//  public List<Element> shuffle(final List<Element> elements, final AdditiveElement blindingValue, final Random random) {
//    if (elements == null) {
//      throw new IllegalArgumentException();
//    }
//    final PermutationElement permutation = this.getPermutationSpace(elements.size()).getRandomElement(random);
//    return this.shuffle(elements, permutation, blindingValue);
//  }
//
//  @Override
//  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation, final AdditiveElement blindingValue) {
//    if (elements == null) {
//      throw new IllegalArgumentException();
//    }
//    final List<Element> blindedElements = new ArrayList<Element>();
//    for (final Element element : elements) {
//      blindedElements.add(this.getBlindingScheme().blind(element, blindingValue));
//    }
//    return super.shuffle(blindedElements, permutation);
//  }
//
//  @Override
//  public BlindingScheme getBlindingScheme() {
//    return this.blindingScheme;
//  }
//
}
