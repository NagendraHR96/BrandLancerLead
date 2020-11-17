package com.example.brandlancerlead;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.brandlancerlead.brandUtility.ServerConnection;
import com.example.brandlancerlead.brandUtility.WebContentsInterface;
import com.example.brandlancerlead.model.InsertInprojectDetailsFilter;
import com.example.brandlancerlead.model.LeadReportListJsonResult;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SiteProgressActivity extends AppCompatActivity {
    ViewPagerAdapter viewPagerAdapter;
    ViewPager viewPager;
    Document document;
    int size;
    String sendImageName;

    private File pictureSaveFolderPath,sendFile;

    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private static final String TAG_TAKE_PICTURE = "TAKE_PICTURE";

    public static final int REQUEST_CODE_TAKE_PICTURE = 1;
    ArrayList<Uri> listImages=new ArrayList<>();
    String imageFileName;
    ProgressDialog progressDialog;
    // This output image file uri is used by camera app to save taken picture.
    private Uri outputImgUri;
    Button submit,takePic;
    EditText feedback;
    String directoryPath,leadId,userID;
    int indexId;
    Double distance;
    String earAddress;
    private LocationManager locManger;
    private static final String FileName = "ExecutiveLogin";
    private static final String LATITUDE = "Latitude";
    private static final String LONGITUDE = "Longitude";
    private Dialog leadProgress;
    Location locationHold;
    private LocationListener endLocationListener = new LocationListener() {
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                sendToGetAddress(location);
                if (locManger != null) {
                    locManger.removeUpdates(endLocationListener);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (!LocationManager.GPS_PROVIDER.equalsIgnoreCase(provider)) {
                if (locManger != null) {
                    locManger.removeUpdates(endLocationListener);
                }
                Toast.makeText(SiteProgressActivity.this, "Enable GPS", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            if (!LocationManager.GPS_PROVIDER.equalsIgnoreCase(provider)) {
                if (locManger != null) {
                    locManger.removeUpdates(endLocationListener);
                }
                Toast.makeText(SiteProgressActivity.this, "Enable GPS", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void sendToGetAddress(Location location) {

        try {

            locationHold=location;
            Geocoder earthGeo = new Geocoder(SiteProgressActivity.this, new Locale("en", "in"));
            List<Address> earthAddress = earthGeo.getFromLocation(location.getLatitude(), location.getLongitude(), 3);
            if (earthAddress != null && !earthAddress.isEmpty() && !TextUtils.isEmpty(leadId)) {
                SharedPreferences locationPrefer = getSharedPreferences(FileName, MODE_PRIVATE);
                String latiString = locationPrefer.getString(LATITUDE, null);
                String longiString = locationPrefer.getString(LONGITUDE, null);
                if (latiString != null && longiString != null) {
                    double sLati = Double.parseDouble(latiString);
                    double sLongi = Double.parseDouble(longiString);
                    Location startingLoc = new Location("startingPoint");
                    startingLoc.setLatitude(sLati);
                    startingLoc.setLongitude(sLongi);
                    float[] distanceMeter = new float[1];
                    Location.distanceBetween(sLati, sLongi, location.getLatitude(), location.getLongitude(), distanceMeter);
                    double distanceInKM = distanceMeter[0] / 998;
                    String endAddress = earthAddress.get(0).getAddressLine(0);
                    submiteLeadWithImage(distanceInKM, endAddress);

                } else {
                    if (leadProgress != null && leadProgress.isShowing())
                        leadProgress.dismiss();
                    Toast.makeText(SiteProgressActivity.this, "Failed To Update Location Please Try Again", Toast.LENGTH_LONG).show();
                }
            } else if (leadProgress != null && leadProgress.isShowing()) {
                leadProgress.dismiss();
                Toast.makeText(SiteProgressActivity.this, "Failed To Update Location Please Try Again", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SiteProgressActivity.this, "Failed To Update Location Please Try Again", Toast.LENGTH_LONG).show();
        }
    }

    private void submiteLeadWithImage(double distanceInKM, String endAddress) {
        distance=distanceInKM;
        earAddress=endAddress;

        try {
            convertPdf();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_progress);
        pictureSaveFolderPath = getExternalCacheDir();

        viewPager=findViewById(R.id.viewImages);
        takePic=findViewById(R.id.takePic);
        submit=findViewById(R.id.submit);
        feedback=findViewById(R.id.feedback);

        SharedPreferences preferencesc = getSharedPreferences("ExecutiveLogin", MODE_PRIVATE);
        userID = preferencesc.getString("ExecutiveId", null);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        locManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        Intent intent=getIntent();
        if (intent!=null){
            Bundle filterBundle=intent.getExtras();
            if (filterBundle!=null){
                leadId=filterBundle.getString("leadId");
                indexId=filterBundle.getInt("IndexId");
            }
        }


        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(SiteProgressActivity.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SiteProgressActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SiteProgressActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        takePhoto();
                    }
                }catch (Exception ex){
                    Toast.makeText ( SiteProgressActivity.this, "camera permission denied", Toast.LENGTH_LONG ).show ();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(feedback.getText().toString())){
                    Toast.makeText(SiteProgressActivity.this,"Please Enter Feedback", Toast.LENGTH_SHORT).show();

                } else if (listImages.size()>0) {
                    if (locManger == null)
                        locManger = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (ActivityCompat.checkSelfPermission(SiteProgressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SiteProgressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        Toast.makeText(SiteProgressActivity.this, "Enable Gps", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    showLoder();
                    locManger.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, endLocationListener);


                }else {
                    Toast.makeText(SiteProgressActivity.this,"Image Cannot be Empty",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

//    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//
//    private void convertPdf( ) throws IOException, DocumentException {
//
//
//        document =new Document(PageSize.A4,25,25,25,25);
//        directoryPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"SubmitProgress.pdf";
//
//        File file=new File(directoryPath);
//        if (file.exists()){
//            file.delete();
//        }
//        file.createNewFile();
//
//        PdfWriter.getInstance(document, new FileOutputStream(directoryPath)); //  Change pdf's name.
//        document.open();
//        for (int i=0;i<listImages.size();i++) {
//            addImageTodoc(document,listImages.get(i));
//        }
//        document.close();
//        new uploadTask().execute(directoryPath);
//
//
//    }

    private void convertPdf( ) throws IOException, DocumentException {
        document =new Document(PageSize.A4,25,25,25,25);
        directoryPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+"SiteVisit.pdf";

        File file=new File(directoryPath);
        if (file.exists()){
            file.delete();
        }
        file.createNewFile();

        PdfWriter.getInstance(document, new FileOutputStream(directoryPath)); //  Change pdf's name.

        document.open();

        size = listImages.size();

        for(Uri furi : listImages) {

            Glide.with(this)
                    .asBitmap()
                    .load(furi)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            try {
                                addImageTodoc(resource,document);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (DocumentException e) {
                                e.printStackTrace();
                            }
                            if(size > 1){
                                size--;
                            }else {
                                document.close();
                                new uploadTask().execute(directoryPath);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    public void addImageTodoc(Bitmap rotatedBitmap,Document document) throws IOException, DocumentException {

        Bitmap scaled = Bitmap.createScaledBitmap(rotatedBitmap, (int) PageSize.A4.getWidth(), (int) PageSize.A4.getHeight() + 30, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.PNG, 100, stream);
        Image image = Image.getInstance(stream.toByteArray());
        image.setCompressionLevel(PdfStream.BEST_COMPRESSION);

        float rf = image.getImageRotation();
        Rectangle documentRect = document.getPageSize();

        if (scaled.getWidth() > documentRect.getWidth() || scaled.getHeight() > documentRect.getHeight()) {
            //bitmap is larger than page,so set bitmap's size similar to the whole page
            image.scaleAbsolute(documentRect.getWidth(), documentRect.getHeight());
        } else {
            //bitmap is smaller than page, so add bitmap simply.[note: if you want to fill page by stretching image, you may set size similar to page as above]
            image.scaleAbsolute(scaled.getWidth(), scaled.getHeight());
        }
        image.setAbsolutePosition((documentRect.getWidth() - image.getScaledWidth()) / 2, (documentRect.getHeight() - image.getScaledHeight()) / 2);
        document.add(image);
        document.newPage();

    }

    public void addImageTodoc(Document document, String s) throws IOException, DocumentException {


        Image image ;
            image = Image.getInstance(s);


        image.setCompressionLevel(PdfStream.BEST_COMPRESSION);

        Rectangle documentRect = document.getPageSize();
        image.scaleAbsolute(documentRect.getWidth(), documentRect.getHeight());

        image.setAbsolutePosition((documentRect.getWidth() - image.getScaledWidth()) / 2, (documentRect.getHeight() - image.getScaledHeight()) / 2);
        document.add(image);


    }

    public void takePhoto() {

        try {
            // Create a random image file name.
            // String imageFileName = "outputImage_" + System.currentTimeMillis() + ".png";
                imageFileName ="Nagendra"+System.currentTimeMillis()+".png";

            // Construct a output file to save camera taken picture temporary.
            File outputImageFile = new File(pictureSaveFolderPath, imageFileName);
            sendFile = outputImageFile;

            // If cached temporary file exist then delete it.
            if (outputImageFile.exists()) {
                outputImageFile.delete();
            }

            // Create a new temporary file.
            outputImageFile.createNewFile();

            // Get the output image file Uri wrapper object.
            outputImgUri = getImageFileUriByOsVersion(outputImageFile);

            // Startup camera app.
            // Create an implicit intent which require take picture action..
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Specify the output image uri for the camera app to save taken picture.
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputImgUri);
            // Start the camera activity with the request code and waiting for the app process result.
            startActivityForResult(cameraIntent, REQUEST_CODE_TAKE_PICTURE);

        }catch(IOException ex) {
            Toast.makeText(SiteProgressActivity.this,ex.getMessage(),Toast.LENGTH_SHORT).show();
            Log.e(TAG_TAKE_PICTURE, ex.getMessage(), ex);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Process result for camera activity.
        if (requestCode == REQUEST_CODE_TAKE_PICTURE && resultCode == RESULT_OK) {
            listImages.add(outputImgUri);
            viewPagerAdapter.updateImageuri(listImages);

        }

    }

    private Uri getImageFileUriByOsVersion(File file) {
        Uri ret = null;

        // Get output image unique resource identifier. This uri is used by camera app to save taken picture temporary.
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            Context ctx = getApplicationContext();
            //ret= FileProvider.getUriForFile(this,  BuildConfig.APPLICATION_ID + ".fileprovider", file);
            ret = FileProvider.getUriForFile(ctx, "com.example.brandlancerlead.provider", file);
        }else
        {
            // For android os version less than 7.0 there are no safety issue,
            // So we can get the output image uri by file real local path directly.
            ret = Uri.fromFile(file);
        }
        return ret;
    }

    public void removeList(int pos) {
        if(listImages.size() > 0){
            viewPagerAdapter.reImageuri(listImages.get(pos));
            listImages.remove(pos);
        }
    }
    private void showLoder() {
        if (leadProgress == null) {
            leadProgress = new Dialog(SiteProgressActivity.this);
            leadProgress.setContentView(new ProgressBar(SiteProgressActivity.this));
            if (leadProgress.getWindow() != null)
                leadProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        if (leadProgress != null && !leadProgress.isShowing()) {
            leadProgress.setCancelable(false);
            leadProgress.show();
        }
    }
    private void addLatiLongiToShared(Location flocation,String message) {
        if(flocation != null) {
            SharedPreferences preferences = getSharedPreferences(FileName, MODE_PRIVATE);
            SharedPreferences.Editor write = preferences.edit();
            write.putString(LATITUDE, String.valueOf(flocation.getLatitude()));
            write.putString(LONGITUDE, String.valueOf(flocation.getLongitude()));
            write.apply();

            progressDialog.dismiss();
            Toast.makeText(SiteProgressActivity.this,message, Toast.LENGTH_SHORT).show();
            Intent back=new Intent();
            setResult(RESULT_OK);
            finish();
        }else {

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult ( requestCode, permissions, grantResults );
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            }
            else {
                Toast.makeText ( this, "camera permission denied", Toast.LENGTH_LONG ).show ();
            }
        }
    }

    class uploadTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(SiteProgressActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            FTPClient con = null;
            String resultset = "";
            try {
                con = new FTPClient();
                con.connect(ServerConnection.fttpUrl);

                if (con.login("administrator", "Neory_321")) {
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType(FTP.BINARY_FILE_TYPE);

                    FileInputStream in = new FileInputStream(new File(params[0]));
                    SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss", Locale.US);
                    sendImageName=leadId+"_"+sdf.format(new Date())+".pdf";
                    boolean result = con.storeFile("Files/Resources/In_Project_Photos/"+sendImageName,in);
                    in.close();
                    if (result) {
                        resultset = "succeeded";
                    } else {
                        resultset = "failed";
                    }
                    con.logout();
                    con.disconnect();

                } else {
                    resultset = con.getStatus();
                }
            } catch (Exception e) {
                resultset = e.getLocalizedMessage();
            }
            return resultset;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("succeeded")){

                InsertInprojectDetailsFilter filter = new InsertInprojectDetailsFilter();
                filter.setLeadID(Integer.valueOf(leadId));
                filter.setPlace(earAddress);
                filter.setRemarks(feedback.getText().toString());
                filter.setFilePath(sendImageName);
                filter.setIndexID(indexId);
                filter.setDistance(String.valueOf(distance));

                WebContentsInterface contentsInterface = ServerConnection.createRetrofitConnection(WebContentsInterface.class);
                Call<LeadReportListJsonResult> stringCall = contentsInterface.insertSiteProgress(filter);
                stringCall.enqueue(new Callback<LeadReportListJsonResult>() {
                    @Override
                    public void onResponse(Call<LeadReportListJsonResult> call, Response<LeadReportListJsonResult> response) {
                        if (response.code() == 200) {
                            LeadReportListJsonResult jsonResult = response.body();
                            if (jsonResult != null && jsonResult.isResult()) {
                                addLatiLongiToShared(locationHold,jsonResult.getResultmessage());
                            } else {
                                Toast.makeText(SiteProgressActivity.this, jsonResult.getResultmessage(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        } else {
                            Toast.makeText(SiteProgressActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }

                    @Override
                    public void onFailure(Call<LeadReportListJsonResult> call, Throwable t) {
                        progressDialog.dismiss();

                    }
                });

            }else {
                Toast.makeText(SiteProgressActivity.this,"Failed Upload Details",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
