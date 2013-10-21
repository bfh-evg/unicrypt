package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.CompositeFunction;
import ch.bfh.unicrypt.math.function.classes.ConvertFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class GeneralEncoder extends AbstractEncoder<Set, Set, Element, Element> {

  protected GeneralEncoder(Function encodingFunction, Function decodingFunction) {
    super(encodingFunction, decodingFunction);
  }

  public static GeneralEncoder getInstance(Set messageSpace) {
    return GeneralEncoder.getInstance(messageSpace, messageSpace);
  }

  public static GeneralEncoder getInstance(Set messageSpace, Set encodingSpace) {
    if (messageSpace == null || encodingSpace == null) {
      throw new IllegalArgumentException();
    }
    return new GeneralEncoder(ConvertFunction.getInstance(messageSpace, encodingSpace), ConvertFunction.getInstance(encodingSpace, messageSpace));
  }

}
