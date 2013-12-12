/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.abstracts;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.PrimeField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeCyclicGroup;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractPrimeField<E extends DualisticElement, M extends MultiplicativeCyclicGroup>
			 extends AbstractCyclicRing<E>
			 implements PrimeField {

	private M multiplicativeGroup;
	private BigInteger characteristic;

	@Override
	public final BigInteger getCharacteristic() {
		return this.characteristic;
	}

	@Override
	public M getMultiplicativeGroup() {
		if (this.multiplicativeGroup == null) {
			this.multiplicativeGroup = this.abstractGetMultiplicativeGroup();
		}
		return this.multiplicativeGroup;
	}

	@Override
	public E divide(Element element1, Element element2) {
		return this.multiply(element1, this.oneOver(element2));
	}

	@Override
	public E oneOver(Element element) {
		if (!this.contains(element)) {
			throw new IllegalArgumentException();
		}
		if (element.isEqual(this.getZeroElement())) {
			throw new UnsupportedOperationException();
		}
		return this.abstractOneOver((E) element);
	}

	protected abstract E abstractOneOver(E element);

	protected abstract M abstractGetMultiplicativeGroup();

}
