package ru.job4j.logging.log2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExJob {

    private static final Logger LOG = LogManager.getLogger(ExJob.class);

    public static void main(String[] args) {
        int version = 1;
        LOG.trace("trace message ver.{}", version);
        LOG.debug("debug message ver.{}", version);
        LOG.info("info message ver.{}", version);
        LOG.warn("warn message ver.{}", version);
        LOG.error("error message ver.{}", version);
    }
}
