package com.example.brandlancerlead;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.PaymentsListJsonObjects;
import com.example.brandlancerlead.model.PaymentsListJsonResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentViewFragment extends BottomSheetDialogFragment implements View.OnClickListener {



    RecyclerView paymentsList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.payment_view_fragment,container,false);
        paymentsList = view.findViewById(R.id.paymentListHolder);
        Button close = view.findViewById(R.id.close_dailog);
        paymentsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        Bundle bookingBundle = getArguments();
        if(bookingBundle != null){
            String booking = bookingBundle.getString("booking_id");
            if(!TextUtils.isEmpty(booking)){
                LoadPayments(booking) ;
            }
        }
        close.setOnClickListener(this);
        return view;
    }

    @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                setupFullHeight(bottomSheetDialog);
            }
        });
        return  dialog;
    }


    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = (int) (getWindowHeight()*0.75);
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
    protected  void LoadPayments(String book){
        WebContentsInterface contentsInterface= ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<PaymentsListJsonResult> jsonResultCall=contentsInterface.bindPayments(book);
        jsonResultCall.enqueue(new Callback<PaymentsListJsonResult>() {
            @Override
            public void onResponse(Call<PaymentsListJsonResult> call, Response<PaymentsListJsonResult> response) {
                if (response.code()==200){
                    PaymentsListJsonResult jsonResult=response.body();
                    if (jsonResult!=null && jsonResult.isResult() && jsonResult.getResultset()!=null){
                        displayResults(jsonResult.getResultset());
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentsListJsonResult> call, Throwable t) {

            }
        });
    }

    private void displayResults(ArrayList<PaymentsListJsonObjects> resultset) {
        PaymentListAdapter adapter=new PaymentListAdapter(getActivity(),resultset);
        paymentsList.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
