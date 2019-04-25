package com.p5m.me.firebase_dynamic_link;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class FirebaseDynamicLinnk {
    public static void getDynamicLink(Activity context,Intent intent) {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(context, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }

                        if (deepLink != null) {
                            Log.d("Deep Link", "Found deep link ClassProfile!");
                        } else {
                            Log.d("Deep Link", "getDynamicLink: no link found");

                        }
                        // [END_EXCLUDE]
                    }
                })
                .addOnFailureListener(context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Deep Link", "getDynamicLink:onFailure", e);
//                        HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 3);

                    }
                });
    }
}
