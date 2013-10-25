package ch.bfh.unicrypt.crypto.schemes.commitment.classes;

import ch.bfh.unicrypt.crypto.encoder.classes.GeneralEncoder;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.crypto.schemes.commitment.abstracts.AbstractRandomizedCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.GeneratorFunction;
import ch.bfh.unicrypt.math.function.classes.GenericFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class StandardCommitmentScheme<M extends Set> extends AbstractRandomizedCommitmentScheme<M, ZMod, CyclicGroup, Element> {

  protected StandardCommitmentScheme(Encoder encoder, Function commitmentFunction, Function decommitmentFunction) {
    super(encoder, commitmentFunction, decommitmentFunction);
  }

  public static StandardCommitmentScheme getInstance(final CyclicGroup cyclicGroup) {
    return StandardCommitmentScheme.getInstance(cyclicGroup, (Encoder) null);
  }

  public static StandardCommitmentScheme getInstance(final CyclicGroup cyclicGroup, Set messageSpace) {
    if (cyclicGroup == null) {
      throw new IllegalArgumentException();
    }
    return StandardCommitmentScheme.getInstance(cyclicGroup, GeneralEncoder.getInstance(messageSpace, cyclicGroup.getZModOrder()));
  }

  public static StandardCommitmentScheme getInstance(CyclicGroup cyclicGroup, Encoder encoder) {
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
    Function commitmentFunction = GenericFunction.getInstance(GeneratorFunction.getInstance(cyclicGroup.getDefaultGenerator()));
    ProductGroup decommitmentDomain = ProductGroup.getInstance(zMod, cyclicGroup);
    Function decommitmentFunction = CompositeFunction.getInstance(
            MultiIdentityFunction.getInstance(decommitmentDomain, 2),
            ProductFunction.getInstance(CompositeFunction.getInstance(SelectionFunction.getInstance(decommitmentDomain, 0),
                                                                      commitmentFunction),
                                        SelectionFunction.getInstance(decommitmentDomain, 1)),
            EqualityFunction.getInstance(cyclicGroup));
    return new StandardCommitmentScheme(encoder, commitmentFunction, decommitmentFunction);
  }

}
