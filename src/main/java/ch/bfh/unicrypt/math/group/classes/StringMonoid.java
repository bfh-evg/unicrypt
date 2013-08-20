/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.group.classes;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.group.abstracts.AbstractAdditiveMonoid;
import ch.bfh.unicrypt.math.group.interfaces.Set;
import ch.bfh.unicrypt.math.helper.Permutation;
import ch.bfh.unicrypt.math.utility.MathUtil;
import java.math.BigInteger;
import java.util.Random;

/**
 *
 * @author rolfhaenni
 */
public class StringMonoid extends AbstractAdditiveMonoid {

  private StringMonoid() {
  }

  @Override
  protected Element abstractGetIdentityElement() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected Element abstractApply(Element element1, Element element2) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected BigInteger abstractGetOrder() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected Element abstractGetElement(BigInteger value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected Element abstractGetRandomElement(Random random) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected boolean abstractContains(BigInteger value) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  protected boolean abstractEquals(Set set) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  //
  // LOCAL ELEMENT CLASS
  //

 final private class StringElement extends Element {

    private static final long serialVersionUID = 1L;

    private final String string;

    private StringElement(final Set set, final String string) {
      super(set);
      this.string = string;
    }

    public String getString() {
      return this.string;
    }

    @Override
    protected BigInteger standardGetValue() {
      return new BigInteger(getString().getBytes());
    }

    @Override
    protected boolean standardEquals(Element element) {
      return this.getString().equals(((StringMonoid.StringElement) element).getString());
    }

    @Override
    protected int standardHashCode() {
      return this.getString().hashCode();
    }

    @Override
    public String standardToString() {
      return this.getString();
    }

  }
  //
  // STATIC FACTORY METHODS
  //

  private static StringMonoid instance;

  /**
   * Returns the singleton object of this class.
   * @return The singleton object of this class
   */
  public static StringMonoid getInstance() {
    if (StringMonoid.instance == null) {
      StringMonoid.instance = new StringMonoid();
    }
    return StringMonoid.instance;
  }

}
