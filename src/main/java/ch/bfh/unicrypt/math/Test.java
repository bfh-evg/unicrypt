/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math;

import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.group.classes.ZPlus;

/**
 *
 * @author rolfhaenni
 */
public class Test {

  public static void main(String[] args) {
    ZPlus zPlus = ZPlus.getInstance();
    AdditiveElement elt1 = zPlus.getElement(5);
    AdditiveElement elt2 = zPlus.getElement(7);
    System.out.println(elt1.add(elt2));
  }
}
