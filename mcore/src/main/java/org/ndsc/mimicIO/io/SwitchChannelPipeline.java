package org.ndsc.mimicIO.io;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.execution.ExecutionHandler;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.ndsc.mimicIO.MNOSManager;
import org.ndsc.mimicIO.elements.network.PhysicalNetwork;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by chengguozhen on 12/22/16.
 */
public class SwitchChannelPipeline extends OpenflowChannelPipeline {

    private ExecutionHandler eh = null;

    public SwitchChannelPipeline(
            MNOSManager manager,
            ThreadPoolExecutor piplineExecutor) {
        super();

        this.manager = manager;
        this.pipelineExecutor = piplineExecutor;
        this.timer = PhysicalNetwork.getTimer();
        this.idleHandler = new IdleStateHandler(this.timer, 20,  25, 0);
        this.readTimeoutHandler = new ReadTimeoutHandler(this.timer,    30);
    }

    public ChannelPipeline getPipeline() throws Exception {
        final SwitchChannelHandler handler = new SwitchChannelHandler(this.manager);

        final ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("ofmessagedecoder", new OVXMessageDecoder());
        pipeline.addLast("ofmessageencoder", new OVXMessageEncoder());
        pipeline.addLast("idle", this.idleHandler);
        pipeline.addLast("timeout", this.readTimeoutHandler);
        pipeline.addLast("handshaketimeout", new HandshakeTimeoutHandler(
                handler, this.timer, 15));

        pipeline.addLast("pipelineExecutor", eh);
        pipeline.addLast("handler", handler);
        return pipeline;
    }
}
