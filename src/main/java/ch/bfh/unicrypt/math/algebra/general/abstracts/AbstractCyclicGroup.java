package ch.bfh.unicrypt.math.algebra.general.abstracts;

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomGenerator;
import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;

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
	public final E getRandomGenerator(RandomGenerator randomGenerator) {
		if (randomGenerator == null) {
			randomGenerator = PseudoRandomGenerator.DEFAULT;
		}
		return this.standardGetRandomGenerator(randomGenerator);
	}

	@Override
	public final E getIndependentGenerator(int index) {
		return this.getIndependentGenerator(index, (RandomReferenceString) null);
	}

	@Override
	public final E getIndependentGenerator(int index, RandomReferenceString randomReferenceString) {
		return this.getIndependentGenerators(index, randomReferenceString)[index];
	}

	@Override
	public final E[] getIndependentGenerators(int maxIndex) {
		return this.getIndependentGenerators(maxIndex, (RandomReferenceString) null);
	}

	@Override
	public final E[] getIndependentGenerators(int maxIndex, RandomReferenceString randomReferenceString) {
		return this.getIndependentGenerators(0, maxIndex, randomReferenceString);
	}

	@Override
	public final E[] getIndependentGenerators(int minIndex, int maxIndex) {
		return this.getIndependentGenerators(minIndex, maxIndex, (RandomReferenceString) null);
	}

	@Override
	public final E[] getIndependentGenerators(int minIndex, int maxIndex, RandomReferenceString randomReferenceString) {
		if (minIndex < 0 || maxIndex < minIndex) {
			throw new IndexOutOfBoundsException();
		}
		if (randomReferenceString == null) {
			randomReferenceString = PseudoRandomReferenceString.getInstance();
		}
		// The following line is necessary for creating a generic array
		E[] generators = (E[]) Array.newInstance(this.getIdentityElement().getClass(), maxIndex - minIndex + 1);
		for (int i = 0; i <= maxIndex; i++) {
			E generator = this.standardGetIndependentGenerator(randomReferenceString);
			if (i >= minIndex) {
				generators[i - minIndex] = generator;
			}
		}
		return generators;
	}

	@Override
	public final boolean isGenerator(Element element) {
		if (!this.contains(element)) {
			throw new IllegalArgumentException();
		}
		return this.abstractIsGenerator((E) element);
	}

	// see Handbook of Applied Cryptography, Algorithm 4.80 and Note 4.81
	protected E standardGetRandomGenerator(RandomGenerator randomGenerator) {
		E element;
		do {
			element = this.getRandomElement(randomGenerator);
		} while (!this.isGenerator(element));
		return element;
	}

	protected E standardGetIndependentGenerator(RandomReferenceString randomReferenceString) {
		return this.standardGetRandomGenerator(randomReferenceString);
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
