package com.upt.cti.smartwallet;

import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AppState {
    private static AppState singletonObject;
    public static synchronized AppState get(){
        if(singletonObject == null){
            singletonObject = new AppState();
        }
        return singletonObject;
    }
    private DatabaseReference databaseReference;
    private Payment currentPayment;
    public DatabaseReference getDatabaseReference(){
        return this.databaseReference;
    }
    public void setDatabaseReference(DatabaseReference databaseReference){
        this.databaseReference = databaseReference;
    }
    public void setCurrentPayment(Payment currentPayment){
        this.currentPayment = currentPayment;
    }
    public Payment getCurrentPayment(){
        return this.currentPayment;
    }
    public static String getCurrentTimeDate(){
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        return sdfDate.format(now);
    }
}
