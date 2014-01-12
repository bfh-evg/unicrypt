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
package ch.bfh.unicrypt.math.group;

import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.group.classes.ZPlusModClass;
import ch.bfh.unicrypt.math.group.classes.ZStarModClass;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import ch.bfh.unicrypt.math.group.interfaces.ZStarMod;
import java.math.BigInteger;

/**
 *
 * @author reto
 */
public class MultiplicationAndGroups {

	public static void main(String[] args) {
		{	//Multiplizieren in der Additiven Modularen Gruppe (Mehrfaches Addieren eines Wertes)
			ZPlusMod hundert = new ZPlusModClass(BigInteger.valueOf(100));
			AtomicElement acht = hundert.createElement(8);
			AtomicElement vieresächzg = acht.selfApply(acht);
			System.out.println(vieresächzg);
		}
		{
			//Multiplizieren in der Multiplikativen Modularen Prim-Gruppe
			ZStarMod hundert = new ZStarModClass(BigInteger.valueOf(100));
			AtomicElement siebe = hundert.createElement(7);
			AtomicElement nünevierzg = siebe.apply(siebe);
			System.out.println(nünevierzg);
		}
		{
			try{
			//Unmögliches Multiplizieren in der Multiplikativen Modularen Prim-Gruppe
			//Das verwendete Element ist nicht in der Gruppe, da es kein multiplikativ-inverses mod 100 gibt.
			ZStarMod hundert = new ZStarModClass(BigInteger.valueOf(100));
			AtomicElement acht = hundert.createElement(8);
			AtomicElement vieresächzg = acht.apply(acht);
			System.out.println(vieresächzg);
			}catch(IllegalArgumentException ex){
				//Eben... geht nicht
				ex.printStackTrace();
			}
		}

	}
}
