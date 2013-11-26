/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.algebra.general.classes;

import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Group;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Monoid;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author rolfhaenni
 */
public class ProductMonoid
	   extends ProductSemiGroup
	   implements Monoid {

	private Tuple identityElement;

	protected ProductMonoid(final Monoid[] monoids) {
		super(monoids);
	}

	protected ProductMonoid(final Monoid monoid, final int arity) {
		super(monoid, arity);
	}

	@Override
	public Monoid getFirst() {
		return (Monoid) super.getFirst();
	}

	@Override
	public Monoid getAt(int index) {
		return (Monoid) super.getAt(index);
	}

	@Override
	public Monoid getAt(int... indices) {
		return (Monoid) super.getAt(indices);
	}

	@Override
	public Monoid[] getAll() {
		return (Monoid[]) super.getAll();
	}

	@Override
	public ProductMonoid removeAt(final int index) {
		return (ProductMonoid) super.removeAt(index);
	}

	@Override
	protected ProductMonoid abstractRemoveAt(Set set, int arity) {
		return ProductMonoid.getInstance((Monoid) set, arity);
	}

	@Override
	protected ProductMonoid abstractRemoveAt(Set[] sets) {
		return ProductMonoid.getInstance((Monoid[]) sets);
	}

	@Override
	public Tuple getIdentityElement() {
		if (this.identityElement == null) {
			final Element[] identityElements = new Element[this.getArity()];
			for (int i = 0; i < identityElements.length; i++) {
				identityElements[i] = this.getAt(i).getIdentityElement();
			}
			this.identityElement = this.standardGetElement(identityElements);
		}
		return this.identityElement;
	}

	@Override
	public boolean isIdentityElement(Element element) {
		return this.areEqual(element, this.getIdentityElement());
	}

	@Override
	public Tuple standardApply(final Element[] elements) {
		if (elements.length == 0) {
			return this.getIdentityElement();
		}
		return super.standardApply(elements);
	}

	@Override
	protected Tuple standardMultiSelfApply(final Element[] elements, BigInteger[] amounts) {
		if (elements.length == 0) {
			return this.getIdentityElement();
		}
		return super.standardMultiSelfApply(elements, amounts);
	}

	/**
	 * This is a static factory method to construct a composed monoid without
	 * calling respective constructors. The input monids are given as an array.
	 * <p/>
	 * @param monoids The array of input monoids
	 * @return The corresponding product monoids
	 * @throws IllegalArgumentException if {@literal monids} is null or contains
	 *                                  null
	 */
	public static ProductMonoid getInstance(final Monoid... monoids) {
		if (monoids == null) {
			throw new IllegalArgumentException();
		}
		boolean isGroup = true;
		if (monoids.length > 0) {
			boolean uniform = true;
			Monoid first = monoids[0];
			for (final Monoid monoid : monoids) {
				if (monoid == null) {
					throw new IllegalArgumentException();
				}
				if (!monoid.isEqual(first)) {
					uniform = false;
				}
				isGroup = isGroup && monoid.isGroup();
			}
			if (uniform) {
				return ProductMonoid.getInstance(first, monoids.length);
			}
		}
		if (isGroup) {
			Group[] groups = Arrays.copyOf(monoids, monoids.length, Group[].class);
			return ProductGroup.getInstance(groups);
		}
		return new ProductMonoid(monoids);
	}

	public static ProductMonoid getInstance(final Monoid monoid, int arity) {
		if ((monoid == null) || (arity < 0)) {
			throw new IllegalArgumentException();
		}
		if (monoid.isGroup()) {
			return ProductGroup.getInstance((Group) monoid, arity);
		}
		if (arity == 0) {
			return new ProductMonoid(new Monoid[]{});
		}
		return new ProductMonoid(monoid, arity);
	}

}
