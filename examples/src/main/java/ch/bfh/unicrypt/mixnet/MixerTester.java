package ch.bfh.unicrypt.mixnet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.unicrypt.blinding.classes.BlindingSchemeClass;
import ch.bfh.unicrypt.blinding.interfaces.BlindingScheme;
import ch.bfh.unicrypt.encryption.classes.ElGamalEncryptionClass;
import ch.bfh.unicrypt.encryption.interfaces.RandomizedAsymmetricHomomorphicEncryptionScheme;
import ch.bfh.unicrypt.keygen.interfaces.KeyPairGenerator;
import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.PermutationElement;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.interfaces.DDHGroup;
import ch.bfh.unicrypt.mixnet.classes.BlindingMixerClass;
import ch.bfh.unicrypt.mixnet.classes.ReEncryptionMixerClass;
import ch.bfh.unicrypt.mixnet.interfaces.BlindingMixer;
import ch.bfh.unicrypt.mixnet.interfaces.ReEncryptionMixer;

public class MixerTester {

  public static void main(final String[] args) {

    final DDHGroup ddhGroup = new GStarSaveClass(BigInteger.valueOf(23));
    final RandomizedAsymmetricHomomorphicEncryptionScheme elGamal = new ElGamalEncryptionClass(ddhGroup);

    final KeyPairGenerator keygen = elGamal.getKeyGenerator();
    final TupleElement keyPair = keygen.generateKeyPair();
    final Element privateKey = keygen.getPrivateKey(keyPair);
    final Element publicKey = keygen.getPublicKey(keyPair);

    final List<Element> encryptions = new ArrayList<Element>();
    Element message;
    for (int i = 1; i <= 10; i++) {
      message = elGamal.getPlaintextSpace().createRandomElement();
      System.out.println(message);
      encryptions.add(elGamal.encrypt(publicKey, message));
    }
    System.out.println(encryptions);

    final ReEncryptionMixer mixer = new ReEncryptionMixerClass(elGamal, publicKey);
    final PermutationElement permutation = mixer.getPermutationSpace(encryptions.size()).createRandomElement();
    System.out.println(permutation);

    final List<Element> mixedEncryptions = mixer.shuffle(encryptions, permutation);
    System.out.println(mixedEncryptions);

    Element plaintext;
    for (final Element encryption : mixedEncryptions) {
      plaintext = elGamal.decrypt(privateKey, encryption);
      System.out.println(plaintext);
    }

    final BlindingScheme blindingScheme = new BlindingSchemeClass(elGamal.getCiphertextSpace());
    final AdditiveElement blindingFactor = blindingScheme.getBlindingValueSpace().createElement(2);
    final BlindingMixer mixer2 = new BlindingMixerClass(blindingScheme);
    final List<Element> squaredEncryptions = mixer2.shuffle(mixedEncryptions, permutation.invert(), blindingFactor);
    System.out.println(squaredEncryptions);

    for (final Element encryption : squaredEncryptions) {
      plaintext = elGamal.decrypt(privateKey, encryption);
      System.out.println(plaintext);
    }
  }

}
