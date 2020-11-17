package com.example.brandlancerlead;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView photo;
    private DrawerLayout drawer;
    RecyclerView list_recyclerView;
    private static String userID;
    NavigationView reportNavigator;
    private static final String FileName = "ExecutiveLogin";
    private static final String UserName = "ExecutiveId";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";

    private static final String  IsLogin = "Remember";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        list_recyclerView=findViewById(R.id.list_recyclerView);
        reportNavigator=findViewById(R.id.reportNavigator);
        drawer=findViewById(R.id.drawer);
        reportNavigator.setNavigationItemSelectedListener(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        SharedPreferences preferencesc = getSharedPreferences(FileName, MODE_PRIVATE);
        userID = preferencesc.getString(UserName, null);

        LinearLayoutManager manager=new LinearLayoutManager(this);
        list_recyclerView.setLayoutManager(manager);
        list_recyclerView.setNestedScrollingEnabled(false);

        bindNavList();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void closeNavigation(int position) {

         if (position == 1){
            drawer.closeDrawer(GravityCompat.START);

            Intent chartPass = new Intent(DashBoardActivity.this, FlowchartActivity.class);
            chartPass.putExtra("chartImg",R.drawable.lead_flowchart);
            startActivity(chartPass);
        }else if (position==2){
            drawer.closeDrawer(GravityCompat.START);

            Intent followSite = new Intent(DashBoardActivity.this, MainActivity.class);
            followSite.putExtra("userName",userID);
            followSite.putExtra("Types","Lead");
            startActivity(followSite);
        }
        else if (position==3){
            drawer.closeDrawer(GravityCompat.START);

            Intent followSite = new Intent(DashBoardActivity.this, FollowSiteActivity.class);
            followSite.putExtra("userName",userID);
            followSite.putExtra("flag","4");
            startActivity(followSite);
        }
        else if (position==4){
            drawer.closeDrawer(GravityCompat.START);

            Intent followSite = new Intent(DashBoardActivity.this, FollowSiteActivity.class);
            followSite.putExtra("userName",userID);
            followSite.putExtra("flag","8");
            startActivity(followSite);
        }else if (position==5){

            Intent leadReportIntent=new Intent(DashBoardActivity.this,LeadReportActivity.class);
            startActivity(leadReportIntent);
            drawer.closeDrawer(GravityCompat.START);

        }
        else if(position==9){
             drawer.closeDrawer(GravityCompat.START);

             Intent chartPass = new Intent(DashBoardActivity.this, FlowchartActivity.class);
             chartPass.putExtra("chartImg",R.drawable.appointment_flow);
             startActivity(chartPass);


        } else if (position==10){
             drawer.closeDrawer(GravityCompat.START);

             Intent changePass = new Intent(DashBoardActivity.this, DpPendingActivity.class);
             changePass.putExtra("userName",userID);
             changePass.putExtra("Type","ZeroPayment");
             startActivity(changePass);


        }
        else if(position==11) {
             drawer.closeDrawer(GravityCompat.START);
             Intent changePass = new Intent(DashBoardActivity.this, DpPendingActivity.class);
             changePass.putExtra("userName",userID);
             changePass.putExtra("Type","DpPending");
             startActivity(changePass);

        }
        else if (position==12){
             drawer.closeDrawer(GravityCompat.START);
             Intent followSite = new Intent(DashBoardActivity.this, MainActivity.class);
             followSite.putExtra("userName",userID);
             followSite.putExtra("Types","Appointment");
             followSite.putExtra("flag","4");
             startActivity(followSite);


        }
        else if(position==13){
            drawer.closeDrawer(GravityCompat.START);
             Intent changePass = new Intent(DashBoardActivity.this, DpPendingActivity.class);
             changePass.putExtra("userName",userID);
             changePass.putExtra("Type","DpClosed");
             startActivity(changePass);


         }else if (position == 6){
             drawer.closeDrawer(GravityCompat.START);
             Intent chartPass = new Intent(DashBoardActivity.this, FlowchartActivity.class);
             startActivity(chartPass);

         }else if(position==7) {
             drawer.closeDrawer(GravityCompat.START);
             Intent chartPass = new Intent(DashBoardActivity.this, SiteActivity.class);
             startActivity(chartPass);

         }
        else if(position==14){
            drawer.closeDrawer(GravityCompat.START);

            Intent changePass = new Intent(DashBoardActivity.this, ChangePasswordActivity.class);
            changePass.putExtra("userName",userID);
            startActivity(changePass);
        }else  if(position==15) {
            drawer.closeDrawer(GravityCompat.START);

            SharedPreferences sharedPreferences = getSharedPreferences ( FileName,MODE_PRIVATE );
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent login = new Intent(DashBoardActivity.this,LoginActivity.class);
            startActivity(login);
            finish();

        }
    }
    private void bindNavList() {
        ArrayList<String> al=new ArrayList<>();
        al.add("Leads");
        al.add("Lead Work Flow");
        al.add("Leads");
        al.add("Followups");
        al.add("Site Visit");
        al.add("Reports");
        al.add("Layout");
        al.add("Site Allot Details");
        al.add("Bookings");
        al.add("Booking Work Flow");
        al.add("Zero Payment");
        al.add("DP Pending");
        al.add("Appointments");
        al.add("DP Closed");

        al.add("Change Password");
        al.add("Logout");
        NavigationListAdapter adapter=new NavigationListAdapter(DashBoardActivity.this,al);
        list_recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }

}
