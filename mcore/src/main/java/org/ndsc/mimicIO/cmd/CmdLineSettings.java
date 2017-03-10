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
package org.ndsc.mimicIO.cmd;

import com.mongodb.ServerAddress;
import org.kohsuke.args4j.Option;

/**
 * Created by chengguozhen on 12/22/16.
 */
public class CmdLineSettings {
    /**
     * Default MNOS host.
     */
    public static final String DEFAULT_OF_HOST = "0.0.0.0";
    /**
     * Default MNOS port.
     */
    public static final Integer DEFAULT_OF_PORT = 6633;

    /**
     * Default database host.
     */
    public static final String DEFAULT_DB_HOST = ServerAddress.defaultHost();
    /**
     * Default database port.
     */
    public static final Integer DEFAULT_DB_PORT = ServerAddress.defaultPort();
    /**
     * Default value if database is cleared at start.
     */
    public static final Boolean DEFAULT_DB_CLEAR = false;
    /**
     * Default value (in seconds) the switch statistics are queried.
     */
    public static final Integer DEFAULT_STATS_REFRESH = 30;
    /**
     * Default number of threads to handle switch connection events.
     */
    public static final Integer DEFAULT_SERVER_THREADS = 32;
    /**
     * Default number of threads to handle controller connection events.
     */
    public static final Integer DEFAULT_CLIENT_THREADS = 32;
    /**
     * Default value if BDDP is used for discovery.
     */
    public static final Boolean DEFAULT_USE_BDDP = false;

    @Option(name = "-p", aliases = "--of-port", metaVar = "INT", usage = "MNOS OpenFlow listen port")
    private Integer ofPort = CmdLineSettings.DEFAULT_OF_PORT;

    @Option(name = "-h", aliases = "--of-host", metaVar = "String", usage = "MNOS host")
    private String ofHost = CmdLineSettings.DEFAULT_OF_HOST;

    @Option(name = "-dh", aliases = "--db-host", metaVar = "String", usage = "Database host")
    private String dbHost = CmdLineSettings.DEFAULT_DB_HOST;

    @Option(name = "-dp", aliases = "--db-port", metaVar = "INT", usage = "Database port")
    private Integer dbPort = CmdLineSettings.DEFAULT_DB_PORT;

    @Option(name = "--db-clear", usage = "Clear database")
    private Boolean dbClear = CmdLineSettings.DEFAULT_DB_CLEAR;

    @Option(name = "--stats-refresh", usage = "Sets what interval to poll statistics with")
    private Integer statsRefresh = CmdLineSettings.DEFAULT_STATS_REFRESH;

    @Option(name = "--ct", aliases = "--client-threads", metaVar = "INT", usage = "Number of threads handles controller connections")
    private Integer clientThreads = CmdLineSettings.DEFAULT_CLIENT_THREADS;

    @Option(name = "--st", aliases = "--server-threads", metaVar = "INT", usage = "Number of threads handles switch connections")
    private Integer serverThreads = CmdLineSettings.DEFAULT_SERVER_THREADS;

    @Option(name = "--ub", aliases = "--use-bddp", usage = "Use BDDP for network discovery; only use if you know what you are doing.")
    private Boolean useBDDP = CmdLineSettings.DEFAULT_USE_BDDP;

    /**
     * Gets the host MNOS is running on.
     *
     * @return the OpenFlow host
     */
    public String getOFHost() {
        return this.ofHost;
    }

    /**
     * Gets the port MNOS is running on.
     *
     * @return the OpenFlow port
     */
    public Integer getOFPort() {
        return this.ofPort;
    }


    /**
     * Gets the host the database is running on.
     *
     * @return the database port
     */
    public String getDBHost() {
        return this.dbHost;
    }

    /**
     * Gets the port the database is running on.
     *
     * @return the database port
     */
    public Integer getDBPort() {
        return this.dbPort;
    }

    /**
     * Checks if the database should be cleared on start.
     *
     * @return True if the database was cleared, false otherwise
     */
    public Boolean getDBClear() {
        return this.dbClear;
    }

    /**
     * Gets the value (in seconds) the switch statistics are queried.
     *
     * @return the statistics refresh period
     */
    public Integer getStatsRefresh() {
        return this.statsRefresh;
    }

    /**
     * Gets the number of threads used to handle controller connection events.
     *
     * @return the number of client threads used
     */
    public Integer getClientThreads() {
        return this.clientThreads;
    }

    /**
     * Gets the number of threads used to handle switch connection events.
     *
     * @return the number of server threads used
     */
    public Integer getServerThreads() {
        return this.serverThreads;
    }

    /**
     * Checks if BDDP is enabled.
     *
     * @return true if BDDP is enabled, false otherwise
     */
    public Boolean getUseBDDP() {
        return this.useBDDP;
    }

}