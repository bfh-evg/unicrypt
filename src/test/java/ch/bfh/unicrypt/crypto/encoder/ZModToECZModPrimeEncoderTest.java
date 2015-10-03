package ch.bfh.unicrypt.crypto.encoder;

import static org.junit.Assert.*;

import org.junit.Test;

import ch.bfh.unicrypt.crypto.encoder.classes.ZModPrimeToECZModPrime;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModPrime;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.additive.parameters.ECZModPrimeParameters;

public class ZModToECZModPrimeEncoderTest {

	@Test
	public void encodeDecodeTest() throws Exception {
		ECZModPrime cyclicGroup = ECZModPrime.getInstance(ECZModPrimeParameters.SECP521r1);
		Encoder encoder = ZModPrimeToECZModPrime.getInstance(cyclicGroup, 15);
		Element message = encoder.getDomain().getElementFrom(278);
		Element encMessage = encoder.encode(message);
		Element decMessage = encoder.decode(encMessage);

		assertEquals(message, decMessage);
	}

}
