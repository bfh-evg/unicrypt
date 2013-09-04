package ch.bfh.unicrypt.crypto.mixnet.classes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.crypto.mixnet.interfaces.Mixer;
import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.PermutationElement;
import ch.bfh.unicrypt.math.algebra.general.classes.Tuple;
import ch.bfh.unicrypt.math.function.classes.PermutationFunction;
import ch.bfh.unicrypt.math.function.interfaces.PermutationFunction;
import ch.bfh.unicrypt.math.algebra.general.classes.PermutationGroup;
import ch.bfh.unicrypt.math.group.classes.PowerGroup;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.PermutationGroup;
import ch.bfh.unicrypt.math.group.interfaces.PowerGroup;

public class MixerClass implements Mixer {

  private final Group group;

  public MixerClass(final Group group) {
    if (group == null) {
      throw new IllegalArgumentException();
    }
    this.group = group;
  }

  @Override
  public List<Element> shuffle(final List<Element> elements) {
    return this.shuffle(elements, (Random) null);
  }

  @Override
  public List<Element> shuffle(final List<Element> elements, final Random random) {
    if (elements == null) {
      throw new IllegalArgumentException();
    }
    final PermutationElement permutation = this.getPermutationSpace(elements.size()).getRandomElement(random);
    return this.shuffle(elements, permutation);
  }

  @Override
  public List<Element> shuffle(final List<Element> elements, final PermutationElement permutation) {
    if ((elements == null) || (permutation == null)) {
      throw new IllegalArgumentException();
    }
    final int size = elements.size();
    final PermutationFunction function = new PermutationFunction(new PowerGroup(this.getGroup(), size));
    final PowerGroup powerGroup = (PowerGroup) function.getDomain().getGroupAt(0);
    final Tuple tuple = powerGroup.getElement(elements.toArray(new Element[size]));
    final Tuple result = function.apply(tuple, permutation);
    return Arrays.asList(result.getElements());
  }

  @Override
  public PermutationGroup getPermutationSpace(final int size) {
    return new PermutationGroup(size);
  }

  @Override
  public Group getGroup() {
    return this.group;
  }

}
