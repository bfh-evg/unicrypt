package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.general.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeSemiGroup;
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
public class MultiplyFunction
			 extends AbstractFunction<ProductSemiGroup, Tuple, MultiplicativeSemiGroup, MultiplicativeElement> {

	private MultiplyFunction(final ProductSemiGroup domain, final MultiplicativeSemiGroup coDomain) {
		super(domain, coDomain);
	}

	@Override
	protected MultiplicativeElement abstractApply(final Tuple element, final Random random) {
		return this.getCoDomain().multiply(element.getAll());
	}

	public static MultiplyFunction getInstance(final MultiplicativeSemiGroup multiplicativeSemiGroup) {
		return MultiplyFunction.getInstance(multiplicativeSemiGroup, 2);
	}

	/**
	 * This is the general factory method of this class. The first parameter is the group on which it operates, and the
	 * second parameter is the number of input elements.
	 * <p/>
	 * @param multiplicativeSemiGroup The group on which this function operates
	 * @param arity                   The number of input elements
	 * @return The resulting function
	 * @throws IllegalArgumentException if {@literal group} is null
	 * @throws IllegalArgumentException if {@literal arity} is negative
	 */
	public static MultiplyFunction getInstance(final MultiplicativeSemiGroup multiplicativeSemiGroup, final int arity) {
		if (multiplicativeSemiGroup == null || arity < 0) {
			throw new IllegalArgumentException();
		}
		return new MultiplyFunction(ProductSemiGroup.getInstance(multiplicativeSemiGroup, arity), multiplicativeSemiGroup);
	}

}
