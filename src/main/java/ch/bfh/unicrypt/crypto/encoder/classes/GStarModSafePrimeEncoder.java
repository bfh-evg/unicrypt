package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.math.BigInteger;
import java.util.Random;

public class GStarModSafePrimeEncoder
			 extends AbstractEncoder<ZModPrime, ZModElement, GStarModSafePrime, GStarModElement> {

	private final GStarModSafePrime gStarModSafePrime;

	protected GStarModSafePrimeEncoder(GStarModSafePrime gStarModSafePrime) {
		this.gStarModSafePrime = gStarModSafePrime;
	}

	public GStarModSafePrime getGStarModSafePrime() {
		return this.gStarModSafePrime;
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new EncodingFunction((ZModPrime) this.getGStarModSafePrime().getZModOrder(), this.getGStarModSafePrime());
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new DecodingFunction(this.getGStarModSafePrime(), (ZModPrime) this.getGStarModSafePrime().getZModOrder());
	}

	public static GStarModSafePrimeEncoder getInstance(GStarModSafePrime gStarModSafePrime) {
		if (gStarModSafePrime == null) {
			throw new IllegalArgumentException();
		}
		return new GStarModSafePrimeEncoder(gStarModSafePrime);
	}

	static class EncodingFunction
				 extends AbstractFunction<ZModPrime, ZModElement, GStarModSafePrime, GStarModElement> {

		protected EncodingFunction(final ZModPrime domain, final GStarMod coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected GStarModElement abstractApply(final ZModElement element, final Random random) {
			final BigInteger value = element.getValue().add(BigInteger.ONE);
			final GStarModSafePrime coDomain = this.getCoDomain();
			if (coDomain.contains(value)) {
				return coDomain.getElement(value);
			}
			return coDomain.getElement(coDomain.getModulus().subtract(value));
		}

		@Override
		protected boolean abstractIsEqual(Function function) {
			return true;
		}

	}

	static class DecodingFunction
				 extends AbstractFunction<GStarModSafePrime, GStarModElement, ZModPrime, ZModElement> {

		protected DecodingFunction(final GStarMod domain, final ZModPrime coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ZModElement abstractApply(final GStarModElement element, final Random random) {
			final BigInteger value = element.getValue();
			final GStarModSafePrime domain = this.getDomain();
			if (value.compareTo(domain.getOrder()) <= 0) {
				return this.getCoDomain().getElement(value.subtract(BigInteger.ONE));
			}
			return this.getCoDomain().getElement((domain.getModulus().subtract(value)).subtract(BigInteger.ONE));
		}

		@Override
		protected boolean abstractIsEqual(Function function) {
			return true;
		}

	}

}
