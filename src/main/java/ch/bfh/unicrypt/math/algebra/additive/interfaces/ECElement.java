package ch.bfh.unicrypt.math.algebra.additive.interfaces;

import java.math.BigInteger;

import ch.bfh.unicrypt.helper.Point;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

public interface ECElement<V, E extends DualisticElement<V>>
	   extends AdditiveElement<Point<E>> {
	
	/**
	 * @see Group#apply(Element, Element)
	 */
	public ECElement<V,E> add(Element element);

	/**
	 * @see Group#applyInverse(Element, Element)
	 */
	public ECElement<V,E> subtract(Element element);

	/**
	 * @see Group#selfApply(Element, BigInteger)
	 */
	public ECElement<V,E> times(BigInteger amount);

	/**
	 * @see Group#selfApply(Element, Element)
	 */
	public ECElement<V,E> times(Element<BigInteger> amount);

	/**
	 * @see Group#selfApply(Element, int)
	 */
	public ECElement<V,E> times(int amount);

	/**
	 * @see Group#selfApply(Element)
	 */
	public ECElement<V,E> timesTwo();

	/**
	 * @see Group#negate(Element)
	 */
	public ECElement<V,E> negate();

	public boolean isZero();

	// The following methods are overridden from Element with an adapted return type


	@Override
	public ECElement<V,E> apply(Element element);

	@Override
	public ECElement<V,E> applyInverse(Element element);

	@Override
	public ECElement<V,E> selfApply(BigInteger amount);

	@Override
	public ECElement<V,E> selfApply(Element<BigInteger> amount);

	@Override
	public ECElement<V,E> selfApply(int amount);

	@Override
	public ECElement<V,E> selfApply();

	@Override
	public ECElement<V,E> invert();
	
	public E getY();
	public E getX();

}
