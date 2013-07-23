package ch.bfh.unicrypt.math.group;

import java.math.BigInteger;
import java.util.ArrayList;

import ch.bfh.unicrypt.math.group.classes.GStarModClass;
import ch.bfh.unicrypt.math.group.classes.GStarPrimeClass;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.classes.ZStarModClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarMod;
import ch.bfh.unicrypt.math.group.interfaces.ZStarMod;

public class OrderExample {

  public static void main(final String[] args) {

    final ArrayList<ZStarMod> groups = new ArrayList<ZStarMod>();

    // BigInteger zero = BigInteger.ZERO;
    // BigInteger one = BigInteger.ONE;
    final BigInteger two = BigInteger.valueOf(2);
    final BigInteger three = BigInteger.valueOf(3);
    final BigInteger five = BigInteger.valueOf(5);
    final BigInteger ten = BigInteger.valueOf(10);

    groups.add(new ZStarModClass(ten));
    groups.add(new ZStarModClass(three, five));
    groups.add(new ZStarModClass(new BigInteger[] { three, five }, new int[] { 2, 1 }));

    groups.add(new GStarModClass(two));
    groups.add(new GStarModClass(two, 1));
    groups.add(new GStarModClass(two, 1, false));
    groups.add(new GStarModClass(two, 1, true));
    groups.add(new GStarModClass(two, 1, true, two));
    groups.add(new GStarModClass(three, 2, false));
    groups.add(new GStarModClass(three, 2, false, two));
    groups.add(new GStarModClass(three, 2, false, three));
    groups.add(new GStarModClass(three, 2, false, two, three));
    groups.add(new GStarModClass(three, 2, true));
    groups.add(new GStarModClass(three, 2, true, two));
    groups.add(new GStarModClass(three, 2, true, three));
    groups.add(new GStarModClass(three, 2, true, two, three));
    groups.add(new GStarModClass(three, 3, true));
    groups.add(new GStarModClass(three, 3, true, two));
    groups.add(new GStarModClass(three, 3, true, three));
    groups.add(new GStarModClass(three, 3, true, two, three));
    groups.add(new GStarModClass(three, 4, true, two, three));
    groups.add(new GStarModClass(five, 4, false));
    groups.add(new GStarModClass(five, 4, false, two));
    groups.add(new GStarModClass(five, 4, false, five));
    groups.add(new GStarModClass(five, 4, false, two, five));

    groups.add(new GStarPrimeClass(two));
    groups.add(new GStarPrimeClass(three));
    groups.add(new GStarPrimeClass(three, two));
    groups.add(new GStarPrimeClass(five));
    groups.add(new GStarPrimeClass(five, two));
    groups.add(new GStarPrimeClass(BigInteger.valueOf(7)));
    groups.add(new GStarPrimeClass(BigInteger.valueOf(7), two));
    groups.add(new GStarPrimeClass(BigInteger.valueOf(7), three));
    groups.add(new GStarPrimeClass(BigInteger.valueOf(7), two, three));
    groups.add(new GStarPrimeClass(BigInteger.valueOf(11)));
    groups.add(new GStarPrimeClass(BigInteger.valueOf(13)));

    groups.add(new GStarSaveClass(BigInteger.valueOf(5)));
    groups.add(new GStarSaveClass(BigInteger.valueOf(7)));
    groups.add(new GStarSaveClass(BigInteger.valueOf(11)));
    groups.add(new GStarSaveClass(BigInteger.valueOf(23)));
    groups.add(new GStarSaveClass(BigInteger.valueOf(23), true));
    groups.add(new GStarSaveClass(BigInteger.valueOf(23), false));

    for (final ZStarMod group : groups) {
      if (group == null) {
        break;
      }
      System.out.print("Mod=" + group.getModulus() + " Order=" + group.getOrder());
      if (group instanceof GStarMod) {
        System.out.print(" GroupOrder=" + ((GStarModClass) group).getGroupOrder());
        System.out.print(" Generator=" + ((GStarModClass) group).getDefaultGenerator().getBigInteger());
      }
      System.out.println();
    }

  }

}
