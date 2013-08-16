package ch.bfh.unicrypt.math.group.abstracts;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlusMod;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import ch.bfh.unicrypt.math.utility.MathUtil;

/**
 * This abstract class provides a basis implementation for atomic sets.
 *
 * @see Element
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractSet implements Set {

  private static final long serialVersionUID = 1L;

  private BigInteger order, minOrder;
  private ZPlusMod orderGroup, minOrderGroup;

  @Override
  public final BigInteger getOrder() {
    if (this.order == null) {
      this.order = this.abstractGetOrder();
    }
    return this.order;
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
  public final ZPlusMod getOrderGroup() {
    if (this.orderGroup == null) {
      BigInteger order = this.getOrder();
      if (order.equals(Set.INFINITE_ORDER) || order.equals(Set.UNKNOWN_ORDER)) {
        throw new UnsupportedOperationException();
      }
      this.orderGroup = ZPlusMod.getInstance(order);
    }
    return this.orderGroup;
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
  public final ZPlusMod getMinOrderGroup() {
    if (this.minOrderGroup == null) {
      BigInteger minOrder = this.getMinOrder();
      if (minOrder.equals(Set.INFINITE_ORDER)) {
        throw new UnsupportedOperationException();
      }
      this.minOrderGroup = ZPlusMod.getInstance(minOrder);
    }
    return this.minOrderGroup;
  }

  @Override
  public final Element getElement(final int value) {
    return this.getElement(BigInteger.valueOf(value));
  }

  @Override
  public final Element getElement(BigInteger value) {
    if (value == null || !this.contains(value)) {
      throw new IllegalArgumentException();
    }
    return this.abstractGetElement(value);
  }

  @Override
  public final Element getElement(final Element element) {
    if (element == null) {
      throw new IllegalArgumentException();
    }
    if (this.contains(element)) {
      return element;
    }
    return this.getElement(element.getValue());
  }

  @Override
  public final Element getRandomElement() {
    return this.getRandomElement(null);
  }

  @Override
  public final Element getRandomElement(Random random) {
    return this.abstractGetRandomElement(random);
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
  public final boolean areEqual(final Element element1, final Element element2) {
    if (!this.contains(element1) || !this.contains(element2)) {
      throw new IllegalArgumentException();
    }
    return this.standardAreEqual(element1, element2);
  }

  @Override
  public final boolean isAtomic() {
    return true;
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
    return this.abstractEquals((Set) object);
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
      this.getClass().getSimpleName();
    }
    return this.getClass().getSimpleName() + "[" + str + "]";
  }

  //
  // The following protected methods are standard implementations for sets.
  // They may need to be changed in certain sub-classes.
  //

  protected BigInteger standardGetMinOrder() {
    return BigInteger.ZERO;
  }

  protected boolean standardAreEqual(Element element1, Element element2) {
    return element1.getValue().equals(element2.getValue());
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

  protected abstract Element abstractGetElement(final BigInteger value);

  protected abstract Element abstractGetRandomElement(Random random);

  protected abstract boolean abstractContains(BigInteger value);

  protected abstract boolean abstractEquals(Set set);

}