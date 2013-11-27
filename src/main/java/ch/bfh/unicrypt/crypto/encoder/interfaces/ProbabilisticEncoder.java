/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.encoder.interfaces;

import ch.bfh.unicrypt.crypto.encoder.exceptions.EncodingException;
import ch.bfh.unicrypt.math.algebra.general.interfaces.Element;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public interface ProbabilisticEncoder
			 extends Encoder {

	/**
	 *
	 * @param element
	 * @return
	 * @throws EncodingException
	 */
	@Override
	public Element encode(Element element) throws EncodingException;

}
