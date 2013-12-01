/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class Triple
			 extends Tuple {

	protected Triple(final ProductSet set, final Element[] elements) {
		super(set, elements);
	}

	public Element getSecond() {
		return this.getAt(1);
	}

	public Element getThird() {
		return this.getAt(2);
	}

	public static Triple getInstance(Element first, Element second, Element third) {
		if (first == null || second == null || third == null) {
			throw new IllegalArgumentException();
		}
		return new Triple(ProductSet.getInstance(first.getSet(), second.getSet(), third.getSet()), new Element[]{first, second, third});
	}

}
