package ch.bfh.unicrypt.crypto.encoder.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public interface Encoder {

  public Set getDomain();

  public Set getCoDomain();

  public Function getEncodingFunction();

  public Function getDecodingFunction();

  public Element encode(Element element);

  public Element decode(Element element);

}
