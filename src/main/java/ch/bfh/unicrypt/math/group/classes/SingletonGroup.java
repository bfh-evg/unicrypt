/**
 * 
 */
package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.abstracts.AbstractGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;

/**
 * @author rolfhaenni
 *
 */
public class SingletonGroup extends AbstractGroup {

  private static final long serialVersionUID = 1L;

  private final Element element;
  
  private SingletonGroup(BigInteger value) {
    this.element = abstractGetElement(value);
  }

  @Override
  protected Element abstractGetRandomElement(Random random) {
    return this.element;
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return BigInteger.ONE;
  }
    
  @Override
  protected boolean abstractContains(BigInteger value) {
    return this.element.getValue().equals(value);
  }

  @Override
  protected Element abstractIdentityElement() {
    return this.element;
  }

  @Override
  protected Element abstractApply(Element element1, Element element2) {
    return this.element;
  }

  @Override
  protected Element abstractSelfApply(Element element, BigInteger amount) {
    return this.element;
  }

  @Override
  protected Element abstractInvert(Element element) {
    return this.element;
  }
  
  //
  // The following protected methods override the standard implementation from {@code AbstractGroup}
  //

  @Override
  protected Element abstractGetElement(final BigInteger value, final Element... values) {
    return new SingletonElement(this, value);
  }

  //
  // LOCAL ELEMENT CLASS
  //  

  final private class SingletonElement extends Element {

    private static final long serialVersionUID = 1L;

    protected SingletonElement(final Group group, final BigInteger value) {
      super(group, value);
    }

  }

  //
  // STATIC FACTORY METHODS
  //

  private static final Map<BigInteger,SingletonGroup> instances = new HashMap<BigInteger,SingletonGroup>();

  public static SingletonGroup getInstance(final BigInteger value) {
    if (value == null) {
      throw new IllegalArgumentException();
    }
    SingletonGroup instance = SingletonGroup.instances.get(value);
    if (instance == null) {
      instance = new SingletonGroup(value);
      SingletonGroup.instances.put(value, instance);
    }    
    return instance;
  }

  public static SingletonGroup getInstance() {
    return getInstance(BigInteger.ZERO);
  }

}
