package ch.bfh.unicrypt.math.group.abstracts;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlusMod;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.utility.MathUtil;

/**
 * This abstract class provides a basis implementation for objects of type {@link Group}.
 *
 * @see Element
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractGroup implements Group {

  private static final long serialVersionUID = 1L;

  private BigInteger order, minOrder;
  private ZPlusMod orderGroup, minOrderGroup;
  private Element identityElement;
  private Group superGroup;

  public final Element getElement() {
    return this.getElement(new BigInteger[]{});
  }

  @Override
  public final Element getElement(final int value) {
    return this.getElement(BigInteger.valueOf(value));
  }

  @Override
  public final Element getElement(final int... values) {
    return this.getElement(MathUtil.intToBigIntegerArray(values));
  }

  @Override
  public final Element getElement(BigInteger value) {
    if (value == null) {
      throw new IllegalArgumentException();
    }
    if (this.isAtomicGroup()) {
      if (!this.contains(value)) {
        throw new IllegalArgumentException();
      }
      return this.abstractGetElement(value);
    }
    return this.getElement(MathUtil.elegantUnpair(value, this.getArity()));
  }

  @Override
  public final Element getElement(BigInteger... values) {
    if (values == null || values.length != this.getArity()) {
      throw new IllegalArgumentException();
    }
    if (this.isAtomicGroup()) {
      return this.getElement(values[0]);
    }
    Element[] elements = new Element[this.getArity()];
    for (int i=0; i<this.getArity(); i++) {
      elements[i] = this.getGroupAt(i).getElement(values[i]);
    }
    return this.abstractGetElement(null, elements);
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
  public final Element getElement(final Element... elements) {
    if (elements == null || elements.length != this.getArity()) {
      throw new IllegalArgumentException();
    }
    if (this.isAtomicGroup()) {
      return this.getElement(elements[0]);
    }
    Element[] newElements = new Element[this.getArity()];
    for (int i=0; i<this.getArity(); i++) {
      newElements[i] = this.getGroupAt(i).getElement(elements[i]);
    }
    return abstractGetElement(null, newElements);
  }

  @Override
  public final int getArity() {
    return this.standardGetArity();
  }

  @Override
  public final BigInteger getOrder() {
    if (this.order == null) {
      this.order = this.abstractGetOrder();
    }
    return this.order;
  }

  @Override
  public final boolean isAtomicGroup() {
    return this.standardIsAtomicGroup();
  }

  @Override
  public final boolean isEmptyGroup() {
    return this.getArity() == 0;
  }

  @Override
  public final boolean isPowerGroup() {
    if (this.getArity() <= 1) {
      return true;
    }
    return this.standardIsPowerGroup();
  }

  @Override
  public final boolean isSingletonGroup() {
    return this.getOrder().equals(BigInteger.ONE);
  }

  @Override
  public final boolean hasSameOrder(final Group group) {
    if (group == null) {
      throw new IllegalArgumentException();
    }
    return this.getOrder().equals(group.getOrder()) && (!this.getOrder().equals(Group.UNKNOWN_ORDER) || this.equals(group));
  }

  @Override
  public final Group getGroup() {
    return this.getGroupAt(0);
  }

  @Override
  public final Group getGroupAt(final int index) {
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    return standardGetGroupAt(index);
  }

  @Override
  public final Group getGroupAt(final int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    Group group = this;
    for (final int index : indices) {
      group = group.getGroupAt(index);
    }
    return group;
  }

  @Override
  public final Group removeGroupAt(int index) {
    if (this.getArity() == 0) {
      throw new UnsupportedOperationException();
    }
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    return standardRemoveGroupAt(index);
  }

  @Override
  public final boolean isSubGroup() {
    return this.getSuperGroup() != this;
  }

  @Override
  public final Group getSuperGroup() {
    if (this.superGroup == null) {
      this.superGroup = standardGetSuperGroup();
    }
    return this.superGroup;
  }

  @Override
  public final BigInteger getMinOrder() {
    if (this.minOrder == null) {
      BigInteger order = this.getOrder();
      if (order.equals(Group.UNKNOWN_ORDER)) {
        this.minOrder = this.standardGetMinOrder();
      } else {
        this.minOrder = order;
      }
    }
    return this.minOrder;
  }

  @Override
  public final ZPlusMod getOrderGroup() {
    if (this.orderGroup == null) {
      BigInteger order = this.getOrder();
      if (order.equals(Group.INFINITE_ORDER) || order.equals(Group.UNKNOWN_ORDER)) {
        throw new UnsupportedOperationException();
      }
      this.orderGroup = ZPlusMod.getInstance(order);
    }
    return this.orderGroup;
  }

  @Override
  public final ZPlusMod getMinOrderGroup() {
    if (this.minOrderGroup == null) {
      BigInteger minOrder = this.getMinOrder();
      if (minOrder.equals(Group.INFINITE_ORDER)) {
        throw new UnsupportedOperationException();
      }
      this.minOrderGroup = ZPlusMod.getInstance(minOrder);
    }
    return this.minOrderGroup;
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
  public final Element getIdentityElement() {
    if (this.identityElement == null) {
      this.identityElement = this.abstractIdentityElement();
    }
    return this.identityElement;
  }

  @Override
  public final Element applyInverse(Element element1, Element element2) {
    return this.apply(element1, this.invert(element2));
  }

  @Override
  public final Element apply(final Element element1, final Element element2) {
    if (!this.contains(element1) || !this.contains(element2)) {
      throw new IllegalArgumentException();
    }
    return abstractApply(element1, element2);
  }

  @Override
  public final Element apply(final Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    Element result = getIdentityElement();
    for (Element element: elements) {
      result = this.apply(result, element);
    }
    return result;
  }

  @Override
  public final Element selfApply(final Element element, final BigInteger amount) {
    if (!this.contains(element) || (amount == null)) {
      throw new IllegalArgumentException();
    }
    return abstractSelfApply(element, amount);
  }

  @Override
  public final Element selfApply(final Element element, final Element amount) {
    if (amount == null) {
      throw new IllegalArgumentException();
    }
    return this.selfApply(element, amount.getValue());
  }

  @Override
  public final Element selfApply(final Element element, final int amount) {
    return this.selfApply(element, BigInteger.valueOf(amount));
  }

  @Override
  public final Element selfApply(final Element element) {
    return this.selfApply(element, 2);
  }

  @Override
  public final Element multiSelfApply(final Element[] elements, final BigInteger[] amounts) {
    if ((elements == null) || (amounts == null) || (elements.length != amounts.length)) {
      throw new IllegalArgumentException();
    }
    int bitLength = 0;
    for (int i = 0; i < amounts.length; i++) {
      if ((amounts[i] == null) || (elements[i] == null)) {
        throw new IllegalArgumentException();
      }
      bitLength = Math.max(bitLength, amounts[i].bitLength());
    }
    Element result = this.getIdentityElement();
    for (int i = bitLength - 1; i >= 0; i--) {
      result = result.selfApply();
      for (int j = 0; j < amounts.length; j++) {
        if (amounts[j].testBit(i)) {
          result = result.apply(elements[j]);
        }
      }
    }
    return result;
  }

  @Override
  public final Element invert(final Element element) {
    if (!this.contains(element)) {
      throw new IllegalArgumentException();
    }
    return abstractInvert(element);
  }

  @Override
  public final boolean isIdentity(final Element element) {
    if (element == null) {
      throw new IllegalArgumentException();
    }
    return this.getIdentityElement().equals(element);
  }

  @Override
  public final boolean areEqual(final Element element1, final Element element2) {
    if (element1 == null || element2 == null) {
      throw new IllegalArgumentException();
    }
    return element1.equals(element2);
  }

  @Override
  public final boolean contains(final BigInteger value) {
    if (value == null) {
      throw new IllegalArgumentException();
    }
    return this.abstractContains(value);
  }

  @Override
  public final boolean contains(BigInteger... values) {
    if (values == null || values.length != this.getArity()) {
      throw new IllegalArgumentException();
    }
    for (int i=0; i<this.getArity(); i++) {
      if (!this.getGroupAt(i).contains(values[i])) {
        return false;
      }
    }
    return true;
  }

  @Override
  public final boolean contains(final Element element) {
    if (element == null) {
      throw new IllegalArgumentException();
    }
    return this.equals(element.getGroup());
  }

  @Override
  public final boolean contains(Element... elements) {
    if (elements == null || elements.length != this.getArity()) {
      throw new IllegalArgumentException();
    }
    for (int i=0; i<this.getArity(); i++) {
      if (!this.getGroupAt(i).contains(elements[i])) {
        return false;
      }
    }
    return true;
  }

  //
  // The following protected methods are standard implementations for most groups of arity 1.
  // The standard implementation may change in sub-classes for groups of higher arity.
  //

  protected Group standardGetSuperGroup() {
    return this;
  }

  @SuppressWarnings("static-method")
  protected int standardGetArity() {
    return 1;
  }

  @SuppressWarnings("static-method")
  protected boolean standardIsAtomicGroup() {
    return true;
  }

  @SuppressWarnings("static-method")
  protected boolean standardIsPowerGroup() {
    return true;
  }

  @SuppressWarnings("unused")
  protected Group standardGetGroupAt(final int index) {
    return this;
  }

  @SuppressWarnings({"static-method", "unused"})
  protected Group standardRemoveGroupAt(final int index) {
    return ProductGroup.getInstance();
  }

  @SuppressWarnings("static-method")
  protected BigInteger standardGetMinOrder() {
    return BigInteger.ONE;
  }

  //
  // The following protected abstract method must be implemented in every direct sub-class.
  //

  protected abstract Element abstractGetElement(final BigInteger value, Element... elements);

  protected abstract Element abstractGetRandomElement(Random random);

  protected abstract BigInteger abstractGetOrder();

  protected abstract boolean abstractContains(BigInteger value);

  protected abstract Element abstractIdentityElement();

  protected abstract Element abstractApply(Element element1, Element element2);

  protected abstract Element abstractSelfApply(Element element, BigInteger amount);

  protected abstract Element abstractInvert(Element element);

}