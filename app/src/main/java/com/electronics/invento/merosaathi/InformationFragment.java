package com.electronics.invento.merosaathi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class InformationFragment extends Fragment implements View.OnClickListener {

    Button button_open_informationView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        button_open_informationView = view.findViewById(R.id.btn_view_information);
        button_open_informationView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        InformationViewFragment open_information_view_frag = new InformationViewFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

        //getActivity().getSupportFragmentManager().popBackStackImmediate();

        //FOR FRAGMENT TRANSITION ANIMATION
        fragmentTransaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out);
        //fragmentTransaction.replace(R.id.fragment_container, open_information_view_frag);

        //NEEDED. ELSE TWO FRAGMENT OVERLAP
        fragmentTransaction.replace(R.id.fragment_container, open_information_view_frag).addToBackStack(null);


        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}