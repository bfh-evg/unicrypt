/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.CompoundIterator;
import ch.bfh.unicrypt.math.random.RandomOracle;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class ProductCyclicGroup
			 extends ProductGroup
			 implements CyclicGroup {

	private Tuple defaultGenerator;

	protected ProductCyclicGroup(final CyclicGroup[] cyclicGroups) {
		super(cyclicGroups);
	}

	protected ProductCyclicGroup(final CyclicGroup cyclicGroup, final int arity) {
		super(cyclicGroup, arity);
	}

	@Override
	public CyclicGroup getFirst() {
		return (CyclicGroup) super.getFirst();
	}

	@Override
	public CyclicGroup getAt(int index) {
		return (CyclicGroup) super.getAt(index);
	}

	@Override
	public CyclicGroup getAt(int... indices) {
		return (CyclicGroup) super.getAt(indices);
	}

	@Override
	public CyclicGroup[] getAll() {
		return (CyclicGroup[]) super.getAll();
	}

	@Override
	public ProductCyclicGroup removeAt(final int index) {
		return (ProductCyclicGroup) super.removeAt(index);
	}

	@Override
	protected ProductCyclicGroup abstractRemoveAt(Set set, int arity) {
		return ProductCyclicGroup.getInstance((CyclicGroup) set, arity);
	}

	@Override
	protected ProductCyclicGroup abstractRemoveAt(Set[] sets) {
		return ProductCyclicGroup.getInstance((CyclicGroup[]) sets);
	}

	@Override
	public Iterable<? extends CyclicGroup> makeIterable() {
		final ProductSet productCyclicGroup = this;
		return new Iterable<CyclicGroup>() {
			@Override
			public Iterator<CyclicGroup> iterator() {
				return new CompoundIterator<CyclicGroup>(productCyclicGroup);
			}
		};
	}

	protected boolean standardCyclicGroup() {
		return true;
	}

	@Override
	protected Iterator<Tuple> standardIterator() {
		final ProductCyclicGroup productCyclicGroup = this;
		return new Iterator<Tuple>() {
			BigInteger counter = BigInteger.ZERO;
			Tuple currentTuple = productCyclicGroup.getIdentityElement();

			@Override
			public boolean hasNext() {
				return counter.compareTo(productCyclicGroup.getOrder()) < 0;
			}

			@Override
			public Tuple next() {
				if (this.hasNext()) {
					this.counter = this.counter.add(BigInteger.ONE);
					Tuple nextElement = currentTuple;
					currentTuple = productCyclicGroup.apply(currentTuple, productCyclicGroup.getDefaultGenerator());
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

	@Override
	public final Tuple getDefaultGenerator() {
		if (this.defaultGenerator == null) {
			int arity = this.getArity();
			Element[] defaultGenerators = new Element[arity];
			for (int i = 0; i < arity; i++) {
				defaultGenerators[i] = this.getAt(i).getDefaultGenerator();
			}
			this.defaultGenerator = this.standardGetElement(defaultGenerators);
		}
		return this.defaultGenerator;
	}

	@Override
	public final Tuple getRandomGenerator() {
		return this.getRandomGenerator(null);
	}

	@Override
	public final Tuple getRandomGenerator(Random random) {
		int arity = this.getArity();
		Element[] randomGenerators = new Element[arity];
		for (int i = 0; i < arity; i++) {
			randomGenerators[i] = this.getAt(i).getRandomGenerator(random);
		}
		return this.standardGetElement(randomGenerators);
	}

	@Override
	public final Tuple getIndependentGenerator(long i) {
		return this.getIndependentGenerator(i, RandomOracle.DEFAULT);
	}

	@Override
	public final Tuple getIndependentGenerator(long query, RandomOracle randomOracle) {
		int arity = this.getArity();
		Element[] independentGenerators = new Element[arity];
		for (int i = 0; i < arity; i++) {
			independentGenerators[i] = this.getAt(i).getIndependentGenerator(query, randomOracle);
		}
		return this.standardGetElement(independentGenerators);
	}

	@Override
	public final boolean isGenerator(Element element) {
		if (!this.contains(element)) {
			throw new IllegalArgumentException();
		}
		Tuple tuple = (Tuple) element;
		for (int i = 0; i < this.getArity(); i++) {
			if (!this.getAt(i).isGenerator(tuple.getAt(i))) {
				return false;
			}
		}
		return true;
	}

	//
	// STATIC FACTORY METHODS
	//
	/**
	 * This is a static factory method to construct a composed cyclic group without calling respective constructors. The
	 * input groups are given as an array.
	 * <p/>
	 * @param cyclicGroups The array of cyclic groups
	 * @return The corresponding composed group
	 * @throws IllegalArgumentException if {@literal groups} is null or contains null
	 */
	public static ProductCyclicGroup getInstance(final CyclicGroup... cyclicGroups) {
		if (cyclicGroups == null) {
			throw new IllegalArgumentException();
		}
		if (ProductCyclicGroup.areRelativelyPrime(cyclicGroups)) {
			return new ProductCyclicGroup(cyclicGroups);
		}
		throw new IllegalArgumentException();
	}

	public static ProductCyclicGroup getInstance(final CyclicGroup group, int arity) {
		if ((group == null) || (arity < 0) || (arity > 1)) {
			throw new IllegalArgumentException();
		}
		if (arity == 0) {
			return new ProductCyclicGroup(new CyclicGroup[]{});
		}
		return new ProductCyclicGroup(new CyclicGroup[]{group});
	}

	//
	// STATIC HELPER METHODS
	//
	private static boolean areRelativelyPrime(CyclicGroup[] cyclicGroups) {
		BigInteger[] orders = new BigInteger[cyclicGroups.length];
		for (int i = 0; i < cyclicGroups.length; i++) {
			if (cyclicGroups[i] == null) {
				throw new IllegalArgumentException();
			}
			orders[i] = cyclicGroups[i].getOrder();
		}
		return MathUtil.areRelativelyPrime(orders);
	}

}
