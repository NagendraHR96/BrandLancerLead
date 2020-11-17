package com.example.brandlancerlead;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.ApprovedProjectsJsonResult;
import com.example.brandlancerlead.model.ApprovedProjectsObjects;
import com.example.brandlancerlead.model.ProjectsData;
import com.example.brandlancerlead.model.ProjectsJsonResult;
import com.example.brandlancerlead.model.SiteObjects;
import com.example.brandlancerlead.model.SiteStatusJsonResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteActivity extends AppCompatActivity {
    Spinner project_spinnner;
    RecyclerView site_recyclerview;
    String projectName;
    boolean isApproved=false;
    String projectNameorId;
    String id;
    String pro_Name;

    ProgressBar progressBar;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener () {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId ()) {
                case R.id.navigation_home:
                    progressBar.setVisibility ( View.GONE );
                    isApproved=false;
                    site_recyclerview.setAdapter ( null );
                    projects();
                    return true;
                case R.id.navigation_dashboard:
                    progressBar.setVisibility (View.GONE);
                    site_recyclerview.setAdapter ( null );
                    isApproved=true;
                    projectIds();
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_site );
        BottomNavigationView navigation = (BottomNavigationView) findViewById ( R.id.navigation );
        navigation.setOnNavigationItemSelectedListener ( mOnNavigationItemSelectedListener );
        project_spinnner=findViewById ( R.id.project_spinner);
        site_recyclerview=findViewById ( R.id.gridview);
        progressBar=findViewById ( R.id.fault_loader);
        progressBar.setVisibility ( View.GONE );
        GridLayoutManager layoutManager = new GridLayoutManager(this,5, LinearLayoutManager.VERTICAL,false);
        site_recyclerview.setLayoutManager(layoutManager);
       // ServerConnection.service_base_url = "http://49.206.229.93:8090/WcfLeadServices.svc/";
        ServerConnection.service_base_url = "http://203.192.233.36:8086/WcfLeadServices.svc/";


        projects();


    }

    private void projectIds() {

        WebContentsInterface serviceCall=ServerConnection.createRetrofitConnection ( WebContentsInterface.class );

        Call<ApprovedProjectsJsonResult>approvedProjectsJsonResultCall=serviceCall.approvedpProjects ();

        approvedProjectsJsonResultCall.enqueue ( new Callback<ApprovedProjectsJsonResult> () {
            @Override
            public void onResponse(Call<ApprovedProjectsJsonResult> call, Response<ApprovedProjectsJsonResult> response) {
                if (response.code ()==200){
                    ApprovedProjectsJsonResult jsonResult=response.body ();
                    if (jsonResult!=null & jsonResult.isResult ()){
                        if (jsonResult.getResultset ()!=null){
                            displayAprovedprojects(jsonResult.getResultset ());
                        }
                    }
                }else{
                    Toast.makeText ( SiteActivity.this,response.message (),Toast.LENGTH_SHORT).show ();
                }


            }

            @Override
            public void onFailure(Call<ApprovedProjectsJsonResult> call, Throwable t) {


            }
        } );

    }

    private void displayAprovedprojects(final ArrayList<ApprovedProjectsObjects> resultset) {
        ArrayList<String>proList=new ArrayList<String>();
        for (ApprovedProjectsObjects objects:resultset) {
            proList.add ( objects.getProjectName () );
        }

        ArrayAdapter arrayAdapter=new ArrayAdapter ( SiteActivity.this,android.R.layout.simple_spinner_dropdown_item,proList);
        project_spinnner.setAdapter ( arrayAdapter );
        if (project_spinnner!=null){
            project_spinnner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                      site_recyclerview.setAdapter ( null );
                       id=resultset.get ( i ).getProjectId ();
                      projectNameorId=resultset.get ( i ).getProjectId ();

                      pro_Name=resultset.get ( i ).getProjectName ();

                    siteDisplays();
                    progressBar.setVisibility ( View.VISIBLE );

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            } );
        }

    }

    private void projects() {

        WebContentsInterface servicecal=ServerConnection.createRetrofitConnection ( WebContentsInterface.class );

        Call<ProjectsJsonResult>projectsJsonResultCall=servicecal.projectsAvilables ();
        projectsJsonResultCall.enqueue ( new Callback<ProjectsJsonResult> () {
            @Override
            public void onResponse(Call<ProjectsJsonResult> call, Response<ProjectsJsonResult> response) {
                if (response.code ()==200){
                    ProjectsJsonResult jsonResult=response.body ();
                    if (jsonResult!=null & jsonResult.isResult () ){
                        if (jsonResult.getResultset ()!=null){
                            displayProjects(jsonResult.getResultset ());
                        }

                    }

                }


            }

            @Override
            public void onFailure(Call<ProjectsJsonResult> call, Throwable t) {




            }
        } );

    }

    private void displayProjects(final ArrayList<ProjectsData> resultset) {
        ArrayList<String>projectList=new ArrayList<> (  );
        for (ProjectsData data:resultset) {
            projectList.add ( data.getProjectName () );
        }
        ArrayAdapter arrayAdapter=new ArrayAdapter ( SiteActivity.this,android.R.layout.simple_spinner_dropdown_item,projectList);
        project_spinnner.setAdapter ( arrayAdapter );

        if (project_spinnner!=null){
            project_spinnner.setOnItemSelectedListener ( new AdapterView.OnItemSelectedListener () {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    site_recyclerview.setAdapter ( null );
                       projectName=resultset.get ( i ).getProjectName ();

                       projectNameorId=resultset.get ( i ).getProjectName ();

                       pro_Name=projectName;
                       siteDisplay();
                      progressBar.setVisibility ( View.VISIBLE );

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            } );

        }
    }

    private void siteDisplay() {

        WebContentsInterface serviceCall=ServerConnection.createRetrofitConnection ( WebContentsInterface.class );

        Call<SiteStatusJsonResult>siteStatusJsonResultCall=serviceCall.siteStatus ( isApproved,projectName );
        siteStatusJsonResultCall.enqueue ( new Callback<SiteStatusJsonResult> () {
            @Override
            public void onResponse(Call<SiteStatusJsonResult> call, Response<SiteStatusJsonResult> response) {
                if (response.code ()==200){
                    SiteStatusJsonResult jsonResult=response.body ();
                    if (jsonResult!=null & jsonResult.isResult ()){
                        if (jsonResult.getResultset ()!=null){
                            displaySites(jsonResult.getResultset ());
                        }
                    }
                }
                else {

                    Toast.makeText ( SiteActivity.this,response.message (),Toast.LENGTH_SHORT ).show ();
                    progressBar.setVisibility ( View.GONE );
                }

            }

            @Override
            public void onFailure(Call<SiteStatusJsonResult> call, Throwable t) {
                progressBar.setVisibility ( View.GONE );


            }
        } );
    }

    private void siteDisplays() {

        WebContentsInterface serviceCall=ServerConnection.createRetrofitConnection ( WebContentsInterface.class );

        Call<SiteStatusJsonResult>siteStatusJsonResultCall=serviceCall.siteStatus ( isApproved,id);
        siteStatusJsonResultCall.enqueue ( new Callback<SiteStatusJsonResult> () {
            @Override
            public void onResponse(Call<SiteStatusJsonResult> call, Response<SiteStatusJsonResult> response) {
                if (response.code ()==200){
                    SiteStatusJsonResult jsonResult=response.body ();
                    if (jsonResult!=null & jsonResult.isResult ()){
                        if (jsonResult.getResultset ()!=null){
                            displaySites(jsonResult.getResultset ());
                        }
                    }
                } else {
                    Toast.makeText ( SiteActivity.this,response.message (),Toast.LENGTH_SHORT ).show ();
                    progressBar.setVisibility ( View.GONE );
                }

            }
            @Override
            public void onFailure(Call<SiteStatusJsonResult> call, Throwable t) {
                progressBar.setVisibility ( View.GONE );


            }
        } );

    }

    private void displaySites(ArrayList<SiteObjects> resultset) {
        SiteDisplayAdapter siteDisplyCustomListAdapter=new SiteDisplayAdapter(SiteActivity.this,resultset);
        site_recyclerview.setAdapter ( siteDisplyCustomListAdapter );
        progressBar.setVisibility ( View.GONE );
    }

    @Override
    protected void onDestroy() {
       //ServerConnection.service_base_url = "http://49.206.229.93:8089/BrandServices.svc/";
       ServerConnection.service_base_url = "http://203.192.233.36:8090/BrandServices.svc/";
        super.onDestroy();
    }
}
