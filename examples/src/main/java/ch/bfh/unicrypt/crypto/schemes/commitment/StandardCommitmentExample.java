/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.commitment;

import ch.bfh.unicrypt.crypto.schemes.commitment.classes.StandardCommitmentScheme;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.general.classes.BooleanElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import java.math.BigInteger;

/**
 *
 *
 */
public class StandardCommitmentExample {

    public static void exampleWithExpliciteGenerator() {
	// All calculations are done within that group
	GStarModSafePrime cyclicGroup = GStarModSafePrime.getInstance(BigInteger.valueOf(167));

	// The generator is explicitly given...
	Element generator = cyclicGroup.getElement(BigInteger.valueOf(98));

	// Test if it really is a ganerator
	if (!cyclicGroup.isGenerator(generator)) {
	    return;
	}

	// The commitmentScheme to be used
	StandardCommitmentScheme<GStarModSafePrime, Element> commitmentScheme = StandardCommitmentScheme.getInstance(generator);

	ZMod additiveGroup = cyclicGroup.getZModOrder();

	//The element to be commited
	Element message = additiveGroup.getElement(42);

	Element commitment = commitmentScheme.commit(message);

	System.out.println(commitment);

	BooleanElement result = commitmentScheme.decommit(message, commitment);
	System.out.println(result);
    }

    public static void main(String[] args) {
	exampleWithExpliciteGenerator();
    }
}
