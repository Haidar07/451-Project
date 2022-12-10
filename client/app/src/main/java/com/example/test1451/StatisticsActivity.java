package com.example.test1451;

import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class StatisticsActivity extends Activity {
    EditText startD, endD;
    private DatePickerDialog datePickerDialog;
    TextView opConn, netConn, netPow, devPow, netSNR;
    boolean flag = true;
    DBHelper DB;
    String Stat;
    String[] arr1 = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        opConn = (TextView) findViewById(R.id.operatorConnectivity);
        netConn = (TextView) findViewById(R.id.networkConnectivity);
        netPow = (TextView) findViewById(R.id.networkPower);
        devPow = (TextView) findViewById(R.id.devicePower);
        netSNR = (TextView) findViewById(R.id.networkSNR);
        startD = findViewById(R.id.startDate);
        endD = findViewById(R.id.endDate);
        DB = new DBHelper(this);


        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateCalendar();
            }

            private void updateCalendar() {
                String Format = "dd/MM/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);
                if (flag) {
                    startD.setText(sdf.format(calendar.getTime()));
                    flag = false;
                } else {
                    endD.setText(sdf.format(calendar.getTime()));
                }
            }
        };
        startD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = true;
                new DatePickerDialog(StatisticsActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        endD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(StatisticsActivity.this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button back = (Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticsActivity.this, HomeActivity.class));
            }
        });


        Thread myThread = new Thread(new MyServerThread());
        myThread.start();

        Button request = (Button) findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sender dataSender = new Sender();
                dataSender.execute("REQUEST");

                opConn.setText(arr1[0]);
                netConn.setText(arr1[1]);
                netSNR.setText(arr1[2]);
                netPow.setText(arr1[3]);
                devPow.setText(arr1[4]);
            }
        });
    }

    class MyServerThread implements Runnable {

        Socket s;
        ServerSocket ss;
        InputStreamReader isr;
        BufferedReader br;

        @Override
        public void run(){
            try{
                ss = new ServerSocket(4000);
                while(true) {
                    System.out.println("Client is Listening on Port 50000");
                    s = ss.accept();
                    System.out.println("Stopped Listening");
                    isr = new InputStreamReader(s.getInputStream());
                    br = new BufferedReader(isr);
                    Stat = br.readLine();
                    s.close();
                }

            }catch(IOException ioe){
                ioe.printStackTrace();
            }

            StringTokenizer t = new StringTokenizer(Stat);

            String word ="";

            int i = 0;


            while(t.hasMoreTokens()){

                word = t.nextToken();
                System.out.print(word + "  ");

                arr1[i] = word;

                i++;
            }

        }
    }
}