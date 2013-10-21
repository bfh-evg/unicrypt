package ch.bfh.unicrypt.crypto.commitment.classes;

import ch.bfh.unicrypt.crypto.commitment.abstracts.AbstractDeterministicCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.DDHGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class StandardCommitmentScheme extends AbstractDeterministicCommitmentScheme<ZMod, CyclicGroup, Element> {

  protected StandardCommitmentScheme(Function commitmentFunction, Function decommitmentFunction) {
    super(commitmentFunction, decommitmentFunction);
  }

  private static Function createCommitFunction(final DDHGroup ddhGroup, final ZMod orderGroup, final Element generator) {
    return SelfApplyFunction.getInstance(ddhGroup, orderGroup).partiallyApply(generator, 0);
  }

  private static Function createOpenFunction(final DDHGroup ddhGroup, final ZMod orderGroup, final Function commitFunction) {
    final ProductGroup openingDomain = new ProductGroup(orderGroup, ddhGroup);
    //@formatter:off
    return new CompositeFunction(
            new MultiIdentityFunction(openingDomain, 2),
            new ProductFunction(
            new CompositeFunction(
            new SelectionFunction(openingDomain, 0),
            commitFunction),
            new SelectionFunction(openingDomain, 1)),
            new EqualityFunction(ddhGroup));
    //@formatter:on
  }

  public static StandardCommitmentScheme getInstance(final CyclicGroup cyclicGroup) {
    if (cyclicGroup == null) {
      throw new IllegalArgumentException();
    }
    return StandardCommitmentScheme.getInstance(cyclicGroup.getDefaultGenerator());
  }

  public static StandardCommitmentScheme getInstance(final Element generator) {
    if ((generator == null) || !generator.getSet().isCyclic() || !generator.isGenerator()) {
      throw new IllegalArgumentException();
    }
    final ZMod zModOrder = cyclicGroup.getZModOrder();
    this.messageSpace = zModOrder;
    this.commitmentSpace = cyclicGroup;

    this.commitmentFunction = StandardCommitmentScheme.createCommitFunction(cyclicGroup, zModOrder, generator);
    this.decommitmentFunction = StandardCommitmentScheme.createOpenFunction(cyclicGroup, zModOrder, this.commitmentFunction);
  }

}
