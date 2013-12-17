/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.random.interfaces;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.HashMethod;
import java.math.BigInteger;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public interface RandomOracle {

	public HashMethod getHashMethod();

	public RandomReferenceString getRandomReferenceString();

	public RandomReferenceString getRandomReferenceString(byte[] query);

	public RandomReferenceString getRandomReferenceString(int query);

	public RandomReferenceString getRandomReferenceString(BigInteger query);

	public RandomReferenceString getRandomReferenceString(Element query);

}
