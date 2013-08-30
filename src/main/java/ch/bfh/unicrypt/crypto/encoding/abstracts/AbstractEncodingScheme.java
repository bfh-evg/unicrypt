package ch.bfh.unicrypt.crypto.encoding.abstracts;

import ch.bfh.unicrypt.crypto.encoding.interfaces.EncodingScheme;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

public abstract class AbstractEncodingScheme implements EncodingScheme {

  @Override
  public Element encode(final Element element) {
    return this.getEncodingFunction().apply(element);
  }

  @Override
  public Element decode(final Element element) {
    return this.getDecodingFunction().apply(element);
  }

  @Override
  public Group getMessageSpace() {
    return this.getEncodingFunction().getDomain();
  }

  @Override
  public Group getEncodingSpace() {
    return this.getEncodingFunction().getCoDomain();
  }

}
