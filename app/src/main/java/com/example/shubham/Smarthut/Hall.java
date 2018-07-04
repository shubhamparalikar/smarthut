package com.example.shubham.Smarthut;





import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Hall.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Hall#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Hall extends Fragment {
    ToggleButton t1h,t2h,f1h,f2h,b1h,b2h,acpower;
    RadioButton f1s1h,f1s2h,f1s3h,f1s4h,f2s1h,f2s2h,f2s3h,f2s4h;
    Button mode,plusac,minusac;
    TextView temphall;
        int temperature=14;
    SharedPreferences sharedpref;
   String address;


    View myView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Hall.
     */
    // TODO: Rename and change types and number of parameters
    public  Hall newInstance(String param1, String param2) {
        Hall fragment = new Hall();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Hall() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("track", "onCreate ");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        ConnectivityManager connmgr=(ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni=connmgr.getActiveNetworkInfo();
        if(ni!=null&&ni.isConnected()){
            Toast.makeText(getActivity(),"Internet connection detected.. you can control your devices now..!!",Toast.LENGTH_LONG).show();

        }
        else
        {
            Toast.makeText(getActivity(),"Sorry!!  no internet connection.. \n you cannot control your devices!!",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("track", "onCreateView ");
        // Inflate the layout for this fragment
        myView=inflater.inflate(R.layout.fragment_hall, container, false);
        t1h=(ToggleButton)myView.findViewById(R.id.t1hall);
        t2h=(ToggleButton)myView.findViewById(R.id.t2hall);
        f1h=(ToggleButton)myView.findViewById(R.id.f1hall);
        f2h=(ToggleButton)myView.findViewById(R.id.f2hall);
        b1h=(ToggleButton)myView.findViewById(R.id.b1hall);
        b2h=(ToggleButton)myView.findViewById(R.id.b2halll);
        f1s1h=(RadioButton)myView.findViewById(R.id.f1s1h);
        f1s2h=(RadioButton)myView.findViewById(R.id.f1s2h);
        f1s3h=(RadioButton)myView.findViewById(R.id.f1s3h);
        f1s4h=(RadioButton)myView.findViewById(R.id.f1s4h);
        f2s1h=(RadioButton)myView.findViewById(R.id.f2s1h);
        f2s2h=(RadioButton)myView.findViewById(R.id.f2s2h);
        f2s3h=(RadioButton)myView.findViewById(R.id.f2s3h);
        f2s4h=(RadioButton)myView.findViewById(R.id.f2s4h);
        acpower=(ToggleButton)myView.findViewById(R.id.powerachall);
        mode=(Button)myView.findViewById(R.id.modeachall);
        plusac=(Button)myView.findViewById(R.id.plusachall);
        minusac=(Button)myView.findViewById(R.id.minusachall);
        temphall=(TextView)myView.findViewById(R.id.temphall);






        sharedpref = this.getActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        if (sharedpref.contains("t1hState")){
            t1h.setChecked(sharedpref.getBoolean("t1hState", false));
        }

        if (sharedpref.contains("t2hState")){
            t2h.setChecked(sharedpref.getBoolean("t2hState", true));
        }

        if (sharedpref.contains("f1hState")){
            f1h.setChecked(sharedpref.getBoolean("f1hState", true));
            if(!f1h.isChecked()){
                f1s1h.setVisibility(View.INVISIBLE);
                f1s2h.setVisibility(View.INVISIBLE);
                f1s3h.setVisibility(View.INVISIBLE);
                f1s4h.setVisibility(View.INVISIBLE);
            }
        }
        if (sharedpref.contains("f2hState")){
            f2h.setChecked(sharedpref.getBoolean("f2hState",true));
            if(!f2h.isChecked()){
                f2s1h.setVisibility(View.INVISIBLE);
                f2s2h.setVisibility(View.INVISIBLE);
                f2s3h.setVisibility(View.INVISIBLE);
                f2s4h.setVisibility(View.INVISIBLE);
            }
        }
        if (sharedpref.contains("b1hState")){
            b1h.setChecked(sharedpref.getBoolean("b1hState", true));
        }
        if (sharedpref.contains("b2hState")){
            b2h.setChecked(sharedpref.getBoolean("b2hState",true));
        }

       /* plusac.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();

                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                }
                return false;
            }
        });
        */
        t1h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedpref.edit();
                    showdelay();

                if (isChecked) {
                    editor.putBoolean("t1hState", t1h.isChecked());
                    String s = "a1";
                    Log.d("track", "onCheckedChanged " + isChecked);

                    //manageConnectedSocket.write(s.getBytes());  //method to turn on
                    MainActivity.sendMessage(s);
                } else {
                    editor.putBoolean("t1hState", t1h.isChecked());
                    String s = "a2";
                    Log.d("track", "onCheckedChanged " + isChecked);

                      //method to turn on
                   // manageConnectedSocket.write(s.getBytes());  //method to turn on
                    MainActivity.sendMessage(s);

                }
                editor.commit();
            }
        });

        t2h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 SharedPreferences.Editor editor = sharedpref.edit();
                showdelay();
                if (isChecked) {
                     editor.putBoolean("t2hState", t2h.isChecked());
                    String s = "3";
                    Log.d("track", "onCheckedChanged " + isChecked);

                    // manageConnectedSocket.write(s.getBytes());  //method to turn on
                    MainActivity.sendMessage(s);
                } else {
                     editor.putBoolean("t2hState", t2h.isChecked());
                    String s = "4";
                    Log.d("track", "onCheckedChanged " + isChecked);
                   // manageConnectedSocket.write(s.getBytes());  //method to turn on
                    MainActivity.sendMessage(s);
                }
                 editor.commit();
            }
        });
        b1h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedpref.edit();
                showdelay();
                if (isChecked) {
                    editor.putBoolean("b1hState", b1h.isChecked());
                    String s = "5";
                    Log.d("track", "onCheckedChanged " + isChecked);

                    //manageConnectedSocket.write(s.getBytes());  //method to turn on
                    MainActivity.sendMessage(s);
                } else {
                    editor.putBoolean("b1hState", b1h.isChecked());
                    String s = "6";
                    Log.d("track", "onCheckedChanged " + isChecked);
                    // manageConnectedSocket.write(s.getBytes());  //method to turn on
                    MainActivity.sendMessage(s);
                }
                editor.commit();
            }
        });
        b2h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedpref.edit();
                showdelay();
                if (isChecked) {
                    editor.putBoolean("b2hState", b2h.isChecked());
                    String s = "7";
                    Log.d("track", "onCheckedChanged " + isChecked);

                  //  manageConnectedSocket.write(s.getBytes());  //method to turn on
                    MainActivity.sendMessage(s);
                } else {
                    editor.putBoolean("b2hState", b2h.isChecked());
                    String s = "8";
                    Log.d("track", "onCheckedChanged " + isChecked);
                    //manageConnectedSocket.write(s.getBytes());  //method to turn on
                    MainActivity.sendMessage(s);
                }
                editor.commit();
            }
        });


        f1h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedpref.edit();
                showdelay();
                if (isChecked) {

                    f1s1h.setVisibility(View.VISIBLE);
                    f1s2h.setVisibility(View.VISIBLE);
                    f1s3h.setVisibility(View.VISIBLE);
                    f1s4h.setVisibility(View.VISIBLE);

                    if (sharedpref.contains(("f1s1"))) {

                        f1s1h.setChecked(sharedpref.getBoolean("f1s1",true));
                    }
                    if (sharedpref.contains(("f1s2"))) {

                        f1s2h.setChecked(sharedpref.getBoolean("f1s2",true));
                    }
                    if (sharedpref.contains(("f1s3"))) {

                        f1s3h.setChecked(sharedpref.getBoolean("f1s3",true));
                    }
                    if (sharedpref.contains(("f1s4"))) {

                        f1s4h.setChecked(sharedpref.getBoolean("f1s4",true));
                    }


                    editor.putBoolean("f1hState", f1h.isChecked());
                    String s = "9";
                    Log.d("track", "onCheckedChanged " + isChecked);

                    MainActivity.sendMessage(s);
                } else {

                    f1s1h.setVisibility(View.INVISIBLE);
                    f1s2h.setVisibility(View.INVISIBLE);
                    f1s3h.setVisibility(View.INVISIBLE);
                    f1s4h.setVisibility(View.INVISIBLE);

                    editor.putBoolean("f1hState", f1h.isChecked());
                    String s = "10";
                    Log.d("track", "onCheckedChanged " + isChecked);
                    //manageConnectedSocket.write(s.getBytes());  //method to turn on
                    MainActivity.sendMessage(s);
                }
                editor.commit();
            }

        });
        f2h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedpref.edit();
                showdelay();
                if (isChecked) {

                    f2s1h.setVisibility(View.VISIBLE);
                    f2s2h.setVisibility(View.VISIBLE);
                    f2s3h.setVisibility(View.VISIBLE);
                    f2s4h.setVisibility(View.VISIBLE);

                    if (sharedpref.contains(("f2s1"))) {

                        f2s1h.setChecked(sharedpref.getBoolean("f2s1",false));
                    }
                    if (sharedpref.contains(("f2s2"))) {

                        f2s2h.setChecked(sharedpref.getBoolean("f2s2",false));
                    }
                    if (sharedpref.contains(("f2s3"))) {

                        f2s3h.setChecked(sharedpref.getBoolean("f2s3",false));
                    }
                    if (sharedpref.contains(("f2s4"))) {

                        f2s4h.setChecked(sharedpref.getBoolean("f2s4",false));
                    }
                    editor.putBoolean("f2hState", f2h.isChecked());
                    String s = "11";
                    Log.d("track", "onCheckedChanged " + isChecked);

                  // MainActivity.sendMessage(s);
                    MainActivity.sendMessage(s);
                } else {

                    f2s1h.setVisibility(View.INVISIBLE);
                    f2s2h.setVisibility(View.INVISIBLE);
                    f2s3h.setVisibility(View.INVISIBLE);
                    f2s4h.setVisibility(View.INVISIBLE);
                    editor.putBoolean("f2hState", f2h.isChecked());
                    String s = "12";
                    Log.d("track", "onCheckedChanged " + isChecked);

                    MainActivity.sendMessage(s);
                }
                editor.commit();
            }
        });


        f1s1h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(f1h.isChecked()==true) {
                     SharedPreferences.Editor editor = sharedpref.edit();
                    if (isChecked) {
                         editor.putBoolean("f1s1", f1s1h.isChecked());
                        String s = "13";
                       // manageConnectedSocket.write(s.getBytes());  //method to turn on
                        MainActivity.sendMessage(s);
                        f1s2h.setChecked(false);
                        f1s3h.setChecked(false);
                        f1s4h.setChecked(false);

                    }


                    editor.commit();
                }
            }
        });
        f1s2h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(f1h.isChecked()==true) {
                     SharedPreferences.Editor editor = sharedpref.edit();
                    if (isChecked) {
                         editor.putBoolean("f1s2", f1s2h.isChecked());
                        String s = "14";
                        //manageConnectedSocket.write(s.getBytes());  //method to turn on
                        MainActivity.sendMessage(s);
                        f1s1h.setChecked(false);
                        f1s3h.setChecked(false);
                        f1s4h.setChecked(false);

                    }

                     editor.commit();
                }
            }
        });
        f1s3h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(f1h.isChecked()==true) {
                    SharedPreferences.Editor editor = sharedpref.edit();
                    if (isChecked) {
                         editor.putBoolean("f1s3", f1s3h.isChecked());
                        String s = "15";
                        //manageConnectedSocket.write(s.getBytes());  //method to turn on

                        MainActivity.sendMessage(s);
                        f1s1h.setChecked(false);
                        f1s2h.setChecked(false);
                        f1s4h.setChecked(false);

                    }

                    editor.commit();
                }
            }
        });
        f1s4h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(f1h.isChecked()==true) {
                 SharedPreferences.Editor editor = sharedpref.edit();
                    if (isChecked) {
                       editor.putBoolean("f1s4", f1s4h.isChecked());
                        String s = "16";
                      // manageConnectedSocket.write(s.getBytes());  //method to turn on
                        MainActivity.sendMessage(s);
                        f1s1h.setChecked(false);
                        f1s2h.setChecked(false);
                        f1s3h.setChecked(false);

                    }

                  editor.commit();
                }
            }
        });

        f2s1h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(f2h.isChecked()==true) {
                  SharedPreferences.Editor editor = sharedpref.edit();
                    if (isChecked) {
                       editor.putBoolean("f2s1", f2s1h.isChecked());
                        String s = "17";
                        // manageConnectedSocket.write(s.getBytes());  //method to turn on
                        MainActivity.sendMessage(s);
                        f2s2h.setChecked(false);
                        f2s3h.setChecked(false);
                        f2s4h.setChecked(false);

                    }

                     editor.commit();
                }
            }
        });

        f2s2h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(f2h.isChecked()==true) {
                    SharedPreferences.Editor editor = sharedpref.edit();
                    if (isChecked) {
                        editor.putBoolean("f2s2", f2s2h.isChecked());
                        String s = "18";
                        // manageConnectedSocket.write(s.getBytes());  //method to turn on
                        MainActivity.sendMessage(s);
                        f2s1h.setChecked(false);
                        f2s3h.setChecked(false);
                        f2s4h.setChecked(false);

                    }

                    editor.commit();
                }
            }
        });
        f2s3h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(f2h.isChecked()==true) {
                  SharedPreferences.Editor editor = sharedpref.edit();
                    if (isChecked) {
                       editor.putBoolean("f2s3", f2s3h.isChecked());
                        String s = "19";
                        // manageConnectedSocket.write(s.getBytes());  //method to turn on
                        MainActivity.sendMessage(s);
                        f2s1h.setChecked(false);
                        f2s2h.setChecked(false);
                        f2s4h.setChecked(false);

                    }

                   editor.commit();
                }

            }
        });
        f2s4h.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(f2h.isChecked()==true) {
                     SharedPreferences.Editor editor = sharedpref.edit();
                    if (isChecked) {
                      editor.putBoolean("f2s4", f2s4h.isChecked());
                        String s = "20";
                        //manageConnectedSocket.write(s.getBytes());  //method to turn on
                        MainActivity.sendMessage(s);
                        f2s1h.setChecked(false);
                        f2s2h.setChecked(false);
                        f2s3h.setChecked(false);

                    }

                    editor.commit();
                }
            }
        });
        plusac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "21";
               // manageConnectedSocket.write(s.getBytes());
               MainActivity.sendMessage(s);
            if(temperature<=30) {
                temperature++;
                temphall.setText("temp:"+temperature);

            }

            }
        });
       minusac.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               String s = "22";
             //  manageConnectedSocket.write(s.getBytes());
               //MainActivity.sendMessage(s);
                if(temperature>=14) {
                    temperature--;
                    temphall.setText("temp:"+temperature);
                }

            }
        });
        return myView;
    }


    public  void showdelay(){

        final ProgressDialog myPd_ring=ProgressDialog.show(getActivity(), "doing as per your order!", "controlling the lights", true);
        myPd_ring.setCancelable(true);
        new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub


                try
                {
                    Thread.sleep(2000);


                }
                catch(Exception e){}
                // b.dismiss();
            }
        }).start();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), "done ",
                        Toast.LENGTH_SHORT).show();
                myPd_ring.dismiss();

            }
        }, 2000);
    }

    @Override
    public void onResume() {
        Log.d("track", "onResume ");

        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("track", "onPause ");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("track", "onstop ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("track", "ondestroy ");


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


}
