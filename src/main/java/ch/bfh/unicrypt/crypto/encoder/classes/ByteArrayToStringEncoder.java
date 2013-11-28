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
import ch.bfh.unicrypt.math.helper.Alphabet;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ByteArrayToStringEncoder
			 extends AbstractEncoder<ByteArrayMonoid, StringMonoid, ByteArrayElement, StringElement> {

	private final StringMonoid stringMonoid;

	protected ByteArrayToStringEncoder(Alphabet alphabet) {
		this.stringMonoid = StringMonoid.getInstance(alphabet);
	}

	public Alphabet getAlphabet() {
		return this.stringMonoid.getAlphabet();
	}

	public StringToByteArrayEncoder getDecoder() {
		return StringToByteArrayEncoder.getInstance(this.getAlphabet());
	}

	@Override
	protected Function abstractGetEncodingFunction() {
		return ConvertFunction.getInstance(ByteArrayMonoid.getInstance(), this.stringMonoid);
	}

	@Override
	protected Function abstractGetDecodingFunction() {
		return ConvertFunction.getInstance(this.stringMonoid, ByteArrayMonoid.getInstance());
	}

	public static ByteArrayToStringEncoder getInstance(Alphabet alphabet) {
		if (alphabet == null) {
			throw new IllegalArgumentException();
		}
		return new ByteArrayToStringEncoder(alphabet);
	}

}
