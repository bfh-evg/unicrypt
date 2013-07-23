package ch.bfh.unicrypt.commitment;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import ch.bfh.unicrypt.commitment.classes.ExtendedPedersenCommitmentClass;
import ch.bfh.unicrypt.commitment.interfaces.ExtendedPedersenCommitment;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;

public class ExtendedPedersenCommitmentExample {
  public static void main(final String[] args) {
    final GStarSave g_q = new GStarSaveClass(BigInteger.valueOf(23));
    final ExtendedPedersenCommitment pcs = new ExtendedPedersenCommitmentClass(g_q, 3);

    final Element message1 = pcs.getMessageSpace().createElement(9);
    final Element message2 = pcs.getMessageSpace().createElement(2);
    final Element message3 = pcs.getMessageSpace().createElement(3);
    final Element randomization = pcs.getRandomizationSpace().createElement(7);
    final List<Element> messages = new ArrayList<Element>();
    messages.add(message1);
    messages.add(message2);
    messages.add(message3);
    final AtomicElement commitment = pcs.commit(messages, randomization);
    System.out.println(commitment);
    {
      // true
      final boolean result = pcs.open(messages, randomization, commitment);
      System.out.println(result);

    }
    {
      // false (testing for wrong message)
      final List<Element> wrongMessages = new ArrayList<Element>();
      wrongMessages.add(message1);
      wrongMessages.add(message2);
      wrongMessages.add(message2);
      final boolean result = pcs.open(wrongMessages, randomization, commitment);
      System.out.println(result);
    }

  }
}
