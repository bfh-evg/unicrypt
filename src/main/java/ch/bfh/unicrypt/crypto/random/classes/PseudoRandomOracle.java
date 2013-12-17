/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.random.classes;

import ch.bfh.unicrypt.crypto.random.abstracts.AbstractRandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.helper.HashMethod;

/**
 *
 * @author rolfhaenni
 */
public class PseudoRandomOracle
			 extends AbstractRandomOracle {

	public static final PseudoRandomOracle DEFAULT = PseudoRandomOracle.getInstance();

	private final String algorithmName;

	protected PseudoRandomOracle(String algorithmName, HashMethod hashMethod) {
		super(hashMethod);
		this.algorithmName = algorithmName;
	}

	public String getAlgorithmName() {
		return this.algorithmName;
	}

	@Override
	protected RandomReferenceString abstractGetRandomReferenceString(byte[] query) {
		return PseudoRandomReferenceString.getInstance(this.algorithmName, query);
	}

	public static PseudoRandomOracle getInstance() {
		return PseudoRandomOracle.getInstance(PseudoRandomGenerator.DEFAULT_ALGORITHM_NAME, HashMethod.DEFAULT);
	}

	public static PseudoRandomOracle getInstance(String algorithmName) {
		return PseudoRandomOracle.getInstance(algorithmName, HashMethod.DEFAULT);
	}

	public static PseudoRandomOracle getInstance(HashMethod hashMethod) {
		return PseudoRandomOracle.getInstance(PseudoRandomGenerator.DEFAULT_ALGORITHM_NAME, HashMethod.DEFAULT);
	}

	public static PseudoRandomOracle getInstance(String algorithmName, HashMethod hashMethod) {
		if (algorithmName == null || hashMethod == null) {
			throw new IllegalArgumentException();
		}
		return new PseudoRandomOracle(algorithmName, hashMethod);
	}

}
