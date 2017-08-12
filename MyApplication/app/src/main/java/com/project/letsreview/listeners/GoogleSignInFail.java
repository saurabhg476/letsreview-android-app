package com.project.letsreview.listeners;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by sayan94 on 12-Aug-17.
 */

public class GoogleSignInFail implements  GoogleApiClient.OnConnectionFailedListener{

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG,"Sorry, connection failed");
    }
}



