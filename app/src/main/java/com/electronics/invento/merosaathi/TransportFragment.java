package com.electronics.invento.merosaathi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class TransportFragment extends Fragment implements View.OnClickListener{

    Button button_transport_main;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_transport, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchInitialize(view);

        button_transport_main.setOnClickListener(this);
    }

    private void searchInitialize(View view){

        button_transport_main = view.findViewById(R.id.button_start_transport);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_start_transport:
                Intent intent = new Intent(getActivity(), TransportMainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
