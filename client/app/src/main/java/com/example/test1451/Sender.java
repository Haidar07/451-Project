package com.example.test1451;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Sender extends AsyncTask<String,Void,Void> {

    Socket socket;
    PrintWriter writer;


    @Override
    protected Void doInBackground(String... voids){

        String message = voids[0];



        try{
            System.out.println(message);
            socket = new Socket("10.169.32.87", 7800);
            writer = new PrintWriter(socket.getOutputStream());
            writer.write(message);
            writer.flush();
            writer.close();
            socket.close();

        }catch (IOException e){
            e.printStackTrace();
        }


        return null;
    }

}
