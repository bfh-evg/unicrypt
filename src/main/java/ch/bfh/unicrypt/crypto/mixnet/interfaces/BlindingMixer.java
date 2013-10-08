package ch.bfh.unicrypt.crypto.mixnet.interfaces;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.crypto.blinding.interfaces.BlindingScheme;
import ch.bfh.unicrypt.math.algebra.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationElement;

public interface BlindingMixer extends Mixer {

  public List<Element> shuffle(List<Element> elements, AdditiveElement blindingValue);

  public List<Element> shuffle(List<Element> elements, PermutationElement permutation, Random random);

  public List<Element> shuffle(List<Element> elements, AdditiveElement blindingValue, Random random);

  public List<Element> shuffle(List<Element> elements, PermutationElement permutation, AdditiveElement blindingValue);

  public BlindingScheme getBlindingScheme();

}
