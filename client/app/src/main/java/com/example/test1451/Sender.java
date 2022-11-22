package com.example.test1451;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender extends AsyncTask<String,Void,Void> {

    Socket socket;
    DataOutputStream data;
    PrintWriter writer;

    @Override
    protected Void doInBackground(String... voids){

        String message = voids[0];

        try{
            socket = new Socket("10.169.33.132", 7800);
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
