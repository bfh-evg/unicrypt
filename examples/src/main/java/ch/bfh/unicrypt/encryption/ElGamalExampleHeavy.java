package ch.bfh.unicrypt.encryption;

import java.math.BigInteger;

import ch.bfh.unicrypt.encryption.classes.ElGamalEncryptionClass;
import ch.bfh.unicrypt.keygen.interfaces.DDHGroupKeyPairGenerator;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;

public class ElGamalExampleHeavy {
  public static void main(final String[] args) {
    final GStarSave g_q = new GStarSaveClass(BigInteger.valueOf(23));
    final ElGamalEncryptionClass ecs = new ElGamalEncryptionClass(g_q);
    final DDHGroupKeyPairGenerator keyGen = ecs.getKeyGenerator();

    final AtomicElement message = ecs.getPlaintextSpace().createElement(BigInteger.valueOf(9));

    final TupleElement keyPair = keyGen.generateKeyPair();
    final AtomicElement randomization = ecs.getRandomizationSpace().createElement(BigInteger.valueOf(7));
    final TupleElement cipherText = ecs.encrypt(keyGen.getPublicKey(keyPair), message, randomization);
    System.out.println(keyPair);
    System.out.println(cipherText);

    final Element newMessage = ecs.decrypt(keyGen.getPrivateKey(keyPair), cipherText);
    System.out.println(newMessage);

    final AtomicElement reRandomization = ecs.getRandomizationSpace().createElement(BigInteger.valueOf(3));
    final TupleElement reEncryptedCipherText = ecs.reEncrypt(keyGen.getPublicKey(keyPair), cipherText, reRandomization);
    final Element reEncMessage = ecs.decrypt(keyGen.getPrivateKey(keyPair), reEncryptedCipherText);
    System.out.println("ciphertext: " + cipherText);
    System.out.println("reEnc text: " + reEncryptedCipherText);
    System.out.println(reEncMessage);

  }
}
