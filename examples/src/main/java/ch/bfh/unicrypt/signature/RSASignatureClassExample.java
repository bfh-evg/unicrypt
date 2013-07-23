package ch.bfh.unicrypt.signature;

import java.math.BigInteger;

import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.classes.ZPlusClass;
import ch.bfh.unicrypt.math.group.classes.ZPlusModClass;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;
import ch.bfh.unicrypt.math.util.mapper.classes.CharsetXRadixYMapperClass;
import ch.bfh.unicrypt.signature.classes.RSASignatureClass;
import ch.bfh.unicrypt.signature.interfaces.SignatureScheme;

public class RSASignatureClassExample {
  public static void main(final String[] args) {
    // Beispiel und Zahlen aus 'Handbook of Applied Cryptography': 11.20 Example
    final ZPlusMod n = new ZPlusModClass(BigInteger.valueOf(55465219));
    final Element publicKey = n.createElement(BigInteger.valueOf(5));
    final Element privateKey = n.createElement(BigInteger.valueOf(44360237));
    {
      final SignatureScheme signatureScheme = new RSASignatureClass(n);
      {
        final Element message = ZPlusClass.getInstance().createElement(31229978);
        final Element signature = signatureScheme.sign(privateKey, message);
        System.out.println(signature);
        System.out.println(signatureScheme.verify(publicKey, message, signature));
      }

      {
        final Element message = ZPlusClass.getInstance().createElement(new BigInteger("Hello".getBytes()));

        final Element signature = signatureScheme.sign(privateKey, message);
        System.out.println(signature);

        System.out.println(signatureScheme.verify(publicKey, message, signature));

        System.out.println("Must be false:" + signatureScheme.verify(publicKey, message.selfApply(2), signature));
      }
    }
    {
      final SignatureScheme signatureScheme = new RSASignatureClass(n, RSASignatureClass.DEFAULT_HASH_ALGORITHM, new CharsetXRadixYMapperClass(CharsetXRadixYMapperClass.DEFAULT_CHARSET,36));
      {
        final Element message = ZPlusClass.getInstance().createElement(31229978);
        final Element signature = signatureScheme.sign(privateKey, message);
        System.out.println(signature);
        System.out.println(signatureScheme.verify(publicKey, message, signature));
      }

      {
        final Element message = ZPlusClass.getInstance().createElement(new BigInteger("Hello".getBytes()));

        final Element signature = signatureScheme.sign(privateKey, message);
        System.out.println(signature);

        System.out.println(signatureScheme.verify(publicKey, message, signature));

        System.out.println("Must be false:" + signatureScheme.verify(publicKey, message.selfApply(2), signature));
      }

    }

  }
}
