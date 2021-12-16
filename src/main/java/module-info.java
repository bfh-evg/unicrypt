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
module ch.bfh.unicrypt {
    exports ch.bfh.unicrypt.helper.hash;
    exports ch.bfh.unicrypt.helper.math;
    exports ch.bfh.unicrypt.helper.prime;
    exports ch.bfh.unicrypt.helper.random;
    exports ch.bfh.unicrypt.helper.random.deterministic;
    exports ch.bfh.unicrypt.helper.random.hybrid;
    exports ch.bfh.unicrypt.helper.random.nondeterministic;
    exports ch.bfh.unicrypt.helper.random.password;
    exports ch.bfh.unicrypt.helper.sequence;
    exports ch.bfh.unicrypt.helper.tree;
    exports ch.bfh.unicrypt.math.algebra.additive.abstracts;
    exports ch.bfh.unicrypt.math.algebra.additive.classes;
    exports ch.bfh.unicrypt.math.algebra.additive.interfaces;
    exports ch.bfh.unicrypt.math.algebra.additive.parameters;
    exports ch.bfh.unicrypt.math.algebra.concatenative.abstracts;
    exports ch.bfh.unicrypt.math.algebra.concatenative.classes;
    exports ch.bfh.unicrypt.math.algebra.concatenative.interfaces;
    exports ch.bfh.unicrypt.math.algebra.dualistic.abstracts;
    exports ch.bfh.unicrypt.math.algebra.dualistic.classes;
    exports ch.bfh.unicrypt.math.algebra.dualistic.interfaces;
    exports ch.bfh.unicrypt.math.algebra.general.abstracts;
    exports ch.bfh.unicrypt.math.algebra.general.classes;
    exports ch.bfh.unicrypt.math.algebra.general.interfaces;
    exports ch.bfh.unicrypt.math.algebra.multiplicative.abstracts;
    exports ch.bfh.unicrypt.math.algebra.multiplicative.classes;
    exports ch.bfh.unicrypt.math.algebra.multiplicative.interfaces;
    exports ch.bfh.unicrypt.math.function.abstracts;
    exports ch.bfh.unicrypt.math.function.classes;
    exports ch.bfh.unicrypt.math.function.interfaces;

    requires java.xml.bind;
    requires jnagmp;
}
