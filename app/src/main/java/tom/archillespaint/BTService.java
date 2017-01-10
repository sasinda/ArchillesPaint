package tom.archillespaint;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BTService extends Activity
{
<<<<<<< HEAD
    TextView myLabel;
    EditText myTextbox;
=======

>>>>>>> origin/master
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
<<<<<<< HEAD
    int counter;
    volatile boolean stopWorker;
    int stretchCount;
=======
    int angle;
    int state; //0=off, 1 = on
    volatile boolean stopWorker;
>>>>>>> origin/master

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bt);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Button openButton = (Button)findViewById(R.id.open);
        Button sendButton = (Button)findViewById(R.id.send);
        Button closeButton = (Button)findViewById(R.id.close);
<<<<<<< HEAD
        myLabel = (TextView)findViewById(R.id.label);
        myTextbox = (EditText)findViewById(R.id.entry);
=======
>>>>>>> origin/master

        //Open Button
        openButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try
                {
                    findBT();
                    openBT();
                }
                catch (IOException ex) { }
            }
        });

        //Send Button
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try
                {
                    sendData();
                }
                catch (IOException ex) { }
            }
        });

        //Close button
        closeButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                try
                {
                    closeBT();
                }
                catch (IOException ex) { }
            }
        });
    }

    void findBT()
    {

        if(mBluetoothAdapter == null)
        {
<<<<<<< HEAD
            myLabel.setText("No bluetooth adapter available");
=======
//            myLabel.setText("No bluetooth adapter available");
>>>>>>> origin/master
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                System.out.println("________________________");
                System.out.println(device.getName());
                if(device.getName().equals("Tom17"))
                {
                    mmDevice = device;
                    break;
                }
            }
        }
<<<<<<< HEAD
        myLabel.setText("Bluetooth Device Found");
=======
//        myLabel.setText("Bluetooth Device Found");
>>>>>>> origin/master
    }

    void openBT() throws IOException
    {

        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();

<<<<<<< HEAD
        myLabel.setText("Bluetooth Opened");
=======
//        myLabel.setText("Bluetooth Opened");
>>>>>>> origin/master
    }

    void beginListenForData()
    {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable()
        {
            public void run()
            {
                while(!Thread.currentThread().isInterrupted() && !stopWorker)
                {
                    try
                    {
                        int bytesAvailable = mmInputStream.available();
                        if(bytesAvailable > 0)
                        {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for(int i=0;i<bytesAvailable;i++)
                            {
                                byte b = packetBytes[i];
                                if(b == delimiter)
                                {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;
<<<<<<< HEAD

                                    counter+=Integer.parseInt(data);
                                    System.out.println(counter);
=======
                                    if (data=="101"){
                                        state=1;
                                        angle=100;
                                    }else {
                                        state=0;
                                        angle=0;
                                    }
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
//                                    counter+=Integer.parseInt(data);

>>>>>>> origin/master
//                                    handler.post(new Runnable()
//                                    {
//                                        public void run()
//                                        {
//                                            myLabel.setText(data);
//                                        }
//                                    });
                                }
                                else
                                {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }
                    catch (IOException ex)
                    {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    void sendData() throws IOException
    {
<<<<<<< HEAD
        String msg = myTextbox.getText().toString();
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        myLabel.setText("Data Sent");
=======
//        String msg = myTextbox.getText().toString();
//        msg += "\n";
//        mmOutputStream.write(msg.getBytes());
//        myLabel.setText("Data Sent");
>>>>>>> origin/master
    }

    void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
<<<<<<< HEAD
        myLabel.setText("Bluetooth Closed");
=======
//        myLabel.setText("Bluetooth Closed");
>>>>>>> origin/master
    }
}