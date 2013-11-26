package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
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
public class IdentityFunction
	   extends AbstractFunction<Set, Set, Element> {

	private IdentityFunction(final Set set) {
		super(set, set);
	}

	//
	// The following protected method implements the abstract method from {@code AbstractFunction}
	//
	@Override
	protected Element abstractApply(final Element element, final Random random) {
		return element;
	}

	//
	// STATIC FACTORY METHODS
	//
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
	public static IdentityFunction getInstance(final Set set) {
		if (set == null) {
			throw new IllegalArgumentException();
		}
		return new IdentityFunction(set);
	}

	@Override
	protected boolean abstractIsEqual(Function function) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
