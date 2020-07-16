package com.hashexclude.instamojo_sdk_android.restCalls;

/**
 * Created by Sidhant Panda on 28/02/16.
 * Email me at sidhantpanda@gmail.com
 */
class Endpoints {
    private static final String HOST = "https://www.instamojo.com/api/1.1/";

    protected static final String CREATE_PAYMENT = HOST + "payment-requests/",
                                  PAYMENT_STATUS = HOST + "payment-requests/"; // + payment_request_id
}
