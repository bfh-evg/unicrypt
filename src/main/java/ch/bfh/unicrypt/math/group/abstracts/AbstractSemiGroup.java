package ch.bfh.unicrypt.math.group.abstracts;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.classes.ProductGroup;
import ch.bfh.unicrypt.math.group.classes.ZPlusMod;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.SemiGroup;
import ch.bfh.unicrypt.math.utility.MathUtil;

/**
 * This abstract class provides a basis implementation for objects of type
 * {@link SemiGroup}.
 *
 * @see Element
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public abstract class AbstractSemiGroup extends AbstractSet implements SemiGroup {

  private static final long serialVersionUID = 1L;

  @Override
  public final Element apply(final Element element1, final Element element2) {
    if (!this.contains(element1) || !this.contains(element2)) {
      throw new IllegalArgumentException();
    }
    return abstractApply(element1, element2);
  }

  @Override
  public final Element apply(final Element... elements) {
    if (elements == null || elements.length == 0) {
      throw new IllegalArgumentException();
    }
    Element result = null;
    for (Element element : elements) {
      if (result == null) {
        result = element;
      } else {
        result = this.apply(result, element);
      }
    }
    return result;
  }

  @Override
  public final Element selfApply(final Element element, final BigInteger amount) {
    if (!this.contains(element) || (amount == null)) {
      throw new IllegalArgumentException();
    }
    return standardSelfApply(element, amount);
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
    return this.apply(element, element);
  }

  @Override
  public final Element multiSelfApply(final Element[] elements, final BigInteger[] amounts) {
    if ((elements == null) || (amounts == null) || (elements.length != amounts.length) || (elements.length == 0)) {
      throw new IllegalArgumentException();
    }
    Element[] results = new Element[elements.length];
    for (int i = 0; i < elements.length; i++) {
      results[i] = this.selfApply(elements[i], amounts[i]);
    }
    return this.apply(results);
  }

  //
  // The following protected methods are standard implementations for sets.
  // They may need to be changed in certain sub-classes.
  //

  protected Element standardSelfApply(Element element, BigInteger amount) {
    if (amount.signum() <= 0) {
      throw new IllegalArgumentException();
    }
    Element result = null;
    for (int i = amount.bitLength()-1; i >= 0; i--) {
      if (result == null) {
        result = element;
      } else {
        result = result.selfApply();
      }
      if (amount.testBit(i)) {
        result = result.apply(element);
      }
    }
    return result;
  }

  //
  // The following protected abstract method must be implemented in every direct sub-class.
  //

  protected abstract Element abstractApply(Element element1, Element element2);

}

// THIS IS OLD CODE FOR AN OPTIMZED multiSelfApply ALGORITHM (works only for commutative operators)
//      bitLength = Math.max(bitLength, amounts[i].bitLength());
//    }
//    int bitLength = 0;
//    for (int i = 0; i < elements.length; i++) {
//      if ((elements[i] == null) || (amounts[i] == null) || (amounts[i].equals(BigInteger.ZERO))) {
//        throw new IllegalArgumentException();
//      }
//      bitLength = Math.max(bitLength, amounts[i].bitLength());
//    }
//    Element result = this.getIdentityElement();
//    for (int i = bitLength - 1; i >= 0; i--) {
//      result = result.selfApply();
//      for (int j = 0; j < amounts.length; j++) {
//        if (amounts[j].testBit(i)) {
//          result = result.apply(elements[j]);
//        }
//      }
//    }
//    return result;