package com.http;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpFileServer {
    private int serverPort;
    private String pathToStorage;
    private Logger logger;

    public void run() {
        logger.print("Server started on port: " + serverPort);
        try (ServerSocket socketServer = new ServerSocket(serverPort)) {
            ExecutorService executor = Executors.newCachedThreadPool();

            while (true) {
                Socket socket = socketServer.accept();
                SocketProcessor sp = new SocketProcessor(logger, pathToStorage, socket);
                executor.submit(sp);
            }


        } catch (Exception e) {
            logger.print(e.getMessage());
        }

    }

    public HttpFileServer setServerPort(int serverPort) {
        this.serverPort = serverPort;
        return this;
    }

    public HttpFileServer setPathToStorage(String pathToStorage) {
        this.pathToStorage = pathToStorage;
        return this;
    }

    public HttpFileServer setLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

}
