package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.IdentityFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class IdentityEncoder
       extends AbstractEncoder<Set, Set, Element, Element> {

  protected IdentityEncoder(Function encodingFunction, Function decodingFunction) {
    super(encodingFunction, decodingFunction);
  }

  public static IdentityEncoder getInstance(Set domain) {
    return new IdentityEncoder(IdentityFunction.getInstance(domain), IdentityFunction.getInstance(domain));
  }

}
