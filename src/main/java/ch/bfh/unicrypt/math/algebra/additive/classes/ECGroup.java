package ch.bfh.unicrypt.math.algebra.additive.classes;

import java.math.BigInteger;
import java.util.Random;
import ch.bfh.unicrypt.math.algebra.additive.abstracts.AbstractAdditiveCyclicGroup;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.utility.MathUtil;

public abstract class ECGroup extends
		AbstractAdditiveCyclicGroup<ECGroupElement> {
	private FiniteField finiteField;
	private ECGroupElement generator;
	private DualisticElement a, b;
	private BigInteger order, h;
	private final ECGroupElement Identity;
	private final DualisticElement zero = null;

	protected ECGroup(FiniteField Finitefiled, DualisticElement a,
			DualisticElement b, DualisticElement gx, DualisticElement gy,
			BigInteger order, BigInteger h) {
		super();
		this.a = a;
		this.b = b;
		this.order = order;
		this.h = h;
		this.finiteField = Finitefiled;
		this.Identity = this.getElement(zero, zero);
		this.generator = this.getElement(MathUtil.elegantPair(gx.getValue(),
				gy.getValue()));
		if (!this.isGenerator(generator)) {
			throw new IllegalArgumentException("Point " + generator.toString()
					+ " is not a generator");
		}

	}

	@Override
	protected ECGroupElement abstractGetDefaultGenerator() {
		return this.generator;
	}

	@Override
	protected boolean abstractIsGenerator(Element element) {
		ECGroupElement e = (ECGroupElement) element;
		e = e.selfApply(order);
		return MathUtil.isPrime(this.order) && e.isZero();
	}

	@Override
	protected ECGroupElement abstractInvert(Element element) {
		ECGroupElement r = (ECGroupElement) element;

		if (r.isZero()) {
			return this.Identity;
		}

		return new ECGroupElement(this, r.getX(), r.getY().invert());
	}

	@Override
	protected ECGroupElement abstractGetIdentityElement() {
		return this.Identity;
	}


	@Override
	protected BigInteger abstractGetOrder() {
		return this.order;
	}

	@Override
	protected ECGroupElement abstractGetElement(BigInteger value) {
		if (value.equals(zero)) {
			return this.Identity;
		} else {
			BigInteger[] result = MathUtil.elegantUnpair(value);
			DualisticElement x = this.getFiniteField().getElement(result[0]);
			DualisticElement y = this.getFiniteField().getElement(result[1]);

			if (y.power(2).equals(x.power(3).add(x.multiply(a)).add(b))) {
				return new ECGroupElement(this, x, y);
			} else {
				throw new IllegalArgumentException("(" + x + "," + y
						+ ") is not a point on the elliptic curve");
			}
		}
	}

	public ECGroupElement getElement(DualisticElement x, DualisticElement y) {
		if (x == zero && y == zero) {
			return new ECGroupElement(this, x, y);
		} else if (x == zero || y == zero) {
			throw new IllegalArgumentException("One coordinate is zero");
		} else {
			return this.abstractGetElement(MathUtil.elegantPair(x.getValue(),
					y.getValue()));
		}
	}

	@Override
	protected ECGroupElement abstractGetRandomElement(Random random) {
		if (generator != null) {
			DualisticElement r = this.finiteField.getRandomElement(random);
			return this.generator.selfApply(r);
		} else {
			throw new IllegalArgumentException("Get Randomelement without generator not supported yet.");
		}

	}

	@Override
	protected boolean abstractContains(BigInteger value) {
		BigInteger[] result = MathUtil.elegantUnpair(value);
		DualisticElement x = this.getFiniteField().getElement(result[0]);
		DualisticElement y = this.getFiniteField().getElement(result[1]);
		return y.power(2).equals(
				x.power(3).add(x.multiply(this.getA())).add(this.b));
	}

	public FiniteField getFiniteField() {
		return finiteField;
	}

	public DualisticElement getA() {
		return a;
	}

	public DualisticElement getB() {
		return b;
	}

	public BigInteger getH() {
		return h;
	}

	public Boolean contains(DualisticElement x, DualisticElement y) {
		y = y.power(2);
		x = x.power(3).add(x.multiply(a)).add(b);

		return y.equals(x);
	}

	@Override
	public boolean isZeroElement(Element element) {
		ECGroupElement e = (ECGroupElement) element;
		return e.getX() == null && e.getY() == null;
	}

}
