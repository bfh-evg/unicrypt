package ch.bfh.unicrypt.math.algebra.general.abstracts;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.random.RandomOracle;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public abstract class AbstractCyclicGroup<E extends Element>
			 extends AbstractGroup<E>
			 implements CyclicGroup, Iterable<E> {

	private E defaultGenerator;

	@Override
	public final E getDefaultGenerator() {
		if (this.defaultGenerator == null) {
			this.defaultGenerator = this.abstractGetDefaultGenerator();
		}
		return this.defaultGenerator;
	}

	@Override
	public final E getRandomGenerator() {
		return this.getRandomGenerator(null);
	}

	@Override
	public final E getRandomGenerator(Random random) {
		return this.standardGetRandomGenerator(random);
	}

	@Override
	public final E getIndependentGenerator(long query) {
		return this.getIndependentGenerator(query, RandomOracle.DEFAULT);
	}

	@Override
	public final E getIndependentGenerator(long query, RandomOracle randomOracle) {
		if (randomOracle == null) {
			throw new IllegalArgumentException();
		}
		return this.standardGetRandomGenerator(randomOracle.getRandom(query));
	}

	@Override
	public final boolean isGenerator(Element element) {
		if (!this.contains(element)) {
			throw new IllegalArgumentException();
		}
		return this.abstractIsGenerator((E) element);
	}

	// see Handbook of Applied Cryptography, Algorithm 4.80 and Note 4.81
	protected E standardGetRandomGenerator(Random random) {
		E element;
		do {
			element = this.getRandomElement(random);
		} while (!this.isGenerator(element));
		return element;
	}

	@Override
	protected Iterator<E> standardIterator() {
		final AbstractCyclicGroup<E> cyclicGroup = this;
		return new Iterator<E>() {
			BigInteger counter = BigInteger.ZERO;
			E currentElement = cyclicGroup.getIdentityElement();

			@Override
			public boolean hasNext() {
				return counter.compareTo(cyclicGroup.getOrder()) < 0;
			}

			@Override
			public E next() {
				if (this.hasNext()) {
					this.counter = this.counter.add(BigInteger.ONE);
					E nextElement = currentElement;
					currentElement = cyclicGroup.apply(currentElement, cyclicGroup.getDefaultGenerator());
					return nextElement;
				}
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException("Not supported yet.");
			}
		};
	}

	//
	// The following protected abstract method must be implemented in every direct sub-class
	//
	protected abstract E abstractGetDefaultGenerator();

	protected abstract boolean abstractIsGenerator(E element);

}
