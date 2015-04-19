package ch.bfh.unicrypt.crypto.encoder.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.crypto.encoder.exceptions.ProbabilisticEncodingException;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECPolynomialField;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModPrime;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.EC;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.ECElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;

public class ECEncoder extends AbstractEncoder<ZModPrime, ZModElement, EC, ECElement> {

	private ZModPrime zModPrime;
	private EC ec;
	private AbstractEncoder encoder;
	private int shift;
	
	
	
	public ECEncoder(ZModPrime zModPrime, EC ec,int shift) {
		super();
		this.zModPrime = zModPrime;
		this.ec = ec;
		this.shift=shift;
		
		if(ECPolynomialField.class.isInstance(ec)){
			ZMod zmod=ZMod.getInstance(ec.getOrder());
			encoder=ZModToBinaryPolynomialEncoder.getInstance(zmod,(PolynomialField) ec.getFiniteField());
		}
		else if(ECZModPrime.class.isInstance(ec)){
			encoder=ZModToZmodPrime.getInstance((ZMod)ec.getFiniteField(), (ZModPrime)ec.getFiniteField());
		}
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new EncodingFunction(this.zModPrime, this.ec,this.shift, encoder);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new DecodingFunction(ec, zModPrime, this.shift, encoder);
	}
	
	public static ECEncoder getInstance(ZModPrime zModPrime, EC ec, int shift){
		return new ECEncoder(zModPrime, ec, shift);
	}
	
	static class EncodingFunction extends AbstractFunction<EncodingFunction, ZModPrime, ZModElement, EC, ECElement>{
		private int shift;
		private AbstractEncoder<ZMod, ZModElement, FiniteField, DualisticElement> encoder;
		protected EncodingFunction(ZModPrime domain, EC coDomain,int shift, AbstractEncoder<ZMod, ZModElement, FiniteField, DualisticElement> encoder) {
			super(domain, coDomain);
			this.shift=shift;
			this.encoder=encoder;
		}

		@Override
		protected ECElement abstractApply(ZModElement element,
				RandomByteSequence randomByteSequence) {
			
			boolean firstOption = true;
			
			ZModPrime zModPrime = this.getDomain();
			EC ec = this.getCoDomain();

			int msgSpace = zModPrime.getOrder().toString(2).length();
			int msgBitLength = element.getValue().toString(2).length() + 2;

			BigInteger c;

			if (msgSpace / 3 > msgBitLength) {
				c = BigInteger.ZERO;
				this.shift = msgSpace / 3 * 2;
			} else if (msgSpace / 2 > msgBitLength) {
				c = BigInteger.ONE;
				this.shift = msgSpace / 2;
			} else if (msgSpace / 3 * 2 > msgBitLength) {
				c = new BigInteger("2");
				this.shift = msgSpace / 3;
			} else {
				c = new BigInteger("3");
			}

			BigInteger e = element.getValue();
			e = e.shiftLeft(shift + 2);
			e = e.add(c);

			ZModElement zModElement = this.getDomain().getElement(e);
			DualisticElement x = encoder.encode(zModElement);
			ZModElement stepp = zModPrime.getElement(4);
			
			int count = 0;
			while (!ec.contains(x)) {
				if (count >= (1 << shift)) {
					firstOption=false;
				}

				zModElement = zModElement.add(stepp);
				
				x = encoder.encode(zModElement);
				count++;

			}

			if (firstOption) {
				ECElement[] y = ec.getY(x);
				DualisticElement y1 = y[0].getY();
				DualisticElement y2 = y[1].getY();
				if (isBigger(y1, y2)) {
					return y[0];

				}
				return y[1];
			} else {

				zModElement = element.invert();
				msgBitLength = zModElement.getValue().toString(2).length();

				if (msgSpace / 3 > msgBitLength) {
					c = BigInteger.ZERO;
					this.shift = msgSpace / 3 * 2;
				} else if (msgSpace / 2 > msgBitLength) {
					c = BigInteger.ONE;
					this.shift = msgSpace / 2;
				} else if (msgSpace / 3 * 2 > msgBitLength) {
					c = new BigInteger("2");
					this.shift = msgSpace / 3;
				} else {
					c = new BigInteger("3");
				}

				e = zModElement.getValue();
				e = e.shiftLeft(shift + 2);
				e = e.add(c);

				x = encoder.encode(zModElement);

				count = 0;
				while (!ec.contains(x)) {
					if (count >= (1 << shift)) {
						throw new ProbabilisticEncodingException(e + " can not be encoded");
					}

					zModElement = zModElement.add(stepp);
					x = encoder.encode(zModElement);
					count++;

				}

				ECElement[] y = ec.getY(x);
				DualisticElement y1 = y[0].getY();
				DualisticElement y2 = y[1].getY();

				if (isBigger(y1, y2)) {
					return y[1];
				}
				return y[0];
			}

		}
		
	}
	
	static class DecodingFunction extends AbstractFunction<DecodingFunction, EC, ECElement, ZModPrime, ZModElement>{

		private int shift;
		private AbstractEncoder<ZMod, ZModElement, FiniteField, DualisticElement> encoder;
		
		protected DecodingFunction(Set domain, Set coDomain, int shift, AbstractEncoder<ZMod, ZModElement, FiniteField, DualisticElement> encoder) {
			super(domain, coDomain);	
			this.shift=shift;
			this.encoder=encoder;
		
		
		}

		@Override
		protected ZModElement abstractApply(ECElement element,
				RandomByteSequence randomByteSequence) {
			ZModPrime zModPrime = this.getCoDomain();
			EC ec = this.getDomain();
			int msgSpace = zModPrime.getOrder().toString(2).length();

			DualisticElement x = element.getX();
			DualisticElement y = element.getY();
			DualisticElement y1 = element.invert().getY();

			BigInteger x1 = encoder.decode(x).getBigInteger();

			BigInteger c = x1.subtract(x1.shiftRight(2).shiftLeft(2));

			if (c.equals(BigInteger.ZERO)) {
				this.shift = msgSpace / 3 * 2;
			} else if (c.equals(BigInteger.ONE)) {
				this.shift = msgSpace / 2;
			} else if (c.equals(new BigInteger("2"))) {
				this.shift = msgSpace / 3;
			}

			x1 = x1.shiftRight(shift + 2);

			if (y.isEquivalent(getBiggerY(y, y1))) {
				return zModPrime.getElement(x1);
			} else {
				return zModPrime.getElement(x1).invert();
			}
		}
		
	}
	
	public static DualisticElement getBiggerY(DualisticElement y1, DualisticElement y2){
		return y1;
	}
	
	public static boolean isBigger(DualisticElement y1, DualisticElement y2){
		return y1.isEquivalent(getBiggerY(y1, y2));
	}

}
