package ir.pepotec.app.bluetoothremote;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

public class FragmentBlutooth  extends Fragment implements AdapterDeviceList.OnDeviceClicked, View.OnClickListener {

    View view;
    TextView txtStatus, txtEmpty;
    ImageView imgStatus;
    RecyclerView deviceList;
    ArrayList<DeviceModel> devicesSource = new ArrayList<>();
    AdapterDeviceList adapter;
    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> devices;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bluetooth,container,false);
        init();
        return view;
    }

    private void init() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        txtStatus = view.findViewById(R.id.txtStatus);
        txtEmpty =view.findViewById(R.id.txtEmpty);
        imgStatus = view.findViewById(R.id.imgStatus);
        deviceList =view.findViewById(R.id.RVDevice);
        imgStatus.setOnClickListener(this);
        checkBluetoothStatus();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imgStatus)
            changeBlueToothStatus();

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
            startActivityForResult(intent, 1);

        }
    }

    public void blueOned(){
        imgStatus.setImageResource(R.drawable.ic_bluetooth);
        txtStatus.setText("بلوتوث روشن");
        txtStatus.setHint("on");
        settingUpList();
    }

    @Override
    public void deviceChosed(int position) {

        ((ActivityMain)context).connectToDevice(bluetoothAdapter.getRemoteDevice(devicesSource.get(position).dId));
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
        adapter = new AdapterDeviceList(this, context, devicesSource);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
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
}
