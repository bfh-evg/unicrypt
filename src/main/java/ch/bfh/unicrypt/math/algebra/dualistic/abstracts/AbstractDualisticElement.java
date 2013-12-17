/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractAdditiveElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.Field;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.SemiRing;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractDualisticElement<S extends SemiRing, E extends DualisticElement>
	   extends AbstractAdditiveElement<S, E>
	   implements DualisticElement {

	protected AbstractDualisticElement(final S ring) {
		super(ring);
	}

	protected AbstractDualisticElement(final S ring, final BigInteger value) {
		super(ring);
		this.value = value;
	}

	@Override
	public final E multiply(final Element element) {
		return (E) this.getSet().multiply(this, element);
	}

	@Override
	public final E power(final BigInteger amount) {
		return (E) this.getSet().power(this, amount);
	}

	@Override
	public final E power(final Element amount) {
		return (E) this.getSet().power(this, amount);
	}

	@Override
	public final E power(final int amount) {
		return (E) this.getSet().power(this, amount);
	}

	@Override
	public final E square() {
		return (E) this.getSet().square(this);
	}

	@Override
	public final E divide(final Element element) {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return (E) field.divide(this, element);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public final E oneOver() {
		if (this.getSet().isField()) {
			Field field = ((Field) this.getSet());
			return (E) field.oneOver(this);
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isOne() {
		return this.getSet().isOneElement(this);
	}

}
