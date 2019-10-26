package com.http;

import java.io.IOException;

public class PostRequestProcessor extends BaseRequestProcessor {

    private String content;
    private boolean overwrite = true;


    public PostRequestProcessor(String content, String fName, String pathToStorage, Logger logger) {
        super(fName, logger, pathToStorage);
        this.content = content;
        if (fName.contains("?")) {
            String[] w = fName.split("\\?");
            this.fName = w[0];
            String overwriteParam = w[1];
            w = overwriteParam.split("=");
            this.overwrite = Boolean.getBoolean(w[1]);
        }
    }

    @Override
    public String process() throws IOException {
        logger.print("Post request processing started.");
        String response = this.storage.writeFile(this.fName, this.content, this.overwrite);
        logger.print("Post request processing finished.");
        return response;

    }
}
