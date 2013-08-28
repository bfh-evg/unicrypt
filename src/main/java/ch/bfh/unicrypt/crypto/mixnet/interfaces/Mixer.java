package ch.bfh.unicrypt.crypto.mixnet.interfaces;

import java.util.List;
import java.util.Random;

import ch.bfh.unicrypt.math.element.Element;
import ch.bfh.unicrypt.math.general.interfaces.PermutationElement;
import ch.bfh.unicrypt.math.general.interfaces.Group;
import ch.bfh.unicrypt.math.group.interfaces.PermutationGroup;

public interface Mixer {

  public List<Element> shuffle(List<Element> elements);

  public List<Element> shuffle(List<Element> elements, Random random);

  public List<Element> shuffle(List<Element> elements, PermutationElement permutation);

  public PermutationGroup getPermutationSpace(int size);

  public Group getGroup();

}
