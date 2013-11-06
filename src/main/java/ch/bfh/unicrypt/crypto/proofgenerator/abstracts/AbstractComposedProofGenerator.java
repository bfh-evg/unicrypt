/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.proofgenerator.abstracts;

import ch.bfh.unicrypt.crypto.proofgenerator.interfaces.AndProofGenerator;
import ch.bfh.unicrypt.math.algebra.general.classes.ProductSet;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.classes.ProductFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 * @param <E>
 * @param <PR>
 * @param <PU>
 * @param <PS>
 * @param <F>
 */
public abstract class AbstractComposedProofGenerator<E extends Element, PR extends Set, PU extends ProductSet, PS extends Set, F extends ProductFunction> extends AbstractProofGenerator<E, PR, PU, PS, F> implements ComposedProofGenerator {

  public AbstractComposedProofGenerator(F proofFunction, Set otherInputSpace, PS proofSpace) {
    super(proofFunction, otherInputSpace, proofSpace);
  }

  @Override
  public Function[] getProofFunctions() {
    return this.getProofFunction().getAll();
  }

}
