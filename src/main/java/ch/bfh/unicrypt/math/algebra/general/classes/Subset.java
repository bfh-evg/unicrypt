/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Random;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class Subset
			 extends AbstractSet<Element>
			 implements Iterable<Element> {

	private final Set superSet;
	private final HashSet<Element> hashSet;

	protected Subset(Set superSet, HashSet<Element> elementSet) {
		this.superSet = superSet;
		this.hashSet = elementSet;
	}

	public Set getSuperset() {
		return this.superSet;
	}

	public Element[] getElements() {
		return this.hashSet.toArray(new Element[]{});
	}

	@Override
	protected Iterator<Element> standardIterator() {
		return this.hashSet.iterator();
	}

	@Override
	protected boolean standardIsEqual(Set set) {
		Subset other = (Subset) set;
		return this.superSet.isEqual(other.superSet) && this.hashSet.equals(other.hashSet);
	}

	@Override
	protected boolean standardContains(final Element element) {
		return this.hashSet.contains(element);
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		return this.getSuperset().contains(value);
	}

	@Override
	protected Element abstractGetElement(BigInteger value) {
		return this.getSuperset().getElement(value);
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return BigInteger.valueOf(this.hashSet.size());
	}

	@Override
	protected Element abstractGetRandomElement(Random random) {
		return this.getSuperset().getRandomElement(random);
	}

	public static Subset getInstance(Set superSet, Element... elements) {
		if (superSet == null || elements == null) {
			throw new IllegalArgumentException();
		}
		// A LinkedHashSet retains the order
		HashSet<Element> hashSet = new LinkedHashSet<Element>();
		for (Element element : elements) {
			if (element == null || !superSet.contains(element)) {
				throw new IllegalArgumentException();
			}
			hashSet.add(element);
		}
		return new Subset(superSet, hashSet);
	}

}
