package ch.bfh.unicrypt.encryption;

import ch.bfh.unicrypt.crypto.encoder.classes.ProbabilisticECGroupFpEncoder;
import ch.bfh.unicrypt.crypto.keygenerator.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.crypto.schemes.encryption.classes.ElGamalEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.additive.classes.ECZModPrimeElement;
import ch.bfh.unicrypt.math.algebra.additive.classes.StandardECZModPrime;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.FiniteField;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.params.classes.SECECCParamsFp;

public class ElGamalExample {

	public static void main(final String[] args) {

	//Example over GStarSave
	/* final GStarSave g_q = new GStarSaveClass(BigInteger.valueOf(23));
		 * final ElGamalEncryptionClass ecs = new ElGamalEncryptionClass(g_q); final DDHGroupKeyPairGenerator keyGen =
		 * ecs.getKeyGenerator();
		 *
		 * final AtomicElement message = ecs.getPlaintextSpace().createElement(BigInteger.valueOf(9));
		 *
		 * final TupleElement keyPair = keyGen.generateKeyPair(); final AtomicElement randomization =
		 * ecs.getRandomizationSpace().createElement(BigInteger.valueOf(7)); final TupleElement cipherText =
		 * ecs.encrypt(keyGen.getPublicKey(keyPair), message, randomization); System.out.println(keyPair);
		 * System.out.println(cipherText);
		 *
		 * final Element newMessage = ecs.decrypt(keyGen.getPrivateKey(keyPair), cipherText);
		 * System.out.println(newMessage);
		 *
		 * final AtomicElement reRandomization = ecs.getRandomizationSpace().createElement(BigInteger.valueOf(3)); final
		 * TupleElement reEncryptedCipherText = ecs.reEncrypt(keyGen.getPublicKey(keyPair), cipherText, reRandomization);
		 * final Element reEncMessage = ecs.decrypt(keyGen.getPrivateKey(keyPair), reEncryptedCipherText);
		 * System.out.println("ciphertext: " + cipherText); System.out.println("reEnc text: " + reEncryptedCipherText);
		 * System.out.println(reEncMessage);
		 */
		//Example Elgamal over ECFp
		final StandardECZModPrime g_q = StandardECZModPrime.getInstance(SECECCParamsFp.secp521r1); //Possible curves secp{112,160,192,224,256,384,521}r1
		final ElGamalEncryptionScheme<StandardECZModPrime, ECZModPrimeElement> ecs = ElGamalEncryptionScheme.getInstance(g_q);
		final KeyPairGenerator keyGen = ecs.getKeyPairGenerator();

		FiniteField f = g_q.getFiniteField();
		DualisticElement m = f.getElement(123456789);
		ProbabilisticECGroupFpEncoder enc = ProbabilisticECGroupFpEncoder.getInstance(g_q);

		ECZModPrimeElement message = enc.encode(m);

		//final Element message =g_q.getRandomElement();
		System.out.println("Message: " + m);
		System.out.println("Message encoded: " + message);

	  // Generate private key
		//KeyGenerator privateKeyGenerator =  keyGen..getPrivateKeyGenerator();
		final Element privateKey = keyGen.generatePrivateKey();
		System.out.println("Private Key: " + privateKey);

		//Generate pubilc key
		long time = System.currentTimeMillis();
		Element publigKey = keyGen.generatePublicKey(privateKey);
		time = System.currentTimeMillis() - time;
		System.out.println("Public Key: " + publigKey);
		System.out.println("Time for encryption: " + time + " ms");

		//Encrypt message
		time = System.currentTimeMillis();
		final Tuple cipherText = ecs.encrypt(publigKey, message);
		time = System.currentTimeMillis() - time;
		System.out.println("Cipher Text: " + cipherText);
		System.out.println("Time for decryption: " + time + " ms");

		//decrypt message
		Element newMessage = ecs.decrypt(privateKey, cipherText);
		System.out.println("New Message: " + newMessage);
		System.out.println("Message == New Message: " + message.isEquivalent(newMessage));
		ZModElement plain = enc.decode(newMessage);
		System.out.println("Message decoded: " + plain);

	}

}
