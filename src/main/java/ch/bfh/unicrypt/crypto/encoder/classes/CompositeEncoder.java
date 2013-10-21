package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class CompositeEncoder extends AbstractEncoder<Set, Set, Element, Element> {

  protected CompositeEncoder(Function encodingFunction, Function decodingFunction) {
    super(encodingFunction, decodingFunction);
  }

  public static CompositeEncoder getInstance(Encoder... encoders) {
    if (encoders == null || encoders.length == 0) {
      throw new IllegalArgumentException();
    }
    int length = encoders.length;
    Function[] encodingFunctions = new Function[length];
    Function[] decodingFunctions = new Function[length];
    for (int i = 0; i < length; i++) {
      if (encoders[i] == null) {
        throw new IllegalArgumentException();
      }
      encodingFunctions[i] = encoders[i].getEncodingFunction();
      decodingFunctions[length - i - 1] = encoders[i].getDecodingFunction();
    }
    return new CompositeEncoder(CompositeFunction.getInstance(encodingFunctions), CompositeFunction.getInstance(decodingFunctions));
  }

}
