package ch.bfh.unicrypt.crypto.encoder;

import ch.bfh.unicrypt.crypto.encoder.classes.ZModPrimeToECPolynomialField;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECPolynomialField;
import ch.bfh.unicrypt.math.algebra.additive.parameters.SEC2_PolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ZModToECPolynomialFieldTest {

	@Test
	public void encodeDecodeTest() throws Exception {
		ECPolynomialField ec = ECPolynomialField.getInstance(SEC2_PolynomialField.sect113r1);
		ZModPrime zmod = ec.getZModOrder();
		Encoder enc = ZModPrimeToECPolynomialField.getInstance(zmod, ec, 15);

		ZModElement message = zmod.getElement(167);
		Element encMessage = enc.encode(message);
		Element decMessage = enc.decode(encMessage);
		assertEquals(message, decMessage);

	}

}
