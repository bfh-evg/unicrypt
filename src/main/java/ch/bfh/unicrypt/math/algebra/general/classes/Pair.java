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
public class Pair
			 extends Tuple {

	protected Pair(final ProductSet set, final Element[] elements) {
		super(set, elements);
	}

	public Element getSecond() {
		return this.getAt(1);
	}

	public static Pair getInstance(Element first, Element second) {
		if (first == null || second == null) {
			throw new IllegalArgumentException();
		}
		return new Pair(ProductSet.getInstance(first.getSet(), second.getSet()), new Element[]{first, second});
	}

}
