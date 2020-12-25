package ir.pepotec.app.bluetoothremote;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class FragmentControl extends Fragment implements View.OnTouchListener, View.OnClickListener {
    View view;
    Context context;
    ImageView btnUp, btnDown, btnRight, btnLeft;
    SeekBar seekBar;
    String up = "0", down = "0", right = "0", left = "0", height = "0";
    public TextView txtSpeed;
    TextView tA, speedA, tB, speedB, tC, speedC, tD, speedD, aX, aY, aZ, cAX, cAY, cAZ;
    ImageButton btnSettings;
    Button btnCalibre, btnReset;
    int steps;
    final String PKey = "max";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_control, container, false);
        setHasOptionsMenu(true);
        init();
        return view;
    }

    private void init() {
        sendMaxTValueToDrone();
        btnDown = view.findViewById(R.id.imgDown);
        btnLeft = view.findViewById(R.id.imgLeft);
        btnRight = view.findViewById(R.id.imgRight);
        btnUp = view.findViewById(R.id.imgUp);
        seekBar = view.findViewById(R.id.seek);
        txtSpeed = view.findViewById(R.id.txtSpeed);
        tA = view.findViewById(R.id.tA);
        tB = view.findViewById(R.id.tB);
        tC = view.findViewById(R.id.tC);
        tD = view.findViewById(R.id.tD);

        aX = view.findViewById(R.id.aX);
        aY = view.findViewById(R.id.aY);
        aZ = view.findViewById(R.id.aZ);

        cAX = view.findViewById(R.id.cAX);
        cAY = view.findViewById(R.id.cAY);
        cAZ = view.findViewById(R.id.cAZ);

        speedA = view.findViewById(R.id.speedA);
        speedB = view.findViewById(R.id.speedB);
        speedC = view.findViewById(R.id.speedC);
        speedD = view.findViewById(R.id.speedD);

        btnSettings = view.findViewById(R.id.btnSettings);
        btnReset = view.findViewById(R.id.btnReset);
        btnCalibre = view.findViewById(R.id.btnCalibre);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                height = i * steps + "";
                txtSpeed.setText(height);
                sendDataToMicro();
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
        btnSettings.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnCalibre.setOnClickListener(this);

        btnUp.setOnTouchListener(this);
        btnDown.setOnTouchListener(this);
        btnLeft.setOnTouchListener(this);
        btnRight.setOnTouchListener(this);

    }

    private void sendMaxTValueToDrone() {
        steps = (255-(((ActivityMain)context).preferences.getInt(PKey,45)))/10;
        ((ActivityMain) context).connectedThread.write(((ActivityMain)context).preferences.getInt(PKey,45)+"");
        }

    private void sendDataToMicro() {

        String JData = "{\"F\": " + up + ",\"B\": " + down + ",\"R\": " + right + ",\"L\": " + left + ",\"S\": " + height + "}";
        ((ActivityMain) context).connectedThread.write(JData);
        //{"F": 0,"B": 0,"R": 0,"L": 0,"S": 10}
        // Toast.makeText(this, JData, Toast.LENGTH_SHORT).show();

    }

    public void setData(String JData) {
        try {
            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(JData);
            aX.setText(object.get("aX").getAsString());
            aY.setText(object.get("aY").getAsString());
            cAX.setText(object.get("cAX").getAsString());
            cAY.setText(object.get("cAY").getAsString());
            tA.setText(object.get("tA").getAsString());
            tB.setText(object.get("tB").getAsString());
            tC.setText(object.get("tC").getAsString());
            tD.setText(object.get("tD").getAsString());
            speedA.setText(object.get("speed").getAsString());
            cAZ.setText(object.get("max").getAsString());
        } catch (Exception ignored) {
            String x = ignored.getMessage();
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.imgUp:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    up = "1";
                    sendDataToMicro();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    up = "0";
                    sendDataToMicro();
                }

                return false;
            case R.id.imgDown:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    down = "1";
                    sendDataToMicro();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    down = "0";
                    sendDataToMicro();
                }
                return false;
            case R.id.imgRight:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    right = "1";
                    sendDataToMicro();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    right = "0";
                    sendDataToMicro();
                }
                return false;
            case R.id.imgLeft:
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    left = "1";
                    sendDataToMicro();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    left = "0";
                    sendDataToMicro();
                }
                return false;
            default:
                return false;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSettings:
                showPopUp();
                break;
            case R.id.btnCalibre:
                ((ActivityMain) context).connectedThread.write("{calibre}");
                seekBar.setProgress(0);
                break;
            case R.id.btnReset:
                ((ActivityMain) context).connectedThread.write("{reset}");
                break;


        }
    }

    private void showPopUp() {
        PopupMenu menu = new PopupMenu(context, btnSettings);
        menu.getMenuInflater().inflate(R.menu.main_menu,menu.getMenu());
        menu.getMenu().findItem(R.id.maxTValue).setTitle(((ActivityMain)context).preferences.getInt(PKey,45)+"");
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.reConnect){
                    ((ActivityMain)context).replaceView(new FragmentBlutooth());
                }
                else{
                    getMaxTValueFromUser();
                }
                return false;
            }
        });
        menu.show();
    }

    private void getMaxTValueFromUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("5~215");
        final EditText text = new EditText(context);
        builder.setPositiveButton("ارسال", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((ActivityMain)context).preferences.edit().putInt(PKey,Integer.parseInt(text.getText().toString())).apply();
                sendMaxTValueToDrone();
            }
        });
        builder.setView(text);
        builder.show();
    }

}
