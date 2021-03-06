package tom.archillespaint;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BTActivity extends FullscreenActivity
{
    TextView myLabel;
    EditText myTextbox;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    byte[] readBuffer;
    int readBufferPosition;
    int angle;
    int state; //0=off, 1 = on
    volatile boolean stopWorker;
    public Button controllerButton;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        controllerButton=(Button)findViewById(R.id.controller);
        myLabel = (TextView)findViewById(R.id.label);
        myTextbox = (EditText)findViewById(R.id.entry);

        //Open Button
//        openButton.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View v)
//            {
//                try
//                {
//                    findBT();
//                    openBT();
//                }
//                catch (IOException ex) { }
//            }
//        });
//
//        //Send Button
//        sendButton.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View v)
//            {
//                try
//                {
//                    sendData();
//                }
//                catch (IOException ex) { }
//            }
//        });
//
//        //Close button
//        closeButton.setOnClickListener(new View.OnClickListener()
//        {
//            public void onClick(View v)
//            {
//                try
//                {
//                    closeBT();
//                }
//                catch (IOException ex) { }
//            }
//        });
    }

    void findBT()
    {

        if(mBluetoothAdapter == null)
        {
            myLabel.setText("No bluetooth adapter available");
            controllerButton.setBackgroundColor(Color.RED);
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
        controllerButton.setBackgroundColor(Color.GREEN);
        myLabel.setText("Bluetooth Device Found");
    }

    void openBT() throws IOException
    {

        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); //Standard SerialPortService ID
        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        beginListenForData();

        myLabel.setText("Bluetooth Opened");
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
        String msg = myTextbox.getText().toString();
        msg += "\n";
        mmOutputStream.write(msg.getBytes());
        myLabel.setText("Data Sent");
    }

    void closeBT() throws IOException
    {
        stopWorker = true;
        mmOutputStream.close();
        mmInputStream.close();
        mmSocket.close();
        myLabel.setText("Bluetooth Closed");
    }
}