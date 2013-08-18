package ch.bfh.unicrypt.math.element;

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
import java.util.List;

/**
 * This abstract class represents the concept an element in a mathematical
 * group. It allows applying the group operation and other methods from a
 * {@link Group} in a convenient way. Most methods provided by {@link Element}
 * have an equivalent method in {@link Group}.
 *
 * @see Group
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class Element implements Serializable {

  private static final long serialVersionUID = 1L;

  public static final String STANDARD_HASH_ALGORITHM = "SHA-256";
  private final Set set;
  private BigInteger value;

  protected Element(final Set set) {
    if (set == null) {
      throw new IllegalArgumentException();
    }
    this.set = set;
  }

  protected Element(final Set set, final BigInteger value) {
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
  public final Set getSet() {
    return this.set;
  }

  /**
   *
   * @return
   */
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
  public final BigInteger getValue() {
    if (this.value == null) {
      this.value = standardGetValue();
    }
    return this.value;
  }

  /**
   * Returns the element with the given index. The indices are numbered from 0
   * to the element's arity minus one.
   *
   * @param index The given index
   * @return The corresponding element
   * @throws IndexOutOfBoundsException if
   * {@code index<0} or {@code index>arity-1}
   */
  public final Element getAt(final int index) {
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    return standardGetAt(index);
  }

  /**
   * Selects and returns in a hierarchy of elements the element that corresponds
   * to a given array of indices (e.g., 0,3,2 for the third element in the
   * fourth element of the first element). Returns {@code this} element if
   * {@code indices} is empty.
   *
   * @param indices The given array of indices
   * @return The corresponding element
   * @throws IllegalArgumentException if {@code indices} is null or if its
   * length does exceed the hierarchy's depth
   * @throws IndexOutOfBoundsException if {@code indices} contains an
   * out-of-bounds index
   */
  public final Element getAt(final int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    Element element = this;
    for (final int index : indices) {
      element = element.getAt(index);
    }
    return element;
  }

  /**
   * Returns an array of length {@code this.getArity()} containing all the
   * elements of which {@code this} element is composed of. If the arity is 1,
   * the array contains only {@code this}.
   */
  public final Element[] getAll() {
    Element[] result = new Element[this.getArity()];
    for (int index = 0; index < this.getArity(); index++) {
      result[index] = this.getAt(index);
    }
    return result;
  }

  public final byte[] hashValue() {
    return this.hashValue(Element.STANDARD_HASH_ALGORITHM);
  }

  public final byte[] hashValue(String hashAlgorithm) {
    if (hashAlgorithm == null) {
      throw new IllegalArgumentException();
    }
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance(hashAlgorithm);
    } catch (final NoSuchAlgorithmException e) {
      throw new IllegalArgumentException();
    }
    return this.hashValue(messageDigest);
  }

  public byte[] hashValue(MessageDigest messageDigest) {
    if (messageDigest == null) {
      throw new IllegalArgumentException();
    }
    messageDigest.reset();
    return messageDigest.digest(this.getValue().toByteArray());
  }

  public byte[] recursiveHashValue() {
    return this.recursiveHashValue(Element.STANDARD_HASH_ALGORITHM);
  }

  public byte[] recursiveHashValue(String hashAlgorithm) {
    if (hashAlgorithm == null) {
      throw new IllegalArgumentException();
    }
    MessageDigest messageDigest;
    try {
      messageDigest = MessageDigest.getInstance(hashAlgorithm);
    } catch (final NoSuchAlgorithmException e) {
      throw new IllegalArgumentException();
    }
    return this.recursiveHashValue(messageDigest);
  }

  public byte[] recursiveHashValue(MessageDigest messageDigest) {
    if (messageDigest == null) {
      throw new IllegalArgumentException();
    }
    messageDigest.reset();
    if (this.getArity() == 1) {
      return messageDigest.digest(this.getValue().toByteArray());
    }
    byte[][] hashValues = new byte[this.getArity()][];
    for (int i = 0; i < this.getArity(); i++) {
      hashValues[i] = this.getAt(i).recursiveHashValue(messageDigest);
    }
    for (int i = 0; i < this.getArity(); i++) {
      messageDigest.update(hashValues[i]);
    }
    return messageDigest.digest();
  }

  //
  // The following methods are equivalent to corresponding Set methods
  //

  /**
   * @see Group#getArity()
   */
  public final int getArity() {
    ProductSet productSet = this.getProductSet();
    return productSet.getArity();
  }

  /**
   * @see Group#isNull()
   */
  public final boolean isNull() {
    ProductSet productSet = this.getProductSet();
    return productSet.isNull();
  }

  /**
   * @see Group#isAtomicGroup()
   */
  public final boolean isAtomic() {
    return this.set.isAtomic();
  }

  /**
   * @see Group#apply(Element, Element)
   */
  public final Element apply(final Element element) {
    SemiGroup semiGroup = this.getSemiGroup();
    return semiGroup.apply(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public final Element applyInverse(final Element element) {
    Group group = this.getGroup();
    return group.applyInverse(this, element);
  }

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public final Element selfApply(final BigInteger amount) {
    SemiGroup semiGroup = this.getSemiGroup();
    return semiGroup.selfApply(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  public final Element selfApply(final Element amount) {
    SemiGroup semiGroup = this.getSemiGroup();
    return semiGroup.selfApply(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  public final Element selfApply(final int amount) {
    SemiGroup semiGroup = this.getSemiGroup();
    return semiGroup.selfApply(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  public final Element selfApply() {
    SemiGroup semiGroup = this.getSemiGroup();
    return semiGroup.selfApply(this);
  }

  /**
   * @see Group#apply(Element, Element)
   */
  public final Element add(final Element element) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return semiGroup.add(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public final Element subtract(final Element element) {
    AdditiveGroup group = this.getAdditiveGroup();
    return group.subtract(this, element);
  }

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public final Element times(final BigInteger amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  public final Element times(final Element amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  public final Element times(final int amount) {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return semiGroup.times(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  public final Element timesTwo() {
    AdditiveSemiGroup semiGroup = this.getAdditiveSemiGroup();
    return semiGroup.timesTwo(this);
  }

  /**
   * @see Group#apply(Element, Element)
   */
  public final Element multiply(final Element element) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return semiGroup.multiply(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public final Element divide(final Element element) {
    MultiplicativeGroup group = this.getMultiplicativeGroup();
    return group.divide(this, element);
  }

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public final Element power(final BigInteger amount) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return semiGroup.power(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  public final Element power(final Element amount) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return semiGroup.power(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  public final Element power(final int amount) {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return semiGroup.power(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  public final Element square() {
    MultiplicativeSemiGroup semiGroup = this.getMultiplicativeSemiGroup();
    return semiGroup.square(this);
  }

  /**
   * @see Group#invert(Element)
   */
  public final Element invert() {
    Group group = this.getGroup();
    return group.invert(this);
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
    return this.standardEquals((Element) object);
  }

  @Override
  public final int hashCode() {
    final int prime = 31;
    int result = 1;
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

  protected Element standardGetAt(final int index) {
    throw new UnsupportedOperationException();
  }

  protected boolean standardEquals(Element element) {
    return this.getValue().equals(element.getValue());
  }

  protected int standardHashCode() {
    return this.getValue().hashCode();
  }

  protected String standardToString() {
    return this.getValue().toString();
  }

}