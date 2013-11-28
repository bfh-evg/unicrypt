package ch.bfh.unicrypt.crypto.schemes.commitment.classes;

import ch.bfh.unicrypt.crypto.schemes.commitment.abstracts.AbstractRandomizedCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
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

public class GeneralizedPedersenCommitmentScheme<CS extends CyclicGroup, CE extends Element>
	extends AbstractRandomizedCommitmentScheme<ZMod, CS, CE, ZMod> {

    private final CS cyclicGroup;
    private final CE firstGenerator;
    private final CE secondGenerator;

    protected GeneralizedPedersenCommitmentScheme(CS cyclicGroup, CE firstGenerator, CE secondGenerator) {
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
			GeneratorFunction.getInstance(this.getFirstGenerator())),
		ApplyFunction.getInstance(this.getCyclicGroup()));
    }

    public static <CS extends CyclicGroup, CE extends Element> GeneralizedPedersenCommitmentScheme<CS, CE> getInstance(CS cyclicGroup) {
	return GeneralizedPedersenCommitmentScheme.<CS, CE>getInstance(cyclicGroup, (RandomOracle) null);
    }

    public static <CS extends CyclicGroup, CE extends Element> GeneralizedPedersenCommitmentScheme<CS, CE> getInstance(CS cyclicGroup, RandomOracle randomOracle) {
	return GeneralizedPedersenCommitmentScheme.<CS, CE>getInternalInstance(cyclicGroup, randomOracle);
    }

    public static GeneralizedPedersenCommitmentScheme<GStarMod, GStarModElement> getInstance(GStarMod gStarMod) {
	return GeneralizedPedersenCommitmentScheme.<GStarMod, GStarModElement>getInstance(gStarMod);
    }

    public static GeneralizedPedersenCommitmentScheme<GStarMod, GStarModElement> getInstance(GStarMod gStarMod, RandomOracle randomOracle) {
	return GeneralizedPedersenCommitmentScheme.<GStarMod, GStarModElement>getInternalInstance(gStarMod, randomOracle);

    }

    private static <CS extends CyclicGroup, CE extends Element> GeneralizedPedersenCommitmentScheme<CS, CE> getInternalInstance(CS cyclicGroup, RandomOracle randomOracle) {
	if (randomOracle == null) {
	    randomOracle = RandomOracle.DEFAULT;
	}
	CE firstGenerator = (CE) cyclicGroup.getIndependentGenerator(0, randomOracle);
	CE secondGenerator = (CE) cyclicGroup.getIndependentGenerator(1, randomOracle);
	return new GeneralizedPedersenCommitmentScheme<CS, CE>(cyclicGroup, firstGenerator, secondGenerator);
    }

//  public static GeneralizedPedersenCommitmentScheme getInstance(CyclicGroup cyclicGroup, Encoder encoder, RandomOracle randomOracle) {
//    if (cyclicGroup == null) {
//      throw new IllegalArgumentException();
//    }
//    ZMod zMod = cyclicGroup.getZModOrder();
//    if (encoder == null) {
//      encoder = GeneralEncoder.getInstance(zMod);
//    } else {
//      if (!encoder.getCoDomain().isEqual(zMod)) {
//        throw new IllegalArgumentException();
//      }
//    }
//    if (randomOracle == null) {
//      randomOracle = RandomOracle.DEFAULT;
//    }
//    Element firstGenerator = cyclicGroup.getIndependentGenerator(0, randomOracle);
//    Element secondGenerator = cyclicGroup.getIndependentGenerator(1, randomOracle);
//    Function commitmentFunction = CompositeFunction.getInstance(
//            ProductFunction.getInstance(GeneratorFunction.getInstance(firstGenerator),
//                                        GeneratorFunction.getInstance(secondGenerator)),
//            ApplyFunction.getInstance(cyclicGroup));
//    ProductGroup decommitmentDomain = ProductGroup.getInstance(zMod, zMod, cyclicGroup);
//    Function decommitmentFunction = CompositeFunction.getInstance(
//            MultiIdentityFunction.getInstance(decommitmentDomain, 2),
//            ProductFunction.getInstance(CompositeFunction.getInstance(AdapterFunction.getInstance(decommitmentDomain, 0, 1),
//                                                                      commitmentFunction),
//                                        SelectionFunction.getInstance(decommitmentDomain, 2)),
//            EqualityFunction.getInstance(cyclicGroup));
//    return new GeneralizedPedersenCommitmentScheme(encoder, commitmentFunction, decommitmentFunction);
//  }
}
