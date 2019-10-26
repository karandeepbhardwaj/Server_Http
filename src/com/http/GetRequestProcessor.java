package com.http;


public class GetRequestProcessor extends BaseRequestProcessor {

    public GetRequestProcessor(String fName, String pathToStorage, Logger logger) {
        super(fName, logger, pathToStorage);
    }

    @Override
    public String process() {
        this.fName = this.fName.trim();
        if (this.fName.isEmpty() || this.fName.equals("/")) {
            return processDiscoverFilesListRequest();
        } else {
            return processGetFileContentRequest();
        }

    }

    private String processGetFileContentRequest() {
        logger.print("Get file content request processing started.");
        String result = this.storage.readFile(this.fName);
        logger.print("Get file content request processing finished.");
        return result;
    }

    private String processDiscoverFilesListRequest() {
        logger.print("Get files list request processing started.");
        String result = this.storage.listFiles();
        logger.print("Get files list request processing finished.");
        return result;
    }

}
