package com.example.bk_home_smarter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bk_home_smarter.src.models.Device;

import java.util.List;

public class DeviceAdapter extends BaseAdapter {

    Context context;
    int layout;
    List<Device> listDevice;
    private int position;
    private View convertView;
    private ViewGroup parent;

    public DeviceAdapter(Context context, int layout, List<Device> listDevice){
        this.context = context;
        this.layout = layout;
        this.listDevice = listDevice;
    }

    @Override
    public int getCount() {
        return listDevice == null ? 0 : listDevice.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(layout, null);


        // Map to view
        TextView txtDeviceName = convertView.findViewById(R.id.livingroomDvName);
        ImageView imgDeviceImage = convertView.findViewById(R.id.livingroomDvImg);

        // Pass value to view
        Device device = listDevice.get(position);
        txtDeviceName.setText(device.deviceName);

        if (device.deviceType.equals("light")){
            imgDeviceImage.setImageResource(R.drawable.light);
        }
        else {
            imgDeviceImage.setImageResource(R.drawable.fan);
        }

        return convertView;
    }
}
