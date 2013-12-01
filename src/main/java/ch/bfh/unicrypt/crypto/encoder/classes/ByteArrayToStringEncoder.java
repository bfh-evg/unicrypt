/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.encoder.classes;

import ch.bfh.unicrypt.crypto.encoder.abstracts.AbstractEncoder;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.ByteArrayMonoid;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringElement;
import ch.bfh.unicrypt.math.algebra.concatenative.classes.StringMonoid;
import ch.bfh.unicrypt.math.function.classes.ConvertFunction;
import ch.bfh.unicrypt.math.function.interfaces.Function;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArrayToStringEncoder
			 extends AbstractEncoder<ByteArrayMonoid, ByteArrayElement, StringMonoid, StringElement> {

	private final StringMonoid stringMonoid;

	protected ByteArrayToStringEncoder(StringMonoid stringMonoid) {
		this.stringMonoid = stringMonoid;
	}

	public StringMonoid getStringMonoid() {
		return this.stringMonoid;
	}

	public StringToByteArrayEncoder getDecoder() {
		return StringToByteArrayEncoder.getInstance(this.getStringMonoid());
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return ConvertFunction.getInstance(ByteArrayMonoid.getInstance(), this.getStringMonoid());
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return ConvertFunction.getInstance(this.getStringMonoid(), ByteArrayMonoid.getInstance());
	}

	public static ByteArrayToStringEncoder getInstance(StringMonoid stringMonoid) {
		if (stringMonoid == null) {
			throw new IllegalArgumentException();
		}
		return new ByteArrayToStringEncoder(stringMonoid);
	}

}
