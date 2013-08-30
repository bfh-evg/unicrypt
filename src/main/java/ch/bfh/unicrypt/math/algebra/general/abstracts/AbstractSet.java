package ch.bfh.unicrypt.math.algebra.general.abstracts;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlusMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.ZTimesMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 * This abstract class provides a basis implementation for atomic sets.
 *
 * @param <E>
 * @see AbstractElement
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractSet<E extends Element> implements Set {

  private BigInteger order, minOrder;

  @Override
  public final boolean isCompound() {
    return this.standardIsCompound();
  }

  @Override
  public final BigInteger getOrder() {
    if (this.order == null) {
      this.order = this.abstractGetOrder();
    }
    return this.order;
  }

  @Override
  public final BigInteger getMinOrder() {
    if (this.minOrder == null) {
      BigInteger order = this.getOrder();
      if (order.equals(Set.UNKNOWN_ORDER)) {
        this.minOrder = this.standardGetMinOrder();
      } else {
        this.minOrder = order;
      }
    }
    return this.minOrder;
  }

  @Override
  public final boolean isEmpty() {
    return this.getOrder().equals(BigInteger.ZERO);
  }

  @Override
  public final boolean isSingleton() {
    return this.getOrder().equals(BigInteger.ONE);
  }

  @Override
  public final ZPlusMod getZPlusModOrder() {
    BigInteger order = this.getOrder();
    if (order.equals(Set.INFINITE_ORDER) || order.equals(Set.UNKNOWN_ORDER)) {
      throw new UnsupportedOperationException();
    }
    return ZPlusMod.getInstance(order);
  }

  @Override
  public final ZTimesMod getZTimesModOrder() {
    BigInteger order = this.getOrder();
    if (order.equals(Set.INFINITE_ORDER) || order.equals(Set.UNKNOWN_ORDER)) {
      throw new UnsupportedOperationException();
    }
    return ZTimesMod.getInstance(order);
  }

  @Override
  public final ZStarMod getZStarModOrder() {
    BigInteger order = this.getOrder();
    if (order.equals(Set.INFINITE_ORDER) || order.equals(Set.UNKNOWN_ORDER)) {
      throw new UnsupportedOperationException();
    }
    return ZStarMod.getInstance(order);
  }

  @Override
  public final boolean contains(final int value) {
    return this.contains(BigInteger.valueOf(value));
  }

  @Override
  public final boolean contains(final BigInteger value) {
    if (value == null) {
      throw new IllegalArgumentException();
    }
    return this.abstractContains(value);
  }

  @Override
  public final boolean contains(final Element element) {
    if (element == null) {
      throw new IllegalArgumentException();
    }
    return this.equals(element.getSet());
  }

  @Override
  public final E getElement(final int value) {
    return this.getElement(BigInteger.valueOf(value));
  }

  @Override
  public final E getElement(BigInteger value) {
    if (value == null || !this.contains(value)) {
      throw new IllegalArgumentException();
    }
    return this.abstractGetElement(value);
  }

  @Override
  public final E getElement(final Element element) {
    if (element == null) {
      throw new IllegalArgumentException();
    }
    if (this.contains(element)) {
      return (E) element;
    }
    return this.getElement(element.getValue());
  }

  @Override
  public final E getRandomElement() {
    return this.getRandomElement(null);
  }

  @Override
  public final E getRandomElement(Random random) {
    return this.abstractGetRandomElement(random);
  }

  @Override
  public final boolean areEqual(final Element element1, final Element element2) {
    if (!this.contains(element1) || !this.contains(element2)) {
      throw new IllegalArgumentException();
    }
    return element1.equals(element2);
  }

  @Override
  public final boolean isCompatible(Set set) {
    if (set == null) {
      throw new IllegalArgumentException();
    }
    return standardIsCompatible(set);
  }

  @Override
  public final boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (object == null) {
      return false;
    }
    Set other = ((Set) object);
    if (!this.isCompatible(other)) {
      return false;
    }
    return this.standardEquals(other);
  }

  @Override
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.getClass().hashCode();
    result = prime * result + this.standardHashCode();
    return result;
  }

  @Override
  public String toString() {
    String str = this.standardToString();
    if (str.length() == 0) {
      return this.getClass().getSimpleName();
    }
    return this.getClass().getSimpleName() + "[" + str + "]";
  }

  //
  // The following protected methods are standard implementations for sets.
  // They may need to be changed in certain sub-classes.
  //
  protected boolean standardIsCompound() {
    return false;
  }

  protected BigInteger standardGetMinOrder() {
    return BigInteger.ZERO;
  }

  protected boolean standardIsCompatible(Set set) {
    return this.getClass() == set.getClass();
  }

  protected boolean standardEquals(Set set) {
    return true;
  }

  protected int standardHashCode() {
    return 0;
  }

  protected String standardToString() {
    return "";
  }

  //
  // The following protected abstract method must be implemented in every direct
  // sub-class.
  //
  protected abstract BigInteger abstractGetOrder();

  protected abstract E abstractGetElement(BigInteger value);

  protected abstract E abstractGetRandomElement(Random random);

  protected abstract boolean abstractContains(BigInteger value);

}
