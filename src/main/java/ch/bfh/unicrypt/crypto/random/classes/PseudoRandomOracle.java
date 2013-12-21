/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.crypto.random.abstracts.AbstractRandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.HashMethod;

/**
 *
 * @author rolfhaenni
 */
public class PseudoRandomOracle
	   extends AbstractRandomOracle {

	public static final PseudoRandomOracle DEFAULT = PseudoRandomOracle.getInstance();

	protected PseudoRandomOracle(HashMethod hashMethod) {
		super(hashMethod);

	}

	@Override
	protected RandomReferenceString abstractGetRandomReferenceString(Element query) {
		return PseudoRandomReferenceString.getInstance(query);
	}

	public static PseudoRandomOracle getInstance() {
		return PseudoRandomOracle.getInstance(HashMethod.DEFAULT);
	}

	public static PseudoRandomOracle getInstance(HashMethod hashMethod) {
		if (hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new PseudoRandomOracle(hashMethod);
	}

}
