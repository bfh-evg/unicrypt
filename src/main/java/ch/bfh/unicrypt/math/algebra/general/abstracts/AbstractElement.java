package ch.bfh.unicrypt.math.algebra.general.abstracts;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.CyclicGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.helper.UniCrypt;

/**
 * This abstract class represents the concept an element in a mathematical
 * group. It allows applying the group operation and other methods from a
 * {@link Group} in a convenient way. Most methods provided by
 * {@link AbstractElement} have an equivalent method in {@link Group}.
 *
 * @param <E>
 * @see Group
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractElement<S extends Set, E extends Element> extends UniCrypt implements Element {

  private final S set;
  protected BigInteger value;

  protected AbstractElement(final S set) {
    if (set == null) {
      throw new IllegalArgumentException();
    }
    this.set = set;
  }

  protected AbstractElement(final S set, final BigInteger value) {
    this(set);
    if (!set.contains(value)) {
      throw new IllegalArgumentException();
    }
    this.value = value;
  }

  @Override
  public final boolean isCompound() {
    return this.standardIsCompound();
  }

  /**
   * Returns the unique {@link Set} to which this element belongs
   *
   * @return The element's set
   */
  @Override
  public final S getSet() {
    return this.set;
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
  public final ByteArrayElement getHashValue() {
    return this.getHashValue(Element.STANDARD_HASH_ALGORITHM);
  }

  @Override
  public final ByteArrayElement getHashValue(String hashAlgorithm) {
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
  public ByteArrayElement getHashValue(MessageDigest messageDigest) {
    if (messageDigest == null) {
      throw new IllegalArgumentException();
    }
    messageDigest.reset();
    return ByteArrayMonoid.getInstance().getElement(messageDigest.digest(this.getValue().toByteArray()));
  }

  @Override
  public ByteArrayElement getRecursiveHashValue() {
    return this.getRecursiveHashValue(AbstractElement.STANDARD_HASH_ALGORITHM);
  }

  @Override
  public ByteArrayElement getRecursiveHashValue(String hashAlgorithm) {
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
  public ByteArrayElement getRecursiveHashValue(MessageDigest messageDigest) {
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
    if (this.getSet().isSemiGroup()) {
      SemiGroup semiGroup = ((SemiGroup) this.getSet());
      return (E) semiGroup.apply(this, element);
    }
    throw new UnsupportedOperationException();
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  @Override
  public final E applyInverse(final Element element) {
    if (this.getSet().isGroup()) {
      Group group = ((Group) this.getSet());
      return (E) group.applyInverse(this, element);
    }
    throw new UnsupportedOperationException();
  }

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  @Override
  public final E selfApply(final BigInteger amount) {
    if (this.getSet().isSemiGroup()) {
      SemiGroup semiGroup = ((SemiGroup) this.getSet());
      return (E) semiGroup.selfApply(this, amount);
    }
    throw new UnsupportedOperationException();
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  @Override
  public final E selfApply(final Element amount) {
    if (this.getSet().isSemiGroup()) {
      SemiGroup semiGroup = ((SemiGroup) this.getSet());
      return (E) semiGroup.selfApply(this, amount);
    }
    throw new UnsupportedOperationException();
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  @Override
  public final E selfApply(final int amount) {
    if (this.getSet().isSemiGroup()) {
      SemiGroup semiGroup = ((SemiGroup) this.getSet());
      return (E) semiGroup.selfApply(this, amount);
    }
    throw new UnsupportedOperationException();
  }

  /**
   * @see Group#selfApply(Element)
   */
  @Override
  public final E selfApply() {
    if (this.getSet().isSemiGroup()) {
      SemiGroup semiGroup = ((SemiGroup) this.getSet());
      return (E) semiGroup.selfApply(this);
    }
    throw new UnsupportedOperationException();
  }

  /**
   * @see Group#invert(Element)
   */
  @Override
  public final E invert() {
    if (this.getSet().isGroup()) {
      Group group = ((Group) this.getSet());
      return (E) group.invert(this);
    }
    throw new UnsupportedOperationException();
  }

  /**
   * @see Group#isIdentityElement(Element)
   */
  @Override
  public final boolean isIdentity() {
    if (this.getSet().isMonoid()) {
      Monoid monoid = ((Monoid) this.getSet());
      return monoid.isIdentityElement(this);
    }
    throw new UnsupportedOperationException();
  }

  /**
   * @see CyclicGroup#isGenerator(Element)
   */
  @Override
  public final boolean isGenerator() {
    if (this.getSet().isCyclicGroup()) {
      CyclicGroup cyclicGroup = ((CyclicGroup) this.getSet());
      return cyclicGroup.isGenerator(this);
    }
    throw new UnsupportedOperationException();
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

  //
  // The following protected methods are standard implementations, which may change in sub-classes
  //
  protected boolean standardIsCompound() {
    return false;
  }

  protected BigInteger standardGetValue() {
    throw new UnsupportedOperationException();
  }

  protected boolean standardEquals(Element element) {
    return this.getValue().equals(element.getValue());
  }

  protected ByteArrayElement standardGetRecursiveHashValue(MessageDigest messageDigest) {
    return this.getHashValue(messageDigest);
  }

  protected int standardHashCode() {
    return this.getValue().hashCode();
  }

  @Override
  protected String standardToStringName() {
    return this.getClass().getSuperclass().getSimpleName();
  }

  @Override
  protected String standardToStringContent() {
    return this.getValue().toString();
  }

}
