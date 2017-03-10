package org.ndsc.mimicIO.mapi;

import org.ndsc.mimicIO.mip.OpenFlowMessage;

/**
 * Created by arne on 20.07.16.
 */
public interface RoutingRequestCallback {

    void onResponseReceived(OpenFlowMessage message);
}
