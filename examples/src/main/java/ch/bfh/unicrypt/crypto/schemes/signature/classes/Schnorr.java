/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.signature.classes;

import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModElement;
import ch.bfh.unicrypt.math.algebra.multiplicative.classes.GStarModSafePrime;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.helper.Alphabet;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class Schnorr {

    public Schnorr() {
	GStarModSafePrime g_q = GStarModSafePrime.getInstance(23);
	GStarModElement g = g_q.getElement(4);
	Element randomization = g_q.getZModOrder().getElement(3);
	Element privateKey = g_q.getZModOrder().getElement(5);
	StringElement message = StringMonoid.getInstance(Alphabet.BASE64).getElement("MessageXX");
	SchnorrSignatureScheme<StringMonoid, StringElement> schnorr = new SchnorrSignatureScheme(message.getSet(), g_q, g);
	Function ssF = schnorr.getSignatureFunction();
	Element s = ssF.apply(privateKey, message, randomization);
	System.out.println("s: " + s);
    }

    public static void main(String[] args) {
	new Schnorr();
    }
}
