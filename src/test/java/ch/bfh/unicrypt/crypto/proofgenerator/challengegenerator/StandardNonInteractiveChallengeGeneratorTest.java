package ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator;

import ch.bfh.unicrypt.crypto.proofgenerator.challengegenerator.classes.StandardNonInteractiveChallengeGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import org.junit.Test;

public class StandardNonInteractiveChallengeGeneratorTest {

	@Test
	public void testStandardNonInteractiveElementChallengeGenerator() {


		CyclicGroup cyclicGroup = GStarModSafePrime.getInstance(167);
		StandardNonInteractiveChallengeGenerator cg = StandardNonInteractiveChallengeGenerator.getInstance(cyclicGroup, cyclicGroup, 10);
		Tuple elements = (Tuple) cg.generate(cyclicGroup.getRandomElement());

		System.out.println(elements);

	}

}
