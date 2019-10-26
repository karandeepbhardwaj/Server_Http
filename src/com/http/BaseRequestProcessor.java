package com.http;


public abstract class BaseRequestProcessor implements RequestProcessor {

    protected String fName;
    protected Logger logger;
    protected FileStorage storage;

    public BaseRequestProcessor(String fName, Logger logger, String pathToStorage) {
        this.fName = fName;
        this.logger = logger;
        this.storage = new FileStorage(pathToStorage, logger);
    }


}
