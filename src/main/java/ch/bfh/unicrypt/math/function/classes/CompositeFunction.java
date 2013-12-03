package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractCompoundFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

/**
 * This class represents the concept of a composite function f:X_1->Y_n. It consists of multiple internal functions
 * f_i:X_i->Y_i, which are applied sequentially to the input element in the given order. For this to work, X_i=Y_{i-1}
 * must hold for i=2,...,n, i.e., the co-domain of the first function must correspond to the domain of the second
 * function, the co-domain of the second function must correspond to the domain of the third function, and so on.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public final class CompositeFunction
			 extends AbstractCompoundFunction<CompositeFunction, Function, Set, Element, Set, Element> {

	private CompositeFunction(final Set domain, final Set coDomain, final Function[] functions) {
		super(domain, coDomain, functions);
	}

	protected CompositeFunction(Set domain, Set coDomain, Function function, int arity) {
		super(domain, coDomain, function, arity);
	}

	@Override
	protected final Element abstractApply(final Element element, final Random random) {
		Element result = element;
		for (Function function : this) {
			result = function.apply(result, random);
		}
		return result;
	}

	@Override
	protected CompositeFunction abstractRemoveAt(Function function, int arity) {
		return CompositeFunction.getInstance(function, arity);
	}

	@Override
	protected CompositeFunction abstractRemoveAt(Function[] functions) {
		return CompositeFunction.getInstance(functions);
	}

	/**
	 * This is the general factory method of this class. It takes an array of functions as input and produces the
	 * corresponding composite function.
	 * <p>
	 * @param functions The given array of functions
	 * @return The resulting composite function
	 * @throws IllegalArgumentException if {@literal functions} is null, contains null, or is empty
	 * @throws IllegalArgumentException if the domain of a function is different from the co-domain of the previous
	 *                                  function
	 */
	public static CompositeFunction getInstance(final Function... functions) {
		if (functions == null || functions.length < 1 || functions[0] == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 1; i < functions.length; i++) {
			if (functions[i] == null || !(functions[i - 1].getCoDomain().isEqual(functions[i].getDomain()))) {
				throw new IllegalArgumentException();
			}
		}
		return new CompositeFunction(functions[0].getDomain(), functions[functions.length - 1].getCoDomain(), functions);
	}

	public static CompositeFunction getInstance(final Function function, final int arity) {
		if (function == null || arity < 0) {
			throw new IllegalArgumentException();
		}
		if (arity > 1 && !function.getDomain().isEqual(function.getCoDomain())) {
			throw new IllegalArgumentException();
		}
		return new CompositeFunction(ProductSet.getInstance(function.getDomain(), arity), ProductSet.getInstance(function.getCoDomain(), arity), function, arity);
	}

}
