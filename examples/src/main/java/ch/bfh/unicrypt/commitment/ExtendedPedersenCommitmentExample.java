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
package ch.bfh.unicrypt.commitment;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.unicrypt.commitment.classes.ExtendedPedersenCommitmentClass;
import ch.bfh.unicrypt.commitment.interfaces.ExtendedPedersenCommitment;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;

public class ExtendedPedersenCommitmentExample {
  public static void main(final String[] args) {
    final GStarSave g_q = new GStarSaveClass(BigInteger.valueOf(23));
    final ExtendedPedersenCommitment pcs = new ExtendedPedersenCommitmentClass(g_q, 3);

    final Element message1 = pcs.getMessageSpace().createElement(9);
    final Element message2 = pcs.getMessageSpace().createElement(2);
    final Element message3 = pcs.getMessageSpace().createElement(3);
    final Element randomization = pcs.getRandomizationSpace().createElement(7);
    final List<Element> messages = new ArrayList<Element>();
    messages.add(message1);
    messages.add(message2);
    messages.add(message3);
    final AtomicElement commitment = pcs.commit(messages, randomization);
    System.out.println(commitment);
    {
      // true
      final boolean result = pcs.open(messages, randomization, commitment);
      System.out.println(result);

    }
    {
      // false (testing for wrong message)
      final List<Element> wrongMessages = new ArrayList<Element>();
      wrongMessages.add(message1);
      wrongMessages.add(message2);
      wrongMessages.add(message2);
      final boolean result = pcs.open(wrongMessages, randomization, commitment);
      System.out.println(result);
    }

  }
}
