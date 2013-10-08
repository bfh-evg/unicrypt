package ch.bfh.unicrypt.crypto.blinding.classes;

import ch.bfh.unicrypt.crypto.blinding.abstracts.AbstractBlindingScheme;
import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlusMod;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.function.classes.SelfApplyFunction;

public class StandardBlindingScheme extends AbstractBlindingScheme {

  Group blindingSpace;
  ZPlusMod blindingValueSpace;
  SelfApplyFunction blindingFunction;

  public StandardBlindingScheme(final Group blindingSpace) {
    if (blindingSpace == null)
      throw new IllegalArgumentException();
    this.blindingSpace = blindingSpace;
    this.blindingValueSpace = blindingSpace.getZModOrder();
    this.blindingFunction = new SelfApplyFunction(this.blindingSpace, this.blindingValueSpace);
  }


  // @Override
  // public Element unblind(final Element value, final AdditiveElement
  // blindingValue) {
  // if(blindingValue==null)
  // throw new IllegalArgumentException();
  // return this.blind(value, blindingValue.invert());
  // That does not work, as the blindingValue is an element of an additive
  // group. Hence blinding again with the
  // inverse value always results in 1
  // (x^y)^{-y}=x^{y-y}=x^0=1
  // }


}
