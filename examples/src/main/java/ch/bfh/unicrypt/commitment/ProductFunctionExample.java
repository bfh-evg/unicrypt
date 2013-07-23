package ch.bfh.unicrypt.commitment;

import java.math.BigInteger;

import ch.bfh.unicrypt.commitment.classes.PedersenCommitmentClass;
import ch.bfh.unicrypt.math.element.interfaces.AdditiveElement;
import ch.bfh.unicrypt.math.element.interfaces.Element;
import ch.bfh.unicrypt.math.element.interfaces.TupleElement;
import ch.bfh.unicrypt.math.function.classes.PowerFunctionClass;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import ch.bfh.unicrypt.math.function.interfaces.PowerFunction;
import ch.bfh.unicrypt.math.group.classes.GStarSaveClass;
import ch.bfh.unicrypt.math.group.interfaces.GStarSave;
import ch.bfh.unicrypt.math.group.interfaces.PowerGroup;
import ch.bfh.unicrypt.math.group.interfaces.ProductGroup;

public class ProductFunctionExample {

  public static void main(final String[] args) {
    final GStarSave g_q = new GStarSaveClass(BigInteger.valueOf(23));
    final PedersenCommitmentClass pcs = new PedersenCommitmentClass(g_q);

    final Function commitFunction = pcs.getCommitFunction();
    final PowerFunction tripleCommitFunction = new PowerFunctionClass(commitFunction, 3);

    final AdditiveElement m1 = pcs.getMessageSpace().createElement(BigInteger.valueOf(9));
    final AdditiveElement r1 = pcs.getRandomizationSpace().createElement(BigInteger.valueOf(7));
    final AdditiveElement m2 = pcs.getMessageSpace().createElement(BigInteger.valueOf(12));
    final AdditiveElement r2 = pcs.getRandomizationSpace().createElement(BigInteger.valueOf(6));
    final AdditiveElement m3 = pcs.getMessageSpace().createElement(BigInteger.valueOf(2));
    final AdditiveElement r3 = pcs.getRandomizationSpace().createElement(BigInteger.valueOf(2222));

    final ProductGroup commitFunctionDomain = (ProductGroup) (commitFunction.getDomain());
    final PowerGroup tripleCommitFunctionDomain = tripleCommitFunction.getDomain();
    final TupleElement tripleInput = tripleCommitFunctionDomain.createElement(commitFunctionDomain.createElement(m1, r1),
        commitFunctionDomain.createElement(m2, r2),
        commitFunctionDomain.createElement(m3, r3));

    final TupleElement tripleOutput = tripleCommitFunction.apply(tripleInput);
    System.out.println(tripleOutput);
    // -----

    final Function openingFunction = pcs.getOpenFunction();
    final ProductGroup openingFunctionDomain = (ProductGroup) openingFunction.getDomain();

    final PowerFunction tripleOpeningFunction = new PowerFunctionClass(openingFunction, 3);
    final ProductGroup tripleOpeningFunctionDomain = (tripleOpeningFunction.getDomain());

    final TupleElement tripleOpeningInput = tripleOpeningFunctionDomain.createElement(openingFunctionDomain.createElement(tripleInput.getElementAt(0, 0),
        tripleInput.getElementAt(0, 1),
        tripleOutput.getElementAt(0)), openingFunctionDomain.createElement(tripleInput.getElementAt(1, 0),
        tripleInput.getElementAt(1, 1),
        tripleOutput.getElementAt(1)), openingFunctionDomain.createElement(tripleInput.getElementAt(2, 0),
        tripleInput.getElementAt(2, 1),
        tripleOutput.getElementAt(2)));
    final Element element = tripleOpeningFunction.apply(tripleOpeningInput);
    System.out.println(element);

  }
}
