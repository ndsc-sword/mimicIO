
package org.ndsc.mnos.io;

import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.ExternalResourceReleasable;
import org.jboss.netty.util.Timer;
import org.ndsc.mnos.MNOSManager;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by chengguozhen on 12/22/16.
 */
public abstract class OpenflowChannelPipeline implements
        ChannelPipelineFactory, ExternalResourceReleasable {

    protected MNOSManager manager;
    protected ThreadPoolExecutor pipelineExecutor;
    protected Timer timer;
    protected IdleStateHandler idleHandler;
    protected ReadTimeoutHandler readTimeoutHandler;

    public void releaseExternalResources() {
        this.timer.stop();
    }
}
