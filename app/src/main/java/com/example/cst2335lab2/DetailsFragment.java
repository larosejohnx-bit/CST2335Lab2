package com.example.cst2335lab2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DetailsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_details, container, false);

        Bundle args = getArguments();

        if (args != null) {
            TextView name = view.findViewById(R.id.nameValue);
            TextView height = view.findViewById(R.id.heightValue);
            TextView mass = view.findViewById(R.id.massValue);

            name.setText(args.getString("name", ""));
            height.setText(args.getString("height", ""));
            mass.setText(args.getString("mass", ""));
        }

        return view;
    }
}
