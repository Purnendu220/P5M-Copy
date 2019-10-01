package com.p5m.me.firebase_dynamic_link;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

public class FirebaseDynamicLinnk {
    public static void getDynamicLink(Activity context,Intent intent) {
         FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnSuccessListener(context, pendingDynamicLinkData -> {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink = null;
                    if (pendingDynamicLinkData != null) {
                        deepLink = pendingDynamicLinkData.getLink();
                    }

                    if (deepLink != null) {
                        Log.d("Deep Link", "Found deep link !");
                    } else {
                        Log.d("Deep Link", "getDynamicLink: no link found");

                    }
                    // [END_EXCLUDE]
                })
                .addOnFailureListener(context, e -> {
                    Log.w("Deep Link", "getDynamicLink:onFailure", e);
//                        HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 3);

                });
    }
}
