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
package ch.bfh.unicrypt.crypto.schemes.blinding.abstracts;

public abstract class AbstractBlindingScheme {
//import java.util.Random;
//
//import ch.bfh.unicrypt.crypto.schemes.blinding.interfaces.BlindingScheme;
//import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
//import ch.bfh.unicrypt.math.function.interfaces.Function;
//
//public abstract class AbstractBlindingScheme implements BlindingScheme {
//
//  Group blindingSpace;
//  Group blindingValueSpace;
//  Function blindingFunction;
//  Function unblindingFunction;
//
//  protected AbstractBlindingScheme(final Group blindingSpace, Group blindingValueSpace) {
//    if (blindingSpace == null || blindingValueSpace == null) {
//      throw new IllegalArgumentException();
//    }
//    this.blindingSpace = blindingSpace;
//    this.blindingValueSpace = blindingValueSpace;
//  }
//
//  @Override
//  public Element blind(final Element value) {
//    return this.blind(value, (Random) null);
//  }
//
//  @Override
//  public Element blind(final Element value, final Random random) {
//    return this.blind(value, this.getBlindingValueSpace().getRandomElement(random));
//  }
//
//  @Override
//  public Element blind(final Element value, final Element blindingValue) {
//    return this.getBlindingFunction().apply(value, blindingValue);
//  }
//
//  @Override
//  public Element unblind(final Element blindedValue, final Element blindingValue) {
//    return this.getUnblindingFunction().apply(blindedValue, blindingValue);
//  }
//
//  @Override
//  public Group getBlindingSpace() {
//    return this.blindingSpace;
//  }
//
//  @Override
//  public Group getBlindingValueSpace() {
//    return this.blindingValueSpace;
//  }
//
//  @Override
//  public Function getBlindingFunction() {
//    return this.blindingFunction;
//  }
//
//  @Override
//  public Function getBlindingFunction(final Element value) {
//    return this.getBlindingFunction().partiallyApply(value, 0);
//  }
//
//  @Override
//  public Function getUnblindingFunction() {
//    return this.unblindingFunction;
//  }
//
//  @Override
//  public Function getUnblindingFunction(final Element value) {
//    return this.getUnblindingFunction().partiallyApply(value, 0);
//  }

}
