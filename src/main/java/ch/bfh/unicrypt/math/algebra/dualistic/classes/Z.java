/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.abstracts.AbstractCyclicRing;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.utility.RandomUtil;
import java.math.BigInteger;
import java.util.Random;

/**
 * /**
 * This class implements the additive cyclic group of (positive and negative) integers with infinite order. Its identity
 * element is 0, and there are exactly two generators, namely 1 and -1. To invert an element, it is multiplied with -1.
 * <p>
 * @see "Handbook of Applied Cryptography, Example 2.164"
 * @see <a href="http://en.wikipedia.org/wiki/Integer">http://en.wikipedia.org/wiki/Integer</a>
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class Z
			 extends AbstractCyclicRing<ZElement> {

	//
	// The following protected methods override the standard implementation from
	// various super-classes
	//
	@Override
	protected ZElement standardSelfApply(Element element, BigInteger amount) {
		return this.abstractGetElement(element.getValue().multiply(amount));
	}

	@Override
	protected ZElement standardGetRandomGenerator(final Random random) {
		if (RandomUtil.getRandomBoolean(random)) {
			return this.getDefaultGenerator();
		}
		return this.getDefaultGenerator().invert();
	}

	//
	// The following protected methods implement the abstract methods from
	// various super-classes
	//
	@Override
	protected ZElement abstractApply(ZElement element1, ZElement element2) {
		return this.abstractGetElement(element1.getValue().add(element2.getValue()));
	}

	@Override
	protected ZElement abstractGetIdentityElement() {
		return this.abstractGetElement(BigInteger.ZERO);
	}

	@Override
	protected ZElement abstractInvert(ZElement element) {
		return this.abstractGetElement(element.getValue().negate());
	}

	@Override
	protected ZElement abstractMultiply(ZElement element1, ZElement element2) {
		return this.abstractGetElement(element1.getValue().multiply(element2.getValue()));
	}

	@Override
	protected ZElement abstractGetOne() {
		return this.abstractGetElement(BigInteger.ONE);
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return Group.INFINITE_ORDER;
	}

	@Override
	protected ZElement abstractGetElement(BigInteger value) {
		return new ZElement(this, value);
	}

	@Override
	protected ZElement abstractGetRandomElement(Random random) {
		throw new UnsupportedOperationException();
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		return true;
	}

	@Override
	protected ZElement abstractGetDefaultGenerator() {
		return this.abstractGetElement(BigInteger.ONE);
	}

	@Override
	protected boolean abstractIsGenerator(final ZElement element) {
		return element.getValue().abs().equals(BigInteger.ONE);
	}

	//
	// STATIC FACTORY METHODS
	//
	private static Z instance;

	/**
	 * Returns the singleton object of this class.
	 * <p>
	 * @return The singleton object of this class
	 */
	public static Z getInstance() {
		if (Z.instance == null) {
			Z.instance = new Z();
		}
		return Z.instance;
	}

}
