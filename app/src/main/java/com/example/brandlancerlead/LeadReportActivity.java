package com.example.brandlancerlead;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.ConversionReport;
import com.example.brandlancerlead.model.LeadReport;
import com.example.brandlancerlead.model.LeadReportListJsonObjects;
import com.example.brandlancerlead.model.LeadReportListJsonResult;
import com.example.brandlancerlead.model.RawReportSummary;
import com.example.brandlancerlead.model.SiteVisitReport;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeadReportActivity extends AppCompatActivity {
    TextView fromDate,toDate
    ,txtTotalLed,txtTotalmet,txtTotalnotmet,txtTotalconvert,txtConvertOpen,txtconvertClose,txtconvert,txtnotconvert,txtSitvisitConform,txtsitevisit,txtnotSiteVisit;
    private Calendar calendar;
    boolean isFromDate;
    Button getReport;
    String userID;
    private Dialog progressOnLoad;
    private LinearLayout contentScroll;
    private PieChart metChart,convertionChart,siteChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_report);
        fromDate=findViewById(R.id.FromDate);
        toDate=findViewById(R.id.ToDate);
        getReport=findViewById(R.id.getReport);

        txtTotalLed=findViewById(R.id.total_led);
        txtTotalmet=findViewById(R.id.total_met);
        txtTotalnotmet=findViewById(R.id.total_not_met);
        txtTotalconvert=findViewById(R.id.total_conversion_led);
        txtConvertOpen=findViewById(R.id.total_conversion_open);
        txtconvertClose=findViewById(R.id.total_conversion_close);
        txtconvert=findViewById(R.id.total_conversion_done);
        txtnotconvert=findViewById(R.id.total_conversion_not_done);
        metChart=findViewById(R.id.lead_met_chart);
        convertionChart=findViewById(R.id.conversion_chart);
        siteChart=findViewById(R.id.sitvisit_chart);

        txtSitvisitConform=findViewById(R.id.total_sitevisit);
        txtsitevisit=findViewById(R.id.total_sitevisit_done);
        txtnotSiteVisit=findViewById(R.id.total_sitevisit_pending);
        contentScroll = findViewById(R.id.scroll_content);

        contentScroll.setVisibility(View.INVISIBLE);

        calendar=Calendar.getInstance();

        SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        String dateToString=sdf.format(calendar.getTime());
        fromDate.setText(dateToString);
        toDate.setText(dateToString);


        SharedPreferences preferencesc = getSharedPreferences("ExecutiveLogin", MODE_PRIVATE);
        userID = preferencesc.getString("ExecutiveId", null);
        progressOnLoad = new Dialog(this);


        progressOnLoad.setContentView(new ProgressBar(this));
        progressOnLoad.setCancelable(false);

        if( progressOnLoad.getWindow() != null)
            progressOnLoad.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        final DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                calendar.set(Calendar.MONTH,month);
                updateDate();

            }
        };


        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromDate=true;
                new DatePickerDialog(LeadReportActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromDate=false;
                new DatePickerDialog(LeadReportActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        getReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // bindLeadReports();

                bindRawSummaryData();


            }
        });


        metChart.setUsePercentValues(false);

        metChart.setDrawCenterText(true);
        metChart.setCenterTextColor(Color.parseColor("#EBBC46"));
        metChart.setCenterTextSize(15f);
        metChart.setCenterText("Lead");
        metChart.setDrawHoleEnabled(true);
        metChart.setHoleColor(Color.GRAY);
        metChart.setHoleRadius(50f);
        metChart.setDrawEntryLabels(false);
        metChart.getDescription().setEnabled(false);



        convertionChart.setUsePercentValues(false);
        convertionChart.setDrawCenterText(true);
        convertionChart.setCenterTextColor(Color.parseColor("#EBBC46"));
        convertionChart.setCenterTextSize(15f);
        convertionChart.setCenterText("Converted");
        convertionChart.setDrawHoleEnabled(true);
        convertionChart.setHoleColor(Color.GRAY);
        convertionChart.setHoleRadius(50f);
        convertionChart.setDrawEntryLabels(false);
        convertionChart.getDescription().setEnabled(false);


        siteChart.setUsePercentValues(false);
        siteChart.setDrawCenterText(true);
        siteChart.setCenterTextColor(Color.parseColor("#EBBC46"));
        siteChart.setCenterTextSize(15f);
        siteChart.setCenterText("Site Visit");
        siteChart.setDrawHoleEnabled(true);
        siteChart.setHoleColor(Color.GRAY);
        siteChart.setHoleRadius(50f);
        siteChart.setDrawEntryLabels(false);
        siteChart.getDescription().setEnabled(false);






    }

    private void bindRawSummaryData() {
        WebContentsInterface contentsInterface = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<RawReportSummary> reportSummaryCall = contentsInterface.bindLeadsummaryReport(fromDate.getText().toString(),toDate.getText().toString(),userID);
        reportSummaryCall.enqueue(new Callback<RawReportSummary>() {
            @Override
            public void onResponse(Call<RawReportSummary> call, Response<RawReportSummary> response) {
                if(response.code() == 200){
                    RawReportSummary reportSummary = response.body();
                    if(reportSummary != null){

                            loadDataToView(reportSummary);
                    }
                }

            }

            @Override
            public void onFailure(Call<RawReportSummary> call, Throwable t) {

            }
        });
    }

    private void loadDataToView(RawReportSummary resultset) {
        if(resultset.isResult()){
            LeadReport leadData = resultset.getLeadsMet();
            if(leadData!= null){
                String app = leadData.getTotal_Leads_Assigned()+"";
                String appmet = leadData.getMet()+"";
                String appnotmet = leadData.getNot_Met()+"";
                txtTotalLed.setText(app);
                txtTotalmet.setText(appmet);
                txtTotalnotmet.setText(appnotmet);

                ArrayList<PieEntry> metEntry = new ArrayList<PieEntry>();
                metEntry.add(new PieEntry(leadData.getMet(),"Met"));
                metEntry.add(new PieEntry(leadData.getNot_Met(),"Not Met"));
                PieDataSet pieData = new PieDataSet(metEntry,"");
                pieData.setSliceSpace(2f);
                pieData.setSelectionShift(5f);

                ArrayList<Integer> colors = new ArrayList<Integer>();


                    colors.add(Color.GREEN);
                    colors.add(Color.RED);

                colors.add(ColorTemplate.getHoloBlue());
                pieData.setColors(colors);

                PieData metData = new PieData(pieData);


                metChart.setData(metData);
                metChart.invalidate();


            }
            ConversionReport conversion = resultset.getLeadsConverted();
            if(conversion != null){
             String totalcon =   conversion.getTotal_Leads_Assigned()+"";
             String totalconopen =   conversion.getNot_Closed()+"";
             String totalconclose =   conversion.getClosed()+"";
             String conDone =   conversion.getConverted()+"";
             String conFai =   conversion.getNot_Converted()+"";
             txtTotalconvert.setText(totalcon);
             txtConvertOpen.setText(totalconopen);
             txtconvertClose.setText(totalconclose);
             txtconvert.setText(conDone);
             txtnotconvert.setText(conFai);

                ArrayList<PieEntry> metEntry = new ArrayList<PieEntry>();
                metEntry.add(new PieEntry(conversion.getNot_Closed(),"Open"));

                metEntry.add(new PieEntry(conversion.getConverted(),"CV Done"));
                metEntry.add(new PieEntry(conversion.getNot_Converted(),"CV Not Done"));
                PieDataSet pieData = new PieDataSet(metEntry,"");
                pieData.setSliceSpace(2f);
                pieData.setSelectionShift(5f);

                ArrayList<Integer> colors = new ArrayList<Integer>();

                colors.add(Color.RED);
                colors.add(Color.GREEN);
                colors.add(Color.YELLOW);



                colors.add(ColorTemplate.getHoloBlue());
                pieData.setColors(colors);

                PieData metData = new PieData(pieData);


                convertionChart.setData(metData);
                convertionChart.invalidate();

            }
            SiteVisitReport siteVisit = resultset.getSiteVisits();
            if(siteVisit != null){
                String siteTotal = siteVisit.getSite_Vist_Confirmed()+"";
                String siteDone = siteVisit.getSite_Visit_Done()+"";
                String siteclose= siteVisit.getSite_Visit_Pending()+"";
                txtSitvisitConform.setText(siteTotal);
                txtsitevisit.setText(siteDone);
                txtnotSiteVisit.setText(siteclose);

                ArrayList<PieEntry> metEntry = new ArrayList<PieEntry>();
                metEntry.add(new PieEntry(siteVisit.getSite_Visit_Done(),"Done"));
                metEntry.add(new PieEntry(siteVisit.getSite_Visit_Pending(),"Pending"));

                PieDataSet pieData = new PieDataSet(metEntry,"");
                pieData.setSliceSpace(2f);
                pieData.setSelectionShift(5f);

                ArrayList<Integer> colors = new ArrayList<Integer>();

                colors.add(Color.GREEN);
                colors.add(Color.RED);

                colors.add(ColorTemplate.getHoloBlue());
                pieData.setColors(colors);

                PieData metData = new PieData(pieData);


                siteChart.setData(metData);
                siteChart.invalidate();

            }
            contentScroll.setVisibility(View.VISIBLE);
        }else{
            contentScroll.setVisibility(View.INVISIBLE);
        }
    }

    private void bindLeadReports() {
        if(progressOnLoad != null && !progressOnLoad.isShowing()){
            progressOnLoad.show();
        }
       // leads_Recycler.setAdapter(null);

        WebContentsInterface contentsInterface= ServerConnection.createRetrofitConnection(WebContentsInterface.class);
        Call<LeadReportListJsonResult>jsonResultCall=contentsInterface.bindLeadReport(fromDate.getText().toString(),toDate.getText().toString(),userID);
        jsonResultCall.enqueue(new Callback<LeadReportListJsonResult>() {
            @Override
            public void onResponse(Call<LeadReportListJsonResult> call, Response<LeadReportListJsonResult> response) {
                if (response.code()==200){
                    LeadReportListJsonResult jsonResult=response.body();
                    if (jsonResult!=null && jsonResult.isResult() && jsonResult.getResultset()!=null){
                        displayDetails(jsonResult.getResultset());
                    }else {
                        progressOnLoad.dismiss();
                        Toast.makeText(LeadReportActivity.this,jsonResult.getResultmessage(),Toast.LENGTH_SHORT).show();
                    }
                }else {
                    progressOnLoad.dismiss();
                    Toast.makeText(LeadReportActivity.this,response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LeadReportListJsonResult> call, Throwable t) {
                progressOnLoad.dismiss();
            }
        });

    }

    private void displayDetails(ArrayList<LeadReportListJsonObjects> resultset) {
        LeadReportAdapter adapter=new LeadReportAdapter(LeadReportActivity.this,resultset);


    }

    private void updateDate() {
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("MM/dd/yyyy",Locale.US);
        if (isFromDate){
            fromDate.setText(simpleDateFormat.format(calendar.getTime()));
        }else {
            toDate.setText(simpleDateFormat.format(calendar.getTime()));
        }
    }

    @Override
    public void onBackPressed() {

        if (progressOnLoad!=null && progressOnLoad.isShowing()){

        }else {
            finish();
        }
        super.onBackPressed();
    }
}
