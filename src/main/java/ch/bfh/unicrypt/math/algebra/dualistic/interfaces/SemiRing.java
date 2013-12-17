/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.interfaces;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeMonoid;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public interface SemiRing
			 extends AdditiveMonoid, MultiplicativeMonoid {

	// The following methods are overridden from Set with an adapted return type
	@Override
	public DualisticElement getElement(int value);

	@Override
	public DualisticElement getElement(BigInteger value);

	@Override
	public DualisticElement getElement(Element element);

	@Override
	public DualisticElement getRandomElement();

	@Override
	public DualisticElement getRandomElement(RandomGenerator randomGenerator);

	// The following methods are overridden from SemiGroup with an adapted return type
	@Override
	public DualisticElement apply(Element element1, Element element2);

	@Override
	public DualisticElement apply(Element... elements);

	@Override
	public DualisticElement selfApply(Element element, BigInteger amount);

	@Override
	public DualisticElement selfApply(Element element, Element amount);

	@Override
	public DualisticElement selfApply(Element element, int amount);

	@Override
	public DualisticElement selfApply(Element element);

	@Override
	public DualisticElement multiSelfApply(Element[] elements, BigInteger[] amounts);

	// The following methods are overridden from Monoid with an adapted return type
	@Override
	public DualisticElement getIdentityElement();

	// The following methods are overridden from AdditiveSemiGroup with an adapted return type
	@Override
	public DualisticElement add(Element element1, Element element2);

	@Override
	public DualisticElement add(Element... elements);

	@Override
	public DualisticElement times(Element element, BigInteger amount);

	@Override
	public DualisticElement times(Element element, Element amount);

	@Override
	public DualisticElement times(Element element, int amount);

	@Override
	public DualisticElement timesTwo(Element element);

	@Override
	public DualisticElement sumOfProducts(Element[] elements, BigInteger[] amounts);

	// The following methods are overridden from AdditiveMonoid with an adapted return type
	@Override
	public DualisticElement getZeroElement();

	// The following methods are overridden from MultiplicativeSemiGroup with an adapted return type
	@Override
	public DualisticElement multiply(Element element1, Element element2);

	@Override
	public DualisticElement multiply(Element... elements);

	@Override
	public DualisticElement power(Element element, BigInteger amount);

	@Override
	public DualisticElement power(Element element, Element amount);

	@Override
	public DualisticElement power(Element element, int amount);

	@Override
	public DualisticElement square(Element element);

	@Override
	public DualisticElement productOfPowers(Element[] elements, BigInteger[] amounts);

	// The following methods are overridden from MultiplicativeMonoid with an adapted return type
	@Override
	public DualisticElement getOneElement();

}
