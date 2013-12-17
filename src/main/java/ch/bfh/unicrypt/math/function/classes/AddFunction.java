package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
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
public class AddFunction
			 extends AbstractFunction<ProductSemiGroup, Tuple, AdditiveSemiGroup, AdditiveElement> {

	private AddFunction(final ProductSemiGroup domain, final AdditiveSemiGroup coDomain) {
		super(domain, coDomain);
	}

	@Override
	protected AdditiveElement abstractApply(final Tuple element, final RandomGenerator randomGenerator) {
		return this.getCoDomain().add(element.getAll());
	}

	public static AddFunction getInstance(final AdditiveSemiGroup additiveSemiGroup) {
		return AddFunction.getInstance(additiveSemiGroup, 2);
	}

	/**
	 * This is the general factory method of this class. The first parameter is the group on which it operates, and the
	 * second parameter is the number of input elements.
	 * <p/>
	 * @param additiveSemiGroup The group on which this function operates
	 * @param arity             The number of input elements
	 * @return The resulting function
	 * @throws IllegalArgumentException if {@literal group} is null
	 * @throws IllegalArgumentException if {@literal arity} is negative
	 */
	public static AddFunction getInstance(final AdditiveSemiGroup additiveSemiGroup, final int arity) {
		if (additiveSemiGroup == null || arity < 0) {
			throw new IllegalArgumentException();
		}
		return new AddFunction(ProductSemiGroup.getInstance(additiveSemiGroup, arity), additiveSemiGroup);
	}

}
