package com.example.democlient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myfirstapp.R;

public class FirstFragment extends Fragment {

    TextView showCountTextView;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View fragmentFirstLayout = inflater.inflate(R.layout.fragment_first, container, false);
        // Get the count text view
        return fragmentFirstLayout;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.light_switch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((Switch) v).isChecked();
                if (checked){
                    ((MainActivity)getActivity()).sendDataMQTT("ON");
                }
                else{
                    ((MainActivity)getActivity()).sendDataMQTT("OFF");
                }
//                Intent intent = new Intent(getActivity(), ShowListQuizActivity.class);
//                Intent intent = new Intent(getActivity(), MainActivity2.class);
//                startActivity(intent);
            }
        });
    }
}