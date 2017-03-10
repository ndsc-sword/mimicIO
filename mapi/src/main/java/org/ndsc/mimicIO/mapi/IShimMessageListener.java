package org.ndsc.mimicIO.mapi;

import org.ndsc.mimicIO.mip.Message;

/**
 * Interface for shim message listeners.
 *
 * Created by timvi on 08.07.2015.
 */
public interface IShimMessageListener {
    /**
     * Called by the manager when a message from the shim is received.
     *
     * @param message The received message.
     * @param originId The id of the shim that sent the message.
     */
    MessageHandlingResult OnShimMessage(Message message, String originId);


    /**
     * Called by the manager when the manager sends a message to the shim.
     * Used for debugging/logging.
     * @param message The message sent.
     */
    void OnOutgoingShimMessage(Message message);

    /**
     * Called by the manager when a message from the shim is received and
     * all listeners have declared PASS as result before.
     *
     * @param message The received message.
     * @param originId The id of the shim that sent the message.
     */
    void OnUnhandeldShimMessage(Message message, String originId);
}
