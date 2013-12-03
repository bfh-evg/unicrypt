/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;
import java.util.Random;

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

	@Override
	protected CE abstractApply(DE element, Random random) {
		return (CE) this.function.apply(element, random);
	}

	public static <D extends Set, DE extends Element, C extends Set, CE extends Element> GenericFunction<D, DE, C, CE> getInstance(Function function) {
		if (function == null) {
			throw new IllegalArgumentException();
		}
		return new GenericFunction<D, DE, C, CE>(function);
	}

	@Override
	protected boolean abstractIsEqual(Function function) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
