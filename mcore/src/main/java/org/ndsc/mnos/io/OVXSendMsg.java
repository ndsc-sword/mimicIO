package org.ndsc.mnos.io;

import org.openflow.protocol.OFMessage;

/**
 * Created by cgz on 12/27/16.
 */
public interface OVXSendMsg {
    public void sendMsg(OFMessage msg, OVXSendMsg from);

    public String getName();
}
