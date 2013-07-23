package ch.bfh.unicrypt.signature;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.signature.classes.SchnorrSignatureClass;
import ch.bfh.unicrypt.signature.interfaces.RandomizedSignatureScheme;

public class SchnorrSignatureClassExample {
  public static void main(String[] args) {

    final RandomizedSignatureScheme schnorr = new SchnorrSignatureClass(new GStarSaveClass(BigInteger.valueOf(23)));
    final TupleElement pair = schnorr.getKeyPairGenerator().generateKeyPair();
    final Element prk = schnorr.getKeyPairGenerator().getPrivateKey(pair);
    final Element puk = schnorr.getKeyPairGenerator().getPublicKey(pair);
    final Element message = schnorr.getMessageSpace().createElement(10);
    final Element rand = schnorr.getRandomizationSpace().createRandomElement();

    final Element signature = schnorr.sign(prk, message, rand);
    System.out.println(signature);
    System.out.println("Correct Signature: "+schnorr.verify(puk, message, signature));
    System.out.println("Wrong Signature: "+schnorr.verify(puk, schnorr.getMessageSpace().createRandomElement(), signature));
  }
}
