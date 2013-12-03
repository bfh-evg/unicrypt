package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

/**
 * This class represents the concept of a function, which is derived from another function with a product (or power)
 * group domain by applying a single input element and thus by fixing the corresponding parameter to a constant value.
 * Therefore, the input arity of such a function of is the input arity of the parent function minus 1. Functions of that
 * type are usually constructed by calling the method {@link Function#partiallyApply(Element, int)} for a given function
 * with a product (or power) group domain.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class PartiallyAppliedFunction
			 extends AbstractFunction<ProductSet, Tuple, Set, Element> {

	private final Function parentFunction;
	private final Element parameter;
	private final int index;

	/**
	 * This is the standard constructor of this class. It derives from a given function a new function, in which one input
	 * element is fixed to a given element and thus expects one input element less.
	 * <p>
	 * @param parentFunction The given function
	 * @param parameter      The given parameter to fix
	 * @param index          The index of the parameter to fix
	 * @throws IllegalArgumentException  if the {@literal function} is null or not a ProductGroup
	 * @throws IndexOutOfBoundsException if the {@literal index} is negative or > the arity of the function's domain
	 * @throws IllegalArgumentException  if the {@literal element} is not an element of the corresponding group
	 */
	private PartiallyAppliedFunction(final ProductSet domain, final Set coDomain, final Function parentFunction, final Element parameter, final int index) {
		super(domain, coDomain);
		this.parentFunction = parentFunction;
		this.parameter = parameter;
		this.index = index;
	}

	/**
	 * Returns the parent function from which {@literal this} function has been derived.
	 * <p>
	 * @return The parent function
	 */
	public Function getParentFunction() {
		return this.parentFunction;
	}

	/**
	 * Returns the input element that has been used to derive {@literal this} function from the parent function.
	 * <p>
	 * @return The input element
	 */
	public Element getParameter() {
		return this.parameter;
	}

	/**
	 * Returns the index of the parameter that has been fixed to derive {@literal this} function from the parent function.
	 * <p>
	 * @return The index of the input element
	 */
	public int getIndex() {
		return this.index;
	}

	@Override
	protected boolean abstractIsEqual(Function function) {
		PartiallyAppliedFunction other = (PartiallyAppliedFunction) function;
		return this.getParentFunction().isEqual(other.getParentFunction())
					 && this.getParameter().isEqual(other.getParameter())
					 && this.getIndex() == other.getIndex();
	}

	//
	// The following protected method implements the abstract method from {@code AbstractFunction}
	//
	@Override
	protected Element abstractApply(final Tuple element, final Random random) {
		int arity = element.getArity();
		final Element[] allElements = new Element[arity + 1];
		for (int i = 0; i < arity; i++) {
			if (i < this.getIndex()) {
				allElements[i] = element.getAt(i);
			} else {
				allElements[i + 1] = element.getAt(i);
			}
			allElements[this.getIndex()] = this.getParameter();
		}
		return this.getParentFunction().apply(allElements, random);
	}

	//
	// STATIC FACTORY METHODS
	//
	/**
	 * This is the standard constructor of this class. It derives from a given function a new function, in which one input
	 * element is fixed to a given element and thus expects one input element less.
	 * <p>
	 * @param parentfunction The given function
	 * @param element        The given parameter to fix
	 * @param index          The index of the parameter to fix
	 * @throws IllegalArgumentException  if the {@literal function} is null or not a ProductGroup
	 * @throws IndexOutOfBoundsException if the {@literal index} is negative or > the arity of the function's domain
	 * @throws IllegalArgumentException  if the {@literal element} is not an element of the corresponding group
	 */
	public static PartiallyAppliedFunction getInstance(final Function parentFunction, final Element element, final int index) {
		if (parentFunction == null) {
			throw new IllegalArgumentException();
		}
		if (parentFunction.getDomain().isProduct()) {
			ProductSet domain = (ProductSet) parentFunction.getDomain();
			if (!domain.getAt(index).contains(element)) {
				throw new IllegalArgumentException();
			}
			return new PartiallyAppliedFunction(domain.removeAt(index), parentFunction.getCoDomain(), parentFunction, element, index);
		}
		throw new IllegalArgumentException();
	}

}
