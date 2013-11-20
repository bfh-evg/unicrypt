package ch.bfh.unicrypt.crypto.schemes.blinding.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface BlindingScheme {

  public Element blind(Element value, Element blindingValue);

  public Element unblind(Element value, Element blindingValue);

  public Set getBlindingValueSpace();

  public Function getBlindingFunction();

  public Function getBlindingFunction(Element value);

  public Function getUnblindingFunction();

  public Function getUnblindingFunction(Element value);

}
