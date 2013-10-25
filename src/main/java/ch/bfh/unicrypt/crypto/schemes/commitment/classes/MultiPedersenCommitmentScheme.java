package ch.bfh.unicrypt.crypto.schemes.commitment.classes;

import ch.bfh.unicrypt.crypto.encoder.classes.GeneralEncoder;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.crypto.schemes.commitment.abstracts.AbstractRandomizedCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanSet;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.classes.AdapterFunction;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.GenericFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

public class MultiPedersenCommitmentScheme extends AbstractRandomizedCommitmentScheme<Set, ZMod, CyclicGroup, Element> {

  protected MultiPedersenCommitmentScheme(Encoder encoder, Function commitmentFunction, Function decommitmentFunction) {
    super(encoder, commitmentFunction, decommitmentFunction);
  }

//  @Override
//  public GenericFunction<ProductGroup, BooleanSet, BooleanElement> getDecommitmentFunction() {
//    return (GenericFunction<ProductGroup, BooleanSet, BooleanElement>) super.getDecommitmentFunction();
//  }
  public static MultiPedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup) {
    return MultiPedersenCommitmentScheme.getInstance(cyclicGroup, (Encoder) null);
  }

  public static MultiPedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup, Set messageSpace) {
    return MultiPedersenCommitmentScheme.getInstance(cyclicGroup, GeneralEncoder.getInstance(messageSpace));
  }

  public static MultiPedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup, Encoder encoder) {
    return MultiPedersenCommitmentScheme.getInstance(cyclicGroup, (Random) null, encoder);
  }

  public static MultiPedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final Random random) {
    return MultiPedersenCommitmentScheme.getInstance(cyclicGroup, random, (Encoder) null);
  }

  public static MultiPedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final Random random, Set messageSpace) {
    return MultiPedersenCommitmentScheme.getInstance(cyclicGroup, random, GeneralEncoder.getInstance(messageSpace));
  }

  public static MultiPedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final Random random, Encoder encoder) {
    if (cyclicGroup == null) {
      throw new IllegalArgumentException();
    }
    return MultiPedersenCommitmentScheme.getInstance(cyclicGroup.getDefaultGenerator(), cyclicGroup.getRandomGenerator(random), encoder);
  }

  public static MultiPedersenCommitmentScheme getInstance(final Element generator, final Random random) {
    return MultiPedersenCommitmentScheme.getInstance(generator, random, (Encoder) null);
  }

  public static MultiPedersenCommitmentScheme getInstance(final Element generator, final Random random, Set messageSpace) {
    return MultiPedersenCommitmentScheme.getInstance(generator, random, GeneralEncoder.getInstance(messageSpace));
  }

  public static MultiPedersenCommitmentScheme getInstance(final Element generator, final Random random, Encoder encoder) {
    if (generator == null || !generator.getSet().isCyclic()) {
      throw new IllegalArgumentException();
    }
    return MultiPedersenCommitmentScheme.getInstance(generator, ((CyclicGroup) generator.getSet()).getRandomGenerator(random), encoder);
  }

  public static MultiPedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final Element otherGenerator) {
    return MultiPedersenCommitmentScheme.getInstance(cyclicGroup, otherGenerator, (Encoder) null);
  }

  public static MultiPedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final Element otherGenerator, Set messageSpace) {
    return MultiPedersenCommitmentScheme.getInstance(cyclicGroup, otherGenerator, GeneralEncoder.getInstance(messageSpace));
  }

  public static MultiPedersenCommitmentScheme getInstance(final CyclicGroup cyclicGroup, final Element otherGenerator, Encoder encoder) {
    if (cyclicGroup == null) {
      throw new IllegalArgumentException();
    }
    return MultiPedersenCommitmentScheme.getInstance(cyclicGroup.getDefaultGenerator(), otherGenerator, encoder);
  }

  public static MultiPedersenCommitmentScheme getInstance(final Element generator, final Element otherGenerator) {
    return MultiPedersenCommitmentScheme.getInstance(generator, otherGenerator, (Encoder) null);
  }

  public static MultiPedersenCommitmentScheme getInstance(final Element generator, final Element otherGenerator, Set messageSpace) {
    return MultiPedersenCommitmentScheme.getInstance(generator, otherGenerator, GeneralEncoder.getInstance(messageSpace));
  }

  public static MultiPedersenCommitmentScheme getInstance(final Element generator, final Element otherGenerator, Encoder encoder) {
    if ((generator == null) || (otherGenerator == null) || !generator.isGenerator() || !otherGenerator.isGenerator() || !generator.getSet().equals(otherGenerator.getSet())) {
      throw new IllegalArgumentException();
    }
    CyclicGroup cyclicGroup = (CyclicGroup) generator.getSet();
    ZMod zMod = cyclicGroup.getZModOrder();
    if (encoder == null) {
      encoder = GeneralEncoder.getInstance(zMod);
    } else {
      if (!encoder.getEncodingSpace().equals(zMod)) {
        throw new IllegalArgumentException();
      }
    }
    Function commitmentFunction = CompositeFunction.getInstance(
            ProductFunction.getInstance(GeneratorFunction.getInstance(generator),
                                        GeneratorFunction.getInstance(otherGenerator)),
            ApplyFunction.getInstance(cyclicGroup));
    ProductGroup decommitmentDomain = ProductGroup.getInstance(zMod, zMod, cyclicGroup);
    Function decommitmentFunction = CompositeFunction.getInstance(
            MultiIdentityFunction.getInstance(decommitmentDomain, 2),
            ProductFunction.getInstance(CompositeFunction.getInstance(AdapterFunction.getInstance(decommitmentDomain, 0, 1),
                                                                      commitmentFunction),
                                        SelectionFunction.getInstance(decommitmentDomain, 2)),
            EqualityFunction.getInstance(cyclicGroup));
    return new MultiPedersenCommitmentScheme(encoder, commitmentFunction, decommitmentFunction);
  }

}
