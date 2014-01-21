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
package ch.bfh.unicrypt.crypto.schemes.commitment;

import ch.bfh.unicrypt.crypto.schemes.commitment.classes.PedersenCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class PedersenCommitmentExample {

    public static void main(String[] args) {

	PedersenCommitmentScheme pcs = PedersenCommitmentScheme.getInstance(GStarModSafePrime.getInstance(23));

	Element message = pcs.getMessageSpace().getElement(2);
	Element randomization = pcs.getRandomizationSpace().getElement(1);
	Element commitment = pcs.commit(message, randomization);
	System.out.println(commitment);

	{
	    Element result = pcs.decommit(message, randomization, commitment);
	    System.out.println(result);
	}
	{
	    Element wrongMessage = pcs.getMessageSpace().getElement(7);
	    Element result = pcs.decommit(wrongMessage, randomization, commitment);
	    System.out.println(result);
	}

    }
}
