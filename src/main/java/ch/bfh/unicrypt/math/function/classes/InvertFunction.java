package ch.bfh.unicrypt.math.function.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.function.abstracts.AbstractFunction;
import java.util.Random;

/**
 * This interface represents the the concept of a function f:X->X, which computes the inverse of the given input
 * element.
 * <p/>
 * @see Group#invert(Element)
 * @see Element#invert()
 * <p/>
 * @author R. Haenni
 * @author R. E. Koenig
 * @version 1.0
 */
public class InvertFunction
			 extends AbstractFunction<Group, Element, Group, Element> {

	private InvertFunction(final Group domain, Group coDomain) {
		super(domain, coDomain);
	}

	//
	// The following protected method implements the abstract method from {@code AbstractFunction}
	//
	@Override
	protected Element abstractApply(final Element element, final Random random) {
		return element.invert();
	}

	//
	// STATIC FACTORY METHODS
	//
	/**
	 * This is the standard constructor for this class. It creates an invert function for a given group.
	 * <p/>
	 * @param group The given Group
	 * @return
	 * @throws IllegalArgumentException if the group is null
	 */
	public static InvertFunction getInstance(final Group group) {
		return new InvertFunction(group, group);
	}

}
