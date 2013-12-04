package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import java.util.Random;

/**
 * This interface represents the the concept of a function f:X^n->X, which applies the group operation sequentially to
 * several input elements. For this to work, the input elements is given as a tuple element of a corresponding power
 * group of arity n. For n=0, the function returns the group's identity element. For n=1, the function returns the
 * single element included in the tuple element.
 * <p/>
 * @see Group#apply(Element[])
 * @see Element#apply(Element)
 * <p/>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class SubtractFunction
			 extends AbstractFunction<ProductGroup, Pair, AdditiveGroup, AdditiveElement> {

	private SubtractFunction(final ProductGroup domain, final AdditiveGroup coDomain) {
		super(domain, coDomain);
	}

	@Override
	protected AdditiveElement abstractApply(final Pair element, final Random random) {
		return this.getCoDomain().subtract(element.getFirst(), element.getSecond());
	}

	/**
	 * This is the general factory method of this class. The first parameter is the group on which it operates, and the
	 * second parameter is the number of input elements.
	 * <p/>
	 * @param additiveGroup
	 * @return The resulting function
	 * @throws IllegalArgumentException if {@literal group} is null
	 * @throws IllegalArgumentException if {@literal arity} is negative
	 */
	public static SubtractFunction getInstance(final AdditiveGroup additiveGroup) {
		if (additiveGroup == null) {
			throw new IllegalArgumentException();
		}
		return new SubtractFunction(ProductGroup.getInstance(additiveGroup, 2), additiveGroup);
	}

}
