package ch.bfh.unicrypt.crypto.commitment.classes;

import java.util.Random;

import ch.bfh.unicrypt.crypto.commitment.abstracts.AbstractRandomizedCommitmentScheme;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.IdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlusMod;
import ch.bfh.unicrypt.math.group.interfaces.DDHGroup;

public class PedersenCommitmentScheme extends AbstractRandomizedCommitmentScheme {

  public PedersenCommitmentScheme(final DDHGroup ddhGroup) {
    this(ddhGroup, (Random) null);
  }

  public PedersenCommitmentScheme(final DDHGroup ddhGroup, final Random random) {
    this(ddhGroup, ddhGroup.getDefaultGenerator(), ddhGroup.getRandomGenerator(random));
  }

  public PedersenCommitmentScheme(final DDHGroup ddhGroup, final Element generator, final Random random) {
    this(ddhGroup, generator, ddhGroup.getRandomGenerator(random));
  }

  public PedersenCommitmentScheme(final DDHGroup ddhGroup, final Element otherGenerator) {
    this(ddhGroup, ddhGroup.getDefaultGenerator(), otherGenerator);
  }

  public PedersenCommitmentScheme(final DDHGroup ddhGroup, final Element generator, final Element otherGenerator) {
    if ((ddhGroup == null) || (generator == null) || !ddhGroup.contains(generator) || (otherGenerator == null)
        || !ddhGroup.contains(otherGenerator)) {
      throw new IllegalArgumentException();
    }
    final ZPlusMod orderGroup = ddhGroup.getZPlusModOrder();
    this.messageSpace = orderGroup;
    this.randomizationSpace = orderGroup;
    this.commitmentSpace = ddhGroup;

    this.commitFunction = this.createCommitFunction(generator, otherGenerator);
    this.openFunction = this.createOpeningFunction();
  }

  @Override
  public ZPlusMod getMessageSpace() {
    return (ZPlusMod) super.getMessageSpace();
  }

  @Override
  public ZPlusMod getRandomizationSpace() {
    return (ZPlusMod) super.getRandomizationSpace();
  }

  @Override
  public DDHGroup getCommitmentSpace() {
    return (DDHGroup) super.getCommitmentSpace();
  }

  private Function createCommitFunction(final Element generator, final Element otherGenerator) {
    //@formatter:off
    return new CompositeFunction(
        new ProductFunction(
            new SelfApplyFunction(this.getCommitmentSpace(), this.getMessageSpace()).partiallyApply(generator, 0),
            new SelfApplyFunction(this.getCommitmentSpace(), this.getRandomizationSpace()).partiallyApply(otherGenerator, 0)),
        new ApplyFunction(this.getCommitmentSpace()));
    //@formatter:on
  }

  private Function createOpeningFunction() {
    final ProductGroup openingDomain = new ProductGroup(this.getMessageSpace(), this.getRandomizationSpace(), this.getCommitmentSpace());
    //@formatter:off
    return new CompositeFunction(
        new IdentityFunction(openingDomain,2),
        new ProductFunction(
            new CompositeFunction(
                new IdentityFunction(openingDomain,2),
                new ProductFunction(
                    new SelectionFunction(openingDomain, 0),
                    new SelectionFunction(openingDomain, 1)),
                this.getCommitFunction()),
            new SelectionFunction(openingDomain,2)), 
        new EqualityFunction(this.getCommitmentSpace()));
    //@formatter:on
  }

}