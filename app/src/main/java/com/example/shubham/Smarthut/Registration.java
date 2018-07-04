package com.example.shubham.Smarthut;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import java.io.IOException;

public class Registration extends Activity {
    EditText id;
    Button submit;
    String uid = null;
    Context context;
    String regid;
    GoogleCloudMessaging gcm;
    String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        //checking if the use is already registered
        if(isUserRegistered(context)){
            Log.d("track", "onCreate in if condition");

            startActivity(new Intent(Registration.this,MainActivity.class));
            finish();

        }
        else {
            id = (EditText) findViewById(R.id.uid);
            submit = (Button) findViewById(R.id.submit);

            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uid=id.getText().toString();

                    if(uid.isEmpty()){
                        Log.d("track", "Please input unique id ");
                        Toast.makeText(Registration.this,"PLEASE INPUT YOUR UNIQUE ID!",Toast.LENGTH_LONG).show();
                    }
                    else{
                        showDelay();
                        Log.d("track", "getting registered in bg ");
                        registerInBackground();             // getting reg id from gcm in registerInBackground
                    }
                }
            });


            // Check device for Play Services APK. If check succeeds, proceed with
            //  GCM registration.

            if (checkPlayServices()) {
                regid="";
                gcm = GoogleCloudMessaging.getInstance(this);
                regid = getRegistrationId(context);

            } else {
                Log.i("track", "No valid Google Play Services APK found.");
            }
        }
    }
    private boolean isUserRegistered(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String User_name = prefs.getString(Util.PROPERTY_REG_ID, "");
        if (User_name.isEmpty()) {
            Log.i("track", "Registration not found.");
            return false;
        }

        return true;
    }
    private SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(MainActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        Util.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("track", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(Util.PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i("track", "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.


        return registrationId;
    }

    private void sendRegistrationIdToBackend(String uid) {
        // Your implementation here.

        Log.d("track", "sendRegistrationIdToBackend called ");
        new SendGcmToServer().execute(uid);
    }

    private class SendGcmToServer extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();


        }

        @Override
        protected Void doInBackground(String... params) {
            Log.d("track", "doInBackground of send regidtobg" + uid + regid);
            // TODO Auto-generated method stub
            if(uid!=null) {
                Pubnub pubnub = new Pubnub("pub-c-090509cd-0e4e-429c-8345-51a9c4a232cc", "sub-c-de29477c-0f60-11e6-8c3e-0619f8945a4f", false);
                pubnub.enablePushNotificationsOnChannel(uid, regid, new com.pubnub.api.Callback() {
                    @Override
                    public void successCallback(String s, Object o) {
                        super.successCallback(s, o);
                        Log.d("track", "successCallback of sending regidto bg"+uid+regid);

                    }

                    @Override
                    public void errorCallback(String s, PubnubError pubnubError) {
                        super.errorCallback(s, pubnubError);
                        Log.d("track", "errorCallback of snding regid to bg");
                    }

                });
            }

return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //Toast.makeText(context,"response "+result,Toast.LENGTH_LONG).show();
            Toast.makeText(Registration.this,"reg id"+regid,Toast.LENGTH_LONG).show();
            if(uid!=null && regid!=null) {
                Intent i = new Intent(Registration.this, MainActivity.class);
                startActivity(i);
            }
            else{
                Toast.makeText(Registration.this,"Something went wrong, please try again!!",Toast.LENGTH_SHORT).show();
            }

        }


    }
    //showing a dialog box while the getting gcm reg id and sending it to pubnub
    public  void showDelay(){

        final ProgressDialog myPd_ring=ProgressDialog.show(Registration.this, "setting up everything", "WAIT!!", true);
        myPd_ring.setCancelable(true);
        new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub


                try
                {
                    Thread.sleep(7000);


                }
                catch(Exception e){}
                // b.dismiss();
            }
        }).start();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Toast.makeText(Registration.this, "done ",
                        Toast.LENGTH_SHORT).show();
                myPd_ring.dismiss();

            }
        }, 5000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_registration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void registerInBackground() {
        new AsyncTask() {



            @Override
            protected String doInBackground(Object[] params) {

                Log.d("track", "doInBackground of register in bg");
                try {

                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(Registration.this);
                    }
                    regid = gcm.register(Util.SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;




                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                Log.d("track", "doInBackground returning" + msg);
                return msg;



            }


        }.execute();

    }
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);


        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Util.PROPERTY_REG_ID, regId);
        editor.commit();
        Log.d("track", "storeRegistrationId " + regId);
        sendRegistrationIdToBackend(uid);
    }
}
