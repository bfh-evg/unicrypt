package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;
import java.util.Random;

/**
 * This class represents the concept of an identity function f:X->Z_2 with f(x)=x mod N for all elements x in X.
 * <p>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class ModuloFunction
			 extends AbstractFunction<Set, ZMod, ZModElement> {

	private final BigInteger modulus;

	private ModuloFunction(final Set domain, final ZMod coDomain) {
		super(domain, coDomain);
		this.modulus = coDomain.getModulus();
	}

	public BigInteger getModulus() {
		return this.modulus;
	}

	@Override
	protected boolean standardIsEqual(Function function) {
		return this.getModulus().equals(((ModuloFunction) function).getModulus());
	}

	//
	// The following protected method implements the abstract method from {@code AbstractFunction}
	//
	@Override
	protected ZModElement abstractApply(final Element element, final Random random) {
		return this.getCoDomain().getElement(element.getValue().mod(this.getModulus()));
	}

	//
	// STATIC FACTORY METHODS
	//
	/**
	 * This is the standard constructor for this class. It creates an identity function for a given group.
	 * <p>
	 * @param domain  The given Group
	 * @param modulus
	 * @return
	 * @throws IllegalArgumentException if the group is null
	 */
	public static ModuloFunction getInstance(final Set domain, BigInteger modulus) {
		if (domain == null || modulus == null) {
			throw new IllegalArgumentException();
		}
		return new ModuloFunction(domain, ZMod.getInstance(modulus));
	}

	public static ModuloFunction getInstance(final Set domain, ZMod coDomain) {
		if (domain == null || coDomain == null) {
			throw new IllegalArgumentException();
		}
		return new ModuloFunction(domain, coDomain);
	}

}
