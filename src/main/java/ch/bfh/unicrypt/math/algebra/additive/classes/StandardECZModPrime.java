package ch.bfh.unicrypt.math.algebra.additive.classes;

import ch.bfh.unicrypt.math.params.interfaces.StandardECParams;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import java.math.BigInteger;

public class StandardECZModPrime
       extends ECZModPrime {

  private StandardECZModPrime(ZModPrime field, DualisticElement a,
         DualisticElement b, DualisticElement gx, DualisticElement gy,
         BigInteger order, BigInteger h) {
    super(field, a, b, gx, gy, order, h);
    // TODO Auto-generated constructor stub
  }

  public static StandardECZModPrime getInstance(final StandardECParams params) {
    ZModPrime field;
    DualisticElement a, b, gx, gy;
    BigInteger order, h;

    field = params.getFiniteField();
    a = params.getA();
    b = params.getB();
    gx = params.getGx();
    gy = params.getGy();
    order = params.getOrder();
    h = params.getH();

    return new StandardECZModPrime(field, a, b, gx, gy, order, h);
  }

  public static void main(String[] args) {
    String[] params = {"secp112r1", "secp160r1", "secp192k1", "secp192r1", "secp224k1", "secp224r1", "secp256k1", "secp256r1", "secp384r1", "secp521r1"};

    for (String string : params) {
      StandardECZModPrime ec = StandardECZModPrime.getInstance(string);
      System.out.println(string + "(\"" + ec.getP().toString(16) + "\",\"" + ec.getA().getValue().toString(16) + "\",\"" + ec.getB().getValue().toString(16) + "\",\"" + ec.getDefaultGenerator().getX().getValue().toString(16) + "\",\"" + ec.getDefaultGenerator().getY().getValue().toString(16) + "\",\"" + ec.getOrder().toString(16) + "\",\"" + ec.getH() + "\"),");
    }
  }

}
