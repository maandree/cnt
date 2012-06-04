/**
 * Coop Network Tetris — A cooperative tetris over the Internet.
 * 
 * Copyright © 2012  Calle Lejdbrandt, Mattias Andrée, Peyman Eshtiagh
 * 
 * Project for prutt12 (DD2385), KTH.
 */
package cnt.network;

import java.io.*;


/**
 * Interfaace for messages sent and interpreted by {@link ConnectionNetworking}
 */
public interface ConnectionMessage extends Serializable
{
    //Marker interface
}
