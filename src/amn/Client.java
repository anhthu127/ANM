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
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import javax.sound.midi.Soundbank;

/**
 *
 * @author admin
 */
public class Client {

    public static void main(String[] args) throws Exception {
        try {
            Socket socket = new Socket();
            SocketAddress address = new InetSocketAddress(8000);
            socket.connect(address);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String clientMessage = "", serverMessage = "";
            
            
            System.out.println(input.readUTF());
                 System.out.println("Type command :");
            while (!br.equals("-exit")) {
           
                clientMessage = br.readLine();
                output.writeUTF(clientMessage);
                output.flush();
                serverMessage = input.readUTF();
                System.out.println(serverMessage);
            }
            output.close();
            output.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);

        }
    }
}
