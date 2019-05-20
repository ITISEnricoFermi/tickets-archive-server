/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package archive;

/**
 *
 * @author 5G
 */
public class Record {

    private String address;
    private int counter;

    public Record(String record) {
        this.address = record.split(":")[0];
        this.counter = Integer.parseInt(record.split(":")[1]);
    }
    
    public Record(String ip, int counter) {
        this.address = ip;
        this.counter = counter;
    }

    public String getAddress() {
        return this.address;
    }

    public int getCounter() {
        return this.counter;
    }

    public void increment() {
        this.counter++;
    }
   
}
