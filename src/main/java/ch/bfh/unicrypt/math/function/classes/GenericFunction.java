/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.crypto.random.interfaces.RandomGenerator;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author rolfhaenni
 * @param <D>
 * @param <C>
 * @param <CE>
 */
public class GenericFunction<D extends Set, DE extends Element, C extends Set, CE extends Element>
			 extends AbstractFunction<D, DE, C, CE> {

	Function function;

	protected GenericFunction(Function function) {
		super(function.getDomain(), function.getCoDomain());
		this.function = function;
	}

	public Function getFunction() {
		return this.getFunction();
	}

	@Override
	protected boolean standardIsEquivalent(Function function) {
		return this.getFunction().isEquivalent(((GenericFunction) function).getFunction());
	}

	@Override
	protected CE abstractApply(DE element, RandomGenerator randomGenerator) {
		return (CE) this.function.apply(element, randomGenerator);
	}

	public static <D extends Set, DE extends Element, C extends Set, CE extends Element> GenericFunction<D, DE, C, CE> getInstance(Function function) {
		if (function == null) {
			throw new IllegalArgumentException();
		}
		return new GenericFunction<D, DE, C, CE>(function);
	}

}
