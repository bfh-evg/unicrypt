/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.random.abstracts;

import ch.bfh.unicrypt.crypto.random.classes.PseudoRandomReferenceString;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomOracle;
import ch.bfh.unicrypt.crypto.random.interfaces.RandomReferenceString;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.helper.HashMethod;
import ch.bfh.unicrypt.math.helper.UniCrypt;
import java.math.BigInteger;

/**
 *
 * @author rolfhaenni
 */
public abstract class AbstractRandomOracle
	   extends UniCrypt
	   implements RandomOracle {

	private final HashMethod hashMethod;

	protected AbstractRandomOracle(HashMethod hashMethod) {
		this.hashMethod = hashMethod;
	}

	@Override
	public final HashMethod getHashMethod() {
		return this.hashMethod;
	}

	@Override
	public final RandomReferenceString getRandomReferenceString() {
		return this.getRandomReferenceString(PseudoRandomReferenceString.DEFAULT_SEED);
	}

	@Override
	public final RandomReferenceString getRandomReferenceString(int query) {
		return this.getRandomReferenceString(BigInteger.valueOf(query));
	}

	@Override
	public final RandomReferenceString getRandomReferenceString(BigInteger query) {
		if (query == null) {
			throw new IllegalArgumentException();
		}
		return this.getRandomReferenceString(query.toByteArray());
	}

	@Override
	public final RandomReferenceString getRandomReferenceString(Element query) {
		if (query == null) {
			throw new IllegalArgumentException();
		}
		return this.getRandomReferenceString(query.getHashValue(this.hashMethod).getByteArray());
	}

	@Override
	public final RandomReferenceString getRandomReferenceString(byte[] query) {
		if (query == null) {
			throw new IllegalArgumentException();
		}
		return this.abstractGetRandomReferenceString(query);
	}

	protected abstract RandomReferenceString abstractGetRandomReferenceString(byte[] query);

}
