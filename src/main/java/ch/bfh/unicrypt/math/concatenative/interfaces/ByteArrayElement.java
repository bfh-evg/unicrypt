/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.concatenative.interfaces;

import ch.bfh.unicrypt.math.general.interfaces.Element;

/**
 *
 * @author rolfhaenni
 */
public interface ByteArrayElement extends ConcatenativeElement {

  public byte[] getBytes();

}
