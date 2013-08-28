package ch.bfh.unicrypt.crypto.mixnet.interfaces;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.crypto.blinding.interfaces.BlindingScheme;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.general.interfaces.PermutationElement;

public interface BlindingMixer extends Mixer {

  public List<Element> shuffle(List<Element> elements, AdditiveElement blindingValue);

  public List<Element> shuffle(List<Element> elements, PermutationElement permutation, Random random);

  public List<Element> shuffle(List<Element> elements, AdditiveElement blindingValue, Random random);

  public List<Element> shuffle(List<Element> elements, PermutationElement permutation, AdditiveElement blindingValue);

  public BlindingScheme getBlindingScheme();

}
