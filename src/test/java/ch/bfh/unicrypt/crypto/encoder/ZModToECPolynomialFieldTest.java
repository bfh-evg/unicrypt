package ch.bfh.unicrypt.crypto.encoder;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.bfh.unicrypt.crypto.encoder.classes.ZModToBinaryPolynomialEncoder;
import ch.bfh.unicrypt.crypto.encoder.classes.ZModPrimeToECPolynomialFieldEncoder;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECPolynomialElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECPolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.params.classes.SECECCParamsF2m;

public class ZModToECPolynomialFieldTest {

	@Test
	public void encodeDecodeTest() throws Exception {
		ECPolynomialField ec=ECPolynomialField.getInstance(SECECCParamsF2m.sect113r1);
		ZModPrime zmod=ec.getZModOrder();
		Encoder enc=ZModPrimeToECPolynomialFieldEncoder.getInstance(zmod, ec, 15);
		
		ZModElement message=zmod.getElement(167);
		Element encMessage=enc.encode(message);
		Element decMessage=enc.decode(encMessage);
		assertEquals(message, decMessage);
		
	}

}
