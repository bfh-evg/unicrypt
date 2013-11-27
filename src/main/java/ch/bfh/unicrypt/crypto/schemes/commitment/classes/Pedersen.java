/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.commitment.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class Pedersen {

    public static void main(String[] args) {

	PedersenCommitmentScheme pcs = PedersenCommitmentScheme.getInstance(GStarModSafePrime.getInstance(23));

	Element message = pcs.getMessageSpace().getElement(2);
	Element randomization = pcs.getRandomizationSpace().getElement(1);
	Element commitment = pcs.commit(message, randomization);
	System.out.println(commitment);

	{
	    Element result = pcs.decommit(message, randomization, commitment);
	    System.out.println(result);
	}
	{
	    Element wrongMessage = pcs.getMessageSpace().getElement(7);
	    Element result = pcs.decommit(wrongMessage, randomization, commitment);
	    System.out.println(result);
	}

    }
}
