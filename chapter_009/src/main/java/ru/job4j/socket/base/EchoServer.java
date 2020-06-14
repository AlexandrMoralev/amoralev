package ru.job4j.socket.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

public class EchoServer {

    private static final String EXIT = "Exit";
    private static final String ACTION_PARAM = "msg";

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(9000)) {
            boolean isRun = true;
            List<String> requestInfo = new ArrayList<>();
            while (isRun) {
                Socket socket = server.accept();
                try (OutputStream out = socket.getOutputStream();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String str;
                    while (!(str = in.readLine()).isEmpty()) {
                        requestInfo.add(str);
                        System.out.println(str);
                    }
                    Map<String, String> requestParams = parseRequestParameters(requestInfo);
                    requestInfo.clear();
                    String answer = "";
                    if (EXIT.equals(requestParams.get(ACTION_PARAM))) {
                        isRun = false;
                    } else {
                        answer = requestParams.get(ACTION_PARAM);
                    }
                    out.write(String.format("HTTP/1.1 200 OK >> %s \r\n", answer).getBytes());
                }
            }
        }
    }

    private static Map<String, String> parseRequestParameters(Collection<String> requestInfo) {
        return requestInfo.stream()
                .map(String::strip)
                .filter(not(String::isBlank))
                .filter(s -> s.contains("/?"))
                .map(extractRequestParams())
                .map(v -> v.split(","))
                .flatMap(Stream::of)
                .map(String::strip)
                .filter(not(String::isBlank))
                .map(s -> s.split("="))
                .collect(
                        Collectors.toMap(
                                arr -> arr[0].replaceFirst("\\?", "").strip().toLowerCase(),
                                arr -> arr[1].strip(),
                                (existing, replacement) -> existing)
                );
    }

    private static Function<String, String> extractRequestParams() {
        return s -> s.split("\\?", -1)[1].strip().split("\\s+")[0].strip();
    }
}
