package ch.bfh.unicrypt.math.function.abstracts;

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomGenerator;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.PartiallyAppliedFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.UniCrypt;
import java.util.Random;

/**
 * This abstract class contains standard implementations for most methods of type {@link Function}. For most classes
 * implementing {@link Function}, it is sufficient to inherit from {@link AbstractFunction} and to implement the single
 * abstract method {@link abstractApply(Element element, Random random)}.
 * <p>
 * <p/>
 * @param <D>
 * @param <DE>
 * @param <CE>
 * @param <C>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractFunction<D extends Set, DE extends Element, C extends Set, CE extends Element>
			 extends UniCrypt
			 implements Function {

	private final D domain;
	private final C coDomain;

	protected AbstractFunction(final Set domain, final Set coDomain) {
		this.domain = (D) domain;
		this.coDomain = (C) coDomain;
	}

	@Override
	public final boolean isCompound() {
		return this.standardIsCompound();
	}

	@Override
	public final CE apply(final Element element) {
		return this.apply(element, (RandomGenerator) null);
	}

	@Override
	public final CE apply(final Element element, RandomGenerator randomGenerator) {
		if (randomGenerator == null) {
			randomGenerator = PseudoRandomGenerator.DEFAULT;
		}
		if (this.getDomain().contains(element)) {
			return this.abstractApply((DE) element, randomGenerator);
		}
		// This is for increased convenience for a function with a CompoundSet domain of arity 1.
		return this.apply(new Element[]{element}, randomGenerator);
	}

	@Override
	public final CE apply(final Element... elements) {
		return this.apply(elements, (RandomGenerator) null);
	}

	@Override
	public final CE apply(final Element[] elements, final RandomGenerator randomGenerator) {
		if (this.getDomain().isProduct()) {
			return this.apply(((ProductSet) this.getDomain()).getElement(elements), randomGenerator);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public final CE apply(final RandomGenerator randomGenerator) {
		return this.apply(new Element[]{}, randomGenerator);
	}

	@Override
	public D getDomain() {
		return this.domain;
	}

	@Override
	public C getCoDomain() {
		return this.coDomain;
	}

	@Override
	public final PartiallyAppliedFunction partiallyApply(final Element element, final int index) {
		return PartiallyAppliedFunction.getInstance(this, element, index);
	}

	@Override
	public final boolean isEquivalent(final Function function) {
		if (function == null) {
			throw new IllegalArgumentException();
		}
		if (this == function) {
			return true;
		}
		if (this.getClass() != function.getClass()) {
			return false;
		}
		if (!this.getDomain().isEquivalent(function.getDomain())) {
			return false;
		}
		if (!this.getCoDomain().isEquivalent(function.getCoDomain())) {
			return false;
		}
		return this.standardIsEquivalent(function);
	}

	@Override
	public String standardToStringContent() {
		return "[" + this.getDomain() + " => " + this.getCoDomain() + "]";
	}

	//
	// The following protected methods are standard implementations for sets.
	// They may need to be changed in certain sub-classes.
	//
	protected boolean standardIsCompound() {
		return false;
	}

	protected boolean standardIsEquivalent(Function function) {
		return true;
	}

	//
	// The following protected abstract method must be implemented in every direct
	// sub-class
	//
	/**
	 * This abstract method is the main method to implement in each sub-class of {@link AbstractFunction}. The validity of
	 * the two parameters has already been tested.
	 * <p>
	 * <p/>
	 * @see apply(Element, Random)
	 * @see Group#apply(Element[])
	 * @see Element#apply(Element)
	 * <p>
	 * @param element         The given input element
	 * @param randomGenerator Either {@literal null} or a given random generator
	 * @return The resulting output element
	 */
	protected abstract CE abstractApply(DE element, RandomGenerator randomGenerator);

}
