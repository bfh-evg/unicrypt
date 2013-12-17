package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.Pair;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;

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
public class DivideFunction
			 extends AbstractFunction<ProductGroup, Pair, MultiplicativeGroup, MultiplicativeElement> {

	private DivideFunction(final ProductGroup domain, final MultiplicativeGroup coDomain) {
		super(domain, coDomain);
	}

	@Override
	protected MultiplicativeElement abstractApply(final Pair element, final RandomGenerator randomGenerator) {
		return this.getCoDomain().divide(element.getFirst(), element.getSecond());
	}

	/**
	 * This is the general factory method of this class. The first parameter is the group on which it operates, and the
	 * second parameter is the number of input elements.
	 * <p/>
	 * @param multiplicativeGroup
	 * @return The resulting function
	 * @throws IllegalArgumentException if {@literal group} is null
	 * @throws IllegalArgumentException if {@literal arity} is negative
	 */
	public static DivideFunction getInstance(final MultiplicativeGroup multiplicativeGroup) {
		if (multiplicativeGroup == null) {
			throw new IllegalArgumentException();
		}
		return new DivideFunction(ProductGroup.getInstance(multiplicativeGroup, 2), multiplicativeGroup);
	}

}
