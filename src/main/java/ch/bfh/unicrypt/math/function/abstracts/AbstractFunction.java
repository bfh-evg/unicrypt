package ch.bfh.unicrypt.math.function.abstracts;

import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.algebra.product.classes.ProductSet;
import ch.bfh.unicrypt.math.function.classes.PartiallyAppliedFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 * This abstract class contains standard implementations for most methods of
 * type {@link Function}. For most classes implementing {@link Function}, it is
 * sufficient to inherit from {@link AbstractFunction} and to implement the
 * single abstract method {@link abstractApply(Element element, Random random)}.
 *
 * @param <D>
 * @param <E>
 * @param <C>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractFunction<D extends Set, C extends Set, E extends Element> implements Function {

  private final D domain;
  private final C coDomain;

  protected AbstractFunction(final Set domain, final Set coDomain) {
    this.domain = (D) domain;
    this.coDomain = (C) coDomain;
  }

  @Override
  public final boolean isCompound() {
    return this.standardIsCompound();
  }

  @Override
  public final E apply(final Element element) {
    return this.apply(element, (Random) null);
  }

  @Override
  public final E apply(final Element element, final Random random) {
    if (this.getDomain().contains(element)) {
      return this.abstractApply(element, random);
    }
    // This is for increased convenience for a function with a CompoundSet domain of arity 1.
    return this.apply(new Element[]{element}, random);
  }

  @Override
  public final E apply(final Element... elements) {
    return this.apply(elements, (Random) null);
  }

  @Override
  public final E apply(final Element[] elements, final Random random) {
    if (this.getDomain().isCompound()) {
      return this.apply(((ProductSet) this.getDomain()).getElement(elements), random);
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public D getDomain() {
    return this.domain;
  }

  @Override
  public C getCoDomain() {
    return this.coDomain;
  }

  @Override
  public final PartiallyAppliedFunction partiallyApply(final Element element, final int index) {
    return PartiallyAppliedFunction.getInstance(this, element, index);
  }

  @Override
  public final boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    if (this.getClass() != object.getClass()) {
      return false;
    }
    Function other = (Function) object;
    if (!this.getDomain().equals(other.getDomain())) {
      return false;
    }
    if (!this.getCoDomain().equals(other.getCoDomain())) {
      return false;
    }
    return this.standardEquals(other);
  }

  @Override
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.getClass().hashCode();
    result = prime * result + this.getDomain().hashCode();
    result = prime * result + this.getCoDomain().hashCode();
    result = prime * result + this.standardHashCode();
    return result;
  }

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[" + this.getDomain() + " => " + this.getCoDomain() + "]";
  }

  //
  // The following protected methods are standard implementations for sets.
  // They may need to be changed in certain sub-classes.
  //
  protected boolean standardIsCompound() {
    return false;
  }

  protected boolean standardEquals(Function function) {
    return true;
  }

  protected int standardHashCode() {
    return 0;
  }

  //
  // The following protected abstract method must be implemented in every direct
  // sub-class
  //
  /**
   * This abstract method is the main method to implement in each sub-class of
   * {@link AbstractFunction}. The validity of the two parameters has already
   * been tested.
   *
   * @see apply(Element, Random)
   * @see Group#apply(Element[])
   * @see Element#apply(Element)
   *
   * @param element The given input element
   * @param random Either {@code null} or a given random generator
   * @return The resulting output element
   */
  protected abstract E abstractApply(Element element, Random random);

}
