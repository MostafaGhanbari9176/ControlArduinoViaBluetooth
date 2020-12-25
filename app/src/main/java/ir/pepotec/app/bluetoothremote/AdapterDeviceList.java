package ir.pepotec.app.bluetoothremote;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterDeviceList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnDeviceClicked {
        void deviceChosed(int position);
    }

    private OnDeviceClicked onDeviceClicked;
    private Context context;
    private ArrayList<DeviceModel> source;

    public AdapterDeviceList(OnDeviceClicked onDeviceClicked, Context context, ArrayList<DeviceModel> source) {
        this.onDeviceClicked = onDeviceClicked;
        this.context = context;
        this.source = source;
    }

    private class Holder extends RecyclerView.ViewHolder {
        TextView txtName, txtId;
        CardView item;
        private Holder(@NonNull View v) {
            super(v);
            txtId = v.findViewById(R.id.txtIDItem);
            txtName = v.findViewById(R.id.txtNameItem);
            item = v.findViewById(R.id.Item);
        }

        private void onBind(final int position) {
            DeviceModel data = source.get(position);
            txtName.setText(data.dName);
            txtId.setText(data.dId);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeviceClicked.deviceChosed(position);
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_device, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((Holder) viewHolder).onBind(i);
    }

    @Override
    public int getItemCount() {
        return source.size();
    }
}
