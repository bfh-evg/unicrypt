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
import java.util.HashMap;

/**
 *
 * @author rolfhaenni
 */
public class PseudoRandomOracle
	   extends AbstractRandomOracle {

	HashMap<Element, PseudoRandomReferenceString> referenceStrings;

	public static final PseudoRandomOracle DEFAULT = PseudoRandomOracle.getInstance();

	protected PseudoRandomOracle(HashMethod hashMethod) {
		super(hashMethod);
		referenceStrings = new HashMap<Element, PseudoRandomReferenceString>();

	}

	//TODO: Warning, this is a memory-hog!
	//This code will only work when there is a fast 'equals' method ready for Element
	@Override
	protected RandomReferenceString abstractGetRandomReferenceString(Element query) {
		if (referenceStrings.containsKey(query) == false) {

			referenceStrings.put(query, PseudoRandomReferenceString.getInstance(query));
		}
		RandomReferenceString referenceString = referenceStrings.get(query);
		referenceString.reset();
		return referenceString;
	}
//	@Override
//	protected RandomReferenceString abstractGetRandomReferenceString(Element query) {
//		return PseudoRandomReferenceString.getInstance(query);
//	}

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
