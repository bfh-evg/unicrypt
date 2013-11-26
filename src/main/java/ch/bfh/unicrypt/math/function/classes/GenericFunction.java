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
 */
public class GenericFunction<D extends Set, C extends Set, E extends Element>
	   extends AbstractFunction<D, C, E> {

	Function function;

	protected GenericFunction(Function function) {
		super(function.getDomain(), function.getCoDomain());
		this.function = function;
	}

	@Override
	protected E abstractApply(Element element, Random random) {
		return (E) this.function.apply(element, random);
	}

	public static <D extends Set, C extends Set, E extends Element> GenericFunction<D, C, E> getInstance(Function function) {
		if (function == null) {
			throw new IllegalArgumentException();
		}
		return new GenericFunction<D, C, E>(function);
	}

	@Override
	protected boolean abstractIsEqual(Function function) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

}
