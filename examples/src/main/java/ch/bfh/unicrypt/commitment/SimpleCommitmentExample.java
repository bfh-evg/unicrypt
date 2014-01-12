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

import ch.bfh.unicrypt.commitment.classes.SimpleCommitmentSchemeClass;
import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.classes.ZPlusModClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;

public class SimpleCommitmentExample {
  public static void main(final String[] args) {
    final GStarSave g_q = new GStarSaveClass(BigInteger.valueOf(23));
    final SimpleCommitmentSchemeClass pcs = new SimpleCommitmentSchemeClass(g_q);

    final AdditiveElement message = pcs.getMessageSpace().createElement(BigInteger.valueOf(9));
    final AtomicElement commitment = pcs.commit(message);
    System.out.println(commitment);
    {
      // true
      final boolean result = pcs.open(message, commitment);
      System.out.println(result);

    }
    {
      // true (testing for equivalent message-group)
      final ZPlusMod orderGroup = g_q.getOrderGroup();
      final AdditiveElement message_new = orderGroup.createElement(BigInteger.valueOf(9));
      final boolean result = pcs.open(message_new, commitment);
      System.out.println(result);
    }
    {
      // false (testing for wrong message)
      final AdditiveElement wrongMessage = pcs.getMessageSpace().createElement(BigInteger.valueOf(2));
      final boolean result = pcs.open(wrongMessage, commitment);
      System.out.println(result);
    }
    {
      // Illegal Argument (testing for wrong message-space)
      final ZPlusMod z_qnew = new ZPlusModClass(g_q.getOrder().add(BigInteger.ONE));
      final AdditiveElement message_new = z_qnew.createElement(BigInteger.valueOf(9));
      final boolean result = pcs.open(message_new, commitment);
      System.out.println(result);

    }

  }
}
