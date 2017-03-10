package org.ndsc.mnos.mapi;

import org.ndsc.mnos.mip.Message;

/**
 * Interface for shim managers.
 *
 * Created by timvi on 08.07.2015.
 */
public interface IShimManager {
    /**
     * Gets the connector that is being used.
     *
     * @return The connector.
     */
    IShimConnector getConnector();

    /**
     * Sends the given message to the shim.
     *
     * @param message The message to send.
     * @return True, if the transmission was succesful, false otherwise.
     */
    boolean sendMessage(Message message);

    /**
     * Get shim last message time to know if it is still alive
     * @return unix time stamp.
     */
    long getLastMessageTime();
}
