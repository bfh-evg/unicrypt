/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Ring;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractRing<E extends DualisticElement>
			 extends AbstractSemiRing<E>
			 implements Ring {

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
	public final E subtract(final Element element1, final Element element2) {
		return this.applyInverse(element1, element2);
	}

	@Override
	public final E minus(final Element element) {
		return this.invert(element);
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
