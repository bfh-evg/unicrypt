package ch.bfh.unicrypt.math.algebra.general.interfaces;

import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import java.math.BigInteger;
import java.security.MessageDigest;

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
public interface Element<S extends Set> {

  public static final String STANDARD_HASH_ALGORITHM = "SHA-256";

  public boolean isCompound();

  /**
   *
   * @return
   */
  public S getSet();

  /**
   * Returns the positive BigInteger value that corresponds the element.
   *
   * @return The corresponding BigInteger value
   */
  public BigInteger getValue();

  public ByteArrayElement getHashValue();

  public ByteArrayElement getHashValue(String hashAlgorithm);

  public ByteArrayElement getHashValue(MessageDigest messageDigest);

  public ByteArrayElement getRecursiveHashValue();

  public ByteArrayElement getRecursiveHashValue(String hashAlgorithm);

  public ByteArrayElement getRecursiveHashValue(MessageDigest messageDigest);

  //
  // The following methods are equivalent to corresponding Set methods
  //
  /**
   * @see Group#apply(Element, Element)
   */
  public Element<S> apply(Element element);

  /**
   * @see Group#applyInverse(Element, Element)
   */
  public Element<S> applyInverse(Element element);

  /**
   * @see Group#selfApply(Element, BigInteger)
   */
  public Element<S> selfApply(BigInteger amount);

  /**
   * @see Group#selfApply(Element, Element)
   */
  public Element<S> selfApply(Element amount);

  /**
   * @see Group#selfApply(Element, int)
   */
  public Element<S> selfApply(int amount);

  /**
   * @see Group#selfApply(Element)
   */
  public Element<S> selfApply();

  /**
   * @see Group#invert(Element)
   */
  public Element<S> invert();

  /**
   * @see Group#isIdentityElement(Element)
   */
  public boolean isIdentity();

  /**
   * @see CyclicGroup#isGenerator(Element)
   */
  public boolean isGenerator();

  //
  // The standard implementations of the following three methods are
  // insufficient for elements.
  //
  @Override
  public boolean equals(Object object);

  @Override
  public int hashCode();

  @Override
  public String toString();

}
