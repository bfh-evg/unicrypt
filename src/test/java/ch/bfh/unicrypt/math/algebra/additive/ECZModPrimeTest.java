package ch.bfh.unicrypt.math.algebra.additive;

import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModPrime;
import ch.bfh.unicrypt.math.algebra.additive.parameters.ECZModPrimeParameters;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import java.math.BigInteger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class ECZModPrimeTest {

	@Test
	public void ContainsXTest() throws Exception {
		ECZModPrime ec = ECZModPrime.getInstance(ECZModPrimeParameters.SECP192r1);
		ZModPrime zmod = ec.getFiniteField();
		ZModElement x = zmod.getElement(new BigInteger("6035619839542366037308058104925986617178040067348624113998"));
		ZModElement falseX = zmod.getElement(new BigInteger("6035619839542366037308058104925986617178040067348624113997"));
		assertTrue(ec.contains(x));
		assertFalse(ec.contains(falseX));

	}

	@Test
	public void ContainsXYTest() throws Exception {
		ECZModPrime ec = ECZModPrime.getInstance(ECZModPrimeParameters.SECP192r1);
		ZModPrime zmod = ec.getFiniteField();
		ZModElement x1 = zmod.getElement(new BigInteger("5485061771863421489705328005527616201666672468700993602311"));
		ZModElement y1 = zmod.getElement(new BigInteger("1420584749125414905060037760781943638553232619342051948120"));
		ZModElement x2 = zmod.getElement(new BigInteger("548506177186342148970532800552761620166667246870099360231"));
		ZModElement y2 = zmod.getElement(new BigInteger("142058474912541490506003776078194363855323261934205148120"));
		assertTrue(ec.contains(x1, y1));
		assertFalse(ec.contains(x1, y2));
		assertFalse(ec.contains(x2, y1));
		assertFalse(ec.contains(x2, y2));

	}

	@Test
	public void addTest() throws Exception {
		ECZModPrime ec = ECZModPrime.getInstance(ECZModPrimeParameters.SECP192r1);
		ZModPrime zmod = ec.getFiniteField();
		ZModElement x1 = zmod.getElement(new BigInteger("5485061771863421489705328005527616201666672468700993602311"));
		ZModElement y1 = zmod.getElement(new BigInteger("1420584749125414905060037760781943638553232619342051948120"));
		ZModElement x2 = zmod.getElement(new BigInteger("2441618544204219939908869884647686795109326640025996237504"));
		ZModElement y2 = zmod.getElement(new BigInteger("2672175946681393184940515848151067610321944501151598069929"));
		ZModElement rx = zmod.getElement(new BigInteger("4979350392405835088678902176245649047066376075794328753425"));
		ZModElement ry = zmod.getElement(new BigInteger("5892043805632463887520453865529377471730573192832422897551"));
		ECZModElement e1 = ec.getElement(x1, y1);
		ECZModElement e2 = ec.getElement(x2, y2);
		ECZModElement r1 = ec.getElement(rx, ry);
		assertEquals(r1, e1.add(e2));
		assertEquals(e1, e1.add(ec.getIdentityElement()));
		assertEquals(e1, ec.getIdentityElement().add(e1));

	}

	@Test
	public void timesTest() throws Exception {
		ECZModPrime ec = ECZModPrime.getInstance(ECZModPrimeParameters.SECP192r1);
		ZModPrime zmod = ec.getFiniteField();
		ZModElement x1 = zmod.getElement(new BigInteger("5485061771863421489705328005527616201666672468700993602311"));
		ZModElement y1 = zmod.getElement(new BigInteger("1420584749125414905060037760781943638553232619342051948120"));
		ZModElement x2 = zmod.getElement(new BigInteger("6054377824002193228966197520487116476082215685270977710965"));
		ZModElement y2 = zmod.getElement(new BigInteger("185752276793014565279863894475032511210116040995173884975"));
		ECZModElement e1 = ec.getElement(x1, y1);
		ECZModElement e2 = ec.getElement(x2, y2);
		assertEquals(e2, e1.add(e1));
	}

	@Test
	public void invertTest() throws Exception {
		ECZModPrime ec = ECZModPrime.getInstance(ECZModPrimeParameters.SECP192r1);
		ZModPrime zmod = ec.getFiniteField();
		ZModElement x1 = zmod.getElement(new BigInteger("5485061771863421489705328005527616201666672468700993602311"));
		ZModElement y1 = zmod.getElement(new BigInteger("1420584749125414905060037760781943638553232619342051948120"));
		ZModElement x2 = zmod.getElement(new BigInteger("5485061771863421489705328005527616201666672468700993602311"));
		ZModElement y2 = zmod.getElement(new BigInteger("4856516986261265858775751662425722777530676081048273013159"));
		ECZModElement e1 = ec.getElement(x1, y1);
		ECZModElement e2 = ec.getElement(x2, y2);
		assertEquals(e2, e1.invert());
		assertEquals(ec.getIdentityElement(), ec.getIdentityElement().invert());

	}

}
