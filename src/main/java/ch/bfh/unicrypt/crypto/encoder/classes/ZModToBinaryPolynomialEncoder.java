package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.helper.array.classes.BooleanArray;
import ch.bfh.unicrypt.helper.converter.classes.booleanarray.ByteArrayToBooleanArray;
import ch.bfh.unicrypt.helper.converter.classes.bytearray.BigIntegerToByteArray;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
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
	   extends AbstractEncoder<ZMod, ZModElement, PolynomialField, PolynomialElement> {

	private final ZMod zMod;
	private final PolynomialField binaryPolynomial;
	private final BigIntegerToByteArray converter1;
	private final ByteArrayToBooleanArray converter2;

	private ZModToBinaryPolynomialEncoder(ZMod zMod, PolynomialField binaryPolynomial) {
		this.zMod = zMod;
		this.binaryPolynomial = binaryPolynomial;
		this.converter1 = BigIntegerToByteArray.getInstance();
		this.converter2 = ByteArrayToBooleanArray.getInstance();
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return new EncodingFunction(this.zMod, this.binaryPolynomial);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new DecodingFunction(this.binaryPolynomial, this.zMod);
	}

	public static ZModToBinaryPolynomialEncoder getInstance(ZMod zMod, PolynomialField polynomialField) {
		if (zMod == null || polynomialField == null) {
			throw new IllegalArgumentException();
		}
		return new ZModToBinaryPolynomialEncoder(zMod, polynomialField);
	}

	class EncodingFunction
		   extends AbstractFunction<EncodingFunction, ZMod, ZModElement, PolynomialField, PolynomialElement> {

		protected EncodingFunction(final ZMod domain, final PolynomialField coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected PolynomialElement abstractApply(final ZModElement element, final RandomByteSequence randomByteSequence) {
			BooleanArray booleanArray = converter2.convert(converter1.convert(element.getValue()));
			return this.getCoDomain().getElement(booleanArray);
		}

	}

	class DecodingFunction
		   extends AbstractFunction<DecodingFunction, PolynomialField, PolynomialElement, ZMod, ZModElement> {

		protected DecodingFunction(final PolynomialField domain, final ZMod coDomain) {
			super(domain, coDomain);
		}

		@Override
		protected ZModElement abstractApply(final PolynomialElement element, final RandomByteSequence randomByteSequence) {
			BooleanArray booleanArray = element.getValue().getCoefficients();
			BigInteger bigInteger = converter1.reconvert(converter2.reconvert(booleanArray));
			return this.getCoDomain().getElement(bigInteger.mod(this.getCoDomain().getModulus()));
		}

	}

}
