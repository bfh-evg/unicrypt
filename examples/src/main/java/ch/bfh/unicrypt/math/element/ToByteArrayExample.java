package ch.bfh.unicrypt.math.element;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.classes.PowerGroupClass;
import ch.bfh.unicrypt.math.group.classes.ZPlusClass;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;

public class ToByteArrayExample {

  public static void main(final String[] args) {
    final ProductGroup pg = new PowerGroupClass(ZPlusClass.getInstance(), 2);
    final Element e1 = ZPlusClass.getInstance().createElement(new BigInteger(1, "Hello World".getBytes()));
    final Element e2 = pg.createElement(ZPlusClass.getInstance().createElement(new BigInteger(1, "Hello ".getBytes())),
        ZPlusClass.getInstance().createElement(new BigInteger(1, "World".getBytes())));
    System.out.println(e1.equals(e2));
  }

}
