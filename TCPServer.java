//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class TCPServer {
    static DataInputStream[] dataInput = new DataInputStream[2];
    static DataOutputStream[] dataOutput = new DataOutputStream[2];
//    static Scanner userInput;
    static Socket[] clientSocket = new Socket[2];
    static ServerSocket serverSocket;
    public TCPServer() {
    }

    public static int receiveNum(int index) {
        try {
            return dataInput[index].readInt();
        } catch (Exception var1) {
            System.err.println(var1.getMessage());
            return -1;
        }
    }

    public static void sendConfig(int numMessages, int seed, int index) {
        try {
            PrintWriter out = new PrintWriter(dataOutput[index], true); // Assuming dataOutput[index] is a Socket's OutputStream
            out.println("Number of messages = " + numMessages);
            out.println("Seed = " + seed);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

//    public static void sendNumber(int numToSend, int index) {
//        try {
//            dataOutput[index].writeInt(numToSend);
//            dataOutput[index].flush();
//        } catch (Exception e) {
//            System.err.println(e.getMessage());
//        }
//
//    }

    public static void cleanUp() {
        try {
            serverSocket.close();
            for (int i = 0; i < 2; i++){
                clientSocket[i].close();
                dataOutput[i].close();
                dataInput[i].close();
            }
            System.exit(0);
        } catch (IOException var1) {
            System.err.println(var1.getMessage());
        }

    }

    public static void main(String[] args) {
        final int port = Integer.parseInt(args[0]);
        if (port <= 1024 || port > 65535){
            throw new IllegalArgumentException("Port out of bounds, must be between (1024-65535)");
        }
        final int seed = Integer.parseInt(args[1]);
        final int numMessages = Integer.parseInt(args[2]);
        final Random rnd = new Random(seed);

        try {
            System.out.println("IP Address: " + InetAddress.getLocalHost() + "\nPort Number: " + port);
            serverSocket = new ServerSocket(port);

            System.out.println("Waiting for clients...");
            for (int i = 0; i < 2 ; i++){
                clientSocket[i] = serverSocket.accept();
            }

            System.out.println("Clients connected!");
            for (int i = 0; i < 2; i++){
                dataOutput[i] = new DataOutputStream(clientSocket[i].getOutputStream());
                dataInput[i] = new DataInputStream(clientSocket[i].getInputStream());
            }

            System.out.println("Sending config to clients...");
            for (int i = 0; i < 2; i++){
                int clientSeed = rnd.nextInt();
                System.out.println(clientSocket[i].getInetAddress().getHostName() + " " + clientSeed);
                sendConfig(numMessages, clientSeed, i);
            }
            System.out.println("Finished sending config to clients.");

            cleanUp();
        }
        catch (BindException e){
            System.out.println(e.getMessage());
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
