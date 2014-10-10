package ch.bfh.unicrypt.math.algebra.additive.interfaces;

import ch.bfh.unicrypt.helper.Point;
import ch.bfh.unicrypt.math.algebra.dualistic.interfaces.DualisticElement;

public interface ECElement<V, E extends DualisticElement<V>>
	   extends AdditiveElement<Point<E>> {

}
