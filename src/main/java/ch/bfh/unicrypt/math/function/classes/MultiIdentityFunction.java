package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Arrays;
import java.util.Random;

/**
 * This class represents the concept of a generalized identity function f:X->X^n
 * with f(x)=(x,...,x) for all elements x in X. This class represents the
 * concept of an identity function f:X->X with f(x)=x for all elements x in X.
 * <p/>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class MultiIdentityFunction
	   extends AbstractFunction<Set, ProductSet, Tuple> {

	private MultiIdentityFunction(final Set domain, final ProductSet coDomain) {
		super(domain, coDomain);
	}

	//
	// The following protected method implements the abstract method from {@code AbstractFunction}
	//
	@Override
	protected Tuple abstractApply(final Element element, final Random random) {
		final Element[] elements = new Element[this.getCoDomain().getArity()];
		Arrays.fill(elements, element);
		return this.getCoDomain().getElement(elements);
	}

	//
	// STATIC FACTORY METHODS
	//
	public static MultiIdentityFunction getInstance(final Set set) {
		return MultiIdentityFunction.getInstance(set, 1);
	}

	/**
	 * This is the standard constructor for this class. It creates a generalized
	 * identity function for a given group, which reproduces the input value
	 * multiple time.
	 * <p/>
	 * @param set   The given set
	 * @param arity The arity of the output element
	 * @throws IllegalArgumentException if {@literal group} is null
	 * @throws IllegalArgumentException if {@literal arity} is negative
	 */
	public static MultiIdentityFunction getInstance(final Set set, final int arity) {
		if (set == null || arity < 0) {
			throw new IllegalArgumentException();
		}
		return new MultiIdentityFunction(set, ProductSet.getInstance(set, arity));
	}

	@Override
	protected boolean abstractIsEqual(Function function) {
		return true;
	}

}
