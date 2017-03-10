package org.ndsc.mimicIO.io;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelHandler;
import org.ndsc.mimicIO.MNOSManager;
import org.openflow.protocol.OFType;

import java.io.IOException;

/**
 * Created by chengguozhen on 12/22/16.
 */
public abstract class OFChannelHandler extends IdleStateAwareChannelHandler{

    @SuppressWarnings("rawtypes")
    protected Switch sw;
    protected Channel channel;
    protected MNOSManager manager;

    public abstract boolean isHandShakeComplete();

    protected abstract String getSwitchInfoString();

    protected abstract void sendHandShakeMessage(OFType type)
            throws IOException;

}
