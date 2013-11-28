/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.encoder.exceptions;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ProbabilisticEncodingException
			 extends RuntimeException {

	public ProbabilisticEncodingException() {
	}

	public ProbabilisticEncodingException(String message) {
		super(message);
	}

	public ProbabilisticEncodingException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProbabilisticEncodingException(Throwable cause) {
		super(cause);
	}

	public ProbabilisticEncodingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
