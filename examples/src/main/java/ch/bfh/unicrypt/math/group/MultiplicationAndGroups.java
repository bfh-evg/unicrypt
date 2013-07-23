/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
