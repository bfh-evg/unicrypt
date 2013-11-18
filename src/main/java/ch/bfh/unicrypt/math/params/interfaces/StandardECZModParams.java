/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.params.interfaces;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.algebra.dualistic.classes.BinaryPolynomialElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.BinaryPolynomialField;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public interface StandardECZModParams extends StandardECParams<ZModPrime, ZModElement> {
	

	
}
