package com.halfdotfull.panchi_app.Activities;


import android.Manifest;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.halfdotfull.panchi_app.Database.ContactDataBase;
import com.halfdotfull.panchi_app.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.android.volley.VolleyLog.TAG;

public class SendSMSActivity extends Activity implements LocationListener {
    ContactDataBase db;
    Button buttonSend;
    EditText textPhoneNo;
    EditText textSMS;
    String address, city, state;
    SharedPreferences sharedpreferences;
    LocationManager lm;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast;

    public static String latitude = null, longitude = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_m_s);

        buttonSend = (Button) findViewById(R.id.buttonSend);
       // textPhoneNo = (EditText) findViewById(R.id.editTextPhoneNo);
        textSMS = (EditText) findViewById(R.id.editTextSMS);
        db = new ContactDataBase(SendSMSActivity.this);

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        buttonSend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String phoneNo = textPhoneNo.getText().toString();
                String sms = textSMS.getText().toString();
                sms=createMessage();

                try {
                    SmsManager smsmanager = SmsManager.getDefault();
                    Cursor res = db.getAllData();

                    if (res.getCount() == 0)
                        Toast.makeText(SendSMSActivity.this, "No contacts given", Toast.LENGTH_SHORT).show();
                    else {
                        while (res.moveToNext()) {
                            smsmanager.sendTextMessage(res.getString(0),null,sms,null,null);


                        }
                        Log.d("service", sms);
                        Toast.makeText(SendSMSActivity.this, "Emergency Message sent to " + res.getString(1), Toast.LENGTH_LONG).show();
                    }


//                    SmsManager smsManager = SmsManager.getDefault();
//                    smsManager.sendTextMessage(phoneNo, null, sms, null, null);
//                    Toast.makeText(getApplicationContext(), "SMS Sent!",
//                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again later!",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
            private String createMessage() {


                String message="";
                Geocoder geocoder=new Geocoder(SendSMSActivity.this, Locale.getDefault());
                List<Address> addressList=new ArrayList<>();

                Log.d("ABC: ", latitude + " " + longitude);

                if(latitude!=null&&longitude!=null){
                    try {

                        message=createMessage();
                        addressList=geocoder.getFromLocation(Double.valueOf(latitude),Double.valueOf(longitude),1);
                        address=addressList.get(0).getAddressLine(0);
                        city=addressList.get(0).getLocality();
                        state=addressList.get(0).getAdminArea();
                        StringBuffer smsAddressLink = new StringBuffer();
                        smsAddressLink.append("http://maps.google.com/?q=");
                        smsAddressLink.append(latitude);
                        smsAddressLink.append(",");
                        smsAddressLink.append(longitude);
                        String messageByUser=sharedpreferences.getString("Message", "");
                        if (messageByUser.equals("")) {
                            messageByUser = " Please help me ";
                        }
                        message=messageByUser + System.getProperty("line.separator")+
                                " I am at " + address +
                                " " + city + " " +
                                state +" "+ System.getProperty("line.separator")+
                                smsAddressLink.toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    message="Please help me yaarrrrr";
                }
                Log.d("MSG: ", message);
                return message;
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = String.valueOf(location.getLatitude());
        longitude = String.valueOf(location.getLongitude());
        Toast.makeText(getApplicationContext(), latitude,Toast.LENGTH_LONG).show();
        location.getProvider();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged: "+provider+" "+String.valueOf(status));
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled: "+provider);

    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled: "+provider);

    }


}



