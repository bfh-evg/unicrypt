package ch.bfh.unicrypt.math.element;

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
 * This abstract class represents the concept an element in a mathematical group.
 * It allows applying the group operation and other methods from a {@link Group}
 * in a convenient way. Most methods provided by {@link Element} have an equivalent
 * method in {@link Group}.
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
   * Returns the positive BigInteger value that corresponds the element.
   * @return The corresponding BigInteger value
   */
  public final BigInteger getValue() {
    if (this.value == null) {
      this.value = standardGetValue();
    }
    return this.value;
  }

  /**
   * Returns the element with the given index. The indices are numbered from 0 to the element's arity minus one.
   * @param index The given index
   * @return The corresponding element
   * @throws IndexOutOfBoundsException if {@code index<0} or {@code index>arity-1}
   */
  public final Element getAt(final int index) {
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    return standardGetAt(index);
  }

  /**
   * Selects and returns in a hierarchy of elements the element that corresponds to a given array of indices
   * (e.g., 0,3,2 for the third element in the fourth element of the first element). Returns {@code this} element if
   * {@code indices} is empty.
   * @param indices The given array of indices
   * @return The corresponding element
   * @throws IllegalArgumentException if {@code indices} is null or if its length does exceed the hierarchy's depth
   * @throws IndexOutOfBoundsException if {@code indices} contains an out-of-bounds index
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
   * Returns an array of length {@code this.getArity()} containing all the elements of which
   * {@code this} element is composed of. If the arity is 1, the array contains only {@code this}.
   */
  public final Element[] getElements() {
    Element[] result = new Element[this.getArity()];
    for (int index=0; index<this.getArity(); index++) {
      result[index] = this.getAt(index);
    }
    return result;
  }

  //
  // The following methods are equivalent to corresponding Group methods
  //

  /**
   * @see Group#getArity()
   */
  public final int getArity() {
    return this.set.getArity();
  }

  /**
   * @see Group#isNull()
   */
  public final boolean isNull() {
    return this.set.isNull();
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
    for (int i=0; i<this.getArity(); i++) {
      hashValues[i] = this.getAt(i).recursiveHashValue(messageDigest);
    }
    for (int i=0; i<this.getArity(); i++) {
      messageDigest.update(hashValues[i]);
    }
    return messageDigest.digest();
  }

  //
  // The standard implementations of the following three methods are
  // insufficient for elements.
  //

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[value=" + this.getValue() + ", " + this.getSet() + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    return (prime * this.getSet().hashCode()) + this.getValue().hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (obj instanceof Element) {
      Element other = (Element) obj;
      return this.getSet().equals(other.getSet()) && this.getValue().equals(other.getValue());
    }
    return false;
  }

  //
  // The following protected methods are standard implementations, which may change in sub-classes
  //

  @SuppressWarnings("static-method")
  protected BigInteger standardGetValue() {
    throw new UnsupportedOperationException();
  }

  @SuppressWarnings("unused")
  protected Element standardGetAt(final int index) {
    return this;
  }

  //
  // STATIC FACTORY METHODS
  //

  /**
   * This is a static factory method to construct a composed element without the need of constructing the
   * corresponding product or power group beforehand. The input elements are given as an array.
   * @param elements The array of input elements
   * @return The corresponding tuple element
   * @throws IllegalArgumentException if {@code elements} is null or contains null
   */
  public static Element getInstance(Element... elements) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    int arity = elements.length;
    if (arity == 1) {
      return elements[0];
    }
    final Set[] sets = new Set[arity];
    int i = 0;
    for (final Element element: elements) {
      if (element == null) {
        throw new IllegalArgumentException();
      }
      sets[i] = element.getSet();
      i++;
    }
    Set productSet = ProductSet.getInstance(sets);
    return productSet.getElement(elements);
  }

  /**
   * This is a static factory method to construct a composed element without the need of constructing the
   * corresponding product or power group beforehand. The input elements are given as a list.
   * @param elements The list of input elements
   * @return The corresponding tuple element
   * @throws IllegalArgumentException if {@code elements} is null or contains null
   */
  public static Element getInstance(List<Element> elements){
    if(elements == null){
      throw new IllegalArgumentException();
    }
    return Element.getInstance(elements.toArray(new Element[0]));
  }

}