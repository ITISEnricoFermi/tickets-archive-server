/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package udp.server;

import archive.*;
import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author 5G
 */
public class UDPServer {

    public static void main(String[] args) throws SocketException, IOException {

        int port = 6789; // Porta del server
        DatagramSocket serverSocket = new DatagramSocket(port); // Socket UDP
        boolean attivo = true;
        byte[] bufferIN = new byte[1024]; // Buffer di lettura
        byte[] bufferOUT = new byte[1024]; // Buffer di scrittura

        Archive archivio = new Archive("data.txt");

        System.out.println("Server started on port " + port + ".");

        while (attivo) {
            DatagramPacket receivePacket = new DatagramPacket(bufferIN, bufferIN.length);
            serverSocket.receive(receivePacket); // Legge i dati in entrata

            int numCaratteri = receivePacket.getLength();
            InetAddress IPClient = receivePacket.getAddress(); // Legge l'indirizzo IP del mittente
            int portaClient = receivePacket.getPort(); // Legge la porta del mittente

            String ricevuto = new String(receivePacket.getData());
            ricevuto = ricevuto.substring(0, numCaratteri);
            String ip = IPClient.toString().substring(1); // Formatta l'indirizzo IP
            
            System.out.println("Ricevuto (" + ip + "): " + ricevuto); // Stampa i dati ricevuti

            String messaggio;
            Record record = archivio.getRecord(ip);
            
            // Verifica che il record esista e che eventualmente non abbia già consumato i 10 ticket gratuiti
            if (record == null || record.getCounter() < 10) {
                archivio.incCounter(ip); // Crea o incrementa il record
                String counter = Integer.toString(archivio.getRecord(ip).getCounter());
                messaggio = "Hai speso " + counter + " ticket.";
            } else {
                messaggio = "I messaggi gratuiti a tua disposizione sono terminati.";
            }
            
            archivio.saveArchive(); // Salva l'archivio sul file

            String daSpedire = messaggio;
            bufferOUT = daSpedire.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(bufferOUT, bufferOUT.length, IPClient, portaClient);

            serverSocket.send(sendPacket);

            if (ricevuto.equals("fine")) {
                System.out.println("Server stopped.");
                attivo = false;
            }
        }
    }

}
