package ch.bfh.unicrypt.crypto.encoder.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;


public class GStarModEncoder extends AbstractEncoder<ZMod, GStarModSafePrime, ZModElement, GStarModElement> {

  protected GStarModEncoder(Function encodingFunction, Function decodingFunction) {
    super(encodingFunction, decodingFunction);
  }

  public static GStarModEncoder getInstance(final GStarModSafePrime gStar) {
    if (gStar == null) {
      throw new IllegalArgumentException();
    }
    ZMod orderGroup = gStar.getZModOrder();
    return new GStarModEncoder(new EncodingFunction(orderGroup, gStar), new DecodingFunction(gStar, orderGroup));
  }

}
class EncodingFunction extends AbstractFunction<ZMod, GStarModSafePrime, GStarModElement> {

  protected EncodingFunction(final ZMod domain, final GStarMod coDomain) {
    super(domain, coDomain);
  }

  @Override
  protected GStarModElement abstractApply(final Element element, final Random random) {
    final BigInteger value = element.getValue().add(BigInteger.ONE);
    final GStarMod coDomain = this.getCoDomain();
    if (coDomain.contains(value)) {
      return coDomain.getElement(value);
    }
    return coDomain.getElement(coDomain.getModulus().subtract(value));
  }

}

class DecodingFunction extends AbstractFunction<GStarModSafePrime, ZMod, ZModElement> {

  protected DecodingFunction(final GStarMod domain, final ZMod coDomain) {
    super(domain, coDomain);
  }

  @Override
  protected ZModElement abstractApply(final Element element, final Random random) {
    final BigInteger value = element.getValue();
    final GStarMod domain = this.getDomain();
    if (value.compareTo(domain.getOrder()) <= 0) {
      return this.getCoDomain().getElement(value.subtract(BigInteger.ONE));
    }
    return this.getCoDomain().getElement((domain.getModulus().subtract(value)).subtract(BigInteger.ONE));
  }

}
