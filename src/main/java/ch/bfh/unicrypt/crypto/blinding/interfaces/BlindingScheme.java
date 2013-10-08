package ch.bfh.unicrypt.crypto.blinding.interfaces;

import java.util.Random;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface BlindingScheme {

  public Element blind(Element value);

  public Element blind(Element value, Random random);

  public Element blind(Element value, Element blindingValue);

  public Element unblind(Element value, Element blindingValue);

  public Group getBlindingSpace();

  public Group getBlindingValueSpace();

  public Function getBlindingFunction();

  public Function getBlindingFunction(Element value);

  public Function getUnblindingFunction();

  public Function getUnblindingFunction(Element value);

}
