/*******************************************************************************
 * Copyright 2016 NDSC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.ndsc.mimicIO;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.execution.OrderedMemoryAwareThreadPoolExecutor;
import org.ndsc.mimicIO.cmd.CmdLineSettings;
import org.ndsc.mimicIO.db.DBManager;
import org.ndsc.mimicIO.io.SwitchChannelPipeline;
import org.openflow.vendor.nicira.OFNiciraVendorExtensions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by chengguozhen on 12/22/16.
 */
public class MNOSManager implements Runnable{

    Logger logger = LoggerFactory.getLogger(MNOSManager.class);
    private static final int SEND_BUFFER_SIZE = 1024 * 1024;
    private static final MNOSManager instance = null;

    @SuppressWarnings("unused")
    private String configFile = null;
    private String ofHost = null;
    private Integer ofPort = null;
    private String dbHost = null;
    private Integer dbPort = null;
    private Boolean dbClear = null;
    Thread server;

    private final NioClientSocketChannelFactory clientSockets = new NioClientSocketChannelFactory(
            Executors.newCachedThreadPool(),Executors.newCachedThreadPool());

    private ThreadPoolExecutor clientThreads = null;
    private ThreadPoolExecutor serverThreads = null;

    private final ChannelGroup sg = new DefaultChannelGroup();
    private final ChannelGroup cg = new DefaultChannelGroup();

    private SwitchChannelPipeline pfact = null;
    // private ClientChannelPipeline cfact = null;

    private Integer statsRefresh;

    private Integer nClientThreads;

    private Integer nServerThreads;

    private final Boolean useBDDP;


    public MNOSManager(CmdLineSettings settings) {
        this.ofHost = settings.getOFHost();
        this.ofPort = settings.getOFPort();
        this.dbHost = settings.getDBHost();
        this.dbPort = settings.getDBPort();
        this.dbClear = settings.getDBClear();
        this.statsRefresh = settings.getStatsRefresh();
        this.nClientThreads = settings.getClientThreads();
        this.nServerThreads = settings.getServerThreads();
        this.useBDDP = settings.getUseBDDP();

        this.clientThreads = new OrderedMemoryAwareThreadPoolExecutor(
                nClientThreads, 1048576, 1048576, 5, TimeUnit.SECONDS);
        this.serverThreads = new OrderedMemoryAwareThreadPoolExecutor(
                nServerThreads, 1048576, 1048576, 5, TimeUnit.SECONDS);


    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run() {
        Runtime.getRuntime().addShutdownHook(new MNOSManagerShutdownHook(this));

        OFNiciraVendorExtensions.initialize();

        this.startDatabase();

        try {
            final ServerBootstrap switchServerBootStrap = this
                    .createServerBootStrap();

            this.setServerBootStrapParams(switchServerBootStrap);

            switchServerBootStrap.setPipelineFactory(this.pfact);
            final InetSocketAddress sa = this.ofHost == null ? new InetSocketAddress(
                    this.ofPort) : new InetSocketAddress(this.ofHost,
                    this.ofPort);
            this.sg.add(switchServerBootStrap.bind(sa));
        }catch (final Exception e){
            throw new RuntimeException(e);
        }

    }


    private void setServerBootStrapParams(final ServerBootstrap bootstrap) {
        bootstrap.setOption("reuseAddr", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.sendBufferSize",
                MNOSManager.SEND_BUFFER_SIZE);

    }

    private void setClientBootStrapParams(final ClientBootstrap bootstrap) {
        bootstrap.setOption("reuseAddr", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.sendBufferSize",
                MNOSManager.SEND_BUFFER_SIZE);

    }

    private ClientBootstrap createClientBootStrap() {
        return new ClientBootstrap(this.clientSockets);
    }

    private ServerBootstrap createServerBootStrap() {
        return new ServerBootstrap(new NioServerSocketChannelFactory(
                Executors.newCachedThreadPool(),
                Executors.newCachedThreadPool()));
    }

    private void startDatabase() {
        DBManager dbManager = DBManager.getInstance();
        dbManager.init(this.dbHost, this.dbPort, this.dbClear);
        logger.info("database is started.");

    }


    public void terminate() {
        if (this.cg != null && this.cg.close().awaitUninterruptibly(1000)) {
            this.logger.info("Shut down all controller connections. Quitting...");
        } else {
            this.logger.error("Error shutting down all controller connections. Quitting anyway.");
        }

        if (this.sg != null && this.sg.close().awaitUninterruptibly(1000)) {
            this.logger.info("Shut down all switch connections. Quitting...");
        } else {
            this.logger.error("Error shutting down all switch connections. Quitting anyway.");
        }

        if (this.pfact != null) {
            this.pfact.releaseExternalResources();
        }
        /*if (this.cfact != null) {
            this.cfact.releaseExternalResources();
        }*/

        this.logger.info("Shutting down database connection");
        DBManager.getInstance().close();
    }
}
