package ch.bfh.unicrypt.math.element.abstracts;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ProductMonoid;
import ch.bfh.unicrypt.math.group.classes.ProductSemiGroup;
import ch.bfh.unicrypt.math.group.classes.ProductSet;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveCyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveGroup;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveMonoid;
import ch.bfh.unicrypt.math.group.interfaces.AdditiveSemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Monoid;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeCyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeGroup;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeMonoid;
import ch.bfh.unicrypt.math.group.interfaces.MultiplicativeSemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This abstract class represents the concept an element in a mathematical
 * group. It allows applying the group operation and other methods from a
 * {@link Group} in a convenient way. Most methods provided by {@link AbstractElement}
 * have an equivalent method in {@link Group}.
 *
 * @see Group
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractElement<T extends Element> implements Element, Serializable {

  private static final long serialVersionUID = 1L;
  private final Set set;
  protected BigInteger value;

  protected AbstractElement(final Set set) {
    if (set == null) {
      throw new IllegalArgumentException();
    }
    this.set = set;
  }

  /**
   * Returns the unique {@link Set} to which this element belongs
   *
   * @return The element's set
   */
  public final Set getSet() {
    return this.set;
  }

  /**
   *
   * @return
   */
  @Override
  public final SemiGroup getSemiGroup() {
    if (this.set instanceof SemiGroup) {
      return (SemiGroup) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final Monoid getMonoid() {
    if (this.set instanceof Monoid) {
      return (Monoid) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final Group getGroup() {
    if (this.set instanceof Group) {
      return (Group) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final CyclicGroup getCyclicGroup() {
    if (this.set instanceof CyclicGroup) {
      return (CyclicGroup) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final AdditiveSemiGroup getAdditiveSemiGroup() {
    if (this.set instanceof AdditiveSemiGroup) {
      return (AdditiveSemiGroup) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final AdditiveMonoid getAdditiveMonoid() {
    if (this.set instanceof AdditiveMonoid) {
      return (AdditiveMonoid) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final AdditiveGroup getAdditiveGroup() {
    if (this.set instanceof AdditiveGroup) {
      return (AdditiveGroup) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final AdditiveCyclicGroup getAdditiveCyclicGroup() {
    if (this.set instanceof AdditiveCyclicGroup) {
      return (AdditiveCyclicGroup) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final MultiplicativeSemiGroup getMultiplicativeSemiGroup() {
    if (this.set instanceof MultiplicativeSemiGroup) {
      return (MultiplicativeSemiGroup) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final MultiplicativeMonoid getMultiplicativeMonoid() {
    if (this.set instanceof MultiplicativeMonoid) {
      return (MultiplicativeMonoid) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final MultiplicativeGroup getMultiplicativeGroup() {
    if (this.set instanceof MultiplicativeGroup) {
      return (MultiplicativeGroup) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final MultiplicativeCyclicGroup getMultiplicativeCyclicGroup() {
    if (this.set instanceof MultiplicativeCyclicGroup) {
      return (MultiplicativeCyclicGroup) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final ProductSet getProductSet() {
    if (this.set instanceof ProductSet) {
      return (ProductSet) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final ProductSemiGroup getProductSemiGroup() {
    if (this.set instanceof ProductSemiGroup) {
      return (ProductSemiGroup) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final ProductMonoid getProductMonoid() {
    if (this.set instanceof ProductMonoid) {
      return (ProductMonoid) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   *
   * @return
   */
  @Override
  public final ProductGroup getProductGroup() {
    if (this.set instanceof ProductGroup) {
      return (ProductGroup) this.set;
    }
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the positive BigInteger value that corresponds the element.
   *
   * @return The corresponding BigInteger value
   */
  @Override
  public final BigInteger getValue() {
    if (this.value == null) {
      this.value = standardGetValue();
    }
    return this.value;
  }

  @Override
  public final byte[] getHashValue() {
    return this.getHashValue(Element.STANDARD_HASH_ALGORITHM);
  }

  @Override
  public final byte[] getHashValue(String hashAlgorithm) {
    if (hashAlgorithm == null) {
      throw new IllegalArgumentException();
    }
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance(hashAlgorithm);
    } catch (final NoSuchAlgorithmException e) {
      throw new IllegalArgumentException();
    }
    return this.getHashValue(messageDigest);
  }

  @Override
  public byte[] getHashValue(MessageDigest messageDigest) {
    if (messageDigest == null) {
      throw new IllegalArgumentException();
    }
    messageDigest.reset();
    return messageDigest.digest(this.getValue().toByteArray());
  }

  @Override
  public byte[] getRecursiveHashValue() {
    return this.getRecursiveHashValue(AbstractElement.STANDARD_HASH_ALGORITHM);
  }

  @Override
  public byte[] getRecursiveHashValue(String hashAlgorithm) {
    if (hashAlgorithm == null) {
      throw new IllegalArgumentException();
    }
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance(hashAlgorithm);
    } catch (final NoSuchAlgorithmException e) {
      throw new IllegalArgumentException();
    }
    return this.getRecursiveHashValue(messageDigest);
  }

  @Override
  public byte[] getRecursiveHashValue(MessageDigest messageDigest) {
    if (messageDigest == null) {
      throw new IllegalArgumentException();
    }
    return standardGetRecursiveHashValue(messageDigest);
  }

  //
  // The following methods are equivalent to corresponding Set methods
  //
  /**
   * @see Group#apply(Element, Element)
   */
  @Override
  public final T apply(final Element element) {
    SemiGroup semiGroup = this.getSemiGroup();
    return (T) semiGroup.apply(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  @Override
  public final T applyInverse(final Element element) {
    Group group = this.getGroup();
    return (T) group.applyInverse(this, element);
  }

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  @Override
  public final T selfApply(final BigInteger amount) {
    SemiGroup semiGroup = this.getSemiGroup();
    return (T) semiGroup.selfApply(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  @Override
  public final T selfApply(final Element amount) {
    SemiGroup semiGroup = this.getSemiGroup();
    return (T) semiGroup.selfApply(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  @Override
  public final T selfApply(final int amount) {
    SemiGroup semiGroup = this.getSemiGroup();
    return (T) semiGroup.selfApply(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  @Override
  public final T selfApply() {
    SemiGroup semiGroup = this.getSemiGroup();
    return (T) semiGroup.selfApply(this);
  }

  /**
   * @see Group#apply(Element, Element)
   */
  @Override
  public final T add(final Element element) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (T) semiGroup.add(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  @Override
  public final T subtract(final Element element) {
    AdditiveGroup group = this.getAdditiveGroup();
    return (T) group.subtract(this, element);
  }

  /**
   * @see Group#T(Element, BigInteger)
   */
  @Override
  public final T times(final BigInteger amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (T) semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  @Override
  public final T times(final Element amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (T) semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  @Override
  public final T times(final int amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (T) semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  @Override
  public final T timesTwo() {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return (T) semiGroup.timesTwo(this);
  }

  /**
   * @see Group#apply(Element, Element)
   */
  @Override
  public final T multiply(final Element element) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (T) semiGroup.multiply(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  @Override
  public final T divide(final Element element) {
    MultiplicativeGroup group = this.getMultiplicativeGroup();
    return (T) group.divide(this, element);
  }

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  @Override
  public final T power(final BigInteger amount) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (T) semiGroup.power(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  @Override
  public final T power(final Element amount) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (T) semiGroup.power(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  @Override
  public final T power(final int amount) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (T) semiGroup.power(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  public final T square() {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return (T) semiGroup.square(this);
  }

  /**
   * @see Group#invert(Element)
   */
  public final T invert() {
    Group group = this.getGroup();
    return (T) group.invert(this);
  }

  /**
   * @see Group#isIdentityElement(Element)
   */
  public final boolean isIdentity() {
    Monoid monoid = this.getMonoid();
    return monoid.isIdentityElement(this);
  }

  /**
   * @see CyclicGroup#isGenerator(Element)
   */
  public final boolean isGenerator() {
    CyclicGroup cyclicGroup = this.getCyclicGroup();
    return cyclicGroup.isGenerator(this);
  }

  //
  // The standard implementations of the following three methods are
  // insufficient for elements.
  //
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
    T other = (T) object;
    if (!this.getSet().equals(other.getSet())) {
      return false;
    }
    return this.standardEquals(other);
  }

  @Override
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + this.getSet().hashCode();
    result = prime * result + this.standardHashCode();
    return result;
  }

  @Override
  public final String toString() {
    String str = this.standardToString();
    if (str.length() == 0) {
      return this.getSet().getClass().getSimpleName() + "Element";
    }
    return this.getSet().getClass().getSimpleName() + "Element[" + str + "]";
  }

  //
  // The following protected methods are standard implementations, which may change in sub-classes
  //
  protected BigInteger standardGetValue() {
    throw new UnsupportedOperationException();
  }

  protected boolean standardEquals(Element element) {
    return this.getValue().equals(element.getValue());
  }

  protected byte[] standardGetRecursiveHashValue(MessageDigest messageDigest) {
    return this.getHashValue(messageDigest);
  }

  protected int standardHashCode() {
    return this.getValue().hashCode();
  }

  protected String standardToString() {
    return this.getValue().toString();
  }

  /**
   * This is a static factory method to create elements of a given set for a
   * given BigInteger value
   * @param value The given BigInteger value
   * @return The corresponding element
   * @throws IllegalArgumentException if {@code set} or {@code value} is null
   */
  public static Element getInstance(Set set, BigInteger value) {
    if (set == null) {
      throw new IllegalArgumentException();
    }
    return set.getElement(value);
  }

}