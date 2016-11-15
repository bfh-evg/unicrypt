package ch.bfh.unicrypt.crypto.encoder;

import ch.bfh.unicrypt.crypto.encoder.classes.ZModToECPolynomialField;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECPolynomialField;
import ch.bfh.unicrypt.math.algebra.additive.parameters.ECPolynomialFieldParameters;
import org.junit.Test;

public class ZModToECPolynomialFieldTest {

	@Test
	public void encodeDecodeTest() throws Exception {
		ECPolynomialFieldParameters parameters = ECPolynomialFieldParameters.SECT113r1;
		ECPolynomialField ec = ECPolynomialField.getInstance(parameters);
		Encoder enc = ZModToECPolynomialField.getInstance(ec, 15);
//
//		ZModElement message = zmod.getElement(167);
//		Element encMessage = enc.encode(message);
//		Element decMessage = enc.decode(encMessage);
//		assertEquals(message, decMessage);

	}

}
