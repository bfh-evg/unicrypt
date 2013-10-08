package ch.bfh.unicrypt.crypto.encoding.classes;

import java.math.BigInteger;
import java.util.Random;

import ch.bfh.unicrypt.crypto.encoding.abstracts.AbstractEncodingScheme;
import ch.bfh.unicrypt.math.algebra.additive.classes.ZPlusMod;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarMod;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public class DamgardEncodingScheme extends AbstractEncodingScheme {

  private final Function encodingFunction;
  private final Function decodingFunction;

  public DamgardEncodingScheme(final GStarMod zPrimSave) {
    if (zPrimSave == null) {
      throw new IllegalArgumentException();
    }
    final ZPlusMod orderGroup = zPrimSave.getZModOrder();
    this.encodingFunction = new EncodingFunction(orderGroup, zPrimSave);
    this.decodingFunction = new DecodingFunction(zPrimSave, orderGroup);
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
  public ZPlusMod getMessageSpace() {
    return (ZPlusMod) super.getMessageSpace();
  }

  @Override
  public GStarMod getEncodingSpace() {
    return (GStarMod) super.getEncodingSpace();
  }

  class EncodingFunction extends AbstractFunction {
    EncodingFunction(final ZPlusMod domain, final GStarMod coDomain) {
      super(domain, coDomain);
    }

    @Override
    protected Element abstractApply(final Element element, final Random random) {
      final BigInteger value = element.getValue().add(BigInteger.ONE);
      final GStarMod coDomain = (GStarMod) this.getCoDomain();
      if (coDomain.contains(value)) {
        return coDomain.getElement(value);
      }
      return coDomain.getElement(coDomain.getModulus().subtract(value));
    }
  }

  class DecodingFunction extends AbstractFunction {
    
    DecodingFunction(final GStarMod domain, final ZPlusMod coDomain) {
      super(domain, coDomain);
    }

    @Override
    protected Element abstractApply(final Element element, final Random random) {
      if (!this.getDomain().contains(element)) {
        throw new IllegalArgumentException();
      }
      final BigInteger value = element.getValue();
      final GStarMod domain = (GStarMod) this.getDomain();
      final ZPlusMod coDomain = (ZPlusMod) this.getCoDomain();
      if (value.compareTo(domain.getOrder()) <= 0) {
        return coDomain.getElement(value.subtract(BigInteger.ONE));
      }
      return coDomain.getElement((domain.getModulus().subtract(value)).subtract(BigInteger.ONE));
    }
  }

}
