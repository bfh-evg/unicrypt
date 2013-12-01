package ch.bfh.unicrypt.crypto.schemes.commitment.classes;

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
import ch.bfh.unicrypt.math.random.RandomOracle;

public class PedersenCommitmentScheme<CS extends CyclicGroup, CE extends Element>
	   extends AbstractRandomizedCommitmentScheme<ZMod, ZModElement, CS, CE, ZMod> {

	private final CS cyclicGroup;
	private final CE firstGenerator;
	private final CE secondGenerator;

	protected PedersenCommitmentScheme(CS cyclicGroup, CE firstGenerator, CE secondGenerator) {
		this.cyclicGroup = cyclicGroup;
		this.firstGenerator = firstGenerator;
		this.secondGenerator = secondGenerator;
	}

	public final CS getCyclicGroup() {
		return this.cyclicGroup;
	}

	public final CE getFirstGenerator() {
		return this.firstGenerator;
	}

	public final CE getSecondGenerator() {
		return this.secondGenerator;
	}

	@Override
	protected Function abstractGetCommitmentFunction() {
		return CompositeFunction.getInstance(
			   ProductFunction.getInstance(GeneratorFunction.getInstance(this.getFirstGenerator()),
										   GeneratorFunction.getInstance(this.getSecondGenerator())),
			   ApplyFunction.getInstance(this.getCyclicGroup()));
	}

	public static <CS extends CyclicGroup, CE extends Element> PedersenCommitmentScheme<CS, CE> getInstance(CS cyclicGroup) {
		return PedersenCommitmentScheme.<CS, CE>getInstance(cyclicGroup, (RandomOracle) null);
	}

	public static <CS extends CyclicGroup, CE extends Element> PedersenCommitmentScheme<CS, CE> getInstance(CS cyclicGroup, RandomOracle randomOracle) {
		return PedersenCommitmentScheme.<CS, CE>getInternalInstance(cyclicGroup, randomOracle);
	}

	public static PedersenCommitmentScheme<GStarMod, GStarModElement> getInstance(GStarMod gStarMod) {
		return PedersenCommitmentScheme.<GStarMod, GStarModElement>getInstance(gStarMod, null);
	}

	public static PedersenCommitmentScheme<GStarMod, GStarModElement> getInstance(GStarMod gStarMod, RandomOracle randomOracle) {
		return PedersenCommitmentScheme.<GStarMod, GStarModElement>getInternalInstance(gStarMod, randomOracle);
	}

	public static PedersenCommitmentScheme<GStarMod, GStarModElement> getInstance(GStarModElement firstGenerator, GStarModElement secondGenerator) {
		if (!firstGenerator.getSet().isEqual(secondGenerator.getSet())) {
			throw new IllegalArgumentException();
		}
		return new PedersenCommitmentScheme<GStarMod, GStarModElement>(firstGenerator.getSet(), firstGenerator, secondGenerator);
	}

	private static <CS extends CyclicGroup, CE extends Element> PedersenCommitmentScheme<CS, CE> getInternalInstance(CS cyclicGroup, RandomOracle randomOracle) {
		if (randomOracle == null) {
			randomOracle = RandomOracle.DEFAULT;
		}
		CE firstGenerator = (CE) cyclicGroup.getIndependentGenerator(0, randomOracle);
		CE secondGenerator = (CE) cyclicGroup.getIndependentGenerator(1, randomOracle);
		return new PedersenCommitmentScheme<CS, CE>(cyclicGroup, firstGenerator, secondGenerator);
	}

}
