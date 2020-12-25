package ir.pepotec.app.bluetoothremote;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.TimerTask;
import java.util.UUID;

public class ActivityMain extends AppCompatActivity implements ConnectedThread.DataReceived {
    private Handler handler = new Handler();
    BluetoothSocket bluetoothSocket = null;
    public ConnectedThread connectedThread = new ConnectedThread(bluetoothSocket, this);
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier
    ObjectAnimator rotation;
    Fragment fragment;
    Dialog dialog;
    public SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        replaceView(new FragmentBlutooth());
        preferences = this.getSharedPreferences("MG", MODE_PRIVATE);
    }

    public void replaceView(Fragment fragment) {
        this.fragment = fragment;
        if (fragment instanceof FragmentBlutooth) {
            ((FragmentBlutooth) fragment).context = this;
            try {
                connectedThread.cancel();
            }catch(Exception ignored){}
        }
        else
            ((FragmentControl) fragment).context = this;

        this.getSupportFragmentManager().beginTransaction().replace(R.id.contentMain, fragment).commit();
    }

    private void showMessage(final String message) {
        handler.post(new TimerTask() {
            @Override
            public void run() {
                dialog.cancel();
                try{
                    connectedThread.cancel();
                }catch(Exception ignored){}
                Toast.makeText(ActivityMain.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void connectedToDevice() {
        handler.post(new TimerTask() {
            @Override
            public void run() {
                dialog.cancel();
                replaceView(new FragmentControl());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && fragment instanceof FragmentBlutooth) {
            ((FragmentBlutooth) fragment).blueOned();
        } else
            Toast.makeText(this, " Error,Blue Not Enabling ! ", Toast.LENGTH_SHORT).show();

    }


    public void connectToDevice(final BluetoothDevice device) {
        progressDialog();
        new Thread() {
            public void run() {
                try {
                    bluetoothSocket = createBluetoothSocket(device);
                    try {
                        bluetoothSocket.connect();

                        connectedThread = new ConnectedThread(bluetoothSocket, ActivityMain.this);
                        connectedThread.start();
                        connectedToDevice();


                    } catch (Exception e) {
                        showMessage(" Error ! ");
                        try {
                            bluetoothSocket.close();

                        } catch (IOException ignored) {

                        }
                    }

                } catch (IOException e) {
                    showMessage(" Error ! ");
                }
            }
        }.start();
    }

    private void progressDialog() {
        dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_progress, null, false);
        ImageView img = view.findViewById(R.id.imgDialog);
        setAnimation(img);
        dialog.setContentView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    private void setAnimation(View view) {
        float y = view.getY();
        ObjectAnimator translation = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, y, 250, y);
        translation.setDuration(1000);
        translation.setRepeatCount(Animation.INFINITE);
        translation.start();
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BTMODULEUUID);
        } catch (Exception ignored) {

        }
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }


    @Override
    public void dataFromDrone(final String JData) {
        handler.post(new TimerTask() {
            @Override
            public void run() {
                if (fragment instanceof FragmentControl)
                    ((FragmentControl) fragment).setData(JData);
            }
        });

    }
}
