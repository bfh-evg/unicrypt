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

public class MixerClass {
//import java.util.Arrays;
//import java.util.List;
//import java.util.Random;
//
//import ch.bfh.unicrypt.crypto.mixer.interfaces.Mixer;
//import ch.bfh.unicrypt.math.algebra.concatenative.classes.PermutationElement;
//import ch.bfh.unicrypt.math.algebra.concatenative.classes.PermutationGroup;
//import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
//import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
//import ch.bfh.unicrypt.math.function.classes.PermutationFunction;
//
//public class MixerClass implements Mixer {
//
//  private final Group group;
//
//  public MixerClass(final Group group) {
//    if (group == null) {
//      throw new IllegalArgumentException();
//    }
//    this.group = group;
//  }
//
//  @Override
//  public List<Element> shuffle(final List<Element> elements) {
//    return this.shuffle(elements, (Random) null);
//  }
//
//  @Override
//  public List<Element> shuffle(final List<Element> elements, final Random random) {
//    if (elements == null) {
//      throw new IllegalArgumentException();
//    }
//    final PermutationElement permutation = this.getPermutationSpace(elements.size()).getRandomElement(random);
//    return this.shuffle(elements, permutation);
//  }
//
//  @Override
//  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation) {
//    if ((elements == null) || (permutation == null)) {
//      throw new IllegalArgumentException();
//    }
//    final int size = elements.size();
//    final PermutationFunction function = new PermutationFunction(new PowerGroup(this.getGroup(), size));
//    final PowerGroup powerGroup = (PowerGroup) function.getDomain().getGroupAt(0);
//    final Tuple tuple = powerGroup.getElement(elements.toArray(new Element[size]));
//    final Tuple result = function.apply(tuple, permutation);
//    return Arrays.asList(result.getElements());
//  }
//
//  @Override
//  public PermutationGroup getPermutationSpace(final int size) {
//    return new PermutationGroup(size);
//  }
//
//  @Override
//  public Group getGroup() {
//    return this.group;
//  }
//
}
