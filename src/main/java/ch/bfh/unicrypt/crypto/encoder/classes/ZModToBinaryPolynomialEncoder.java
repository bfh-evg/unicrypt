package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.BigIntegerToByteArray;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.BinaryPolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModTwo;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;
import java.math.BigInteger;

/**
 * Encodes a ZModElement as a binary polynomial by taking the bit-array representation of the ZModElement to create the
 * polynomial
 * <p>
 * @author lutzch
 * <p>
 */
public class ZModToBinaryPolynomialEncoder
	   extends AbstractEncoder<ZMod, ZModElement, BinaryPolynomialField, PolynomialElement<ZModTwo>> {

	private final ZMod zMod;
	private final BinaryPolynomialField binaryPolynomial;
	private final BigIntegerToByteArray converter;

	public ZModToBinaryPolynomialEncoder(ZMod zMod, BinaryPolynomialField binaryPolynomial) {
		this.zMod = zMod;
		this.binaryPolynomial = binaryPolynomial;
		this.converter = BigIntegerToByteArray.getInstance();
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new EncodingFunction(this.zMod, this.binaryPolynomial);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new DecodingFunction(this.binaryPolynomial, this.zMod);
	}

	public static ZModToBinaryPolynomialEncoder getInstance(ZMod zMod, BinaryPolynomialField polynomialField) {
		if (zMod == null || polynomialField == null) {
			throw new IllegalArgumentException();
		}
		return new ZModToBinaryPolynomialEncoder(zMod, polynomialField);
	}

	class EncodingFunction
		   extends AbstractFunction<EncodingFunction, ZMod, ZModElement, BinaryPolynomialField, PolynomialElement<ZModTwo>> {

		protected EncodingFunction(final ZMod domain, final BinaryPolynomialField coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected PolynomialElement<ZModTwo> abstractApply(final ZModElement element, final RandomByteSequence randomByteSequence) {
			final BigInteger value = element.getValue();
			final BinaryPolynomialField coDomain = this.getCoDomain();

			return coDomain.getElementFromBitString(value.toString(2));

		}

	}

	class DecodingFunction
		   extends AbstractFunction<DecodingFunction, BinaryPolynomialField, PolynomialElement<ZModTwo>, ZMod, ZModElement> {

		protected DecodingFunction(final BinaryPolynomialField domain, final ZMod coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ZModElement abstractApply(final PolynomialElement<ZModTwo> element, final RandomByteSequence randomByteSequence) {
			ByteArray arr = element.getValue().getCoefficients();
			final String value = new StringBuilder(arr.toBitString()).reverse().toString();
			BigInteger res = new BigInteger(value, 2);
			return this.getCoDomain().getElement(res);
//			return this.getCoDomain().getElement(converter.convert(arr));
		}

	}

}
