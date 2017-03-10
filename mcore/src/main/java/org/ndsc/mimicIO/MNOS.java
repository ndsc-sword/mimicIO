package org.ndsc.mimicIO;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.ndsc.mimicIO.cmd.CmdLineSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by chengguozhen on 12/22/16.
 */
public class MNOS {
    public static final String VERSION = "MNOS-1.0-SNAPSHOT";
    private static Logger logger = LoggerFactory.getLogger(MNOS.class);

    private MNOS() {
    }

    public static void main(final String[] args) {

        /*command line*/
        final CmdLineSettings cmd = new CmdLineSettings();
        final CmdLineParser parser = new CmdLineParser(cmd);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            e.printStackTrace();
        }

        /*starting mnos main thread*/
        MNOSManager manager = new MNOSManager(cmd);
        MNOS.logger.info("starting mnos......");
        manager.run();
    }
}
