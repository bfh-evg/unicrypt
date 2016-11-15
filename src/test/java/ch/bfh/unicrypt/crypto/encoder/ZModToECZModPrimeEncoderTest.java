package ch.bfh.unicrypt.crypto.encoder;

import ch.bfh.unicrypt.crypto.encoder.classes.ZModToECZModPrime;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModPrime;
import ch.bfh.unicrypt.math.algebra.additive.parameters.ECZModPrimeParameters;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ZModToECZModPrimeEncoderTest {

	@Test
	public void encodeDecodeTest() {

		List<ZModToECZModPrime> encoders = new ArrayList();

		ECZModPrime ec = ECZModPrime.getInstance(ECZModPrimeParameters.TEST11);
		encoders.add(ZModToECZModPrime.getInstance(ec, 5));
		encoders.add(ZModToECZModPrime.getInstance(ec, 6));
		encoders.add(ZModToECZModPrime.getInstance(ec, 9));
		encoders.add(ZModToECZModPrime.getInstance(ec, 10));
		encoders.add(ZModToECZModPrime.getInstance(ec, 11));
		encoders.add(ZModToECZModPrime.getInstance(ec));
		ec = ECZModPrime.getInstance(ECZModPrimeParameters.TEST23);
		encoders.add(ZModToECZModPrime.getInstance(ec, 7));
		encoders.add(ZModToECZModPrime.getInstance(ec, 21));
		encoders.add(ZModToECZModPrime.getInstance(ec, 22));
		encoders.add(ZModToECZModPrime.getInstance(ec, 23));
		encoders.add(ZModToECZModPrime.getInstance(ec));
		ec = ECZModPrime.getInstance(ECZModPrimeParameters.TEST29);
		encoders.add(ZModToECZModPrime.getInstance(ec, 4));
		encoders.add(ZModToECZModPrime.getInstance(ec, 5));
		encoders.add(ZModToECZModPrime.getInstance(ec, 6));
		encoders.add(ZModToECZModPrime.getInstance(ec, 27));
		encoders.add(ZModToECZModPrime.getInstance(ec, 28));
		encoders.add(ZModToECZModPrime.getInstance(ec, 39));
		encoders.add(ZModToECZModPrime.getInstance(ec));
		ec = ECZModPrime.getInstance(ECZModPrimeParameters.SECP160k1);
		encoders.add(ZModToECZModPrime.getInstance(ec, 8));
		encoders.add(ZModToECZModPrime.getInstance(ec));
		ec = ECZModPrime.getInstance(ECZModPrimeParameters.SECP521r1);
		encoders.add(ZModToECZModPrime.getInstance(ec, 8));
		encoders.add(ZModToECZModPrime.getInstance(ec));

		for (ZModToECZModPrime encoder : encoders) {
			for (ZModElement element : encoder.getDomain().getElements().limit(100)) {
				ECZModElement encodedElement = encoder.encode(element);
				ZModElement decodedElement = encoder.decode(encodedElement);
				assertEquals(element, decodedElement);
			}
		}
	}

}
