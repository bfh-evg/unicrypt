package ch.bfh.unicrypt.encoding;

import java.math.BigInteger;

import ch.bfh.unicrypt.encoding.classes.DamgardEncodingClass;
import ch.bfh.unicrypt.encoding.interfaces.DamgardEncodingScheme;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;

public class DamgardExample {
  public static void main(final String[] args) {
    final GStarSave g_q = new GStarSaveClass(BigInteger.valueOf(23));
    final DamgardEncodingScheme des = new DamgardEncodingClass(g_q);

    for (int i = 0; i < des.getMessageSpace().getOrder().intValue(); i++) {
      final Element message = des.getMessageSpace().createElement(BigInteger.valueOf(i));
      System.out.print("original: " + message + " ");
      final Element encodedMessage = des.encode(message);
      System.out.print("encoded: " + encodedMessage + " ");
      final Element decodedMessage = des.decode(encodedMessage);
      System.out.println("decoded: " + decodedMessage);
    }
  }
}