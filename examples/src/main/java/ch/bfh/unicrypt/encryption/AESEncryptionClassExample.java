package ch.bfh.unicrypt.encryption;

import java.math.BigInteger;

import ch.bfh.unicrypt.encryption.classes.AESEncryptionClass;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;

public class AESEncryptionClassExample {
  public static void main(String[] args) {
    AESEncryptionClass aes = new AESEncryptionClass();          
    String humanPassphrase = new String("123456");      
    byte[] salt = new byte[]{1,2,3,4,5};
    Element key = aes.getKeyGenerator().generateKey(humanPassphrase, salt);      
    Element plaintext = aes.getPlaintextSpace().createEncodedElement(new BigInteger("Hello World, this is an AES-Test".getBytes()));
    Element ciphertext = aes.encrypt(key, plaintext);
    Element decryptedText=aes.decrypt(key, ciphertext);
    System.out.println(decryptedText); 
    System.out.println(new String(((AtomicElement)decryptedText).getBigInteger().toByteArray()));
  }
  
}
