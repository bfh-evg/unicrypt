/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

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
	protected boolean standardIsEquivalent(Set set) {
		Subset other = (Subset) set;
		return this.superSet.isEquivalent(other.superSet) && this.hashSet.equals(other.hashSet);
	}

	@Override
	protected boolean standardContains(final Element element) {
		return this.hashSet.contains(element);
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		for (Element element : this.hashSet) {
			if (element.getValue().equals(value)) {
				return true;
			}
		}
		return false;
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
	protected Element abstractGetRandomElement(RandomGenerator randomGenerator) {
		return this.getElements()[randomGenerator.nextInteger(this.hashSet.size() - 1)];
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
