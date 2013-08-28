package ch.bfh.unicrypt.crypto.commitment.classes;

import ch.bfh.unicrypt.crypto.commitment.abstracts.AbstractDeterministicCommitmentScheme;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.IdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.product.classes.ProductGroup;
import ch.bfh.unicrypt.math.additive.classes.ZPlusMod;
import ch.bfh.unicrypt.math.general.interfaces.DDHGroup;

public class StandardCommitmentScheme extends AbstractDeterministicCommitmentScheme {

  public StandardCommitmentScheme(final DDHGroup ddhGroup) {
    this(ddhGroup, ddhGroup.getDefaultGenerator());
  }

  public StandardCommitmentScheme(final DDHGroup ddhGroup, final Element generator) {
    if ((ddhGroup == null) || (generator == null) || !ddhGroup.contains(generator)) {
      throw new IllegalArgumentException();
    }
    final ZPlusMod orderGroup = ddhGroup.getZPlusModOrder();
    this.messageSpace = orderGroup;
    this.commitmentSpace = ddhGroup;

    this.commitFunction = StandardCommitmentScheme.createCommitFunction(ddhGroup, orderGroup, generator);
    this.openFunction = StandardCommitmentScheme.createOpenFunction(ddhGroup, orderGroup, this.commitFunction);
  }

  @Override
  public ZPlusMod getMessageSpace() {
    return (ZPlusMod) super.getMessageSpace();
  }

  @Override
  public DDHGroup getCommitmentSpace() {
    return (DDHGroup) super.getCommitmentSpace();
  }

  private static Function createCommitFunction(final DDHGroup ddhGroup, final ZPlusMod orderGroup, final Element generator) {
    return new SelfApplyFunction(ddhGroup, orderGroup).partiallyApply(generator, 0);
  }

  private static Function createOpenFunction(final DDHGroup ddhGroup, final ZPlusMod orderGroup, final Function commitFunction) {
    final ProductGroup openingDomain = new ProductGroup(orderGroup, ddhGroup);
    //@formatter:off
    return new CompositeFunction(
        new IdentityFunction(openingDomain,2),
        new ProductFunction(
            new CompositeFunction(
                new SelectionFunction(openingDomain, 0),
                commitFunction),
            new SelectionFunction(openingDomain,1)), 
        new EqualityFunction(ddhGroup));
    //@formatter:on
  }

}