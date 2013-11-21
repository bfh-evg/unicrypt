/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.params.interfaces;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <E>
 * @param <D>
 */
public interface StandardECParams<E extends FiniteField, D extends DualisticElement> {

	public abstract E getFiniteField();

	public abstract BigInteger getP();

	public abstract D getA();

	public abstract D getB();

	public abstract D getGx();

	public abstract D getGy();

	public abstract BigInteger getOrder();

	public abstract BigInteger getH();

}
