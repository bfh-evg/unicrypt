/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.schemes.sharing.interfaces;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public interface ThresholdSecretSharingScheme
			 extends SecretSharingScheme {

	public int getThreshold();

}
