package ch.bfh.unicrypt.crypto.encoder.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.helper.Alphabet;
import ch.bfh.unicrypt.helper.array.classes.ByteArray;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.BinaryPolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModTwo;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.random.interfaces.RandomByteSequence;

public class ZModToBinaryPolynomialEncoder extends AbstractEncoder<ZMod, ZModElement, BinaryPolynomialField, PolynomialElement<ZModTwo>> {
 
	private ZMod zMod;
	private BinaryPolynomialField binaryPolynomial;
	
	public ZModToBinaryPolynomialEncoder(ZMod zMod,BinaryPolynomialField binaryPolynomial) {
	 this.binaryPolynomial=binaryPolynomial;
	 this.zMod=zMod;
	}
	

	@Override
	protected Function abstractGetEncodingFunction() {
		return new EncodingFunction(zMod, binaryPolynomial);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return new DecodingFunction(getCoDomain(), getDomain());
	}
	
	public static ZModToBinaryPolynomialEncoder getInstance(ZMod zMod, BinaryPolynomialField binarypolynomial) {

		return new ZModToBinaryPolynomialEncoder(zMod, binarypolynomial);
	}
	
	
	
	static class EncodingFunction
	   extends AbstractFunction<EncodingFunction, ZMod, ZModElement, BinaryPolynomialField, PolynomialElement<ZModTwo>> {

	protected EncodingFunction(final ZMod domain, final BinaryPolynomialField coDomain) {
		super(domain, coDomain);
	}

	@Override
	protected PolynomialElement<ZModTwo> abstractApply(final ZModElement element, final RandomByteSequence randomByteSequence) {
		final BigInteger value = element.getValue().getBigInteger();
		final BinaryPolynomialField coDomain = this.getCoDomain();
		
		return coDomain.getElementFromBitString(value.toString(2));
		
		
	}

}
	
	static class DecodingFunction
	   extends AbstractFunction<DecodingFunction, BinaryPolynomialField, PolynomialElement<ZModTwo>, ZMod, ZModElement> {

	protected DecodingFunction(final BinaryPolynomialField domain, final ZMod coDomain) {
		super(domain, coDomain);
	}

	@Override
	protected ZModElement abstractApply(final PolynomialElement<ZModTwo> element, final RandomByteSequence randomByteSequence) {
		StringMonoid monoid=StringMonoid.getInstance(Alphabet.BINARY);
		ByteArray arr=element.getValue().getCoefficients();
		final String value= new StringBuilder(arr.toBitString()).reverse().toString();
		final PolynomialField<ZModTwo> domain = this.getDomain();
		BigInteger res=new BigInteger(value,2);
		return this.getCoDomain().getElement(res);
	}

}

}
