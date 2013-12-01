package ch.bfh.unicrypt.crypto.encoder.abstracts;

import ch.bfh.unicrypt.crypto.encoder.interfaces.Encoder;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Set;
import ch.bfh.unicrypt.math.function.interfaces.Function;

public abstract class AbstractEncoder<D extends Set, DE extends Element, C extends Set, CE extends Element>
			 implements Encoder {

	private Function encodingFunction;
	private Function decodingFunction;

	@Override
	public Function getEncodingFunction() {
		if (this.encodingFunction == null) {
			this.encodingFunction = this.abstractGetEncodingFunction();
		}
		return this.encodingFunction;
	}

	@Override
	public Function getDecodingFunction() {
		if (this.decodingFunction == null) {
			this.decodingFunction = this.abstractGetDecodingFunction();
		}
		return this.decodingFunction;
	}

	@Override
	public CE encode(final Element element) {
		return (CE) this.getEncodingFunction().apply(element);
	}

	@Override
	public DE decode(final Element element) {
		return (DE) this.getDecodingFunction().apply(element);
	}

	@Override
	public D getDomain() {
		return (D) this.getEncodingFunction().getDomain();
	}

	@Override
	public C getCoDomain() {
		return (C) this.getEncodingFunction().getCoDomain();
	}

	protected abstract Function abstractGetEncodingFunction();

	protected abstract Function abstractGetDecodingFunction();

}
