package com.gnusl.actine.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.HelpItemClickEvents;
import com.gnusl.actine.model.Help;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.HelpAdapter;
import com.gnusl.actine.ui.custom.CustomAppBarWithBack;
import com.gnusl.actine.util.Constants;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HelpFragment extends Fragment implements View.OnClickListener, ConnectionDelegate, HelpItemClickEvents {

    View inflatedView;
    private RecyclerView rvHelp;
    private CustomAppBarWithBack cubHelpWithBack;

    private KProgressHUD progressHUD;
    private HelpAdapter helpAdapter;

    public HelpFragment() {
    }

    public static HelpFragment newInstance() {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_help, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        DataLoader.getRequest(Urls.Help.getLink(), this);
        progressHUD = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(getString(R.string.please_wait))
                .setMaxProgress(100)
                .show();

        cubHelpWithBack = inflatedView.findViewById(R.id.cub_help_with_back);

        rvHelp = inflatedView.findViewById(R.id.rv_help);

        helpAdapter = new HelpAdapter(getActivity(), new ArrayList<Help>(), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);

        rvHelp.setLayoutManager(layoutManager);

        rvHelp.setAdapter(helpAdapter);

        cubHelpWithBack.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        cubHelpWithBack.getTvTitle().setText("Help");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    @Override
    public void onConnectionError(int code, String message) {
        if (progressHUD != null)
            progressHUD.dismiss();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        if (progressHUD != null)
            progressHUD.dismiss();
        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        if (progressHUD != null)
            progressHUD.dismiss();
        if (jsonObject.has("about")) {
            List<Help> helpList = Help.newArray(jsonObject.optJSONArray("about"));
            helpAdapter.setList(helpList);
        }
    }


    @Override
    public void onHelpClicked(Help help) {
        if (getActivity() != null) {
            Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
            if (fragment instanceof MoreContainerFragment) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.HelpExtra.getConst(), help);
                ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.HelpDetailsFragment, bundle);
            }
        }
    }
}
