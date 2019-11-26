package com.yogaraj.datastore.service;

import org.springframework.stereotype.Service;

import java.util.logging.Level;

@Service
public class LoggerService {

    private java.util.logging.Logger logger;
    private final String TAG_LOG_FORMAT ="%s -- %s";

    public LoggerService() {
        this.logger = java.util.logging.Logger.getAnonymousLogger();
    }

    public void logInfo(String className,String infoMessage){
        this.logger.log(Level.INFO,String.format(this.TAG_LOG_FORMAT,className,infoMessage));
    }

    public void logWarn(String className,String infoMessage){
        this.logger.log(Level.WARNING,String.format(this.TAG_LOG_FORMAT,className,infoMessage));
    }

    public void logError(String className,String infoMessage){
        this.logger.log(Level.SEVERE,String.format(this.TAG_LOG_FORMAT,className,infoMessage));
    }
}
