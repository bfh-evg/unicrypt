/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bfh.unicrypt.crypto.keygenerator.classes;

import ch.bfh.unicrypt.crypto.keygenerator.abstracts.AbstractKeyGenerator;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZMod;
import ch.bfh.unicrypt.math.algebra.dualistic.classes.ZModElement;
import java.util.Random;

/**
 *
 * @author reto
 */
public class PasswordBasedKeyGenerator extends AbstractKeyGenerator<ZMod, ZModElement> {

    //TODO: Implement
    public PasswordBasedKeyGenerator(ZMod keySpace) {
        super(keySpace);
    }

    @Override
    public ZModElement generateKey(Random random) {
        return null;
    }

    @Override
    public ZModElement generateKey() {
        return null;
    }

}
