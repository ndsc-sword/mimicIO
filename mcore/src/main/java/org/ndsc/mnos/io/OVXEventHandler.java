package org.ndsc.mnos.io;

import org.jboss.netty.channel.Channel;
import org.openflow.protocol.OFMessage;

/**
 * Created by cgz on 12/27/16.
 */
public interface OVXEventHandler {
    public void handleIO(OFMessage msg, Channel channel);
}
