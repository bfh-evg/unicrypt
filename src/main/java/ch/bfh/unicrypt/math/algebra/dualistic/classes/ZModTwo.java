/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.factorization.Prime;

/**
 *
 * @author rolfhaenni
 */
public class ZModTwo
	   extends ZModPrime {

	public static final ZModElement ZERO = ZModTwo.getInstance().getElement(0);
	public static final ZModElement ONE = ZModTwo.getInstance().getElement(1);

	protected ZModTwo() {
		super(Prime.getInstance(2));
	}

	public ZModElement xor(Element element1, Element element2) {
		return this.add(element1, element2);
	}

	public ZModElement and(Element element1, Element element2) {
		return this.multiply(element1, element2);
	}

	public ZModElement not(Element element) {
		return this.minus(element);
	}
  //
	// STATIC FACTORY METHODS
	//
	private static ZModTwo instance;

	/**
	 * Returns the singleton object of this class.
	 * <p>
	 * @return The singleton object of this class
	 */
	public static ZModTwo getInstance() {
		if (ZModTwo.instance == null) {
			ZModTwo.instance = new ZModTwo();
		}
		return ZModTwo.instance;
	}

}
