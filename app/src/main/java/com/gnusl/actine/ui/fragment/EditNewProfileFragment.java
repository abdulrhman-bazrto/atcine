package com.gnusl.actine.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.model.Profile;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.custom.CustomAppBarWithBack;
import com.gnusl.actine.ui.custom.LoaderPopUp;
import com.gnusl.actine.util.Constants;
import com.gnusl.actine.util.MediaUtils;
import com.gnusl.actine.util.PermissionsUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class EditNewProfileFragment extends Fragment implements View.OnClickListener, ConnectionDelegate {

    View inflatedView;

    private CustomAppBarWithBack cubManageProfile;
    private Button btnCancel, btnSave, btnUpload;
    private EditText etProfileName;

    private Profile profile;
    private Uri userProfileImageUri;
    private ImageView ivProfile;
    String imageTransitionName;

    public EditNewProfileFragment() {
    }

    public static EditNewProfileFragment newInstance(Bundle bundle) {
        EditNewProfileFragment fragment = new EditNewProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.profile = (Profile) getArguments().getSerializable(Constants.EditNewProfileExtra.getConst());
            this.imageTransitionName = getArguments().getString("transition");

        }
        setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.fade_transition));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_edit_new_profile, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {

        findViews();

        if (profile != null) {
            etProfileName.setText(profile.getName());
            Picasso.with(getActivity()).load(profile.getImageUrl()).into(ivProfile);
        }

//        cubManageProfile.getTvTitle().setText("Manage Profile");

        cubManageProfile.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    private void findViews() {
        cubManageProfile = inflatedView.findViewById(R.id.cub_manage_profile_with_back);
        btnCancel = inflatedView.findViewById(R.id.btn_cancel);
        btnSave = inflatedView.findViewById(R.id.btn_save);
        btnUpload = inflatedView.findViewById(R.id.btn_upload);
        etProfileName = inflatedView.findViewById(R.id.et_profile_name);
        ivProfile = inflatedView.findViewById(R.id.iv_add_profile);
        ivProfile.setTransitionName(imageTransitionName);

        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel: {
                if (getActivity() != null) {
                    getActivity().onBackPressed();
                }
                break;
            }
            case R.id.btn_upload: {
                if (PermissionsUtils.checkStoragePermissions(getActivity())) {
                    MediaUtils.openGallery(EditNewProfileFragment.this);
                } else {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PermissionsUtils.CAMERA_PERMISSIONS_REQUEST);

                }
                break;
            }
            case R.id.btn_save: {
                if (etProfileName.getText().toString().trim().isEmpty())
                    etProfileName.setError(getString(R.string.hint_empty));
                else {
                    if (profile != null) {
                        HashMap<String, String> body = new HashMap<>();
                        body.put("id", String.valueOf(profile.getId()));
                        body.put("name", etProfileName.getText().toString());
                        if (userProfileImageUri != null) {
                            DataLoader.uploadRequest(Urls.UpdateProfile.getLink(), new File(userProfileImageUri.getPath()), body, EditNewProfileFragment.this);
                        } else {
                            DataLoader.uploadRequest(Urls.UpdateProfile.getLink(), null, body, EditNewProfileFragment.this);
                        }

                    } else {
                        HashMap<String, String> body = new HashMap<>();
                        body.put("name", etProfileName.getText().toString());
                        if (userProfileImageUri != null) {
                            DataLoader.uploadRequest(Urls.CreateProfile.getLink(), new File(userProfileImageUri.getPath()), body, EditNewProfileFragment.this);
                        } else {
                            DataLoader.uploadRequest(Urls.CreateProfile.getLink(), null, body, EditNewProfileFragment.this);
                        }
                    }
                    LoaderPopUp.show(getActivity());
                }
                break;
            }
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
//        Toast.makeText(getActivity(), anError.getErrorBody(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {
        LoaderPopUp.dismissLoader();
        Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    boolean hasOpened;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        requestCode = requestCode & 0x0000ffff;
        if (resultCode == RESULT_OK)
            switch (requestCode) {
                case MediaUtils.PICK_IMAGE_REQUEST_CODE:
                    if (hasOpened)
                        return;
                    Uri galleryPictureUri = data.getData();
                    MediaUtils.startCrop(galleryPictureUri, getActivity());
                    hasOpened = true;
                    break;

                case UCrop.REQUEST_CROP:
                    hasOpened = false;
                    userProfileImageUri = UCrop.getOutput(data);
                    try {
                        Bitmap imageBitmap = SiliCompressor.with(getActivity()).getCompressBitmap(userProfileImageUri.toString());
                        ivProfile.setImageBitmap(imageBitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
    }
}
