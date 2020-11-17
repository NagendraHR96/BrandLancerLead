package com.example.brandlancerlead;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brandlancerlead.model.PaymentsListJsonObjects;

import java.util.ArrayList;

public class PaymentListAdapter extends RecyclerView.Adapter<PaymentListAdapter.PaHolder> {
    Context context;
    ArrayList<PaymentsListJsonObjects> resultset;

    public PaymentListAdapter(Context context, ArrayList<PaymentsListJsonObjects> resultset) {
        this.context = context;
        this.resultset = resultset;
    }

    @NonNull
    @Override
    public PaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.payments,viewGroup,false);
        return new PaymentListAdapter.PaHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaHolder paHolder, int i) {
        PaymentsListJsonObjects payment = resultset.get(i);
        String paymentTowards = payment.getPaymentCategory();
        String state = payment.getStatus();
        String amountVal = payment.getAmount();
        String date_receipt = payment.getReceiptDate();
        String mode_Pay = payment.getModeName();
        String cheNum = payment.getChequeNo();
        String date = payment.getChequeDate();

        paHolder.paymentName.setText(paymentTowards);
        paHolder.receiptDate.setText(date_receipt);
        paHolder.amount.setText(amountVal);
        paHolder.status.setText(state);
        paHolder.Payment_mode.setText(mode_Pay);
        if(!TextUtils.isEmpty(cheNum)){
            paHolder.cheque_num.setText("  "+cheNum);
            if(!TextUtils.isEmpty(date))
                paHolder.cheque_date.setText(date);

        }


    }

    @Override
    public int getItemCount() {
        return resultset.size();
    }

    public class PaHolder extends RecyclerView.ViewHolder {
        TextView paymentName,receiptDate,amount,status,Payment_mode,cheque_num,cheque_date;
        public PaHolder(@NonNull View itemView) {
            super(itemView);
            paymentName = itemView.findViewById(R.id.payment_Catg);
            receiptDate = itemView.findViewById(R.id.receipt_date);
            amount = itemView.findViewById(R.id.amount_pay);
            status = itemView.findViewById(R.id.payment_status);
            Payment_mode = itemView.findViewById(R.id.mode_of_pay);
            cheque_num = itemView.findViewById(R.id.cheque_number);
            cheque_date = itemView.findViewById(R.id.cheque_date);
        }

    }
}
