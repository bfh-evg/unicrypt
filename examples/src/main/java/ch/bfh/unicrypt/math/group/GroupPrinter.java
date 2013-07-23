package ch.bfh.unicrypt.math.group;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GroupPrinter {

  public static void main(final String[] args) {

    Set<BigInteger> cyclic = new HashSet<BigInteger>();
    cyclic = new TreeSet<BigInteger>(cyclic);

    final boolean doubling = false;

    for (int i = 23; i <= 23; i++) {
      for (int j = 1; j <= 1; j++) {
        if (BigInteger.valueOf(i).isProbablePrime(20)) {
          BigInteger value = BigInteger.valueOf(i).pow(j);
          if (doubling) {
            value = value.multiply(BigInteger.valueOf(2));
          }
          cyclic.add(value);
        }
      }
    }
    System.out.println(cyclic);
    System.out.println();
    List<BigInteger> set;

    for (final BigInteger bi : cyclic) {
      set = new ArrayList<BigInteger>();
      for (int j = 1; j < bi.intValue(); j++) {
        final BigInteger bj = BigInteger.valueOf(j);
        if (bi.gcd(bj).equals(BigInteger.ONE)) {
          set.add(bj);
        }
      }
      final int order = set.size();
      System.out.println("n=" + bi + ", order=" + order);
      System.out.println(set);
      for (final BigInteger a : set) {
        BigInteger x = a;
        System.out.format("%2d: [ ", a);
        int k = 0;
        do {
          k++;
          System.out.format("%2d ", x);
          x = x.multiply(a).mod(bi);
        } while (!x.equals(a));
        System.out.println("] => " + k);
      }
      for (int j = 1; j <= order; j++) {
        if (BigInteger.valueOf(j).gcd(BigInteger.valueOf(order)).equals(BigInteger.valueOf(j))) {
          System.out.print("k=" + j + ": ");
          @SuppressWarnings("unused")
          Set<BigInteger> candSet = new HashSet<BigInteger>();
          for (final BigInteger alpha : set) {
            candSet.add(alpha.modPow(BigInteger.valueOf(j), bi));
          }
          candSet = new TreeSet<BigInteger>(candSet);
          System.out.println(candSet);
        }
      }
      System.out.println();
    }

  }

}
