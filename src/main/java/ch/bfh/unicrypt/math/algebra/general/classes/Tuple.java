/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.general.abstracts.AbstractElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.Compound;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author rolfhaenni
 */
public class Tuple
       extends AbstractElement<ProductSet, Tuple>
       implements Compound<Tuple, Element> {

  private final Element[] elements;
  private final int arity;

  protected Tuple(final ProductSet set, final Element[] elements) {
    super(set);
    this.elements = elements.clone();
    this.arity = elements.length;
  }

  @Override
  protected BigInteger standardGetValue() {
    int arity = this.getArity();
    BigInteger[] values = new BigInteger[arity];
    for (int i = 0; i < arity; i++) {
      values[i] = this.elements[i].getValue();
    }
    return MathUtil.elegantPair(values);
  }

  @Override
  protected ByteArrayElement standardGetRecursiveHashValue(MessageDigest messageDigest) {
    int arity = this.getArity();
    ByteArrayElement[] hashValues = new ByteArrayElement[arity];
    for (int i = 0; i < arity; i++) {
      hashValues[i] = this.getAt(i).getRecursiveHashValue(messageDigest);
    }
    return ByteArrayMonoid.getInstance().apply(hashValues).getHashValue(messageDigest);
  }

  @Override
  public int getArity() {
    return this.arity;
  }

  @Override
  public final boolean isNull() {
    return this.getArity() == 0;
  }

  @Override
  public final boolean isUniform() {
    return this.elements.length <= 1;
  }

  @Override
  public Element getFirst() {
    return this.getAt(0);

  }

  @Override
  public Element getAt(int index) {
    if (index < 0 || index >= this.getArity()) {
      throw new IndexOutOfBoundsException();
    }
    if (this.isUniform()) {
      return this.elements[0];
    }
    return this.elements[index];
  }

  @Override
  public Element getAt(int... indices) {
    if (indices == null) {
      throw new IllegalArgumentException();
    }
    Element element = this;
    for (final int index : indices) {
      if (element.isTuple()) {
        element = ((Tuple) element).getAt(index);
      } else {
        throw new IllegalArgumentException();
      }
    }
    return element;
  }

  @Override
  public Element[] getAll() {
    int arity = this.getArity();
    Element[] result = new Element[arity];
    for (int i = 0; i < arity; i++) {
      result[i] = this.getAt(i);
    }
    return result;
  }

  @Override
  public Tuple removeAt(final int index) {
    int arity = this.getArity();
    if (index < 0 || index >= arity) {
      throw new IndexOutOfBoundsException();
    }
    final Element[] remainingElements = new Element[arity - 1];
    for (int i = 0; i < arity - 1; i++) {
      if (i < index) {
        remainingElements[i] = this.getAt(i);
      } else {
        remainingElements[i] = this.getAt(i + 1);
      }
    }
    return this.getSet().removeAt(index).getElement(remainingElements);
  }

  @Override
  public Iterator<Element> iterator() {
    final Tuple tuple = this;
    return new Iterator<Element>() {
      int currentIndex = 0;

      @Override
      public boolean hasNext() {
        return currentIndex < tuple.getArity();
      }

      @Override
      public Element next() {
        if (this.hasNext()) {
          return tuple.getAt(this.currentIndex++);
        }
        throw new NoSuchElementException();
      }

      @Override
      public void remove() {
        throw new UnsupportedOperationException("Not supported yet.");
      }
    };
  }

  @Override
  protected boolean standardIsEqual(Element element) {
    Tuple other = (Tuple) element;
    for (int i = 0; i < this.getArity(); i++) {
      if (!this.getAt(i).isEqual(other.getAt(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  protected String standardToStringName() {
    return this.getSet().getClass().getSimpleName() + "Tuple";
  }

  @Override
  protected String standardToStringContent() {
    String result = "";
    String separator = "";
    for (Element element : this) {
      result = result + separator + element.toString();
      separator = ", ";
    }
    return result;
  }

}
