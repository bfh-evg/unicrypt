package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;

public class ZModToZmodPrime extends AbstractEncoder<ZMod, ZModElement, ZModPrime, ZModElement> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 191230672695611571L;
	private final ZMod zMod;
	private final ZModPrime zModPrime;
	
	private ZModToZmodPrime(ZMod zMod, ZModPrime zModPrime) {
		super();
		this.zMod=zMod;
		this.zModPrime=zModPrime;
		
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new EncodingFunction(this.zMod, this.zModPrime);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new DecodingFunction(this.zModPrime, this.zMod);
	}
	
	public static ZModToZmodPrime getInstance(final ZMod zMod, final ZModPrime zModPrime) {
		if (zModPrime == null || zMod == null) {
			throw new IllegalArgumentException();
		}
		return new ZModToZmodPrime(zMod, zModPrime);
	}
	
	static class EncodingFunction extends AbstractFunction<EncodingFunction, ZMod, ZModElement, ZModPrime, ZModElement>{

		/**
		 * 
		 */
		private static final long serialVersionUID = -8347692169255785315L;

		protected EncodingFunction(ZMod domain, ZModPrime coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ZModElement abstractApply(ZModElement element,
				RandomByteSequence randomByteSequence) {
			
			return this.getCoDomain().getElement(element.getBigInteger());
		}
		
	}
	
	static class DecodingFunction extends AbstractFunction<DecodingFunction, ZModPrime, ZModElement, ZMod, ZModElement>{



		protected DecodingFunction(ZModPrime domain, ZMod coDomain) {
			super(domain, coDomain);
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected ZModElement abstractApply(ZModElement element,
				RandomByteSequence randomByteSequence) {
			return this.getCoDomain().getElement(element.getValue());
		}
		
	}

}
