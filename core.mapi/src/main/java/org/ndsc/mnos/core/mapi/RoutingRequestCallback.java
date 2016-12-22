package org.ndsc.mnos.core.mapi;

import org.ndsc.mnos.core.mapi.core.mip.OpenFlowMessage;

/**
 * Created by arne on 20.07.16.
 */
public interface RoutingRequestCallback {

    void onResponseReceived(OpenFlowMessage message);
}
