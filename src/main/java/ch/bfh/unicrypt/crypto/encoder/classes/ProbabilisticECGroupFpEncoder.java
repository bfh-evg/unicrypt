package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModPrime;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModPrimeElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Random;

public class ProbabilisticECGroupFpEncoder
			 extends AbstractEncoder<ZModPrime, ECZModPrime, ZModElement, ECZModPrimeElement> {

	protected static final int shift = 10;

	private ECZModPrime ec;

	protected ProbabilisticECGroupFpEncoder(ECZModPrime ec) {
		this.ec = ec;
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new ECEncodingFunction(this.ec.getFiniteField(), this.ec);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new ECDecodingFunction(ec, this.ec.getFiniteField());
	}

	public static ProbabilisticECGroupFpEncoder getInstance(final ECZModPrime ec) {
		if (ec == null) {
			throw new IllegalArgumentException();
		}
		return new ProbabilisticECGroupFpEncoder(ec);
	}

	static class ECEncodingFunction
				 extends AbstractFunction<ZModPrime, ECZModPrime, ECZModPrimeElement> {

		protected ECEncodingFunction(ZModPrime domain, ECZModPrime coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ECZModPrimeElement abstractApply(Element element,
					 Random random) {
			ZModPrime zModPrime = this.getDomain();
			ECZModPrime ecPrime = this.getCoDomain();

			BigInteger e = element.getValue();
			e = e.shiftLeft(10);

			if (!zModPrime.contains(e)) {

				throw new IllegalArgumentException(e + " can not be encoded");
			}

			ZModElement x = zModPrime.getElement(e);
			final ZModElement ONE = zModPrime.getElement(1);

			int count = 0;
			while (!ecPrime.contains(x)) {
				if (count >= 1024) {
					throw new IllegalArgumentException("No encoding was found");
				}
				x = x.add(ONE);
				count++;
			}
			ZModElement y1 = x.power(3).add(ecPrime.getA().multiply(x))
						 .add(ecPrime.getB());
			ZModElement y = zModPrime.getElement(MathUtil.sqrtModPrime(
						 y1.getValue(), zModPrime.getModulus()));
			return ecPrime.getElement(x, y);
		}

	}

	static class ECDecodingFunction
				 extends AbstractFunction<ECZModPrime, ZModPrime, ZModElement> {

		protected ECDecodingFunction(ECZModPrime domain, ZModPrime coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ZModElement abstractApply(Element element, Random random) {
			ECZModPrimeElement e = (ECZModPrimeElement) element;
			BigInteger x1 = e.getX().getValue();
			x1 = x1.shiftRight(10);
			return this.getCoDomain().getElement(x1);
		}

	}

}
