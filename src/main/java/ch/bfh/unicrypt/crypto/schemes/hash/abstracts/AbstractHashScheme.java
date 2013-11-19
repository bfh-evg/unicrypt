/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.hash.abstracts;

import ch.bfh.unicrypt.crypto.schemes.hash.interfaces.HashScheme;
import ch.bfh.unicrypt.crypto.schemes.scheme.abstracts.AbstractScheme;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractHashScheme<MS extends Set, HS extends Set, HE extends Element>
       extends AbstractScheme
       implements HashScheme {

  protected Function hashFunction;
  protected Function checkFunction;

  @Override
  public final MS getMessageSpace() {
    return (MS) this.getHashFunction().getDomain();
  }

  @Override
  public Set getHashSpace() {
    return (HS) this.getHashFunction().getCoDomain();
  }

  @Override
  public Function getHashFunction() {
    if (this.hashFunction == null) {
      this.hashFunction = this.abstractGetHashFunction();
    }
    return this.hashFunction;
  }

  @Override
  public Function getCheckFunction() {
    if (this.checkFunction == null) {
      this.hashFunction = CompositeFunction.getInstance(
             MultiIdentityFunction.getInstance(ProductSet.getInstance(this.getMessageSpace(), this.getHashSpace())),
             ProductFunction.getInstance(SelectionFunction.getInstance(null, 0)

    }
    return this.hashFunction;
  }

  @Override
  public Element hash(Element message) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public BooleanElement check(Element message, Element hashValue) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

}
