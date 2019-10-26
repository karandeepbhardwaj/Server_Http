package com.http;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.http.RequestProcessor.BAD_REQUEST;
import static com.http.RequestProcessor.OK;

public class FileStorage {
    private Path storagePath;
    private String pathToStorage;
    private Logger logger;

    public FileStorage(String pathToStorage, Logger logger) {
        this.pathToStorage = pathToStorage;
        this.storagePath = Paths.get(pathToStorage);
        this.logger = logger;
    }

    public String readFile(String fName) {
        String result = BAD_REQUEST;

        Path pathToFile = Paths.get(pathToStorage, fName);
        File file = pathToFile.toFile();

        if (file.exists()) {
            if (isFileUnderTheStorage(file)) {
                try (Stream<String> lines = Files.lines(pathToFile)) {

                    String mimeType = Files.probeContentType(pathToFile);
                    String fileContent = "Content-Type: " + mimeType + "\n";
                    fileContent += String.format("Content-Disposition: attachment; filename=\"%s\"\r?\n", fName);
                    fileContent += lines.collect(Collectors.joining(System.lineSeparator()));

                    result = fileContent;
                } catch (IOException e) {
                    logger.print("File reading error.");
                    logger.print(e.getMessage());
                }
            } else {
                result += "Wrong path to file.\n";
            }
        } else {
            result += "File does not exists.\n";
        }

        return result;
    }

    public String listFiles() {
        try (Stream<Path> walk = Files.walk(Paths.get(this.pathToStorage))) {
            return walk.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .collect(Collectors.joining(",\n"));

        } catch (IOException e) {
            logger.print("Directory reading failed.");
            logger.print(e.getMessage());
        }
        return "";
    }

    public String writeFile(String fName, String content, boolean overwrite) throws IOException {
        String res = BAD_REQUEST;
        boolean fileStartedStarted = false;
        String[] lines = content.split("\\r?\\n");

        FileWriter writer = null;
        for (String line : lines) {
            logger.print(line);

            if (line.isEmpty()) {
                Path fPath = Paths.get(pathToStorage, fName);
                final File file = fPath.toFile();

                if (!isFileUnderTheStorage(file)){
                    res += "You are trying to save file out of storage.";
                    break;
                }

                if (file.exists() && !overwrite) { // we can't overwrite file
                    res = OK + "File exists, we can't overwrite it.";
                    break;
                }
                writer = new FileWriter(file);
                fileStartedStarted = true;
                logger.print("We have found start of file.");
                continue;
            }

            if (fileStartedStarted) {
                line = line.trim();
                writer.write(line);
                writer.write("\n");
            }
        }

        if (writer != null) {
            writer.close();
            logger.print("Income file saved to fs.");
            res = OK;
        }

        return res;
    }

    private boolean isFileUnderTheStorage(File file) {
        try {
            File normalizedFName = file.getCanonicalFile().getParentFile();
            int r = normalizedFName.compareTo(storagePath.toFile());
            return r >= 0;
        } catch (IOException e) {
            logger.print(e.getMessage());
        }
        return false;
    }

}
