package ch.bfh.unicrypt.math.function;

import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.PermutationElement;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.function.classes.PermutationFunctionClass;
import ch.bfh.unicrypt.math.function.interfaces.PermutationFunction;
import ch.bfh.unicrypt.math.group.classes.PowerGroupClass;
import ch.bfh.unicrypt.math.group.classes.ZPlusClass;
import ch.bfh.unicrypt.math.group.interfaces.PowerGroup;
import ch.bfh.unicrypt.math.group.interfaces.ZPlus;

public class PermutationExample {

  public static void main(final String[] args) {

    final ZPlus zPlus = ZPlusClass.getInstance();
    PowerGroup zPlusPower = new PowerGroupClass(zPlus, 6);

    PermutationFunction permutationFunction = new PermutationFunctionClass(zPlusPower);

    final AdditiveElement e0 = zPlus.createElement(0);
    final AdditiveElement e1 = zPlus.createElement(1);
    final AdditiveElement e2 = zPlus.createElement(2);
    final AdditiveElement e3 = zPlus.createElement(3);
    final AdditiveElement e4 = zPlus.createElement(4);
    final AdditiveElement e5 = zPlus.createElement(5);

    TupleElement tuple = zPlusPower.createElement(e0, e1, e2, e3, e4, e5);
    PermutationElement permutation = permutationFunction.getPermutationGroup().createRandomElement();
    TupleElement result = permutationFunction.apply(tuple, permutation);
    System.out.println(permutation);
    System.out.println(tuple);
    System.out.println(result);

    zPlusPower = new PowerGroupClass(zPlus, 0);
    permutationFunction = new PermutationFunctionClass(zPlusPower);
    tuple = zPlusPower.createElement();
    permutation = permutationFunction.getPermutationGroup().createRandomElement();
    result = permutationFunction.apply(tuple, permutation);
    System.out.println(permutation);
    System.out.println(tuple);
    System.out.println(result);
  }

}
