package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.util.Log;


import java.io.IOException;
import java.util.UUID;

public class standard extends AppCompatActivity {
    RadioButton String1, String2, String3, String4, String5, String6;
    Button On, Off, Discnt, Abt;
    String address = null;
    String compare;
    int comparison;
    int TargetELow = 77;
    int TargetEHigh = 89;
    int TargetALow = 104;
    int TargetAHigh = 111;
    int TargetDLow = 142;
    int TargetDHigh = 149;
    int TargetGLow = 192;
    int TargetGHigh = 198;
    int TargetBLow = 242;
    int TargetBHigh = 247;
    int TargetEHIGHLow = 324;
    int TargetEHIGHHigh = 329;
    private ProgressDialog progress;
    private RadioGroup radioGroup;
    private RadioButton radioSelected;
    private ImageButton sendData;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    boolean tuned;
    private Recorder recorder;
    private AudioCalculator audioCalculator;
    private Handler handler;
    private Handler tuningHandle;
    private Runnable tuneRun;
    double frequency;
    int[] data = new int[1000];
    private TextView textFrequency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard);
        getSupportActionBar().hide();

        Intent newint = getIntent();
        address = newint.getStringExtra(bt_device.EXTRA_ADDRESS); //receive the address of the bluetooth device

        addListenerOnButton();

        Discnt =(Button)findViewById(R.id.button3);

        new ConnectBT().execute(); //Call the class to connect

        //commands to be sent to bluetooth

        Discnt.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });

        recorder = new Recorder(callback);
        audioCalculator = new AudioCalculator();
        handler = new Handler(Looper.getMainLooper());

        /*
        tuningHandle = new Handler();
        tuneRun = new Runnable() {
            @Override
            public void run() {
                compare = textFrequency.getText().toString();
            }
        };

        Thread Comp = new Thread(){
          public void run(){
              while(!tuned){

              }
          }
        };
*/


        textFrequency = (TextView) findViewById(R.id.textFrequency);

    }

    protected Callback callback = new Callback() {

        @Override
        public void onBufferAvailable(byte[] buffer) {
            audioCalculator.setBytes(buffer);
            frequency = audioCalculator.getFrequency();

            final String hz = String.valueOf(frequency + " Hz");

            handler.post(new Runnable() {
                @Override
                public void run() {
                    textFrequency.setText(hz);
                }
            });
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        recorder.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        recorder.stop();
    }

    public void addListenerOnButton()
    {
        radioGroup = (RadioGroup) findViewById(R.id.selectedString);
        sendData = (ImageButton) findViewById(R.id.imageButton);

        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectId = radioGroup.getCheckedRadioButtonId();
                radioSelected = (RadioButton) findViewById(selectId);
                Log.d("ADebugTag", "Value: " + (selectId));
                if(selectId == 2131230914){
                    //sendOne();
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(standard.this);
                    a_builder.setMessage("To Tune string E, press OK and pluck the guitar string.")
                        .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //CRASHES
                                    //comparison = Integer.parseInt(compare);
                                    /*Handler hand = new Handler();
                                    Runnable run = new Runnable() {
                                        @Override
                                        public void run() {
                                        }
                                    };
                                    hand.postDelayed(run, 3000);
                                    */
                                    SystemClock.sleep(1900);

                                    loopInAnotherThread();

                                    //double sum = 0;
                                    //for (double d : data) sum += d;

                                    //double average = 1.0d * sum / data.length;
                                    double average;
                                    average = calculateMode(data);

                                    Log.d("ADebugTag", "Value: " + (average));
                                    //while(!tuned) {
                                        if(average < TargetELow){
                                            //Increase freq

                                            sendOne();
                                            tuningLow();
                                        } else if(average > TargetEHigh){
                                            //Decrease freq
                                            sendTwo();
                                            tuningHigh();
                                        } else if ((TargetELow < average) && (average < TargetEHigh)){
                                            //tuned = true;
                                            sendThree();
                                            tuningMessage();
                                        }
                                    //}

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("String Select");
                    alert.show();
                } else if(selectId == 2131230915) {
                    //sendTwo();
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(standard.this);
                    a_builder.setMessage("To Tune string A, press OK and pluck the guitar string.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //CRASHES
                                    //comparison = Integer.parseInt(compare);
                                    /*Handler hand = new Handler();
                                    Runnable run = new Runnable() {
                                        @Override
                                        public void run() {
                                        }
                                    };
                                    hand.postDelayed(run, 3000);
                                    */
                                    SystemClock.sleep(1900);

                                    loopInAnotherThread();

                                    //double sum = 0;
                                    //for (double d : data) sum += d;

                                    //double average = 1.0d * sum / data.length;
                                    double average;
                                    average = calculateMode(data);

                                    Log.d("ADebugTag", "Value: " + (average));
                                    //while(!tuned) {
                                    if(average < TargetALow){
                                        //Increase freq

                                        sendOne();
                                        tuningLow();
                                    } else if(average > TargetAHigh){
                                        //Decrease freq
                                        sendTwo();
                                        tuningHigh();
                                    } else if ((TargetALow < average) && (average < TargetAHigh)){
                                        //tuned = true;
                                        sendThree();
                                        tuningMessage();
                                    }
                                    //}

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("String Select");
                    alert.show();
                } else if(selectId == 2131230916) {
                    //sendThree();
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(standard.this);
                    a_builder.setMessage("To Tune string D, press OK and pluck the guitar string.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //CRASHES
                                    //comparison = Integer.parseInt(compare);
                                    /*Handler hand = new Handler();
                                    Runnable run = new Runnable() {
                                        @Override
                                        public void run() {
                                        }
                                    };
                                    hand.postDelayed(run, 3000);
                                    */
                                    SystemClock.sleep(1900);

                                    loopInAnotherThread();

                                    //double sum = 0;
                                    //for (double d : data) sum += d;

                                    //double average = 1.0d * sum / data.length;
                                    double average;
                                    average = calculateMode(data);

                                    Log.d("ADebugTag", "Value: " + (average));
                                    //while(!tuned) {
                                    if(average < TargetDLow){
                                        //Increase freq

                                        sendOne();
                                        tuningLow();
                                    } else if(average > TargetDHigh){
                                        //Decrease freq
                                        sendTwo();
                                        tuningHigh();
                                    } else if ((TargetDLow < average) && (average < TargetDHigh)){
                                        //tuned = true;
                                        sendThree();
                                        tuningMessage();
                                    }
                                    //}

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("String Select");
                    alert.show();
                } else if(selectId == 2131230917) {
                    //sendFour();
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(standard.this);
                    a_builder.setMessage("To Tune string G, press OK and pluck the guitar string.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //CRASHES
                                    //comparison = Integer.parseInt(compare);
                                    /*Handler hand = new Handler();
                                    Runnable run = new Runnable() {
                                        @Override
                                        public void run() {
                                        }
                                    };
                                    hand.postDelayed(run, 3000);
                                    */
                                    SystemClock.sleep(1900);

                                    loopInAnotherThread();

                                    //double sum = 0;
                                    //for (double d : data) sum += d;

                                    //double average = 1.0d * sum / data.length;
                                    double average;
                                    average = calculateMode(data);

                                    Log.d("ADebugTag", "Value: " + (average));
                                    //while(!tuned) {
                                    if(average < TargetGLow){
                                        //Increase freq

                                        sendTwo();
                                        tuningLow();
                                    } else if(average > TargetGHigh){
                                        //Decrease freq
                                        sendOne();
                                        tuningHigh();
                                    } else if ((TargetGLow < average) && (average < TargetGHigh)){
                                        //tuned = true;
                                        sendThree();
                                        tuningMessage();
                                    }
                                    //}

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("String Select");
                    alert.show();
                } else if(selectId == 2131230918) {
                    //sendFive();
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(standard.this);
                    a_builder.setMessage("To Tune string B, press OK and pluck the guitar string.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //CRASHES
                                    //comparison = Integer.parseInt(compare);
                                    /*Handler hand = new Handler();
                                    Runnable run = new Runnable() {
                                        @Override
                                        public void run() {
                                        }
                                    };
                                    hand.postDelayed(run, 3000);
                                    */
                                    SystemClock.sleep(1900);

                                    loopInAnotherThread();

                                    //double sum = 0;
                                    //for (double d : data) sum += d;

                                    //double average = 1.0d * sum / data.length;
                                    double average;
                                    average = calculateMode(data);

                                    Log.d("ADebugTag", "Value: " + (average));
                                    //while(!tuned) {
                                    if(average < TargetBLow){
                                        //Increase freq

                                        sendTwo();
                                        tuningLow();
                                    } else if(average > TargetBHigh){
                                        //Decrease freq
                                        sendOne();
                                        tuningHigh();
                                    } else if ((TargetBLow < average) && (average < TargetBHigh)){
                                        //tuned = true;
                                        sendThree();
                                        tuningMessage();
                                    }
                                    //}

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("String Select");
                    alert.show();
                } else if(selectId == 2131230919) {
                    //sendSix();
                    AlertDialog.Builder a_builder = new AlertDialog.Builder(standard.this);
                    a_builder.setMessage("To Tune string E High, press OK and pluck the guitar string.")
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    //CRASHES
                                    //comparison = Integer.parseInt(compare);
                                    /*Handler hand = new Handler();
                                    Runnable run = new Runnable() {
                                        @Override
                                        public void run() {
                                        }
                                    };
                                    hand.postDelayed(run, 3000);
                                    */
                                    SystemClock.sleep(1900);

                                    loopInAnotherThread();

                                    //double sum = 0;
                                    //for (double d : data) sum += d;

                                    //double average = 1.0d * sum / data.length;
                                    double average;
                                    average = calculateMode(data);

                                    Log.d("ADebugTag", "Value: " + (average));
                                    //while(!tuned) {
                                    if(average < TargetEHIGHLow){
                                        //Increase freq

                                        sendTwo();
                                        tuningLow();
                                    } else if(average > TargetEHIGHHigh){
                                        //Decrease freq
                                        sendOne();
                                        tuningHigh();
                                    } else if ((TargetEHIGHLow < average) && (average < TargetEHIGHHigh)){
                                        //tuned = true;
                                        sendThree();
                                        tuningMessage();
                                    }
                                    //}

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = a_builder.create();
                    alert.setTitle("String Select");
                    alert.show();
                }
            }
        });

    }


    private void loopInAnotherThread() {
        new Thread(new Runnable() {
            public void run() {
                // Your loop
                for(int i = 0; i < 1000; i++){
                    data[i] = 0;
                }
                for(int i = 0; i < 1000; i++){
                    int temp = (int) frequency;
                    data[i] = temp;
                }
            }
        }).start();

    }

    public static int calculateMode(int[] arr)
    {

        int modeCount = 0;	// The count of the mode value
        int mode = 0;		// The value of the mode

        int currCount = 0;
        int currElement;

        // Iterate through all values in our array and consider it as a possible mode
        for (int candidateMode : arr)
        {
            // Reset the number of times we have seen the current value
            currCount = 0;

            // Iterate through the array counting the number of times we see the current candidate mode
            for (int element : arr)
            {
                // If they match, increment the current count
                if (candidateMode == element)
                {
                    currCount++;
                }
            }

            // We only save this candidate mode, if its count is greater than the current mode
            // we have stored in the "mode" variable
            if (currCount > modeCount)
            {
                modeCount = currCount;
                mode = candidateMode;
            }
        }

        return mode;
    }

    private void tuningMessage()
    {
        AlertDialog.Builder msg_builder = new AlertDialog.Builder(standard.this);
        msg_builder.setMessage("The string is now tuned, proceed to the next string.");
        AlertDialog msg = msg_builder.create();
        msg.setTitle("Tuned");
        msg.show();
    }

    private void tuningLow()
    {
        AlertDialog.Builder msg_builder = new AlertDialog.Builder(standard.this);
        msg_builder.setMessage("String is Low, please wait while the tuner adjusts.");
        AlertDialog msg = msg_builder.create();
        msg.setTitle("Tuning Low");
        msg.show();
    }

    private void tuningHigh()
    {
        AlertDialog.Builder msg_builder = new AlertDialog.Builder(standard.this);
        msg_builder.setMessage("String is High, please wait while the tuner adjusts.");
        AlertDialog msg = msg_builder.create();
        msg.setTitle("Tuning High");
        msg.show();
    }

    private void sendOne()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("a".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void sendTwo()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("b".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void sendThree()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("c".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void sendFour()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("E4".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void sendFive()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("E5".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void sendSix()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("E6".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }
    /*
    private void turnOffLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("0".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void turnOnLed()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("5".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }
    */
    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
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



    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(standard.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}

