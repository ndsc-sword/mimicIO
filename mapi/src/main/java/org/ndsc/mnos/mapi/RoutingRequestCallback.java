package org.ndsc.mnos.mapi;

import org.ndsc.mnos.mip.OpenFlowMessage;

/**
 * Created by arne on 20.07.16.
 */
public interface RoutingRequestCallback {

    void onResponseReceived(OpenFlowMessage message);
}
