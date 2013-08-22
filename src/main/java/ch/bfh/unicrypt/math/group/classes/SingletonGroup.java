/**
 *
 */
package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.abstracts.AbstractCyclicGroup;
import ch.bfh.unicrypt.math.group.abstracts.AbstractGroup;
import ch.bfh.unicrypt.math.group.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.Set;

/**
 * @author rolfhaenni
 *
 */
public class SingletonGroup extends AbstractCyclicGroup {

  private static final long serialVersionUID = 1L;
  private final Element element;

  private SingletonGroup(BigInteger value) {
    this.element = new SingletonElement(this, value);
  }

  public final Element getElement() {
    return this.element;
  }

  public final BigInteger getValue() {
    return this.getElement().getValue();
  }

  //
  // The following protected methods override the standard implementation from {@code AbstractGroup}
  //

  @Override
  protected Element standardSelfApply(Element element, BigInteger amount) {
    return this.getElement();
  }

  @Override
  protected Element abstractGetRandomElement(Random random) {
    return this.getElement();
  }

  @Override
  protected BigInteger abstractGetOrder() {
    return BigInteger.ONE;
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    return this.getValue().equals(value);
  }

  @Override
  protected Element abstractGetIdentityElement() {
    return this.getElement();
  }

  @Override
  protected Element abstractApply(Element element1, Element element2) {
    return this.getElement();
  }

  @Override
  protected Element abstractInvert(Element element) {
    return this.getElement();
  }

  @Override
  protected Element abstractGetElement(final BigInteger value) {
    return this.getElement();
  }

  @Override
  protected Element abstractGetDefaultGenerator() {
    return this.getElement();
  }

  @Override
  protected Element abstractGetRandomGenerator(Random random) {
    return this.getElement();
  }

  @Override
  protected boolean abstractIsGenerator(Element element) {
    return true;
  }

  @Override
  protected boolean abstractEquals(Set set) {
    return this.getValue().equals(((SingletonGroup) set).getValue());
  }

  //
  // LOCAL ELEMENT CLASS
  //

  final private class SingletonElement extends Element {

    private static final long serialVersionUID = 1L;

    protected SingletonElement(final Set set, final BigInteger value) {
      super(set, value);
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
