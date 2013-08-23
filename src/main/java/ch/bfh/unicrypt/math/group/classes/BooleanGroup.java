package ch.bfh.unicrypt.math.group.classes;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.abstracts.AbstractElement;

/**
 * This interface represents the group that consists of two elements only, for
 * example TRUE and FALSE. This group is isomorphic to the additive group of
 * integers modulo 2. It is therefore possible to consider and implement it as a
 * specialization of {@link ZPlusMod}.
 *
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 2.0
 */
public class BooleanGroup extends ZPlusMod {

  private static final long serialVersionUID = 1L;
  public static final AbstractElement TRUE = BooleanGroup.getInstance().getElement(BigInteger.ONE);
  public static final AbstractElement FALSE = BooleanGroup.getInstance().getElement(BigInteger.ZERO);

  private BooleanGroup() {
    super(BigInteger.valueOf(2));
  }

  /**
   * Creates and returns the group element that corresponds to a given Boolean
   * value.
   *
   * @param value The given Boolean value
   * @return The corresponding group element
   */
  public AbstractElement getElement(final boolean value) {
    if (value) {
      return BooleanGroup.TRUE;
    }
    return BooleanGroup.FALSE;
  }

  /**
   * Returns the Boolean value that corresponds to a given Boolean group
   * element.
   *
   * @param element The given Boolean group element
   * @return The corresponding Boolean value
   * @throws IllegalArgumentException if {@code element} is null or does not
   * belong to the group
   */
  public boolean getBoolean(final AbstractElement element) {
    if (!contains(element)) {
      throw new IllegalArgumentException();
    }
    return element.getValue().equals(BigInteger.ONE);
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //

  @Override
  public String standardToString() {
    return "";
  }

  //
  // STATIC FACTORY METHODS
  //

  private static BooleanGroup instance;

  /**
   * Returns the singleton object of this class.
   *
   * @return The singleton object of this class
   */
  public final static BooleanGroup getInstance() {
    if (BooleanGroup.instance == null) {
      BooleanGroup.instance = new BooleanGroup();
    }
    return BooleanGroup.instance;
  }

}
