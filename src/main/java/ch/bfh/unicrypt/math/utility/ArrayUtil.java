/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.math.utility;

import java.math.BigInteger;
import java.util.List;

/**
 *
 * @author Rolf Haenni <rolf.haenni@bfh.ch>
 */
public class ArrayUtil {

	public static int[] bigIntegerToIntArray(BigInteger... values) {
		if (values == null) {
			throw new IllegalArgumentException();
		}
		int[] result = new int[values.length];
		for (int i = 0; i < values.length; i++) {
			if (values[i] == null) {
				throw new IllegalArgumentException();
			}
			result[i] = values[i].intValue();
		}
		return result;
	}

	public static BigInteger[] intToBigIntegerArray(int... values) {
		if (values == null) {
			throw new IllegalArgumentException();
		}
		BigInteger[] result = new BigInteger[values.length];
		for (int i = 0; i < values.length; i++) {
			result[i] = BigInteger.valueOf(values[i]);
		}
		return result;
	}

	public static byte[] byteListToByteArray(List<Byte> byteList) {
		byte[] bytes = new byte[byteList.size()];
		int i = 0;
		for (Byte b : byteList) {
			bytes[i] = b;
			i++;
		}
		return bytes;
	}

}
