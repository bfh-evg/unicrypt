/*
 * UniCrypt
 *
 *  UniCrypt(tm): Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (c) 2016 Bern University of Applied Sciences (BFH), Research Institute for
 *  Security in the Information Society (RISIS), E-Voting Group (EVG)
 *  Quellgasse 21, CH-2501 Biel, Switzerland
 *
 *  Licensed under Dual License consisting of:
 *  1. GNU Affero General Public License (AGPL) v3
 *  and
 *  2. Commercial license
 *
 *
 *  1. This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 *  2. Licensees holding valid commercial licenses for UniCrypt may use this file in
 *   accordance with the commercial license agreement provided with the
 *   Software or, alternatively, in accordance with the terms contained in
 *   a written agreement between you and Bern University of Applied Sciences (BFH), Research Institute for
 *   Security in the Information Society (RISIS), E-Voting Group (EVG)
 *   Quellgasse 21, CH-2501 Biel, Switzerland.
 *
 *
 *   For further information contact <e-mail: unicrypt@bfh.ch>
 *
 *
 * Redistributions of files must retain the above copyright notice.
 */
package ch.bfh.unicrypt.helper.converter.abstracts;

import ch.bfh.unicrypt.UniCrypt;
import ch.bfh.unicrypt.helper.converter.interfaces.Converter;
import ch.bfh.unicrypt.helper.sequence.Sequence;
import ch.bfh.unicrypt.helper.tree.Leaf;
import ch.bfh.unicrypt.helper.tree.Node;
import ch.bfh.unicrypt.helper.tree.Tree;

/**
 * This abstract class serves as a base implementation of the {@link Converter} interface.
 * <p>
 * @author R. Haenni
 * @version 2.0
 * @param <V> The input type
 * @param <W> The output type
 */
public abstract class AbstractConverter<V, W>
	   extends UniCrypt
	   implements Converter<V, W> {

	private final Class<V> inputClass;
	private final Class<W> outputClass;

	protected AbstractConverter(Class<V> inputClass, Class<W> outputClass) {
		this.inputClass = inputClass;
		this.outputClass = outputClass;
	}

	@Override
	public Class<V> getInputClass() {
		return this.inputClass;
	}

	@Override
	public Class<W> getOutputClass() {
		return this.outputClass;
	}

	@Override
	public final W convert(V value) {
		if (this.isValidInput(value)) {
			return this.abstractConvert(value);
		}
		throw new IllegalArgumentException();
	}

	@Override
	public final V reconvert(W value) {
		if (this.isValidOutput(value)) {
			return this.abstractReconvert(value);
		}
		throw new IllegalArgumentException();
	}

	@Override
	public Tree<W> convert(Tree<V> tree) {
		if (tree.isLeaf()) {
			Leaf<V> leaf = (Leaf<V>) tree;
			return Tree.getInstance(this.convert(leaf.getValue()));
		} else {
			Node<V> node = (Node<V>) tree;
			final Converter<V, W> converter = this;
			Sequence<Tree<W>> children = node.getChildren().map(value -> converter.convert(value));
			return Tree.getInstance(children);
		}
	}

	@Override
	public Tree<V> reconvert(Tree<W> tree) {
		if (tree.isLeaf()) {
			Leaf<W> leaf = (Leaf<W>) tree;
			return Tree.getInstance(this.reconvert(leaf.getValue()));
		} else {
			Node<W> node = (Node<W>) tree;
			final Converter<V, W> converter = this;
			Sequence<Tree<V>> children = node.getChildren().map(value -> converter.reconvert(value));
			return Tree.getInstance(children);
		}
	}

	@Override
	public final boolean isValidInput(V value) {
		return value != null && this.defaultIsValidInput(value);
	}

	@Override
	public final boolean isValidOutput(W value) {
		return value != null && this.defaultIsValidOutput(value);
	}

	@Override
	public final Converter<W, V> invert() {
		final Converter<V, W> converter = this;
		return new AbstractConverter<W, V>(this.outputClass, this.inputClass) {

			@Override
			protected V abstractConvert(W value) {
				return converter.reconvert(value);
			}

			@Override
			protected W abstractReconvert(V value) {
				return converter.convert(value);
			}

			@Override
			protected boolean defaultIsValidInput(W value) {
				return converter.isValidOutput(value);
			}

			@Override
			protected boolean defaultIsValidOutput(V value) {
				return converter.isValidInput(value);
			}
		};
	}

	protected boolean defaultIsValidInput(V value) {
		return true;
	}

	protected boolean defaultIsValidOutput(W value) {
		return true;
	}

	protected abstract W abstractConvert(V value);

	protected abstract V abstractReconvert(W value);

}
