package com.gnusl.actine.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.error.ANError;
import com.gnusl.actine.R;
import com.gnusl.actine.enums.FragmentTags;
import com.gnusl.actine.interfaces.CommentLongClickEvent;
import com.gnusl.actine.interfaces.ConnectionDelegate;
import com.gnusl.actine.interfaces.DownloadDelegate;
import com.gnusl.actine.model.Comment;
import com.gnusl.actine.model.Show;
import com.gnusl.actine.network.DataLoader;
import com.gnusl.actine.network.Urls;
import com.gnusl.actine.ui.activity.AccountActivity;
import com.gnusl.actine.ui.activity.AuthActivity;
import com.gnusl.actine.ui.activity.MainActivity;
import com.gnusl.actine.ui.adapter.CommentsAdapter;
import com.gnusl.actine.ui.custom.LoaderPopUp;
import com.gnusl.actine.ui.custom.MarginItemDecoration;
import com.gnusl.actine.util.Constants;
import com.gnusl.actine.util.SharedPreferencesUtils;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ReviewsFragment extends Fragment implements View.OnClickListener, ConnectionDelegate, CommentLongClickEvent {

    View inflatedView;

    private TextView tvAppSetting, tvAccount;
    private Show show;
    private CommentsAdapter commentsAdapter;
    private RecyclerView rvComments;
    private TextView tvCommentsCount, tvLikesCount, tvViewsCount, tvAddComment;
    private View clInputLayout;
    private EditText etCommentText;
    private ImageView ivSendComment;
    Animation animation;
    private ConstraintLayout clRoot;

    public ReviewsFragment() {
    }

    public static ReviewsFragment newInstance(Bundle bundle) {
        ReviewsFragment fragment = new ReviewsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            show = (Show) getArguments().getSerializable(Constants.HomeDetailsExtra.getConst());

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflatedView == null) {
            inflatedView = inflater.inflate(R.layout.fragment_reviews, container, false);
            init();
        }
        return inflatedView;
    }

    private void init() {
        findViews();
        animation = AnimationUtils.loadAnimation(getActivity(), R.anim.translate_left_side);

        commentsAdapter = new CommentsAdapter(getActivity(), new ArrayList<Comment>(), this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rvComments.setLayoutManager(layoutManager);
        int dp1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1,
                getActivity().getResources().getDisplayMetrics());
        rvComments.addItemDecoration(new MarginItemDecoration(20 * dp1,getActivity(), 0));
        rvComments.setAdapter(commentsAdapter);
        String url = "";
        if (show.getIsMovie()) {
            url = Urls.Movie.getLink();
        } else {
            url = Urls.Series.getLink();
        }
        DataLoader.getRequest(url + show.getId(), this);

        sendGetCommentsRequest();

    }

    private void findViews() {

        rvComments = inflatedView.findViewById(R.id.rv_comments);
        tvCommentsCount = inflatedView.findViewById(R.id.tv_comments_count);
        tvLikesCount = inflatedView.findViewById(R.id.tv_likes_count);
        tvViewsCount = inflatedView.findViewById(R.id.tv_views_count);
        clInputLayout = inflatedView.findViewById(R.id.cl_input_layout);
        etCommentText = inflatedView.findViewById(R.id.et_comment_text);
        ivSendComment = inflatedView.findViewById(R.id.iv_send_comment);
        tvAddComment = inflatedView.findViewById(R.id.tv_add_comment);
        clRoot = inflatedView.findViewById(R.id.root_view);

        tvLikesCount.setOnClickListener(this);
        ivSendComment.setOnClickListener(this);
        tvAddComment.setOnClickListener(this);
    }

    private void sendGetCommentsRequest() {

        if (show.getIsMovie()) {
            DataLoader.getRequest(Urls.MovieComments.getLink().replaceAll("%id%", String.valueOf(show.getId())), this);
        } else  {
            DataLoader.getRequest(Urls.SeriesComments.getLink().replaceAll("%id%", String.valueOf(show.getId())), this);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_likes_count: {
                String url = "";
                if (show.getIsMovie()) {
                    url = Urls.MovieLike.getLink();
                } else {
                    url = Urls.SeriesLike.getLink();
                }
                DataLoader.postRequest(url.replaceAll("%id%", String.valueOf(show.getId())), new ConnectionDelegate() {
                    @Override
                    public void onConnectionError(int code, String message) {

                    }

                    @Override
                    public void onConnectionError(ANError anError) {

                    }

                    @Override
                    public void onConnectionSuccess(JSONObject jsonObject) {
                        if (jsonObject.has("status")) {
                            if (jsonObject.optString("status").equalsIgnoreCase("added")) {
                                if (getActivity() != null) {
                                    tvLikesCount.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_liked), null, null, null);
                                    tvLikesCount.setText(String.valueOf(Integer.parseInt(tvLikesCount.getText().toString()) + 1));
                                }
                            } else {
                                if (getActivity() != null) {
                                    tvLikesCount.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_rate), null, null, null);
                                    tvLikesCount.setText(String.valueOf(Integer.parseInt(tvLikesCount.getText().toString()) - 1));
                                }
                            }
                        }
                    }
                });
                break;
            }
            case R.id.tv_add_comment: {
//                if (clInputLayout.getVisibility() == View.VISIBLE)
//                    clInputLayout.setVisibility(View.GONE);
//                else
//                    clInputLayout.setVisibility(View.VISIBLE);
                showAddCommentDialog();
                break;
            }
            case R.id.iv_send_comment: {
                if (etCommentText.getText().toString().isEmpty())
                    return;
                HashMap<String, String> body = new HashMap<>();
                body.put("comment", etCommentText.getText().toString());

                String url = "";
                if (show.getIsMovie()) {
                    url = Urls.MovieComments.getLink();
                } else {
                    url = Urls.SeriesComments.getLink();
                }
                DataLoader.postRequest(url.replaceAll("%id%", String.valueOf(show.getId())), body, new ConnectionDelegate() {
                    @Override
                    public void onConnectionError(int code, String message) {

                    }

                    @Override
                    public void onConnectionError(ANError anError) {
                        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConnectionSuccess(JSONObject jsonObject) {
                        if (jsonObject.has("status") && jsonObject.optString("status").equalsIgnoreCase("success")) {
                            etCommentText.setText("");
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            //Find the currently focused view, so we can grab the correct window token from it.
                            imm.hideSoftInputFromWindow(etCommentText.getWindowToken(), 0);
                            clInputLayout.setVisibility(View.GONE);
                            String url = "";
                            if (show.getIsMovie()) {
                                url = Urls.MovieComments.getLink();
                            } else  {
                                url = Urls.SeriesComments.getLink();
                            }
                            DataLoader.getRequest(url.replaceAll("%id%", String.valueOf(show.getId())), ReviewsFragment.this);
                        }
                    }
                });
                break;
            }
        }
    }

    @Override
    public void onConnectionError(int code, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionError(ANError anError) {
//        Toast.makeText(getActivity(), anError.getErrorBody(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionSuccess(JSONObject jsonObject) {

        if (jsonObject.has("comments")) {
            List<Comment> comments = Comment.newArray(jsonObject.optJSONArray("comments"));
            commentsAdapter.setList(comments);
        }

        if (jsonObject.has("like_count") && jsonObject.has("comment_count") && jsonObject.has("visited")) {
            tvLikesCount.setText(String.valueOf(jsonObject.optInt("like_count")));
            tvViewsCount.setText(String.format(Locale.getDefault(), "%d", jsonObject.optInt("visited")));
            tvCommentsCount.setText(String.valueOf(jsonObject.optInt("comment_count")));

            show.setIsDownloaded(jsonObject.optBoolean("is_downloaded"));
            show.setIsLike(jsonObject.optBoolean("is_like"));

            if (jsonObject.optBoolean("is_like")) {
                if (getActivity() != null)
                    tvLikesCount.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_liked), null, null, null);
            } else {
                if (getActivity() != null)
                    tvLikesCount.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.icon_rate), null, null, null);
            }

        }
    }

    @Override
    public void onLongClickComment(Comment comment) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setMessage("Delete this Comment?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok",
                (dialog, which) -> {
                    String url = "";
                    if (show.getIsMovie()) {
                        url = Urls.MovieComment.getLink();
                    } else {
                        url = Urls.SeriesCommentDelete.getLink();
                    }

                    DataLoader.postRequest(url.replaceAll("%id%", String.valueOf(comment.getId())), new ConnectionDelegate() {
                        @Override
                        public void onConnectionError(int code, String message) {

                        }

                        @Override
                        public void onConnectionError(ANError anError) {
                            Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onConnectionSuccess(JSONObject jsonObject) {
                            String url = "";
                            if (show.getIsMovie()) {
                                url = Urls.MovieComments.getLink();
                            } else  {
                                url = Urls.SeriesComments.getLink();
                            }
                            DataLoader.getRequest(url.replaceAll("%id%", String.valueOf(show.getId())), ReviewsFragment.this);
                        }
                    });
                    dialog.dismiss();
                });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "cancel", (dialog, which) -> dialog.dismiss());

        alertDialog.show();
    }


    private void showAddCommentDialog() {
        final Dialog addCommentDialog = new Dialog(getActivity(), R.style.CustomDialogTheme);
//        addCommentDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        if (addCommentDialog.getWindow() != null) {
            WindowManager.LayoutParams lp = addCommentDialog.getWindow().getAttributes();
            lp.dimAmount = 1.0f; // Dim level. 0.0 - no dim, 1.0 - completely opaque
            addCommentDialog.getWindow().setAttributes(lp);
            addCommentDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation; //style id

        }
//            addCommentDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addCommentDialog.setContentView(R.layout.dialog_add_comment);
        addCommentDialog.setCancelable(true);

        ImageView ivClose;
        EditText etComment;
        Button btnConfirm;
        ivClose = addCommentDialog.findViewById(R.id.iv_close);
        btnConfirm = addCommentDialog.findViewById(R.id.btn_confirm);
        etComment = addCommentDialog.findViewById(R.id.et_comment);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommentDialog.dismiss();
            }
        });


        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etComment.getText().toString().trim().isEmpty()) {
                    etComment.setError("Field comment is required!");
                    return;
                }

                LoaderPopUp.show(getActivity());

                HashMap<String, String> body = new HashMap<>();
                body.put("comment", etComment.getText().toString());

                String url = "";
                if (show.getIsMovie()) {
                    url = Urls.MovieComments.getLink();
                } else {
                    url = Urls.SeriesComments.getLink();
                }
                DataLoader.postRequest(url.replaceAll("%id%", String.valueOf(show.getId())), body, new ConnectionDelegate() {
                    @Override
                    public void onConnectionError(int code, String message) {

                    }

                    @Override
                    public void onConnectionError(ANError anError) {
                        LoaderPopUp.dismissLoader();
                        Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onConnectionSuccess(JSONObject jsonObject) {
                        LoaderPopUp.dismissLoader();
                        if (jsonObject.has("status") && jsonObject.optString("status").equalsIgnoreCase("success")) {
                            etComment.setText("");
                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                            //Find the currently focused view, so we can grab the correct window token from it.
                            imm.hideSoftInputFromWindow(etComment.getWindowToken(), 0);
                            clInputLayout.setVisibility(View.GONE);
                            String url = "";
                            if (show.getIsMovie()) {
                                url = Urls.MovieComments.getLink();
                            } else {
                                url = Urls.SeriesComments.getLink();
                            }
                            DataLoader.getRequest(url.replaceAll("%id%", String.valueOf(show.getId())), ReviewsFragment.this);
                            addCommentDialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "error happened", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        addCommentDialog.show();
    }

    public void startAnimation() {
        animation.setDuration(1200);
        animation.setFillAfter(true);
        animation.setFillEnabled(true);
        clRoot.startAnimation(animation);
    }
}
