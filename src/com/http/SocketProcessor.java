package com.http;

import java.io.*;
import java.net.Socket;

public class SocketProcessor implements Runnable {
    private Logger logger;
    private String pathToStorage;
    private Socket socket;

    public SocketProcessor(Logger logger, String pathToStorage, Socket socket) {
        this.logger = logger;
        this.pathToStorage = pathToStorage;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (InputStream in = socket.getInputStream();
             BufferedInputStream bis = new BufferedInputStream(in)) {

            String response = "";

            logger.print("Message reading started.");
            int available = bis.available();
            int size = available == 0 ? 1024 : available;
            byte[] bytes = new byte[size];

            in.read(bytes);

            String msg = new String(bytes);
            String lines[] = msg.split("\\r?\\n");
            logger.print("Message reading finished.");

            for (String line : lines) {
                msg = msg.replace(line + "\\r?\\n", "");

                if (line.contains("HTTP")) {//it means we can get request type from this line
                    line = line.trim();
                    String[] words = line.split(" ");
                    String strRequestType = words[0];
                    RequestType requestType = RequestType.valueOf(strRequestType);
                    String requestUrl = words[1];

                    RequestProcessor processor = null;
                    switch (requestType) {
                        case POST:
                            processor = new PostRequestProcessor(msg, requestUrl, pathToStorage, logger);
                            break;
                        case GET:
                            processor = new GetRequestProcessor(requestUrl, pathToStorage, logger);
                            break;
                        default:
                    }
                    response = processor.process();
                    break; // stop line reading
                }
            }
            logger.print("Message processing finished.");

            if (!response.isEmpty()) {
                try (OutputStream out = socket.getOutputStream();//try to send response
                     OutputStreamWriter w = new OutputStreamWriter(out)) {
                    w.write(response);
                    w.write("\n");
                    w.flush();
                    logger.print("Response sent.");
                }
            }

        } catch (Exception e) {
            logger.print(e.getMessage());
        }

        destroy();// close the socket
    }

    public void destroy(){
        if(this.socket != null && this.socket.isClosed()){
            try {
                this.socket.close();
                logger.print("Socket closed.");
            } catch (IOException e) {
                logger.print("Socket closing failed.");
                logger.print(e.getMessage());
            }

        }
    }
}
