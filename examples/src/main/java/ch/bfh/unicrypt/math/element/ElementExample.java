package ch.bfh.unicrypt.math.element;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.group.classes.ZPlusClass;

public class ElementExample {

  public static void main(final String[] args) {

    final AdditiveElement e0 = ZPlusClass.getInstance().createElement(BigInteger.valueOf(55));
    System.out.println(e0.getBigInteger());

    // TODO
    // CONCAT
    // final AdditiveElement e1 =
    // ZPlusClass.getInstance().createElement(BigInteger.valueOf(55),
    // BigInteger.valueOf(66));
    // System.out.println(e1.getBigInteger());

    final AdditiveElement e2 = ZPlusClass.getInstance().createElement(new BigInteger("ABC".getBytes()));
    System.out.println(new String(e2.getBigInteger().toByteArray()));

    // TODO
    // Concat
    // final AdditiveElement e3 = ZPlusClass.getInstance().createElement("ABC",
    // "DEF");
    // System.out.println(new String(e3.getBigInteger().toByteArray()));

    // TODO
    // Concat
    // final AdditiveElement e4 = ZPlusClass.getInstance().createElement(new
    // BigInteger("ABC".getBytes()), new BigInteger("DEF".getBytes()),
    // BigInteger.valueOf(55));
    // System.out.println(new String(e4.getBigInteger().toByteArray()));

    // final ProductGroup group = new PowerGroupClass(ZPlusClass.getInstance(),
    // 3);
    // final TupleElement e5 =
    // group.createElement(ZPlusClass.getInstance().createElement(new
    // BigInteger("ABC".getBytes())), ZPlusClass.getInstance().createElement(new
    // BigInteger("DEF".getBytes())),
    // ZPlusClass.getInstance().createElement(BigInteger.valueOf(55)));
    // TODO
    // Concat
    // System.out.println(e5.getString());
    // System.out.println(group.getString(e5)); // alternative call

    // final AdditiveElement e6 = ZPlusClass.getInstance().createElement(e5, e5,
    // e5);
    // System.out.println(new String(e6.getBigInteger().toByteArray()));

  }

}
