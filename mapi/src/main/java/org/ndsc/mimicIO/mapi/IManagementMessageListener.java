package org.ndsc.mimicIO.mapi;

import org.ndsc.mimicIO.mip.ManagementMessage;

/**
 * Interface for ManagementMessage listeners.
 * <p>
 * Created by timvi on 19.08.2015.
 */
public interface IManagementMessageListener {
    void OnManagementMessage(ManagementMessage message);

}
