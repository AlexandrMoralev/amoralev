package ru.job4j.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsageLog4j {

    private static final Logger LOG = LoggerFactory.getLogger(UsageLog4j.class.getName());

    public static void main(String[] args) {
        LOG.trace("trace message");
        LOG.debug("debug message");
        LOG.info("info message");
        LOG.warn("warn message");
        LOG.error("error message");

        boolean isIs = true;
        char symbol = '$';
        byte bt = 0x2;
        short shrt = 2;
        int intgr = Integer.MIN_VALUE;
        float flt = 2.222222f;
        double dbl = 3.3000001d;
        long lng = 100000000L;
        LOG.debug("Logging primitives: boolean: {}, char: {}, byte: {}, short: {}, int: {}, float: {}, double: {}, long: {}",
                isIs, symbol, bt, shrt, intgr, flt, dbl, lng);
    }
}
