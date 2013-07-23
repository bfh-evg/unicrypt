package ch.bfh.unicrypt.commitment;

import java.math.BigInteger;

import ch.bfh.unicrypt.commitment.classes.PedersenCommitmentClass;
import ch.bfh.unicrypt.commitment.interfaces.PedersenCommitment;
import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.AtomicElement;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.classes.ZPlusModClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;
import ch.bfh.unicrypt.math.group.interfaces.ZPlusMod;

public class PedersenCommitmentExample {
  public static void main(final String[] args) {
    final GStarSave g_q = new GStarSaveClass(BigInteger.valueOf(23));
    final PedersenCommitment pcs = new PedersenCommitmentClass(g_q);

    final AdditiveElement message = pcs.getMessageSpace().createElement(9);
    final AdditiveElement randomization = pcs.getRandomizationSpace().createElement(7);
    final AtomicElement commitment = pcs.commit(message, randomization);
    System.out.println(commitment);
    {
      // true
      final boolean result = pcs.open(message, randomization, commitment);
      System.out.println(result);

    }
    {
      // true (testing for equivalent message-group)
      final ZPlusMod orderGroup = g_q.getOrderGroup();
      final AdditiveElement message_new = orderGroup.createElement(9);
      final boolean result = pcs.open(message_new, randomization, commitment);
      System.out.println(result);
    }
    {
      // false (testing for wrong message)
      final AdditiveElement wrongMessage = pcs.getMessageSpace().createElement(2);
      final boolean result = pcs.open(wrongMessage, randomization, commitment);
      System.out.println(result);
    }
    {
      // Illegal Argument (testing for wrong message-space)
      final ZPlusMod z_qnew = new ZPlusModClass(g_q.getOrder().add(BigInteger.ONE));
      final AdditiveElement message_new = z_qnew.createElement(9);
      final boolean result = pcs.open(message_new, randomization, commitment);
      System.out.println(result);

    }

  }
}
