/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.hash.abstracts;

import ch.bfh.unicrypt.crypto.schemes.hash.interfaces.HashScheme;
import ch.bfh.unicrypt.crypto.schemes.scheme.abstracts.AbstractScheme;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArrayElement;
import ch.bfh.unicrypt.math.algebra.general.classes.FiniteByteArraySet;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.EqualityFunction;
import ch.bfh.unicrypt.math.function.classes.MultiIdentityFunction;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.classes.SelectionFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractHashScheme<MS extends ByteArrayMonoid, HS extends FiniteByteArraySet, HE extends FiniteByteArrayElement>
       extends AbstractScheme
       implements HashScheme {

  protected Function hashFunction;
  protected Function checkFunction;

  @Override
  public final MS getMessageSpace() {
    return (MS) this.getHashFunction().getDomain();
  }

  @Override
  public final HS getHashSpace() {
    return (HS) this.getHashFunction().getCoDomain();
  }

  @Override
  public final Function getHashFunction() {
    if (this.hashFunction == null) {
      this.hashFunction = this.abstractGetHashFunction();
    }
    return this.hashFunction;
  }

  @Override
  public final Function getCheckFunction() {
    if (this.checkFunction == null) {
      ProductSet checkDomain = ProductSet.getInstance(this.getMessageSpace(), this.getHashSpace());
      this.hashFunction = CompositeFunction.getInstance(
             MultiIdentityFunction.getInstance(checkDomain),
             ProductFunction.getInstance(CompositeFunction.getInstance(SelectionFunction.getInstance(checkDomain, 0),
                                                                       this.getHashFunction()),
                                         SelectionFunction.getInstance(checkDomain, 1)),
             EqualityFunction.getInstance(this.getHashSpace(), 2));
    }
    return this.hashFunction;
  }

  @Override
  public final HE hash(Element message) {
    return (HE) this.getHashFunction().apply(message);
  }

  @Override
  public final BooleanElement check(Element message, Element hashValue) {
    return (BooleanElement) this.getCheckFunction().apply(message, hashValue);
  }

  protected abstract Function abstractGetHashFunction();

}
