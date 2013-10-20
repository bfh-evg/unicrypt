package ch.bfh.unicrypt.crypto.commitment.classes;

import ch.bfh.unicrypt.crypto.commitment.abstracts.AbstractCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.DDHGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class StandardCommitmentScheme extends AbstractCommitmentScheme {

  public StandardCommitmentScheme(final DDHGroup ddhGroup) {
    this(ddhGroup, ddhGroup.getDefaultGenerator());
  }

  public StandardCommitmentScheme(final DDHGroup ddhGroup, final Element generator) {
    if ((ddhGroup == null) || (generator == null) || !ddhGroup.contains(generator)) {
      throw new IllegalArgumentException();
    }
    final ZMod zModOrder = ddhGroup.getZModOrder();
    this.messageSpace = zModOrder;
    this.commitmentSpace = ddhGroup;

    this.commitmentFunction = StandardCommitmentScheme.createCommitFunction(ddhGroup, zModOrder, generator);
    this.decommitmentFunction = StandardCommitmentScheme.createOpenFunction(ddhGroup, zModOrder, this.commitmentFunction);
  }

  @Override
  public ZMod getMessageSpace() {
    return (ZMod) super.getMessageSpace();
  }

  @Override
  public DDHGroup getCommitmentSpace() {
    return (DDHGroup) super.getCommitmentSpace();
  }

  private static Function createCommitFunction(final DDHGroup ddhGroup, final ZMod orderGroup, final Element generator) {
    return SelfApplyFunction.getInstance(ddhGroup, orderGroup).partiallyApply(generator, 0);
  }

  private static Function createOpenFunction(final DDHGroup ddhGroup, final ZMod orderGroup, final Function commitFunction) {
    final ProductGroup openingDomain = new ProductGroup(orderGroup, ddhGroup);
    //@formatter:off
    return new CompositeFunction(
        new MultiIdentityFunction(openingDomain,2),
        new ProductFunction(
            new CompositeFunction(
                new SelectionFunction(openingDomain, 0),
                commitFunction),
            new SelectionFunction(openingDomain,1)),
        new EqualityFunction(ddhGroup));
    //@formatter:on
  }

}