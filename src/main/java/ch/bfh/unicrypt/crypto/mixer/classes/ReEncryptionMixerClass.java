package ch.bfh.unicrypt.crypto.mixer.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.crypto.schemes.encryption.interfaces.ReEncryptionScheme;
import ch.bfh.unicrypt.crypto.mixer.interfaces.ReEncryptionMixer;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

public class ReEncryptionMixerClass extends MixerClass implements ReEncryptionMixer {

  ReEncryptionScheme encryptionScheme;
  Element publicKey;

  public ReEncryptionMixerClass(final ReEncryptionScheme encryptionScheme, final Element publicKey) {
    super(encryptionScheme.getCiphertextSpace());
    this.encryptionScheme = encryptionScheme;
    this.publicKey = publicKey;
  }

  @Override
  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation) {
    return this.shuffle(elements, permutation, (Random) null);
  }

  @Override
  public List<Element> shuffle(final List<Element> elements, final List<Element> randomizations) {
    return this.shuffle(elements, randomizations, (Random) null);
  }

  @Override
  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation, final Random random) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    final List<Element> randomizations = new ArrayList<Element>();
    for (int i = 1; i <= elements.size(); i++) {
      randomizations.add(this.getRandomizationSpace().getRandomElement(random));
    }
    return this.shuffle(elements, permutation, randomizations);
  }

  @Override
  public List<Element> shuffle(final List<Element> elements, final List<Element> randomizations, final Random random) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    final PermutationElement permutation = this.getPermutationSpace(elements.size()).getRandomElement(random);
    return this.shuffle(elements, permutation, randomizations);
  }

  @Override
  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation, final List<Element> randomizations) {
    if ((elements == null) || (randomizations == null) || (elements.size() != randomizations.size())) {
      throw new IllegalArgumentException();
    }
    final List<Element> reEncryptions = new ArrayList<Element>();
    Element randomization;
    final int i = 0;
    for (final Element encryption : elements) {
      randomization = randomizations.get(i);
      reEncryptions.add(this.getEncryptionScheme().reEncrypt(this.getPublicKey(), encryption, randomization));
    }
    return super.shuffle(reEncryptions, permutation);
  }

  @Override
  public ReEncryptionScheme getEncryptionScheme() {
    return this.encryptionScheme;
  }

  @Override
  public Group getRandomizationSpace() {
    return this.getEncryptionScheme().getRandomizationSpace();
  }

  @Override
  public Element getPublicKey() {
    return this.publicKey;
  }

}
