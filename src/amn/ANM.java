/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package amn;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author admin
 */
public class ANM {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
           System.out.println("Server started....");
      
       int count=0;
       while(true){
       count++;
         Socket clientSocket = serverSocket.accept();
         ServerClientThread sct = new ServerClientThread(clientSocket,count);
        DataInputStream input = new DataInputStream(clientSocket.getInputStream());
        DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
        String mess = "hello client "+count;
        output.writeUTF(mess);
           System.out.println(input.readUTF());
        output.flush();
        
    }}
}
