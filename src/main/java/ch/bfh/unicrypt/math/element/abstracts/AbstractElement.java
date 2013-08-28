package ch.bfh.unicrypt.math.element.abstracts;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.cyclicgroup.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.monoid.interfaces.Monoid;
import ch.bfh.unicrypt.math.semigroup.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.set.interfaces.Set;
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
public abstract class AbstractElement<E extends Element> implements Element {

  private final Set set;
  protected BigInteger value;

  protected AbstractElement(final Set set) {
    if (set == null) {
      throw new IllegalArgumentException();
    }
    this.set = set;
  }

  protected AbstractElement(final Set set, final BigInteger value) {
    this(set);
    if (!set.contains(value)) {
      throw new IllegalArgumentException();
    }
    this.value = value;
  }

  /**
   * Returns the unique {@link Set} to which this element belongs
   *
   * @return The element's set
   */
  @Override
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
  public final E apply(final Element element) {
    SemiGroup semiGroup = this.getSemiGroup();
    return (E) semiGroup.apply(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  @Override
  public final E applyInverse(final Element element) {
    Group group = this.getGroup();
    return (E) group.applyInverse(this, element);
  }

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  @Override
  public final E selfApply(final BigInteger amount) {
    SemiGroup semiGroup = this.getSemiGroup();
    return (E) semiGroup.selfApply(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  @Override
  public final E selfApply(final Element amount) {
    SemiGroup semiGroup = this.getSemiGroup();
    return (E) semiGroup.selfApply(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  @Override
  public final E selfApply(final int amount) {
    SemiGroup semiGroup = this.getSemiGroup();
    return (E) semiGroup.selfApply(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  @Override
  public final E selfApply() {
    SemiGroup semiGroup = this.getSemiGroup();
    return (E) semiGroup.selfApply(this);
  }

  /**
   * @see Group#invert(Element)
   */
  public final E invert() {
    Group group = this.getGroup();
    return (E) group.invert(this);
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
    E other = (E) object;
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

}