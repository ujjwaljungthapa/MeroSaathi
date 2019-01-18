package com.electronics.invento.merosaathi;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class PathFragment extends Fragment {

    private final static int PERMISSION_VALUE = 11;

    Button button_open_path;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_path, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button_open_path = view.findViewById(R.id.btn_frag_path_openPath);
        button_open_path.setEnabled(askPermission());
        button_open_path.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PathMainActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean askPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_VALUE);
            return false;
        }
        else return true;
    }
   /* public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_VALUE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    button_open_path.setEnabled(true);
                }
                else {
                    //finish();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                }
            }
        }
    }*/

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_VALUE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    button_open_path.setEnabled(true);
                }
                else {
                    //finish();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        }
    }
}
