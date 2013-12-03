package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.general.classes.SingletonElement;
import ch.bfh.unicrypt.math.algebra.general.classes.SingletonGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

/**
 * This classrepresents the concept of a randomized function with no input. When the function is called, it selects an
 * element from the co-domain at random and returns it as output value.
 * <p/>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class RandomFunction
			 extends AbstractFunction<SingletonGroup, SingletonElement, Set, Element> {

	private RandomFunction(final Set coDomain) {
		super(SingletonGroup.getInstance(), coDomain);
	}

	//
	// The following protected method implements the abstract method from {@code AbstractFunction}
	//
	@Override
	protected Element abstractApply(final SingletonElement element, final Random random) {
		return this.getCoDomain().getRandomElement(random);
	}

	//
	// STATIC FACTORY METHODS
	//
	/**
	 * This is the general constructor of this class. It creates a function that generates random elements from a given
	 * group.
	 * <p/>
	 * @param set The given group
	 * @return
	 * @throws IllegalArgumentException if {@literal group} is null
	 */
	public static RandomFunction getInstance(final Set set) {
		if (set == null) {
			throw new IllegalArgumentException();
		}
		return new RandomFunction(set);
	}

	@Override
	protected boolean abstractIsEqual(Function function) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
