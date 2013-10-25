package ch.bfh.unicrypt.crypto.schemes.commitment.classes;

import ch.bfh.unicrypt.crypto.encoder.classes.GeneralEncoder;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.crypto.schemes.commitment.abstracts.AbstractRandomizedCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.AdapterFunction;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

public class PedersenCommitmentScheme extends AbstractRandomizedCommitmentScheme<Set, ZMod, CyclicGroup, Element> {

  protected PedersenCommitmentScheme(Encoder encoder, Function commitmentFunction, Function decommitmentFunction) {
    super(encoder, commitmentFunction, decommitmentFunction);
  }

  public static PedersenCommitmentScheme getInstance(CyclicGroup cyclicGroup) {
    return PedersenCommitmentScheme.getInstance(cyclicGroup, (Encoder) null);
  }

  public static PedersenCommitmentScheme getInstance(CyclicGroup cyclicGroup, Set messageSpace) {
    if (cyclicGroup == null) {
      throw new IllegalArgumentException();
    }
    return PedersenCommitmentScheme.getInstance(cyclicGroup, GeneralEncoder.getInstance(messageSpace, cyclicGroup.getZModOrder()));
  }

  public static PedersenCommitmentScheme getInstance(CyclicGroup cyclicGroup, Encoder encoder) {
    if (cyclicGroup == null) {
      throw new IllegalArgumentException();
    }
    ZMod zMod = cyclicGroup.getZModOrder();
    if (encoder == null) {
      encoder = GeneralEncoder.getInstance(zMod);
    } else {
      if (!encoder.getEncodingSpace().equals(zMod)) {
        throw new IllegalArgumentException();
      }
    }
    Element firstGenerator = cyclicGroup.getIndependentGenerator(0, null);
    Element secondGenerator = cyclicGroup.getIndependentGenerator(1, null);
    Function commitmentFunction = CompositeFunction.getInstance(
            ProductFunction.getInstance(GeneratorFunction.getInstance(firstGenerator),
                                        GeneratorFunction.getInstance(secondGenerator)),
            ApplyFunction.getInstance(cyclicGroup));
    ProductGroup decommitmentDomain = ProductGroup.getInstance(zMod, zMod, cyclicGroup);
    Function decommitmentFunction = CompositeFunction.getInstance(
            MultiIdentityFunction.getInstance(decommitmentDomain, 2),
            ProductFunction.getInstance(CompositeFunction.getInstance(AdapterFunction.getInstance(decommitmentDomain, 0, 1),
                                                                      commitmentFunction),
                                        SelectionFunction.getInstance(decommitmentDomain, 2)),
            EqualityFunction.getInstance(cyclicGroup));
    return new PedersenCommitmentScheme(encoder, commitmentFunction, decommitmentFunction);
  }

}
