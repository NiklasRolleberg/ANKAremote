package Model;

import android.test.suitebuilder.annotation.LargeTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;
import java.util.Scanner;

/**
 * Created by Niklas on 2015-12-05.
 */
public class Model extends Observable {

    String ipAddress = null;
    int port = -1;
    Socket socket = null;
    boolean stop = false;
    SocketListener sl;

    double latitude;
    double longitude;
    double heading;

    double lastlatitude;
    double lastlongitude;
    double lastheading;

    public Model()
    {
        System.out.println("Model Constructor");
    }

    public void setIpAdress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean connect() {
        stop = false;
        if(ipAddress == null || port == -1) {
            System.out.println("Something is wrong with ip or port");
            return false;
        }

        try {
            sl = new SocketListener();
            Thread t = new Thread(sl);
            t.start();

        }
        catch(Exception e){
            System.out.println("Error connecting");
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean isConnected() {
        return socket != null;
    }

    public void disconnect() {
        stop = true;
    }


    //Current latlong

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getHeading() {
        return heading;
    }


    //previous latlong

    public double getLastLatitude() {
        return lastlatitude;
    }

    public double getLastLongitude() {
        return lastlongitude;
    }

    public double getLastHeading() {
        return lastheading;
    }


    private class SocketListener implements Runnable {

        SocketListener() {
            System.out.println("SocketListener started");
        }

        @Override
        public void run() {

            PrintWriter out;
            BufferedReader in;
            Scanner scanner;

            try {
                if (socket == null) {
                    socket = new Socket(ipAddress, port);
                }
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                scanner = new Scanner(in).useDelimiter(">");
            } catch (IOException e) {
                System.out.println("Fel!");
                e.printStackTrace();
                stop = true;
                try {
                    socket.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                socket = null;
                return;
            }

            String instr;
            try {
                while ((instr = scanner.next()) != null && !stop) {
                    //System.out.println("Message received: " + instr);

                    int index = 0;
                    //find start of message
                    for(int i=0;i<instr.length();i++) {
                        if (instr.charAt(i) == '<') {
                            index = i;
                            break;
                        }
                    }

                    //save latitude
                    String lat = "";
                    while(index < instr.length()-1){
                        index++;
                        if (instr.charAt(index) == ',') {
                            break;
                        }
                        else {
                            lat += instr.charAt(index);
                        }
                    }

                    //save longitude
                    String lon = "";
                    while(index < instr.length()-1){
                        index++;
                        if (instr.charAt(index) == ',') {
                            break;
                        }
                        else {
                            lon += instr.charAt(index);
                        }
                    }

                    //save heading
                    String dir = "";
                    while(index < instr.length()-1){
                        index++;
                        if (instr.charAt(index) == ',') {
                            break;
                        }
                        else {
                            dir += instr.charAt(index);
                        }
                    }

                    //System.out.println(lat);
                    //System.out.println(lon);
                    //System.out.println(heading);

                    lastlatitude = latitude;
                    lastlongitude = longitude;
                    lastheading = heading;


                    latitude = Double.parseDouble(lat);
                    longitude = Double.parseDouble(lon);
                    heading = Double.parseDouble(dir);

                    setChanged();
                    notifyObservers();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            System.out.println("Connection ended");

            try {
                System.out.println("Closing stuff");
                out.close();
                scanner.close();
                in.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }


}
