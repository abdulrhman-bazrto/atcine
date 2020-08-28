package com.gnusl.actine.ui.Mobile.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gnusl.actine.R;
import com.gnusl.actine.model.Help;
import com.gnusl.actine.ui.Mobile.custom.CustomAppBarWithBack;
import com.gnusl.actine.util.Constants;


public class HelpDetailsFragment extends Fragment {

    View inflatedView;
    TextView tvHelpDetails;

    Help help;

    private CustomAppBarWithBack cubHelpWithBack;


    public HelpDetailsFragment() {
    }

    public static HelpDetailsFragment newInstance(Bundle args) {
        HelpDetailsFragment fragment = new HelpDetailsFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.help = (Help) getArguments().getSerializable(Constants.HelpExtra.getConst());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_help_details, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        cubHelpWithBack = inflatedView.findViewById(R.id.cub_help_with_back);
        tvHelpDetails = inflatedView.findViewById(R.id.tv_help_details);

        cubHelpWithBack.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        cubHelpWithBack.getTvTitle().setText(help.getTitle());

        tvHelpDetails.setMovementMethod(new ScrollingMovementMethod());

        tvHelpDetails.setText(help.getDescription());

    }

}
