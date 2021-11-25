package dev.floffah.gamermode.visual;

import dev.floffah.gamermode.server.Server;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.sql.Timestamp;

public class Logger {
    public static Logger inst;

    Server main;

    /**
     * Instantiate a new Logger
     *
     * @param main The main server instance
     */
    public Logger(Server main) {
        this.main = main;
        Logger.inst = this;
    }

    /**
     * Log info to the console
     *
     * @param message The message to log
     */
    public void info(String... message) {
        log(getFormat("info") + String.join(" ", message) + "\n");
    }

    /**
     * Log a warning to the console
     *
     * @param message The message to log
     */
    public void warn(String... message) {
        log(getFormat("warn") + String.join(" ", message) + "\n");
    }

    /**
     * Log debug info to the console
     *
     * @param message The message to log
     */
    public void debug(String... message) {
        if (main.isDebugMode()) {
            log(getFormat("debug") + String.join(" ", message) + "\n");
        }
    }

    /**
     * Log a debug warning to the console
     *
     * @param message The message to log
     */
    public void debugwarn(String... message) {
        if (main.isDebugMode()) {
            log(getFormat("debugwarn") + String.join(" ", message) + "\n");
        }
    }

    /**
     * Log an error to the console
     *
     * @param message The message to log
     */
    public void err(String... message) {
        err(true, message);
    }

    /**
     * Log an error to the console
     *
     * @param dosout  Whether to print the error to the console as well as the GUI window
     * @param message The message to log
     */
    public void err(boolean dosout, String... message) {
        log(getFormat("err") + String.join(" ", message) + "\n", dosout);
    }

    /**
     * Log an exception to the console and the GUI window
     *
     * @param e The exception to log
     */
    public void printStackTrace(Exception e) {
        String[] stack = ExceptionUtils.getStackFrames(e);
        for (String s : stack) {
            err(!main.isDebugMode(), s);
        }
        if (main.isDebugMode()) e.printStackTrace();
    }

    /**
     * Gets the log format for a given type
     *
     * @param type The type of log message
     * @return The log format
     */
    String getFormat(String type) {
        return String.format("[%s] [%s] [%s]: ", new Timestamp(System.currentTimeMillis()), Thread.currentThread().getName(), type);
    }

    /**
     * Log a raw message without any special formatting
     *
     * @param message The message to log
     */
    public void log(String message) {
        this.log(message, true);
    }

    /**
     * Log a raw message without any special formatting
     *
     * @param message The message to log
     * @param dosout  Whether to print the message to the console as well as the GUI window
     */
    public void log(String message, boolean dosout) {
        if (main.getGui() != null && main.getGui().loaded) {
            main.getGui().log(message);
        }
        if (dosout) System.out.print(message);
    }
}

