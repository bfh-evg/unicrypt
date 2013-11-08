package ch.bfh.unicrypt.encryption;

import java.math.BigInteger;

import sun.security.jca.GetInstance;

import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyGenerator;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECGroup;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECGroupElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.SafeECGroupFp;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModPrimes;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

public class ElGamalExample {
  public static void main(final String[] args) {
    
	//Example over GStarSave
	/*final GStarSave g_q = new GStarSaveClass(BigInteger.valueOf(23));
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
    */
	  
	  //Example Elgamal over ECFp
	  final ECGroup g_q=SafeECGroupFp.getInstance("secp384r1"); //Possible curves secp{112,160,192,224,256,384,521}r1
	  final ElGamalEncryptionScheme<ECGroup, ECGroupElement> ecs= ElGamalEncryptionScheme.getInstance(g_q);
	  final KeyPairGenerator keyGen= ecs.getKeyPairGenerator();
	  final Element message =g_q.getRandomElement();
	  System.out.println("Message: "+message);
	  
	  
	  // Generate private key
	  KeyGenerator privateKeyGenerator = keyGen.getPrivateKeyGenerator();
	  final Element privateKey=privateKeyGenerator.generateKey();
	  System.out.println("Private Key: "+privateKey);
	  
	  //Generate pubilc key
	  long time=System.currentTimeMillis();
	  Element publigKey=keyGen.getPublicKey(privateKey);
	  time=System.currentTimeMillis()-time;
	  System.out.println("Public Key: "+publigKey);
	  System.out.println("Time for encryption: "+time+" ms");
	  
	  //Encrypt message
	  time=System.currentTimeMillis();
	  final Tuple cipherText=ecs.encrypt(publigKey, message);
	  time=System.currentTimeMillis()-time;
	  System.out.println("Cipher Text: "+cipherText);
	  System.out.println("Time for decryption: "+time+" ms");
	  
	  //decrypt message
	  Element newMessage=ecs.decrypt(privateKey, cipherText);
	  System.out.println("New Message: "+newMessage);
	  System.out.println("Message == New Message: "+message.equals(newMessage));
	  
    

  }
}
