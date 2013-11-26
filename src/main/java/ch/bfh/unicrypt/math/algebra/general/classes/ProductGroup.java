/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;

/**
 *
 * @author rolfhaenni
 */
public class ProductGroup
	   extends ProductMonoid
	   implements Group {

	protected ProductGroup(final Group[] groups) {
		super(groups);
	}

	protected ProductGroup(final Group group, final int arity) {
		super(group, arity);
	}

	@Override
	public Group getFirst() {
		return (Group) super.getFirst();
	}

	@Override
	public Group getAt(int index) {
		return (Group) super.getAt(index);
	}

	@Override
	public Group getAt(int... indices) {
		return (Group) super.getAt(indices);
	}

	@Override
	public Group[] getAll() {
		return (Group[]) super.getAll();
	}

	@Override
	public ProductGroup removeAt(final int index) {
		return (ProductGroup) super.removeAt(index);
	}

	@Override
	protected ProductGroup abstractRemoveAt(Set set, int arity) {
		return ProductGroup.getInstance((Group) set, arity);
	}

	@Override
	protected ProductGroup abstractRemoveAt(Set[] sets) {
		return ProductGroup.getInstance((Group[]) sets);
	}

	@Override
	public final Tuple invert(Element element) {
		if (!this.contains(element)) {
			throw new IllegalArgumentException();
		}
		int arity = this.getArity();
		Tuple tuple = (Tuple) element;
		final Element[] invertedElements = new Element[arity];
		for (int i = 0; i < arity; i++) {
			invertedElements[i] = tuple.getAt(i).invert();
		}
		return this.standardGetElement(invertedElements);
	}

	@Override
	public final Tuple applyInverse(Element element1, Element element2) {
		return this.apply(element1, this.invert(element2));
	}
//
	// STATIC FACTORY METHODS
	//

	/**
	 * This is a static factory method to construct a composed group without
	 * calling respective constructors. The input groups are given as an array.
	 * <p/>
	 * @param groups The array of input groups
	 * @return The corresponding composed group
	 * @throws IllegalArgumentException if {@literal groups} is null or contains
	 *                                  null
	 */
	public static ProductGroup getInstance(final Group... groups) {
		if (groups == null) {
			throw new IllegalArgumentException();
		}
		boolean isCyclic = false;
		if (groups.length > 0) {
			boolean uniform = true;
			Group first = groups[0];
			for (final Group group : groups) {
				if (group == null) {
					throw new IllegalArgumentException();
				}
				if (!group.isEqual(first)) {
					uniform = false;
				}
				isCyclic = isCyclic && group.isCyclic();
			}
			if (uniform) {
				return ProductGroup.getInstance(first, groups.length);
			}
		}
		if (isCyclic) {
			return ProductCyclicGroup.getInstance(groups);
		}
		return new ProductGroup(groups);
	}

	public static ProductGroup getInstance(final Group group, int arity) {
		if ((group == null) || (arity < 0)) {
			throw new IllegalArgumentException();
		}
		if (group.isCyclic()) {
			return ProductCyclicGroup.getInstance(group, arity);
		}
		if (arity == 0) {
			return new ProductGroup(new Group[]{});
		}
		return new ProductGroup(group, arity);
	}

}
