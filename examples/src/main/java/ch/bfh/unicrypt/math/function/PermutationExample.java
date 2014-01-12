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
package ch.bfh.unicrypt.math.function;

import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.PermutationElement;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.function.classes.PermutationFunctionClass;
import ch.bfh.unicrypt.math.function.interfaces.PermutationFunction;
import ch.bfh.unicrypt.math.group.classes.PowerGroupClass;
import ch.bfh.unicrypt.math.group.classes.ZPlusClass;
import ch.bfh.unicrypt.math.group.interfaces.PowerGroup;
import ch.bfh.unicrypt.math.group.interfaces.ZPlus;

public class PermutationExample {

  public static void main(final String[] args) {

    final ZPlus zPlus = ZPlusClass.getInstance();
    PowerGroup zPlusPower = new PowerGroupClass(zPlus, 6);

    PermutationFunction permutationFunction = new PermutationFunctionClass(zPlusPower);

    final AdditiveElement e0 = zPlus.createElement(0);
    final AdditiveElement e1 = zPlus.createElement(1);
    final AdditiveElement e2 = zPlus.createElement(2);
    final AdditiveElement e3 = zPlus.createElement(3);
    final AdditiveElement e4 = zPlus.createElement(4);
    final AdditiveElement e5 = zPlus.createElement(5);

    TupleElement tuple = zPlusPower.createElement(e0, e1, e2, e3, e4, e5);
    PermutationElement permutation = permutationFunction.getPermutationGroup().createRandomElement();
    TupleElement result = permutationFunction.apply(tuple, permutation);
    System.out.println(permutation);
    System.out.println(tuple);
    System.out.println(result);

    zPlusPower = new PowerGroupClass(zPlus, 0);
    permutationFunction = new PermutationFunctionClass(zPlusPower);
    tuple = zPlusPower.createElement();
    permutation = permutationFunction.getPermutationGroup().createRandomElement();
    result = permutationFunction.apply(tuple, permutation);
    System.out.println(permutation);
    System.out.println(tuple);
    System.out.println(result);
  }

}
