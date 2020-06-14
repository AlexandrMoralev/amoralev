package ru.job4j.socket.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class EchoServer {

    private static final String STOP = "msg=Bye";

    public static void main(String[] args) throws IOException {
        try (ServerSocket server = new ServerSocket(9000)) {
            boolean isRun = true;
            while (isRun) {
                Socket socket = server.accept();
                try (OutputStream out = socket.getOutputStream();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    String str;
                    List<String> requestInfo = new ArrayList<>();
                    while (!(str = in.readLine()).isEmpty()) {
                        requestInfo.add(str);
                        System.out.println(str);
                    }
                    isRun = requestInfo.stream().noneMatch(s -> s.contains(STOP));
                    out.write("HTTP/1.1 200 OK\r\n".getBytes());
                }
            }
        }
    }
}
