/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 *
 * @author admin
 */
public class ServerClientThread extends Thread {

    Socket serverClient;
    int clientNo;
    int squre;
    String userName, passWord;
    String clientMessage, serverMessage;

    Boolean isLogin = false;

    ServerClientThread(Socket inSocket, int counter) {
        serverClient = inSocket;
        clientNo = counter;
    }

    public void HelpCommand(DataInputStream inStream, DataOutputStream outStream) throws IOException {
        if (isLogin) {
            serverMessage = "Logined";
            outStream.writeUTF(serverMessage);
            outStream.flush();
        } else {
            serverMessage = "-login: Login with a account\n -register: Create a new account \n -exit: exit";
            outStream.writeUTF(serverMessage);
            outStream.flush();

        }

    }

    public void RegisterCommand(DataInputStream inStream, DataOutputStream outStream) throws IOException {
        if (isLogin) {
            serverMessage = "Invalid Command";
            outStream.writeUTF(serverMessage);
            outStream.flush();
        } else {
            outStream.writeUTF("UserName: ");
            outStream.flush();
            userName = inStream.readUTF();
            outStream.writeUTF("PassWord: ");
            outStream.flush();
            passWord = inStream.readUTF();

            File file = new File(".\\data\\admindata\\info_user.txt");
            FileWriter fileWriter = new FileWriter(file, true);
            FileReader fileReader = new FileReader(file);
            boolean isExist = false;
            ArrayList<String> arr = new ArrayList();
            BufferedReader reader = new BufferedReader(fileReader);
            String line = reader.readLine();
            while (line != null) {
                arr.add(line);
                line = reader.readLine();
            }
            reader.close();

            for (int i = 0; i < arr.size(); i++) {
                if (arr.get(i).contains(userName + ":" + passWord)) {
                    isExist = true;
//                    break;
                }
            }
            if (isExist) {
                serverMessage = "create new account failure";
                outStream.writeUTF(serverMessage);
                outStream.flush();
            } else {
                fileWriter.write("\n" + userName + ":" + passWord);
                fileWriter.flush();
                fileWriter.close();
                outStream.writeUTF("Success!");
            }

        }
    }

    public void LoginCommand(DataInputStream inStream, DataOutputStream outStream) throws IOException {
        if (isLogin) {
            serverMessage = "Already!";
            outStream.writeUTF(serverMessage);
            outStream.flush();
        } else {

            serverMessage = "Enter < username:password > ";
            outStream.writeUTF(serverMessage);
            outStream.flush();
            clientMessage = inStream.readUTF();

            String[] UserPass = new String[2];
            UserPass[0] = "";
            UserPass[1] = "";
            int temp = 0;
            for (int i = 0; i < clientMessage.length(); i++) {
                if (clientMessage.charAt(i) == ':') {
                    temp++;
                    continue;
                }
                UserPass[temp]+=clientMessage.charAt(i);
            }
            userName=UserPass[0];
            passWord= UserPass[1];
        }
    }

    public void run() {
        try {
            clientMessage = "";
            serverMessage = "";
            DataInputStream inStream = new DataInputStream(serverClient.getInputStream());
            DataOutputStream outStream = new DataOutputStream(serverClient.getOutputStream());

            while (!clientMessage.equals("-exit")) {
                clientMessage = inStream.readUTF();
                System.out.println("From Client-" + clientNo + ": Number is :" + clientMessage);
                switch (clientMessage) {
                    case "-help":
                        HelpCommand(inStream, outStream);
                    case "-login":
                            LoginCommand(inStream, outStream);
                    case "-register":
                        RegisterCommand(inStream, outStream);
                }

            }
            inStream.close();
            outStream.close();
            serverClient.close();
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            System.out.println("Client -" + clientNo + " exit!! ");
        }
    }
}
