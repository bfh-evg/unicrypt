package ch.bfh.unicrypt.crypto.commitment.classes;

import java.util.ArrayList;
import java.util.List;
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
import ch.bfh.unicrypt.math.group.classes.PowerGroup;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlusMod;
import ch.bfh.unicrypt.math.group.interfaces.DDHGroup;

public class ExtendedPedersenCommitmentScheme extends AbstractRandomizedCommitmentScheme {

  private final int arity;
  private final PowerGroup messagesSpace;

  public ExtendedPedersenCommitmentScheme(final DDHGroup ddhGroup, final int arity) {
    this(ddhGroup, arity, (Random) null);
  }

  public ExtendedPedersenCommitmentScheme(final DDHGroup ddhGroup, final int arity, final Random random) {
    this(ddhGroup, ExtendedPedersenCommitmentScheme.createGenerators(ddhGroup, arity, random), ddhGroup.getDefaultGenerator());
  }

  public ExtendedPedersenCommitmentScheme(final DDHGroup ddhGroup, final List<Element> generators, final Element otherGenerator) {
    if ((ddhGroup == null) || (otherGenerator == null) || !ddhGroup.contains(otherGenerator) || (generators == null) || (generators.size() < 1)) {
      throw new IllegalArgumentException();
    }
    this.arity = generators.size();
    final ZPlusMod orderGroup = ddhGroup.getZPlusModOrder();
    this.messageSpace = orderGroup;
    this.messagesSpace = new PowerGroup(orderGroup, this.arity);
    this.randomizationSpace = orderGroup;
    this.commitmentSpace = ddhGroup;

    this.commitFunction = this.createCommitFunction(generators, otherGenerator);
    this.openFunction = this.createOpeningFunction();
  }

  @Override
  public ZPlusMod getMessageSpace() {
    return (ZPlusMod) super.getMessageSpace();
  }

  public PowerGroup getMessagesSpace() {
    return this.messagesSpace;
  }

  @Override
  public ZPlusMod getRandomizationSpace() {
    return (ZPlusMod) super.getRandomizationSpace();
  }

  @Override
  public DDHGroup getCommitmentSpace() {
    return (DDHGroup) super.getCommitmentSpace();
  }

  public Element commit(final List<Element> messages, final Element randomization) {
    if (messages == null) {
      throw new IllegalArgumentException();
    }
    return this.commit(this.getMessagesSpace().getElement(messages.toArray(new Element[this.getArity()])), randomization);
  }

  public boolean open(final List<Element> messages, final Element randomization, final Element commitment) {
    if (messages == null) {
      throw new IllegalArgumentException();
    }
    return this.open(this.getMessagesSpace().getElement(messages.toArray(new Element[this.getArity()])), randomization, commitment);
  }

  public int getArity() {
    return this.arity;
  }

  private static List<Element> createGenerators(final DDHGroup ddhGroup, final int arity, final Random random) {
    final List<Element> generators = new ArrayList<Element>();
    for (int i = 0; i < arity; i++) {
      generators.add(ddhGroup.getRandomGenerator(random));
    }
    return generators;
  }

  private Function createCommitFunction(final List<Element> generators, final Element otherGenerator) {
    final Function[] functions = new Function[this.getArity()];
    for (int i = 0; i < this.getArity(); i++) {
      functions[i] = new SelfApplyFunction(this.getCommitmentSpace(), this.getMessageSpace()).partiallyApply(generators.get(i), 0);
    }
    //@formatter:off
    return new CompositeFunction(
        new ProductFunction(
            new CompositeFunction(
                new ProductFunction(functions),
                new ApplyFunction(this.getCommitmentSpace(), this.getArity())),
            new SelfApplyFunction(this.getCommitmentSpace(), this.getRandomizationSpace()).partiallyApply(otherGenerator, 0)),
        new ApplyFunction(this.getCommitmentSpace()));
    //@formatter:on
  }

  private Function createOpeningFunction() {
    final ProductGroup openingDomain = new ProductGroup(this.getMessagesSpace(), this.getRandomizationSpace(), this.getCommitmentSpace());
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
