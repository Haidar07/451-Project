package com.example.test1451;

import android.content.Intent;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HomeActivity extends Activity {
    TextView CELL_ID,SIGNAL_POWER,SNRtext,
            NETWORK_ISO, OPERATOR,NETWORK_TYPE,
            FREQ_BAND,PHONE_NUMBER,ROAMING,
            TIME,SIM_ISO,SIM_STATE, refresh;
    DBHelper DB;

    public static final int PERMISSION_RFS = 1024;
    public static final int PERMISSION_ALS = 1025;
    public static final int PERMISSION_PPS = 1025;
    int Counter = 1;

    TelephonyManager tele_man;
    LocalDateTime currentTime;
    int SNR = -100;
    int SignalPower = 0;
    String FrequencyBand = "Unknown";
    @RequiresApi(api = Build.VERSION_CODES.O)
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    String NetworkType = ""; // it'll hold the type of phone i.e CDMA / GSM/ None
    String operator;
    boolean checkRoaming;
    String location = "";
    String CellID = "Not-Available";
    String x;
    String data = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        Context context = getApplicationContext();
        CELL_ID =(TextView) findViewById(R.id.CellID);
        SIGNAL_POWER =(TextView) findViewById(R.id.SignalPower);
        SNRtext =(TextView) findViewById(R.id.SNR);
        NETWORK_ISO =(TextView) findViewById(R.id.NetworkIso);
        FREQ_BAND =(TextView) findViewById(R.id.FBand);
        NETWORK_TYPE =(TextView) findViewById(R.id.Ntype);
        PHONE_NUMBER = (TextView)findViewById(R.id.PhoneNumber);
        ROAMING =(TextView) findViewById(R.id.roaming);
        TIME = (TextView)findViewById(R.id.Time);
        SIM_ISO =(TextView) findViewById(R.id.SimIso);
        SIM_STATE =(TextView) findViewById(R.id.SimState);
        OPERATOR = (TextView)findViewById(R.id.operatorr);
        refresh = (TextView)findViewById(R.id.Refresh);
        Button btn = (Button)findViewById(R.id.button);
        //instance of TelephonyManager

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, StatisticsActivity.class));
            }
        });

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    while (true) {
                        while(Counter >=0){
                            try {
                                Thread.sleep(1000);
                                refresh.setText("Refreshing in "+ Counter + " Seconds...");
                                Counter --;
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_RFS);
                            return;
                        }
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_ALS);
                            return;
                        }
                        tele_man = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        //Variables
                        LocalDateTime currentTime;
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                        //-----------------------------------------to bee looped------------------------------------
                        String nwcountryISO = tele_man.getNetworkCountryIso();
                        String SIMCountryISO = tele_man.getSimCountryIso();
                        boolean checkRoaming = tele_man.isNetworkRoaming();
                        /// GET NETWORK TYPE
                        int phoneType = tele_man.getNetworkType();
                        if (phoneType == TelephonyManager.NETWORK_TYPE_GPRS || phoneType == TelephonyManager.NETWORK_TYPE_EDGE || phoneType == TelephonyManager.NETWORK_TYPE_GSM || phoneType == TelephonyManager.PHONE_TYPE_CDMA )
                        {NetworkType = "2G";
                        }else if (phoneType == TelephonyManager.NETWORK_TYPE_UMTS || phoneType == TelephonyManager.NETWORK_TYPE_HSPA)
                        {NetworkType = "3G";
                        }else if (phoneType == TelephonyManager.NETWORK_TYPE_LTE)
                        {NetworkType = "4G";
                        }else {NetworkType = "Unknown";}
                        String operator = tele_man.getNetworkOperatorName();
                        currentTime = LocalDateTime.now();
                        List<CellInfo> CellInfo = tele_man.getAllCellInfo();

                        if (CellInfo.isEmpty()) location = "NULL, Location is not Available!";
                        for (CellInfo info : CellInfo) {
                            if (info instanceof CellInfoLte) {
                                CellID = ((CellInfoLte) info).getCellIdentity().getMcc() + "-" + ((CellInfoLte) info).getCellIdentity().getMnc() + "-" + ((CellInfoLte) info).getCellIdentity().getCi();
                                if (((CellInfoLte) info).getCellIdentity().getCi() > 0) {
                                    SignalPower = ((CellInfoLte) info).getCellSignalStrength().getDbm();
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        SNR = ((CellInfoLte) info).getCellSignalStrength().getRssi();
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    }
                                    FrequencyBand = ""+((CellInfoLte) info).getCellIdentity().getEarfcn()+"-MHz";
                                }
                                break;
                            }
                            if (info instanceof CellInfoGsm) {
                                CellID = ((CellInfoGsm) info).getCellIdentity().getMcc() + "-" + ((CellInfoGsm) info).getCellIdentity().getMnc() + "-" + ((CellInfoGsm) info).getCellIdentity().getLac() + "-" + ((CellInfoGsm) info).getCellIdentity().getCid();
                                if (((CellInfoGsm) info).getCellIdentity().getCid() > 0) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                        SignalPower = ((CellSignalStrengthGsm) (((CellInfoGsm) info).getCellSignalStrength())).getRssi();
                                    } else {
                                        SignalPower = ((CellSignalStrengthGsm) (((CellInfoGsm) info).getCellSignalStrength())).getDbm();
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                            SNR = ((CellInfoGsm) info).getCellSignalStrength().getRssi();
                                        }
                                    }
                                    FrequencyBand = ""+((CellInfoGsm) info).getCellIdentity().getArfcn()+"-MHz";
                                }
                                break;
                            }
                            else if (info instanceof CellInfoWcdma) {
                                CellID = ((CellInfoWcdma) info).getCellIdentity().getMcc() + "-" + ((CellInfoWcdma) info).getCellIdentity().getMnc() + "-" + ((CellInfoWcdma) info).getCellIdentity().getLac() + "-" + ((CellInfoWcdma) info).getCellIdentity().getCid();
                                if (((CellInfoWcdma) info).getCellIdentity().getCid() > 0) {
                                    SignalPower = ((CellInfoWcdma) info).getCellSignalStrength().getDbm();
                                }
                                FrequencyBand = ""+((CellInfoWcdma) info).getCellIdentity().getUarfcn()+"-MHz";
                                break;
                            }if (info instanceof CellInfoCdma) {
                                CellID = ((CellInfoCdma) info).getCellIdentity().getBasestationId() + "";
                                if (((CellInfoCdma) info).getCellIdentity().getBasestationId() > 0) {
                                    SignalPower = ((CellInfoCdma) info).getCellSignalStrength().getDbm();

                                }
                                break;
                            }
                            else{
                                CellID = "Not-Available!";
                                break;
                            }
                        }

                        int state = tele_man.getServiceState().getState();
                        String stateTExt = "";
                        if (state==0) stateTExt = "In Service";
                        else stateTExt = "Out of Service";
                        String SNRText = "";
                        if(SNR == -100) {
                            SNRText = "Not Available";
                        } else{
                            SNRText = ""+SNR+" dBm";
                        }
                        if(x==null) x = "Unknown";

                        //Now we'll display the information
                        NETWORK_TYPE.setText(NetworkType);
                        OPERATOR.setText(operator);
                        CELL_ID.setText(CellID+location);
                        SNRtext.setText(String.valueOf(SNRText));
                        FREQ_BAND.setText(FrequencyBand);
                        SIGNAL_POWER.setText(String.valueOf(SignalPower) +" dBm");
                        TIME.setText(String.valueOf(currentTime.format(formatter)));
                        NETWORK_ISO.setText(nwcountryISO);
                        SIM_ISO.setText(SIMCountryISO);
                        SIM_STATE.setText(stateTExt);
                        PHONE_NUMBER.setText(x);
                        ROAMING.setText(String.valueOf(checkRoaming));
                        // -------------------------------------------------------------------------------------------------
                        Counter = 10;

                        data = LoginActivity.user + " " + operator + " " + SignalPower + " " + SNR + " " +
                                NetworkType + " " + FrequencyBand + " " + CellID + " " +
                                currentTime.format(formatter);

                        send();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_RFS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.READ_PHONE_STATE)
                            == PackageManager.PERMISSION_GRANTED) {
                        TelephonyManager tele_man = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                        String number = tele_man.getLine1Number();
                        x=number;
                    }

                } else {
                }
                return;
            }
            case PERMISSION_ALS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    }

                } else {
                }
                return;
            }

        }
    }

    public void send() {
        Sender dataSender = new Sender();
        dataSender.execute(data);
    }
}