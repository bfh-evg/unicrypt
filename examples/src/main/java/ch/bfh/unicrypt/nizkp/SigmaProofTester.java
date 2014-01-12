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
package ch.bfh.unicrypt.nizkp;

import java.math.BigInteger;

import ch.bfh.unicrypt.blinding.classes.BlindingSchemeClass;
import ch.bfh.unicrypt.blinding.interfaces.BlindingScheme;
import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.function.classes.PartiallyAppliedFunctionClass;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunctionClass;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.function.interfaces.PartiallyAppliedFunction;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.interfaces.AtomicGroup;
import ch.bfh.unicrypt.math.group.interfaces.DDHGroup;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import ch.bfh.unicrypt.nizkp.classes.SigmaProofGeneratorClass;
import ch.bfh.unicrypt.nizkp.interfaces.SigmaProofGenerator;

public class SigmaProofTester {

  public static void main(final String[] args) {

    // EXAMPLE 1
    final DDHGroup ddhGroup = new GStarSaveClass(BigInteger.valueOf(23));
    final ZPlusMod orderGroup = ddhGroup.getOrderGroup();
    final Function function = new SelfApplyFunctionClass(ddhGroup, orderGroup);
    final Element generator = ddhGroup.getDefaultGenerator();
    final PartiallyAppliedFunction homoFunction = new PartiallyAppliedFunctionClass(function, generator, 0);
    final Element secretInput = homoFunction.getDomain().createElement(orderGroup.createElement(4));
    final Element publicInput = homoFunction.apply(secretInput);
    final SigmaProofGenerator proofgen = new SigmaProofGeneratorClass(homoFunction);
    final TupleElement proof = proofgen.generate(secretInput, publicInput);
    System.out.println(proof);
    if (proofgen.verify(proof, publicInput)) {
      System.out.println("Proof OK");
    } else {
      System.out.println("Proof failed");
    }

    // ab und zu wird der Proof zufallig richtig (ca. jdes 10te mal, wie
    // erwartet)
    if (proofgen.verify(proof, homoFunction.getCoDomain().createRandomElement())) {
      System.out.println("Proof OK");
    } else {
      System.out.println("Proof failed");
    }

    // EXAMPLE 2
    final BlindingScheme bs = new BlindingSchemeClass(ddhGroup);
    final Element value = ((AtomicGroup) bs.getBlindingSpace()).createElement(2);
    final AdditiveElement blindingValue = bs.getBlindingValueSpace().createElement(3);
    final Element blindedValue = bs.blind(value, blindingValue);

    final Function homoFunction2 = bs.getBlindingFunction(value);
    final SigmaProofGenerator proofgen2 = new SigmaProofGeneratorClass(homoFunction2);
    final TupleElement proof2 = proofgen2.generate(blindingValue, blindedValue);
    System.out.println(proof2);
    if (proofgen2.verify(proof2, blindedValue)) {
      System.out.println("Proof OK");
    } else {
      System.out.println("Proof failed");
    }
    // ab und zu wird der Proof zufallig richtig (ca. jdes 10te mal, wie
    // erwartet)
    if (proofgen.verify(proof, homoFunction2.getCoDomain().createRandomElement())) {
      System.out.println("Proof OK");
    } else {
      System.out.println("Proof failed");
    }

  }

}
