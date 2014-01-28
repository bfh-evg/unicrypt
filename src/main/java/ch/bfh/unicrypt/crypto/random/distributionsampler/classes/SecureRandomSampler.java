/*
 * UniCrypt
 *
 *  UniCrypt(tm) : Cryptographical framework allowing the implementation of cryptographic protocols e.g. e-voting
 *  Copyright (C) 2014 Bern University of Applied Sciences (BFH), Research Institute for
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
package ch.bfh.unicrypt.crypto.random.distributionsampler.classes;

import ch.bfh.unicrypt.crypto.random.distributionsampler.interfaces.DistributionSampler;
import ch.bfh.unicrypt.math.helper.ByteArray;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Reto E. Koenig <reto.koenig@bfh.ch>
 */
public class SecureRandomSampler
	   implements Runnable, DistributionSampler {

	private boolean isCollecting;
	private SecureRandom secureRandom;
	private DistributionSamplerCollector distributionSamplerCollector;
	private Thread collectorThread;

	private int securityParameterInBytes;

	public SecureRandomSampler(DistributionSamplerCollector distributionSamplerCollector1) {
		this.distributionSamplerCollector = distributionSamplerCollector1;
		try {
			secureRandom = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(DistributionSamplerCollector.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	public boolean isCollecting() {
		return collectorThread != null;
	}

	public void collectFreshSamples() {
		if (collectorThread != null) {
			return;
		}
		collectorThread = new Thread(this);
		collectorThread.start();
	}

	@Override
	public void run() {
		distributionSamplerCollector.setFreshSamples(ByteArray.getInstance(secureRandom.generateSeed(distributionSamplerCollector.getSecurityParameterInBytes())));
		collectorThread = null;
	}

	/**
	 * Shall return a random looking amount of bytes, created outside the context of ? UniCrypt ?
	 * <p>
	 * @param amountOfBytes
	 * @return ByteArrayElement containing the desired amount of 'random' bytes.
	 */
	public ByteArray getDistributionSample(int amountOfBytes) {
		if (amountOfBytes < 1) {
			throw new IllegalArgumentException();
		}
		return ByteArray.getInstance(secureRandom.generateSeed(amountOfBytes));
	}

}
