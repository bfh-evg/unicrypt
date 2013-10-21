package ch.bfh.unicrypt.crypto.schemes.commitment.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.crypto.schemes.commitment.abstracts.AbstractRandomizedCommitmentScheme;
import ch.bfh.unicrypt.crypto.schemes.commitment.interfaces.MultiCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductGroup;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.DDHGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.ApplyFunction;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class MultiPedersenCommitmentScheme extends AbstractRandomizedCommitmentScheme<ProductSet, ZMod, CyclicGroup, Element> implements MultiCommitmentScheme {

  private final int arity;
  private final ProductGroup messagesSpace;

  public MultiPedersenCommitmentScheme(final DDHGroup ddhGroup, final int arity) {
    this(ddhGroup, arity, (Random) null);
  }

  public MultiPedersenCommitmentScheme(final DDHGroup ddhGroup, final int arity, final Random random) {
    this(ddhGroup, MultiPedersenCommitmentScheme.createGenerators(ddhGroup, arity, random), ddhGroup.getDefaultGenerator());
  }

  public MultiPedersenCommitmentScheme(final DDHGroup ddhGroup, final List<Element> generators, final Element otherGenerator) {
    if ((ddhGroup == null) || (otherGenerator == null) || !ddhGroup.contains(otherGenerator) || (generators == null) || (generators.size() < 1)) {
      throw new IllegalArgumentException();
    }
    this.arity = generators.size();
    final ZMod orderGroup = ddhGroup.getZModOrder();
    this.messageSpace = orderGroup;
    this.messagesSpace = new PowerGroup(orderGroup, this.arity);
    this.randomizationSpace = orderGroup;
    this.commitmentSpace = ddhGroup;

    this.commitFunction = this.createCommitFunction(generators, otherGenerator);
    this.openFunction = this.createOpeningFunction();
  }

  public ProductGroup getMessagesSpace() {
    return this.messagesSpace;
  }

  @Override
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
            new MultiIdentityFunction(openingDomain, 2),
            new ProductFunction(
            new CompositeFunction(
            new MultiIdentityFunction(openingDomain, 2),
            new ProductFunction(
            new SelectionFunction(openingDomain, 0),
            new SelectionFunction(openingDomain, 1)),
            this.getCommitmentFunction()),
            new SelectionFunction(openingDomain, 2)),
            new EqualityFunction(this.getCommitmentSpace()));
    //@formatter:on
  }

}
