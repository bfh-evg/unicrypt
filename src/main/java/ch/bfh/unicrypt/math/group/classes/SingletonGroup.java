/**
 *
 */
package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.element.classes.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ch.bfh.unicrypt.math.group.abstracts.AbstractAtomicCyclicGroup;
import ch.bfh.unicrypt.math.group.interfaces.Set;

/**
 * @author rolfhaenni
 *
 */
public class SingletonGroup extends AbstractAtomicCyclicGroup {

  private static final long serialVersionUID = 1L;
  private final AtomicElement element;

  private SingletonGroup(BigInteger value) {
    this.element = new AtomicElement(this, value){};
  }

  public final AtomicElement getElement() {
    return this.element;
  }

  public final BigInteger getValue() {
    return this.getElement().getValue();
  }

  //
  // The following protected methods override the standard implementation from {@code AbstractGroup}
  //

  @Override
  protected boolean standardEquals(Set set) {
    return this.getValue().equals(((SingletonGroup) set).getValue());
  }

  @Override
  protected int standardHashCode() {
    return this.getValue().hashCode();
  }

  @Override
  protected String standardToString() {
    return this.getValue().toString();
  }

  @Override
  protected AtomicElement abstractGetElement(final BigInteger value) {
    return this.getElement();
  }

  @Override
  protected AtomicElement standardSelfApply(Element element, BigInteger amount) {
    return this.getElement();
  }

  @Override
  protected AtomicElement abstractGetRandomElement(Random random) {
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
  protected AtomicElement abstractGetIdentityElement() {
    return this.getElement();
  }

  @Override
  protected AtomicElement abstractApply(Element element1, Element element2) {
    return this.getElement();
  }

  @Override
  protected AtomicElement abstractInvert(Element element) {
    return this.getElement();
  }

  @Override
  protected AtomicElement abstractGetDefaultGenerator() {
    return this.getElement();
  }

  @Override
  protected AtomicElement abstractGetRandomGenerator(Random random) {
    return this.getElement();
  }

  @Override
  protected boolean abstractIsGenerator(Element element) {
    return true;
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
