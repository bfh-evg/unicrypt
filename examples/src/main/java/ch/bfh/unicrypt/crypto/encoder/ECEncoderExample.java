package ch.bfh.unicrypt.crypto.encoder;

import ch.bfh.unicrypt.Example;
import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.crypto.encoder.classes.ECEncoder;
import ch.bfh.unicrypt.crypto.encoder.classes.ZModToBinaryPolynomialEncoder;
import ch.bfh.unicrypt.crypto.encoder.classes.ZModToZmodPrime;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECPolynomialField;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.params.classes.SECECCParamsF2m;
import ch.bfh.unicrypt.math.algebra.params.classes.SECECCParamsFp;

public class ECEncoderExample {

	
	/**
	 * Example shows how to encode an element from ZModPrime into an elliptic curve over Fq
	 * <p>
	 * @throws Exception
	 */
	public static void example1() throws Exception {
		
		
		ECZModPrime ecFp = ECZModPrime.getInstance(SECECCParamsFp.secp521r1);
		ZModPrime zModPrime=ecFp.getFiniteField();
		AbstractEncoder<ZMod, ZModElement, ZModPrime, ZModElement> enc=ZModToZmodPrime.getInstance(zModPrime, zModPrime);
		Encoder encoder = ECEncoder.getInstance(zModPrime, ecFp, 10);
		
		Element message = encoder.getDomain().getElementFrom(278);
		Element encMessage=encoder.encode(message);
		Element decMessage=encoder.decode(encMessage);
		
		System.out.println(message);
		System.out.println(decMessage);
	}
	
	/**
	 * Example shows how to encode an element from ZModPrime into an elliptic curve over F2m
	 * <p>
	 * @throws Exception
	 */
	public static void example2() throws Exception {
		
		ECPolynomialField ecF2m=ECPolynomialField.getInstance(SECECCParamsF2m.sect113r1);
		ZModPrime zMod=ecF2m.getZModOrder();
		AbstractEncoder<ZMod, ZModElement, PolynomialField, PolynomialElement> enc=ZModToBinaryPolynomialEncoder.getInstance(zMod, ecF2m.getFiniteField());
		Encoder encoder = ECEncoder.getInstance(zMod, ecF2m, 10);
		
		
		Element message = encoder.getDomain().getElementFrom(278);
		Element encMessage=encoder.encode(message);
		Element decMessage=encoder.decode(encMessage);
		
		System.out.println(message);
		System.out.println(decMessage);
	}
	
	public static void main(final String[] args) {
		Example.runExamples();
	}
	
}
