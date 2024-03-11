package nl.han.dea.http;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ConnectionHandler {

    private static final String SERVER_NAME = "Simple DEA Webserver";
    private static final String HTTP_STATUS_200 = "200 OK";
    private static final String HTTP_STATUS_404 = "404 NOT FOUND";
    private static final String HTTP_STATUS_501 = "501 NOT IMPLEMENTED";
    private static final String HTTP_STATUS_505 = "505 HTTP VERSION NOT SUPPORTED";
    private static final String INDEX_HTML_PAGE = "pages/index.html";

    private final String HEADER_TEMPLATE = "HTTP/1.1 {{HTTP_STATUS}}\n" +
            "Date: {{DATE}}\n" +
            "HttpServer: " + SERVER_NAME + "\n" +
            "Content-Length: {{CONTENT_LENGTH}}\n" +
            "Content-Type: text/html\n";


    private final Socket socket;

    public ConnectionHandler(final Socket socket) {
        this.socket = socket;
    }

    public void accept() throws IOException {

        // We create a BufferedReader that reads the HTTP-Request.
        var inputStreamReader = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));

        // We create a BufferedWriter that will be used to write the HTTP-Response
        var outputStreamWriter = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));

        parseHttpRequest(inputStreamReader, outputStreamWriter);

        writeResponseMessage(outputStreamWriter);
    }

    private void parseHttpRequest(final BufferedReader inputStreamReader, final BufferedWriter outputStreamWriter) throws IOException {
        var startLine = true;
        String requestLine;
        while ((requestLine = inputStreamReader.readLine()) != null) {
            System.out.println(requestLine);
            if (startLine) {
                startLine = false;
                processStartLine(outputStreamWriter, requestLine);
            } else {
                if (lineMarksEndOfRequest(requestLine)) {
                    return;
                }
            }
        }
    }

    private void processStartLine(final BufferedWriter outputStreamWriter, final String requestLine) throws IOException {
        var startLineTokens = requestLine.split(" ");
        if (!"GET".equals(startLineTokens[0])) {
            outputStreamWriter.write(generateHeader(HTTP_STATUS_501, null));
            outputStreamWriter.newLine();
            outputStreamWriter.flush();
        } else if (!"/index.html".equals(startLineTokens[1])) {
            outputStreamWriter.write(generateHeader(HTTP_STATUS_404, null));
            outputStreamWriter.newLine();
            outputStreamWriter.flush();
        } else if (!"HTTP/1.1".equals(startLineTokens[2])) {
            outputStreamWriter.write(generateHeader(HTTP_STATUS_505, null));
            outputStreamWriter.newLine();
            outputStreamWriter.flush();
        }
    }

    private void writeResponseMessage(final BufferedWriter outputStreamWriter) {

        try {
            outputStreamWriter.write(generateHeader(HTTP_STATUS_200, INDEX_HTML_PAGE));
            outputStreamWriter.newLine();
            outputStreamWriter.write(readFile(INDEX_HTML_PAGE));
            outputStreamWriter.newLine();
            outputStreamWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String readFile(final String filename) {
        try {
            return new String(Files.readAllBytes(getPath(filename)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String generateHeader(final String status, final String filename) {
        var header = HEADER_TEMPLATE
                .replace("{{DATE}}",
                        OffsetDateTime.now().format(DateTimeFormatter.RFC_1123_DATE_TIME))
                .replace("{{HTTP_STATUS}}", status);

        var contentLength = Long.toString(0);
        if (filename != null) {
            contentLength = Long.toString(getPath(filename).toFile().length());
        }
        header = header.replace("{{CONTENT_LENGTH}}", contentLength);
        return header;
    }

    private Path getPath(final String filename) {
        var classLoader = getClass().getClassLoader();
        return new File(Objects.requireNonNull(classLoader.getResource(filename)).getFile()).toPath();
    }

    private boolean lineMarksEndOfRequest(String line) {
        return line.isEmpty();
    }
}
