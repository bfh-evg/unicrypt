package ch.bfh.unicrypt.crypto.commitment.classes;

import ch.bfh.unicrypt.crypto.commitment.abstracts.AbstractPerfectlyHidingCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.AdaptorFunction;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

public class PedersenCommitmentScheme extends AbstractPerfectlyHidingCommitmentScheme<ZMod, ZMod, CyclicGroup, Element> {

  public PedersenCommitmentScheme(Function commitmentFunction, Function decommitmentFunction) {
    super(commitmentFunction, decommitmentFunction);
  }

  public static PedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup) {
    return PedersenCommitmentScheme.getInstance(cyclicGroup, (Random) null);
  }

  public static PedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final Random random) {
    if (cyclicGroup == null) {
      throw new IllegalArgumentException();
    }
    return PedersenCommitmentScheme.getInstance(cyclicGroup, cyclicGroup.getDefaultGenerator(), cyclicGroup.getRandomGenerator(random));
  }

  public static PedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final Element generator, final Random random) {
    if (cyclicGroup == null) {
      throw new IllegalArgumentException();
    }
    return PedersenCommitmentScheme.getInstance(cyclicGroup, generator, cyclicGroup.getRandomGenerator(random));
  }

  public static PedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final Element otherGenerator) {
    if (cyclicGroup == null) {
      throw new IllegalArgumentException();
    }
    return PedersenCommitmentScheme.getInstance(cyclicGroup, cyclicGroup.getDefaultGenerator(), otherGenerator);
  }

  public static PedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final Element generator, final Element otherGenerator) {
    if ((cyclicGroup == null) || (generator == null) || (otherGenerator == null)
            || !cyclicGroup.contains(generator) || !cyclicGroup.contains(otherGenerator)
            || !cyclicGroup.isGenerator(generator) || !cyclicGroup.isGenerator(otherGenerator)) {
      throw new IllegalArgumentException();
    }
    Function commitmentFunction = CompositeFunction.getInstance(
            ProductFunction.getInstance(GeneratorFunction.getInstance(generator),
                                        GeneratorFunction.getInstance(otherGenerator)),
            ApplyFunction.getInstance(cyclicGroup));
    ZMod zMod = cyclicGroup.getZModOrder();
    ProductGroup decommitmentDomain = ProductGroup.getInstance(zMod, zMod, cyclicGroup);
    Function decommitmentFunction = CompositeFunction.getInstance(
            MultiIdentityFunction.getInstance(decommitmentDomain, 2),
            ProductFunction.getInstance(CompositeFunction.getInstance(AdaptorFunction.getInstance(decommitmentDomain, 0, 1),
                                                                      commitmentFunction),
                                        SelectionFunction.getInstance(decommitmentDomain, 2)),
            EqualityFunction.getInstance(cyclicGroup));
    return new PedersenCommitmentScheme(commitmentFunction, decommitmentFunction);
  }

}
