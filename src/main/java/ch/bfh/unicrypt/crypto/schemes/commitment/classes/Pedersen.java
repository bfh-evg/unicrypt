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
	PedersenCommitmentScheme pcs = PedersenCommitmentScheme.getInstance(GStarModSafePrime.getInstance(167));

	Element element = pcs.commit(pcs.getCommitmentSpace().getElement(2), pcs.getCommitmentSpace().getElement(1));
	System.out.println(element);
    }
}
