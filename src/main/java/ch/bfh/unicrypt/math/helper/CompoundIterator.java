/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.helper;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 * @param <T>
 */
public class CompoundIterator<T>
			 implements Iterator<T> {

	public CompoundIterator(Compound<?, ?> compound) {
		this.compound = compound;
	}

	Compound<?, ?> compound;
	int currentIndex = 0;

	@Override
	public boolean hasNext() {
		return this.currentIndex < this.compound.getArity();
	}

	@Override
	public T next() {
		if (this.hasNext()) {
			return (T) compound.getAt(this.currentIndex++);
		}
		throw new NoSuchElementException();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

}
