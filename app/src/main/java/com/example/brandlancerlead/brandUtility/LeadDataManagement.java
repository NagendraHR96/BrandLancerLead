package com.example.brandlancerlead.brandUtility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LeadDataManagement extends SQLiteOpenHelper {
    private static final String DATABASENAME ="LeadManagement.db";

    private static final int Version =1;

    private static final String LEADTABLE ="tblLeadData";
    private static final String CALLLOGTable ="tblCallLog";

    public LeadDataManagement( Context context) {
        super(context,DATABASENAME,null,Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "Create Table "+LEADTABLE+" ( _id integer Primary key AutoIncrement,leadId text,startLati double,startLongi double," +
                "startAddress text,startTime text,endLati double,endLongi double,endAddress text,endTime text,metType text,feedBack text," +
                "statusId text,fsDate text,fsTime text,rejection txt,receiptNo text,receiptAmount text,metImgPath text,receiptImgPath text," +
                "purp integer,booking text,fttpMet text,fttpreceipt text)");
    }

    public  boolean  updateEndPointDetails(double endLatipoint, double endLongipoint,String endTime,String metSt,String feedBack,
                                        String status,String fDate,String ftime,String rejection,String recNo,String recAmount,
                                           String metImg,String recImg,String lead,int purpose,String book) {
        SQLiteDatabase insertDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("endLati",endLatipoint);
        values.put("endLongi",endLongipoint);
        values.put("endTime",endTime);

        values.put("metType",metSt);
        values.put("feedBack",feedBack);
        values.put("statusId",status);
        values.put("fsDate",fDate);
        values.put("fsTime",ftime);
        values.put("receiptNo",recNo);
        values.put("receiptAmount",recAmount);
        values.put("metImgPath",metImg);
        values.put("receiptImgPath",recImg);
        values.put("rejection",rejection);
        values.put("purp",purpose);
        values.put("booking",book);
      long  rows = insertDb.update(LEADTABLE,values,"leadId =? ",new String[]{lead});
      if(rows > 0){
          return  true;
      }else{
          return   false;
      }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion == 1){
            onCreate(db);
        }
    }
    public  void insertStartData(String lead,double locLati,double locLongi,String time){
        SQLiteDatabase insertDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("leadId",lead);
        values.put("startLati",locLati);
        values.put("startLongi",locLongi);
        values.put("startTime",time);
        Cursor exist = getLeadRecord(lead);
        long rows=0;
        if(exist != null && exist.getCount()>0) {
            values.remove("leadId");
            rows = insertDb.update(LEADTABLE,values,"leadId =? ",new String[]{lead+""});
        }else {
            rows = insertDb.insert(LEADTABLE,null,values);

        }


    }

    public Cursor getLeadRecord(String leadValue) {
        SQLiteDatabase leadData = this.getWritableDatabase();
        Cursor exist = leadData.rawQuery("select * from "+ LEADTABLE + " where leadId =? ",new String[]{leadValue});
        return  exist;
    }

    public void updateLocation(String valueAddress, String leadValue) {
        SQLiteDatabase insertDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("startAddress",valueAddress);
        insertDb.update(LEADTABLE,values,"leadId =? ",new String[]{leadValue});

    }
    public void updatefttpMetfName(String fname, String leadValue) {
        SQLiteDatabase insertDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fttpreceipt",fname);
        insertDb.update(LEADTABLE,values,"leadId =? ",new String[]{leadValue});

    }
    public void updatefttpRecfName(String fname, String leadValue) {
        SQLiteDatabase insertDb = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fttpMet",fname);
        insertDb.update(LEADTABLE,values,"leadId =? ",new String[]{leadValue});

    }



}
