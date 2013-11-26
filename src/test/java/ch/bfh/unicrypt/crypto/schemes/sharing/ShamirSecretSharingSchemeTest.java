package ch.bfh.unicrypt.crypto.schemes.sharing;

import static org.junit.Assert.assertTrue;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ch.bfh.unicrypt.crypto.schemes.sharing.classes.ShamirSecretSharingScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;

public class ShamirSecretSharingSchemeTest {

	final SecureRandom random = new SecureRandom();

	final int size = random.nextInt(100) + 3;
	final int threshold = size - random.nextInt(size / 3) - 1;

	@Test
	public void testShare() {

		// Create a field and initialize Scheme
		ZModPrime field = ZModPrime.getInstance(167);
		ShamirSecretSharingScheme ssss = ShamirSecretSharingScheme.getInstance(
				field, size, threshold);

		// Choose a random message that will be shared
		ZModElement message = field.getRandomElement();

		// Share the message
		Tuple[] shares = ssss.share(message);

		assertTrue(shares.length == size);

		for (Tuple share : shares) {
			assertTrue(share.getArity() == 2);
			assertTrue(field.contains(share.getAt(0)));
			assertTrue(field.contains(share.getAt(1)));
		}
	}

	@Test
	public void testRecover() {
		// Create a field and initialize Scheme
		ZModPrime field = ZModPrime.getInstance(167);
		ShamirSecretSharingScheme ssss = ShamirSecretSharingScheme.getInstance(
				field, size, threshold);

		// Choose a random message that will be shared
		ZModElement message = field.getRandomElement();

		// Share the message
		Tuple[] shares = ssss.share(message);

		List<Tuple> sharesList = Arrays.asList(shares);

		// Shuffle the list of shares
		Collections.shuffle(sharesList);

		// choose a random number of shares between the threshold and the size
		// which will be removed from the list
		int numberToRemove = random.nextInt(size - threshold);

		// remove the last shares
		sharesList = sharesList.subList(0, sharesList.size() - numberToRemove);

		// restore tuple array with remaining shares
		Tuple[] remainingShares = new Tuple[sharesList.size()];
		sharesList.toArray(remainingShares);

		// recover message and check whether it is equal with original message
		ZModElement recoveredMessage = ssss.recover(remainingShares);
		assertTrue(recoveredMessage.isEqual(message));
	}
}
