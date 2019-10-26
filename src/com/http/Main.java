package com.http;

public class Main {

    public static void main(String[] args) {
        String pathToStorage = "";
        int serverPort = 8080;
        Logger logger = new Logger();

        for (int i = 0; i < args.length; i++) {
            String a = args[i];
            switch (a) {
                case "-v":
                    logger.setDebug(true);
                    break;
                case "-p":
                    serverPort = Integer.parseInt(args[i + 1]);
                    break;
                case "-d":
                    pathToStorage = args[i + 1];
                    break;
                default:
                    break;
            }
        }

        new HttpFileServer()
                .setServerPort(serverPort)
                .setLogger(logger)
                .setPathToStorage(pathToStorage)
                .run();

    }
}
