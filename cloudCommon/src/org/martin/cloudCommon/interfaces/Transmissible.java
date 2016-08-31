/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.interfaces;

import java.io.IOException;

/**
 *
 * @author martin
 */
@FunctionalInterface
public interface Transmissible {
    public void sendObject(Object obj) throws IOException;
}
