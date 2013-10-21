package ch.bfh.unicrypt.crypto.encoder.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface Encoder {

  public Set getMessageSpace();

  public Set getEncodingSpace();

  public Function getEncodingFunction();

  public Function getDecodingFunction();

  public Element encode(Element message);

  public Element decode(Element element);

}
