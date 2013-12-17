package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;

/**
 * This interface represents the the concept of a function f:X->X, which computes the inverse of the given input
 * element.
 * <p/>
 * @see Group#invert(Element)
 * @see Element#invert()
 * <p/>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class OneOverFunction
			 extends AbstractFunction<MultiplicativeGroup, MultiplicativeElement, MultiplicativeGroup, MultiplicativeElement> {

	private OneOverFunction(final MultiplicativeGroup domain, MultiplicativeGroup coDomain) {
		super(domain, coDomain);
	}

	//
	// The following protected method implements the abstract method from {@code AbstractFunction}
	//
	@Override
	protected MultiplicativeElement abstractApply(final MultiplicativeElement element, final RandomGenerator randomGenerator) {
		return element.oneOver();
	}

	//
	// STATIC FACTORY METHODS
	//
	/**
	 * This is the standard constructor for this class. It creates an invert function for a given group.
	 * <p/>
	 * @param multiplicativeGroup The given Group
	 * @throws IllegalArgumentException if the group is null
	 */
	public static OneOverFunction getInstance(final MultiplicativeGroup multiplicativeGroup) {
		return new OneOverFunction(multiplicativeGroup, multiplicativeGroup);
	}

}
