/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractSet;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarModPrime;
import ch.bfh.unicrypt.math.utility.RandomUtil;
import java.math.BigInteger;
import java.util.Random;

/**
 * This interface represents the group that consists of two elements only, for example TRUE and FALSE. This group is
 * isomorphic to the additive group of integers modulo 2. It is therefore possible to consider and implement it as a
 * specialization of {@link ZPlusMod}.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class BooleanSet
			 extends AbstractSet<BooleanElement> {

	public static final BooleanElement TRUE = BooleanSet.getInstance().getElement(true);
	public static final BooleanElement FALSE = BooleanSet.getInstance().getElement(false);

	private BooleanSet() {
	}

	/**
	 * Creates and returns the group element that corresponds to a given Boolean value.
	 * <p>
	 * @param value The given Boolean value
	 * @return The corresponding group element
	 */
	public final BooleanElement getElement(final boolean bit) {
		return new BooleanElement(this, bit);
	}

	@Override
	protected ZModPrime standardGetZModOrder() {
		return ZModPrime.getInstance(this.getOrder());
	}

	@Override
	protected ZStarModPrime standardGetZStarModOrder() {
		return ZStarModPrime.getInstance(this.getOrder());
	}

	@Override
	protected BigInteger abstractGetOrder() {
		return BigInteger.valueOf(2);
	}

	@Override
	protected BooleanElement abstractGetElement(BigInteger value) {
		return this.getElement(value.equals(BigInteger.ONE));
	}

	@Override
	protected BooleanElement abstractGetRandomElement(Random random) {
		return this.getElement(RandomUtil.getRandomBoolean(random));
	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		return value.equals(BigInteger.ZERO) || value.equals(BigInteger.ONE);
	}
  //
	// STATIC FACTORY METHODS
	//
	private static BooleanSet instance;

	/**
	 * Returns the singleton object of this class.
	 * <p>
	 * @return The singleton object of this class
	 */
	public static BooleanSet getInstance() {
		if (BooleanSet.instance == null) {
			BooleanSet.instance = new BooleanSet();
		}
		return BooleanSet.instance;
	}

}
