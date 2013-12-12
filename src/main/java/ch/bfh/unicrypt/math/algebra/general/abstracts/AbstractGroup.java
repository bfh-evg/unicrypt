package ch.bfh.unicrypt.math.algebra.general.abstracts;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import java.math.BigInteger;

/**
 * This abstract class provides a basis implementation for objects of type {@link Group}.
 * <p>
 * @param <E>
 * @see AbstractElement
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractGroup<E extends Element>
			 extends AbstractMonoid<E>
			 implements Group {

	@Override
	public final E invert(final Element element) {
		if (!this.contains(element)) {
			throw new IllegalArgumentException();
		}
		return this.abstractInvert((E) element);
	}

	@Override
	public final E applyInverse(Element element1, Element element2) {
		return this.apply(element1, this.invert(element2));
	}

	@Override
	protected E standardSelfApply(Element element, BigInteger amount) {
		if (amount.signum() < 0) {
			return this.invert(super.standardSelfApply(element, amount.abs()));
		}
		return super.standardSelfApply(element, amount);
	}

  //
	// The following protected abstract method must be implemented in every direct sub-class.
	//
	protected abstract E abstractInvert(E element);

}
