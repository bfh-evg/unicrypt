package ch.bfh.unicrypt.crypto.mixnet.interfaces;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.crypto.encryption.interfaces.HomomorphicEncryptionScheme;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;

public interface ReEncryptionMixer extends Mixer {

  public List<Element> shuffle(List<Element> elements, List<Element> randomizations);

  public List<Element> shuffle(List<Element> elements, PermutationElement permutation, Random random);

  public List<Element> shuffle(List<Element> elements, List<Element> randomizations, Random random);

  public List<Element> shuffle(List<Element> elements, PermutationElement permutation, List<Element> randomizations);

  public HomomorphicEncryptionScheme getEncryptionScheme();

  public Group getRandomizationSpace();

  public Element getPublicKey();

}
