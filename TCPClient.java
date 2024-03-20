//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient {
    static DataInputStream dataInput;
    static BufferedReader reader;
    static DataOutputStream dataOutput;
    static Socket clientSocket;

    public TCPClient() {
    }

//    public static int receiveNum() {
//        try {
//            return dataInput.readInt();
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//            return -1;
//        }
//    }

    public static String receiveInfo() {
        try {
            if (reader == null) {
                reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            }
            return reader.readLine() + "\n" + reader.readLine(); // This will block until a line of text is received
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static void sendNumber(int numToSend) {
        try {
            dataOutput.writeInt(numToSend);
            dataOutput.flush();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public static void cleanUp() {
        try {
            clientSocket.close();
            dataOutput.close();
            dataInput.close();
            System.out.println("Connection closed");
            System.exit(0);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        final String SERVER_IP = args[0];
        final int PORT = Integer.parseInt(args[1]);

        try {
            clientSocket = new Socket(SERVER_IP, PORT);
            dataOutput = new DataOutputStream(clientSocket.getOutputStream());
            dataInput = new DataInputStream(clientSocket.getInputStream());

            String info = receiveInfo();
            if (info != null) {
                System.out.println("Received config:\n" + info);
            }

            cleanUp();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
