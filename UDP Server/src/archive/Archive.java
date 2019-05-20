/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author 5G
 */
public class Archive {

    private String path;
    private List<Record> archive = new ArrayList<Record>();

    public Archive(String path) throws IOException {
        this.path = path;
        loadArchive();
    }
    
    /**
     * Carica l'archivio dal file.
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void loadArchive() throws FileNotFoundException, IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
            return;
        }

        Scanner input = new Scanner(file);
        while (input.hasNextLine()) {
            archive.add(new Record(input.nextLine()));
            System.out.println(archive.toString());
        }

        input.close();
    }
    
    /**
     * Salva l'archivio sul file.
     * @throws IOException 
     */
    public void saveArchive() throws IOException {

        Files.delete(Paths.get(path));

        for (Record record : archive) {
            String ip = record.getAddress();
            String counter = Integer.toString(record.getCounter());
            byte[] line = (ip + ":" + counter + "\n").getBytes();
            Files.write(Paths.get(path), line, APPEND, CREATE);
        }
    }

    /**
     * Aggiunge un record all'archivio.
     *
     * @param ip
     */
    private void addRecord(String ip) {
        this.archive.add(new Record(ip, 1));
    }

    /**
     * Restituisce il record corrispondente all'ip.
     *
     * @param ip
     * @return
     */
    public Record getRecord(String ip) {
        for (int i = 0; i < this.archive.size(); i++) {
            if (this.archive.get(i).getAddress().equals(ip)) {
                return this.archive.get(i);
            }
        }

        return null;
    }

    /**
     * Incrementa il contatore di un record.
     *
     * @param ip
     */
    public void incCounter(String ip) {
        if (getRecord(ip) == null) {
            addRecord(ip);
        } else {
            getRecord(ip).increment();
        }
    }

}
