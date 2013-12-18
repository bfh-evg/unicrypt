/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.dualistic.classes;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.PrimeField;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.interfaces.MultiplicativeGroup;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public class PolynomialField extends PolynomialRing implements FiniteField {

  private PolynomialElement irreduciblePolynomial;
  private int degree;

  protected PolynomialField(PrimeField primeField) {
    super(primeField);
  }

  public PrimeField getPrimeField() {
    return (PrimeField) super.getRing();
  }

  public int getDegree() {
    return this.degree;
  }

  //
  // The following protected methods override the standard implementation from
  // various super-classes
  //

  @Override
  public BigInteger getCharacteristic() {
    return this.getPrimeField().getOrder();
  }

  @Override
  public MultiplicativeGroup getMultiplicativeGroup() {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public PolynomialElement divide(Element element1, Element element2) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public PolynomialElement oneOver(Element element) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  //
  // STATIC FACTORY METHODS
  //
  public static PolynomialField getInstance(PrimeField primeField, PolynomialElement irreduciblePolynomial) {
    if (primeField == null || irreduciblePolynomial == null || !irreduciblePolynomial.getSet().getSemiRing().isEquivalent(primeField)) {
      throw new IllegalArgumentException();
    }
    return new PolynomialField(primeField);
  }

}
