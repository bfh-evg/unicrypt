package ch.bfh.unicrypt.crypto.blinding.interfaces;

import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.group.interfaces.Group;

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
