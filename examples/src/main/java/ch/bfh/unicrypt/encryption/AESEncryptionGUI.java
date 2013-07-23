package ch.bfh.unicrypt.encryption;

import ch.bfh.unicrypt.concat.classes.ConcatSchemeClass;
import ch.bfh.unicrypt.concat.interfaces.ConcatScheme;
import java.math.BigInteger;

import ch.bfh.unicrypt.encryption.classes.AESEncryptionClass;
import ch.bfh.unicrypt.hash.classes.HashSchemeClass;
import ch.bfh.unicrypt.hash.interfaces.HashScheme;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.function.classes.ConcatenateFunctionClass;
import ch.bfh.unicrypt.math.function.classes.HashFunctionClass;
import ch.bfh.unicrypt.math.java2unicrypt.classes.ExternalDataMapperClass;
import ch.bfh.unicrypt.math.util.RandomUtil;
import ch.bfh.unicrypt.math.util.mapper.classes.CharsetXRadixYMapperClass;
import java.util.Random;
import java.util.Scanner;

public class AESEncryptionGUI {

	public static final int MINIMUM_PASSPHRASE_LENGTH = 4;
	public static final int SALT_BITS = 255;
	public static final int RADIX = 10;
	private Random random;
	private String saltString;

	public AESEncryptionGUI() {
	}
	//For testing only

	public AESEncryptionGUI(Random random) {
		this.random = random;
	}

	private String createSaltAsString() {
		BigInteger saltValue = RandomUtil.createRandomBigInteger(SALT_BITS, random);
		return saltValue.toString(RADIX);
	}

	private String getHumanPassphraseViaConsole() {
		String humanPassphrase = null;
		while (humanPassphrase == null || humanPassphrase.length() < MINIMUM_PASSPHRASE_LENGTH) {
			System.out.println("Bitte Passwort eingeben: ");
			Scanner s = new Scanner(System.in);
			humanPassphrase = s.nextLine();
		}
		return humanPassphrase;
	}

	private String calculateKey(String humanPassphrase, String saltString) {
		AESEncryptionClass aes = new AESEncryptionClass();
		byte[] salt = new BigInteger(saltString).toByteArray();
		Element key = aes.getKeyGenerator().generateKey(humanPassphrase, salt);
		return ((AtomicElement) key).getBigInteger().toString(RADIX);
	}

	private String calculateHash(String keyString, String saltString) {
		ExternalDataMapperClass mapper = new ExternalDataMapperClass();
		String keySalt = keyString + saltString;
		Element keySaltElement = mapper.getEncodedElement(keySalt);

		//Create Hash
		ConcatScheme concat = new ConcatSchemeClass(ConcatenateFunctionClass.ConcatParameter.Plain,
				new CharsetXRadixYMapperClass());
		HashScheme sha256 = new HashSchemeClass(HashFunctionClass.SHA256, concat);
		AtomicElement hashResult = sha256.hash(keySaltElement);
		String hashString = hashResult.getBigInteger().toString(RADIX);
		return hashString;
	}

	public String[] createDBEntries() {
		System.out.println("Im Folgenden wird der 'Hash' von (<AES-Keys><Salt>), sowie das verwendete <Salt> ausgegeben.");

		Thread saltThread = new Thread() {
			@Override
			public void run() {
				saltString = createSaltAsString();
			}
		};
		saltThread.start();
		String humanPassphrase = getHumanPassphraseViaConsole();
		try {
			saltThread.join();
		} catch (InterruptedException ex) {
			//ok
		}
		String keyString = calculateKey(humanPassphrase, saltString);
		String hashString = calculateHash(keyString, saltString);
		return new String[]{hashString, saltString};
	}

	public boolean testViaConsole(String hashString, String saltString) {
		System.out.println("Zur Verifizierung...");
		String humanPassphrase = getHumanPassphraseViaConsole();
		String keyString = calculateKey(humanPassphrase, saltString);
		return hashString.equals(calculateHash(keyString, saltString));
	}

	public static void main(String[] args) {
		AESEncryptionGUI aes = new AESEncryptionGUI();
		String[] hashAndSalt = null;
		while (hashAndSalt == null) {
			hashAndSalt = aes.createDBEntries();
			if (!aes.testViaConsole(hashAndSalt[0], hashAndSalt[1])) {
				hashAndSalt = null;
				System.out.println("Es ergab nicht den gleichen Hash.");
			}
		}
		System.out.println("Hash: " + hashAndSalt[0]);
		System.out.println("Salt: " + hashAndSalt[1]);
	}
}
