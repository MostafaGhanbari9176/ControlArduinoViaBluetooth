package ir.pepotec.app.bluetoothremote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.TimerTask;
import java.util.UUID;

public class ActivityMain extends AppCompatActivity implements View.OnClickListener, AdapterDeviceList.OnDeviceClicked {

    TextView txtStatus, txtEmpty;
    ImageView btnUp, btnDown, btnRight, btnLeft, imgStatus;
    RecyclerView deviceList;
    SeekBar seekBar;

    private Handler handler = new Handler();
    BluetoothAdapter bluetoothAdapter;
    BluetoothSocket bluetoothSocket = null;
    private ConnectedThread connectedThread = new ConnectedThread(bluetoothSocket);
    Set<BluetoothDevice> devices;
    ArrayList<DeviceModel> devicesSource = new ArrayList<>();
    AdapterDeviceList adapter;

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier
    final int ReqForEnablingBluetooth = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        checkBluetoothStatus();
    }

    private void showMessage(final String message){
        handler.post(new TimerTask() {
            @Override
            public void run() {
                Toast.makeText(ActivityMain.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeIamgeState(){
        handler.post(new TimerTask() {
            @Override
            public void run() {
                imgStatus.setImageResource(R.drawable.ic_bluetooth_connected);
                txtStatus.setText("متصل");
            }
        });
    }

    private void checkBluetoothStatus() {
        if (bluetoothAdapter.isEnabled()) {
            imgStatus.setImageResource(R.drawable.ic_bluetooth);
            txtStatus.setText("بلوتوث روشن");
            txtStatus.setHint("on");

            settingUpList();

        } else {
            imgStatus.setImageResource(R.drawable.ic_bluetooth_disabled);
            txtStatus.setText("بلوتوث خاموش");
            txtStatus.setHint("off");
            devicesSource.clear();
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
    }

    private void settingUpList() {

        createSource();
        if (devicesSource.size() == 0) {
            txtEmpty.setVisibility(View.VISIBLE);
            if (adapter != null)
                adapter.notifyDataSetChanged();
            return;
        }
        txtEmpty.setVisibility(View.GONE);
        adapter = new AdapterDeviceList(this, this, devicesSource);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        deviceList.setLayoutManager(manager);
        deviceList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void createSource() {
        devices = bluetoothAdapter.getBondedDevices();
        for (BluetoothDevice data : devices) {
            DeviceModel model = new DeviceModel();
            model.dName = data.getName();
            model.dId = data.getAddress();
            devicesSource.add(model);
        }
    }

    private void init() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        txtStatus = findViewById(R.id.txtStatus);
        txtEmpty = findViewById(R.id.txtEmpty);
        btnDown = findViewById(R.id.imgDown);
        btnLeft = findViewById(R.id.imgLeft);
        btnRight = findViewById(R.id.imgRight);
        btnUp = findViewById(R.id.imgUp);
        imgStatus = findViewById(R.id.imgStatus);
        deviceList = findViewById(R.id.RVDevice);
        seekBar = findViewById(R.id.seek);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                connectedThread.write(i+"");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnUp.setOnClickListener(this);
        btnDown.setOnClickListener(this);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);

        imgStatus.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgUp:
                connectedThread.write("up");
                break;
            case R.id.imgDown:
                connectedThread.write("Down");
                break;
            case R.id.imgRight:
                connectedThread.write("Right");
                break;
            case R.id.imgLeft:
                connectedThread.write("Left");
                break;
            case R.id.imgStatus:
                changeBlueToothStatus();
                break;
        }
    }

    private void changeBlueToothStatus() {
        if (txtStatus.getHint().equals("on")) {
            bluetoothAdapter.disable();
            imgStatus.setImageResource(R.drawable.ic_bluetooth_disabled);
            txtStatus.setText("بلوتوث خاموش");
            txtStatus.setHint("off");
            devicesSource.clear();
            if (adapter != null)
                adapter.notifyDataSetChanged();
        } else {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, ReqForEnablingBluetooth);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ReqForEnablingBluetooth) {
            if (resultCode == RESULT_OK) {
                imgStatus.setImageResource(R.drawable.ic_bluetooth);
                txtStatus.setText("بلوتوث روشن");
                txtStatus.setHint("on");
                settingUpList();
            } else
                Toast.makeText(this, " Error ! ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void deviceChosed(int position) {

        connectToDevice(devicesSource.get(position).dId);
    }

    private void connectToDevice(final String dId) {
        new Thread()
        {
            public void run() {
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(dId);
        try {
            bluetoothSocket = createBluetoothSocket(device);
            try{
            bluetoothSocket.connect();

                connectedThread = new ConnectedThread(bluetoothSocket);
                connectedThread.start();
                changeIamgeState();


            }catch (Exception e){
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

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BTMODULEUUID);
        } catch (Exception ignored) {

        }
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }
}
