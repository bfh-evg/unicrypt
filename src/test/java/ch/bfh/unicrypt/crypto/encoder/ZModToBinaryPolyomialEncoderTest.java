package ch.bfh.unicrypt.crypto.encoder;

import ch.bfh.unicrypt.crypto.encoder.classes.ZModToBinaryPolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.PolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModTwo;
import java.math.BigInteger;
import static org.junit.Assert.*;
import org.junit.Test;

public class ZModToBinaryPolyomialEncoderTest {

	@Test
	public void EncodingDecodingTest() {
		ZMod zmod=ZMod.getInstance(new BigInteger("5192296858534827689835882578830703"));
		PolynomialField polynomialField=PolynomialField.getInstance(ZModTwo.getInstance(), 113);
		ZModToBinaryPolynomialField enc = ZModToBinaryPolynomialField.getInstance(zmod, polynomialField);
		ZModElement element=zmod.getElement(new BigInteger("4986819005910345345662976"));
		PolynomialElement encElement=enc.encode(element);
		ZModElement decElement=enc.decode(encElement);
		assertEquals(element, decElement);
	}

}
