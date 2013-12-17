package ch.bfh.unicrypt.crypto.schemes.commitment.classes;

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.crypto.schemes.commitment.abstracts.AbstractRandomizedCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModElement;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class GeneralizedPedersenCommitmentScheme<CS extends CyclicGroup, CE extends Element>
			 extends AbstractRandomizedCommitmentScheme<ZMod, ZModElement, CS, CE, ZMod> {

	private final CS cyclicGroup;
	private final CE randomizationGenerator;
	private final CE[] messageGenerators;

	protected GeneralizedPedersenCommitmentScheme(CS cyclicGroup, CE randomizationGenerator, CE[] messageGenerators) {
		this.cyclicGroup = cyclicGroup;
		this.randomizationGenerator = randomizationGenerator;
		this.messageGenerators = messageGenerators;
	}

	public final CS getCyclicGroup() {
		return this.cyclicGroup;
	}

	public final CE getRandomizationGenerator() {
		return this.randomizationGenerator;
	}

	public final CE[] getMessageGenerators() {
		return this.messageGenerators.clone();
	}

	@Override
	protected Function abstractGetCommitmentFunction() {
		final Function[] generatorFunctions = new Function[this.messageGenerators.length];
		for (int i = 0; i < this.messageGenerators.length; i++) {
			generatorFunctions[i] = GeneratorFunction.getInstance(this.messageGenerators[i]);
		}
		return CompositeFunction.getInstance(
					 ProductFunction.getInstance(CompositeFunction.getInstance(ProductFunction.getInstance(generatorFunctions),
																																		 ApplyFunction.getInstance(cyclicGroup, this.messageGenerators.length)),
																			 GeneratorFunction.getInstance(this.getRandomizationGenerator())),
					 ApplyFunction.getInstance(this.getCyclicGroup()));
	}

	public static <CS extends CyclicGroup, CE extends Element> GeneralizedPedersenCommitmentScheme<CS, CE> getInstance(final CS cyclicGroup, final int size) {
		return GeneralizedPedersenCommitmentScheme.<CS, CE>getInstance(cyclicGroup, size, (RandomReferenceString) null);
	}

	public static <CS extends CyclicGroup, CE extends Element> GeneralizedPedersenCommitmentScheme<CS, CE> getInstance(final CS cyclicGroup, final int size, final RandomReferenceString randomReferenceString) {
		return GeneralizedPedersenCommitmentScheme.<CS, CE>getInternalInstance(cyclicGroup, size, randomReferenceString);
	}

	public static GeneralizedPedersenCommitmentScheme<GStarMod, GStarModElement> getInstance(final GStarMod gStarMod, final int size) {
		return GeneralizedPedersenCommitmentScheme.<GStarMod, GStarModElement>getInstance(gStarMod, size, (RandomReferenceString) null);
	}

	public static GeneralizedPedersenCommitmentScheme<GStarMod, GStarModElement> getInstance(final GStarMod gStarMod, final int size, final RandomReferenceString randomReferenceString) {
		return GeneralizedPedersenCommitmentScheme.<GStarMod, GStarModElement>getInternalInstance(gStarMod, size, randomReferenceString);
	}

	private static <CS extends CyclicGroup, CE extends Element> GeneralizedPedersenCommitmentScheme<CS, CE> getInternalInstance(final CS cyclicGroup, final int size, RandomReferenceString randomReferenceString) {
		if (randomReferenceString == null) {
			randomReferenceString = PseudoRandomReferenceString.getInstance();
		} else {
			randomReferenceString.reset();
		}
		CE randomizationGenerator = (CE) cyclicGroup.getIndependentGenerator(0, randomReferenceString);
		CE[] messageGenerators = (CE[]) cyclicGroup.getIndependentGenerators(1, size, randomReferenceString);
		return new GeneralizedPedersenCommitmentScheme<CS, CE>(cyclicGroup, randomizationGenerator, messageGenerators);
	}

}
