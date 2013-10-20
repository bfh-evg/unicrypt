package ch.bfh.unicrypt.crypto.encoding.abstracts;

import ch.bfh.unicrypt.crypto.encoding.interfaces.EncodingScheme;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractEncodingScheme<M extends Set, E extends Set, ME extends Element, EE extends Element> implements EncodingScheme {

  private Function encodingFunction;
  private Function decodingFunction;

  @Override
  public Function getEncodingFunction() {
    return this.encodingFunction;
  }

  @Override
  public Function getDecodingFunction() {
    return this.decodingFunction;
  }

  @Override
  public EE encode(final Element element) {
    return (EE) this.getEncodingFunction().apply(element);
  }

  @Override
  public ME decode(final Element element) {
    return (ME) this.getDecodingFunction().apply(element);
  }

  @Override
  public M getMessageSpace() {
    return (M) this.getEncodingFunction().getDomain();
  }

  @Override
  public E getEncodingSpace() {
    return (E) this.getEncodingFunction().getCoDomain();
  }

}
