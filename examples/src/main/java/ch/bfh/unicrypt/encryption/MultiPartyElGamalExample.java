package ch.bfh.unicrypt.encryption;

import java.math.BigInteger;

import ch.bfh.unicrypt.encryption.classes.ElGamalEncryptionClass;
import ch.bfh.unicrypt.keygen.interfaces.DDHGroupDistributedKeyPairGenerator;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;

public class MultiPartyElGamalExample {
  public static void main(final String[] args) {
    final GStarSave g_q = new GStarSaveClass(BigInteger.valueOf(23));
    final ElGamalEncryptionClass ecs = new ElGamalEncryptionClass(g_q);
    final DDHGroupDistributedKeyPairGenerator keyGen = ecs.getKeyGenerator();

    final AtomicElement message = ecs.getPlaintextSpace().createElement(BigInteger.valueOf(9));

    final TupleElement keyPair1 = keyGen.generateKeyPair();
    System.out.println(keyPair1);
    final TupleElement keyPair2 = keyGen.generateKeyPair();
    System.out.println(keyPair2);
    final TupleElement keyPair3 = keyGen.generateKeyPair();
    System.out.println(keyPair3);
    final AtomicElement publicKey = keyGen.combinePublicKeys(keyGen.getPublicKey(keyPair1), keyGen.getPublicKey(keyPair2), keyGen.getPublicKey(keyPair3));

    final AtomicElement randomization = ecs.getRandomizationSpace().createElement(BigInteger.valueOf(7));
    final TupleElement cipherText = ecs.encrypt(publicKey, message, randomization);

    final Element partialMessage1 = ecs.partialDecrypt(keyGen.getPrivateKey(keyPair1), cipherText);
    final Element partialMessage2 = ecs.partialDecrypt(keyGen.getPrivateKey(keyPair2), cipherText);
    final Element partialMessage3 = ecs.partialDecrypt(keyGen.getPrivateKey(keyPair3), cipherText);

    final Element newMessage = ecs.combinePartialDecryptions(cipherText, partialMessage1, partialMessage2, partialMessage3);
    System.out.println(message);
    System.out.println(newMessage);

  }
}
