package ch.bfh.unicrypt.math.element;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;

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

  private final Group group;
  private BigInteger value;

  protected Element(final Group group) {
    if (group == null) {
      throw new IllegalArgumentException();
    }
    this.group = group;
  }

  protected Element(final Group group, final BigInteger value) {
    this(group);
    if (!group.contains(value)) {
      throw new IllegalArgumentException();
    }
    this.value = value;
  }

  /**
   * Returns the unique {@link Group} to which this element belongs
   * @return The element's group
   */
  public final Group getGroup() {
    return this.group;
  }

  /**
   * Returns the positive BigInteger value that corresponds the element.
   * @return The corresponding BigInteger value
   */
  public final BigInteger getValue() {
    if (this.value == null) {
      this.value = computeValue();
    }
    return this.value;
  }

  /**
   * Returns the element with the given index. The indices are numbered from 0 to the element's arity minus one.
   * @param index The given index
   * @return The corresponding element
   * @throws IndexOutOfBoundsException if {@code index<0} or {@code index>arity-1}  
   */
  public final Element getElementAt(final int index) {
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    return computeElementAt(index);
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
  public final Element getElementAt(final int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    Element element = this;
    for (final int index : indices) {
      element = element.getElementAt(index);
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
      result[index] = this.getElementAt(index);
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
    return this.group.getArity();
  }

  /**
   * @see Group#isAtomicGroup()
   */
  public final boolean isAtomic() {
    return this.group.isAtomicGroup();
  }

  /**
   * @see Group#apply(Element, Element)
   */
  public final Element apply(final Element element) {
    return this.group.apply(this, element);
  }

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public final Element applyInverse(final Element element) {
    return this.group.applyInverse(this, element);
  }

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public final Element selfApply(final BigInteger amount) {
    return this.group.selfApply(this, amount);
  }

  /**
   * @see Group#selfApply(Element, Element)
   */
  public Element selfApply(final Element amount) {
    return this.group.selfApply(this, amount);
  }

  /**
   * @see Group#selfApply(Element, int)
   */
  public final Element selfApply(final int amount) {
    return this.group.selfApply(this, amount);
  }

  /**
   * @see Group#selfApply(Element)
   */
  public final Element selfApply() {
    return this.group.selfApply(this);
  }

  /**
   * @see Group#invert(Element)
   */
  public final Element invert() {
    return this.group.invert(this);
  }

  /**
   * @see Group#isIdentity(Element)
   */
  public final boolean isIdentity() {
    return this.group.isIdentity(this);
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
      hashValues[i] = this.getElementAt(i).recursiveHashValue(messageDigest);
    }
    for (int i=0; i<this.getArity(); i++) {
      messageDigest.update(hashValues[i]);
    }
    return messageDigest.digest();
  }

  //
  // The standard implementations of the following three methods are insufficient for group elements
  //

  @Override
  public String toString() {
    return this.getClass().getSimpleName() + "[value=" + this.getValue() + ", " + this.getGroup() + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    return (prime * this.getGroup().hashCode()) + this.getValue().hashCode();
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
      return this.getGroup().equals(other.getGroup()) && this.getValue().equals(other.getValue());
    }
    return false;
  }

  //
  // The following protected methods are standard implementations, which may change in sub-classes
  //

  @SuppressWarnings("static-method")
  protected BigInteger computeValue() {
    return null;
  }

  @SuppressWarnings("unused")
  protected Element computeElementAt(final int index) {
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
    final Group[] groups = new Group[arity];
    int i = 0;
    for (final Element element: elements) {
      if (element == null) {
        throw new IllegalArgumentException();
      }
      groups[i] = element.getGroup();
      i++;
    }
    Group group = ProductGroup.getInstance(groups);
    return group.getElement(elements);
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