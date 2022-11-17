package com.example.test1451;

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
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainActivity extends Activity {
    TextView tv;
    public static final int PERMISSION_RFS = 1024;
    public static final int PERMISSION_ALS = 1025;
String x ;

    @RequiresApi(api = Build.VERSION_CODES.O)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.textView);

        //instance of TelephonyManager
        TelephonyManager tele_man = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        //Variables
        LocalDateTime currentTime;
        int SNR = -100;
        int SignalPower = 0;
        String FrequencyBand = "Unknown";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nwcountryISO = tele_man.getNetworkCountryIso();
        String SIMCountryISO = tele_man.getSimCountryIso();
        String operator = tele_man.getNetworkOperatorName();
        boolean checkRoaming = tele_man.isNetworkRoaming();
        currentTime = LocalDateTime.now();
        String PhoneType = ""; // it'll hold the type of phone i.e CDMA / GSM/ None

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_RFS);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_ALS);
            return;
        }
        List<CellInfo> CellInfo = tele_man.getAllCellInfo();
        String location = "";
        String CellID = "";
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
                    FrequencyBand = ""+((CellInfoLte) info).getCellIdentity().getEarfcn()+" MHz";
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
                    FrequencyBand = ""+((CellInfoGsm) info).getCellIdentity().getArfcn()+" MHz";
                }
                break;
            }
            else if (info instanceof CellInfoWcdma) {
                CellID = ((CellInfoWcdma) info).getCellIdentity().getMcc() + "-" + ((CellInfoWcdma) info).getCellIdentity().getMnc() + "-" + ((CellInfoWcdma) info).getCellIdentity().getLac() + "-" + ((CellInfoWcdma) info).getCellIdentity().getCid();
                if (((CellInfoWcdma) info).getCellIdentity().getCid() > 0) {
                    SignalPower = ((CellInfoWcdma) info).getCellSignalStrength().getDbm();
                }
                FrequencyBand = ""+((CellInfoWcdma) info).getCellIdentity().getUarfcn();
                break;
            }if (info instanceof CellInfoCdma) {
                CellID = ((CellInfoCdma) info).getCellIdentity().getBasestationId() + "";
                if (((CellInfoCdma) info).getCellIdentity().getBasestationId() > 0) {
                    SignalPower = ((CellInfoCdma) info).getCellSignalStrength().getDbm();

                }

                break;
            }
            if (info instanceof CellInfoCdma) {
                CellID = ((CellInfoCdma) info).getCellIdentity().getBasestationId() + "";
                if (((CellInfoCdma) info).getCellIdentity().getBasestationId() > 0) {
                    SignalPower = ((CellInfoCdma) info).getCellSignalStrength().getDbm();

                }

                break;
            }
            else{
                CellID = "Not Available, Unknown Network Type!";
                break;
            }
        }



        /// GET NETWORK TYPE
        int phoneType = tele_man.getNetworkType();
        if (phoneType == TelephonyManager.NETWORK_TYPE_GPRS || phoneType == TelephonyManager.NETWORK_TYPE_EDGE || phoneType == TelephonyManager.NETWORK_TYPE_GSM || phoneType == TelephonyManager.PHONE_TYPE_CDMA )
        {PhoneType = "2G";
        }else if (phoneType == TelephonyManager.NETWORK_TYPE_UMTS || phoneType == TelephonyManager.NETWORK_TYPE_HSPA)
        {PhoneType = "3G";
        }else if (phoneType == TelephonyManager.NETWORK_TYPE_LTE)
        {PhoneType = "4G";
        }else {PhoneType = "Unknown";}

       int state = tele_man.getServiceState().getState();
        String stateTExt = "";
        if (state==0) stateTExt = "In Service";
        else stateTExt = "Out of Service";

        String SNRText = "";
        //Displaying data
        String data = "Your Mobile Details are enlisted below: \n";
        data+= "\n Main Features: \n";
        data += "\n Network type is: " + PhoneType;
        data += "\n Network operator is: " + operator;
        data += "\n CELL ID: " + CellID + location ;
        if(SNR == -100) {
            SNRText = "Not Available";
        } else{
            SNRText = ""+SNR+" dBm";
        }

        data += "\n Signal-to-Noise Ratio: " + SNRText;
        data += "\n Frequency Band is: " + FrequencyBand;
        data += "\n Signal Power: " + SignalPower +" dBm";
        data += "\n Time:  " + currentTime.format(formatter)+"\n";

        data+= "\n Extra Features: \n";

        data += "\n Network Country ISO is: " + nwcountryISO;
        data += "\n SIM Country ISO is: " + SIMCountryISO;
        data += "\n SimCard State: " + stateTExt;
        if(x==null) x = "Unknown";
        data +="\n Phone Number: "+ x;
        data += "\n Roaming on: " + checkRoaming +"\n";

        //Now we'll display the information
        tv.setText(data);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_RFS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Read Phone State Granted!");
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
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("Access Coarse Location Granted!");
                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                    }

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
    }