package ch.bfh.unicrypt.crypto.mixnet.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.crypto.blinding.interfaces.BlindingScheme;
import ch.bfh.unicrypt.crypto.mixnet.interfaces.BlindingMixer;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.additive.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.general.interfaces.PermutationElement;

public class BlindingMixerClass extends MixerClass implements BlindingMixer {

  BlindingScheme blindingScheme;

  public BlindingMixerClass(final BlindingScheme blindingScheme) {
    super(blindingScheme.getBlindingSpace());
    this.blindingScheme = blindingScheme;
  }

  @Override
  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation) {
    return this.shuffle(elements, permutation, (Random) null);
  }

  @Override
  public List<Element> shuffle(final List<Element> elements, final AdditiveElement blindingFactor) {
    return this.shuffle(elements, blindingFactor, (Random) null);
  }

  @Override
  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation, final Random random) {
    final AdditiveElement blindingFactor = this.getBlindingScheme().getBlindingValueSpace().getRandomElement(random);
    return this.shuffle(elements, permutation, blindingFactor);
  }

  @Override
  public List<Element> shuffle(final List<Element> elements, final AdditiveElement blindingValue, final Random random) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    final PermutationElement permutation = this.getPermutationSpace(elements.size()).getRandomElement(random);
    return this.shuffle(elements, permutation, blindingValue);
  }

  @Override
  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation, final AdditiveElement blindingValue) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    final List<Element> blindedElements = new ArrayList<Element>();
    for (final Element element : elements) {
      blindedElements.add(this.getBlindingScheme().blind(element, blindingValue));
    }
    return super.shuffle(blindedElements, permutation);
  }

  @Override
  public BlindingScheme getBlindingScheme() {
    return this.blindingScheme;
  }

}
