package ch.bfh.unicrypt.crypto.blinding.abstracts;

import java.util.Random;

import ch.bfh.unicrypt.crypto.blinding.interfaces.BlindingScheme;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

public abstract class AbstractBlindingScheme implements BlindingScheme {

  Group blindingSpace;
  Group blindingValueSpace;
  Function blindingFunction;
  Function unblindingFunction;

  protected AbstractBlindingScheme(final Group blindingSpace, Group blindingValueSpace) {
    if (blindingSpace == null || blindingValueSpace == null) {
      throw new IllegalArgumentException();
    }
    this.blindingSpace = blindingSpace;
    this.blindingValueSpace = blindingValueSpace;
  }

  @Override
  public Element blind(final Element value) {
    return this.blind(value, (Random) null);
  }

  @Override
  public Element blind(final Element value, final Random random) {
    return this.blind(value, this.getBlindingValueSpace().getRandomElement(random));
  }

  @Override
  public Element blind(final Element value, final Element blindingValue) {
    return this.getBlindingFunction().apply(value, blindingValue);
  }

  @Override
  public Element unblind(final Element blindedValue, final Element blindingValue) {
    return this.getUnblindingFunction().apply(blindedValue, blindingValue);
  }

  @Override
  public Group getBlindingSpace() {
    return this.blindingSpace;
  }

  @Override
  public Group getBlindingValueSpace() {
    return this.blindingValueSpace;
  }

  @Override
  public Function getBlindingFunction() {
    return this.blindingFunction;
  }

  @Override
  public Function getBlindingFunction(final Element value) {
    return this.getBlindingFunction().partiallyApply(value, 0);
  }

  @Override
  public Function getUnblindingFunction() {
    return this.unblindingFunction;
  }

  @Override
  public Function getUnblindingFunction(final Element value) {
    return this.getUnblindingFunction().partiallyApply(value, 0);
  }

}
