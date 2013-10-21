package ch.bfh.unicrypt.crypto.encoder.abstracts;

import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractEncoder<M extends Set, E extends Set, ME extends Element, EE extends Element> implements Encoder {

  private Function encodingFunction;
  private Function decodingFunction;

  protected AbstractEncoder(Function encodingFunction, Function decodingFunction) {
    this.encodingFunction = encodingFunction;
    this.decodingFunction = decodingFunction;
  }

  @Override
  public Function getEncodingFunction() {
    return this.encodingFunction;
  }

  @Override
  public Function getDecodingFunction() {
    return this.decodingFunction;
  }

  @Override
  public EE encode(final Element message) {
    return (EE) this.getEncodingFunction().apply(message);
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
