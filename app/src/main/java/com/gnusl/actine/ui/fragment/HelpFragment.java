package com.gnusl.actine.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.gnusl.actine.ui.custom.LoaderPopUp;
import com.gnusl.actine.util.Constants;
import com.gnusl.actine.util.PermissionsUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HelpFragment extends Fragment implements View.OnClickListener, ConnectionDelegate, HelpItemClickEvents {

    View inflatedView;
    private RecyclerView rvHelp;
    private CustomAppBarWithBack cubHelpWithBack;
    private Button btnCall;

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
        LoaderPopUp.show(getActivity());

        cubHelpWithBack = inflatedView.findViewById(R.id.cub_help_with_back);

        rvHelp = inflatedView.findViewById(R.id.rv_help);
        btnCall = inflatedView.findViewById(R.id.btn_call);

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                    try {
                        if (PermissionsUtils.checkCallPermissions(getActivity())) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+96566666588"));
                            startActivity(intent);
                        } else {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PermissionsUtils.CALL_PERMISSIONS_REQUEST);
                        }

                    } catch (Exception e) {

                    }
                }

            }
        });

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

        cubHelpWithBack.getTvTitle().setText(getActivity().getString(R.string.help));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }

    @Override
    public void onConnectionError(int code, String message) {
        LoaderPopUp.dismissLoader();
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
        LoaderPopUp.dismissLoader();
//        Toast.makeText(getActivity(), anError.getMessage(), Toast.LENGTH_SHORT).show();
        // Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        LoaderPopUp.dismissLoader();
        if (jsonObject.has("about")) {
            List<Help> helpList = Help.newArray(jsonObject.optJSONArray("about"));
            helpAdapter.setList(helpList);
        }
    }


    @Override
    public void onHelpClicked(Help help) {
        if (getActivity() != null) {
            if (getActivity() instanceof MainActivity) {
                Fragment fragment = ((MainActivity) getActivity()).getmCurrentFragment();
                if (fragment instanceof MoreContainerFragment) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.HelpExtra.getConst(), help);
                    ((MoreContainerFragment) fragment).replaceFragment(FragmentTags.HelpDetailsFragment, bundle, null);
                }
            }
        }
    }
}
