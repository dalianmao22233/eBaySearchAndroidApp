package com.cs571.nanjiang.ebaysearch;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {


    EditText keyword;
    EditText priceFrom;
    EditText priceTo;
    Spinner sortBy;
    TextView error;
    Button btnSearch;
    Button btnClear;


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        keyword = (EditText) findViewById(R.id.keywordInput);
        priceFrom = (EditText) findViewById(R.id.priceFromInput);
        priceTo = (EditText) findViewById(R.id.priceToInput);
        btnSearch = (Button) findViewById(R.id.searchBtn);
        btnClear = (Button) findViewById(R.id.clearBtn);
        error = (TextView) findViewById(R.id.error);
        sortBy = (Spinner) findViewById(R.id.sortBySpiner);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyword.setText("");
                priceTo.setText("");
                priceFrom.setText("");
                sortBy.setSelection(0);
                error.setText("");
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String keywordValue = keyword.getText().toString().trim();
                String priceFromValue = priceFrom.getText().toString().trim();
                String priceToValue = priceTo.getText().toString().trim();
                String sortByValue = sortBy.getSelectedItem().toString();
                if (isValid(keywordValue, priceFromValue, priceToValue)) {
                    getJSON(keywordValue, priceFromValue, priceToValue, sortByValue);
                }
            }
        });
    }

    public boolean isValid(String keyword, String priceFrom, String priceTo) {
        if (keyword.length() == 0) {
            error.setText("Please enter a keyword");
            return false;
        }

        double min = 0.0;
        double max = 0.0;

        if (priceFrom.length() > 0) {
            try {
                min = Float.parseFloat(priceFrom);
            } catch (NumberFormatException e) {
                error.setText("Price should be a valid number");
                return false;
            }

            if (min < 0) {
                error.setText("Price From must be a positive integer or decimal number");
                return false;
            }

        }

        if (priceTo.length() > 0) {
            try {
                max = Float.parseFloat(priceTo);
            } catch (NumberFormatException e) {
                error.setText("Price should be a valid number");
                return false;
            }

            if (max < 0) {
                error.setText("Price To must be a positive integer or decimal number");
                return false;
            }

            if (priceFrom.length() > 0 && min > max) {
                error.setText("Price To must not be less than Price From");
                return false;
            }

        }

        error.setText("");
        return true;


    }

    private class jsonParse extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setUseCaches(false);
                conn.setRequestMethod("GET");
                conn.connect();
                if (conn.getResponseCode() == 200 || conn.getResponseCode() == 201) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    return sb.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res_string) {
            String resultFor = "";
            JSONObject json = null;
            try {
                json = new JSONObject(res_string);
                if (json.getString("ack").equals("Success")) {
                    ArrayList<ProductParcel> res_json = new ArrayList<ProductParcel>();
                    int resultCount = json.getInt("resultCount");

                    if (resultCount > 0) {
                        resultFor = "Result for '" + ((EditText) findViewById(R.id.keywordInput)).getText().toString() + "'";

                        for (int i = 0; i < 5; i++) {
                            ProductParcel item = new ProductParcel();
                            JSONObject basicInfo = json.getJSONObject("item" + i).getJSONObject("basicInfo");
                            JSONObject sellerInfo = json.getJSONObject("item" + i).getJSONObject("sellerInfo");
                            JSONObject shippingInfo = json.getJSONObject("item" + i).getJSONObject("shippingInfo");

                            // Get Basic Info
                            item.title = basicInfo.getString("title");
                            item.viewItemURL = basicInfo.getString("viewItemURL");
                            item.galleryURL = basicInfo.getString("galleryURL");
                            item.pictureURLSuperSize = (basicInfo.getString("pictureURLSuperSize").length() > 0) ? basicInfo.getString("pictureURLSuperSize") : item.galleryURL;
                            item.convertedCurrentPrice = "Price: $" + basicInfo.getString("convertedCurrentPrice");
                            item.shippingServiceCost = (basicInfo.getString("shippingServiceCost").equals("0.0") || basicInfo.getString("shippingServiceCost").length() == 0) ? "(Free Shipping)" : "(+$" + basicInfo.getString("shippingServiceCost") + " Shipping)";
                            item.conditionDisplayName = (basicInfo.getString("conditionDisplayName").length() > 0) ? basicInfo.getString("conditionDisplayName") : "N/A";
                            item.listingType = basicInfo.getString("listingType");
                            item.location = basicInfo.getString("location");
                            item.categoryName = basicInfo.getString("categoryName");
                            item.topRatedListing = basicInfo.getString("topRatedListing");

                            // Get Seller Info
                            item.sellerUserName = sellerInfo.getString("sellerUserName");
                            item.feedbackScore = sellerInfo.getString("feedbackScore");
                            item.positiveFeedbackPercent = sellerInfo.getString("positiveFeedbackPercent");
                            item.feedbackRatingStar = sellerInfo.getString("feedbackRatingStar");
                            item.topRatedSeller = sellerInfo.getString("topRatedSeller");
                            item.sellerStoreName = (sellerInfo.getString("sellerStoreName").length() > 0) ? sellerInfo.getString("sellerStoreName") : "N/A";
                            item.sellerStoreURL = sellerInfo.getString("sellerStoreURL");


                            // Get Shipping Info
                            item.shippingType = shippingInfo.getString("shippingType");
                            item.shipToLocations = shippingInfo.getString("shipToLocations");
                            item.expeditedShipping = shippingInfo.getString("expeditedShipping");
                            item.oneDayShippingAvailable = shippingInfo.getString("oneDayShippingAvailable");
                            item.returnsAccepted = shippingInfo.getString("returnsAccepted");
                            item.handlingTime = shippingInfo.getString("handlingTime") + " day(s)";

                            res_json.add(item);
                        }

                        Intent tmpIntent = new Intent(getApplicationContext(), ResultActivity.class);
                        Bundle tmpBundle = new Bundle();
                        tmpBundle.putParcelableArrayList("res_json", res_json);
                        tmpBundle.putString("resultFor", resultFor);
                        tmpIntent.putExtras(tmpBundle);
                        MainActivity.this.startActivity(tmpIntent);
                    } else {
                        //resultFor = "No Results Found";
                        error.setText("No Results Found");
                    }
                } else {
                    error.setText("No Results Found");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void getJSON(String keyword, String pFrom, String pTo, String sortBy) {
        String apicall = "http://nanjiang571-env.elasticbeanstalk.com/?itemsPerRange=5&pageNumber=1";
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (Exception e) {
            keyword = keyword;
        }
        apicall += "&keyword=" + keyword;

        if (pFrom.trim().length() > 0) {
            apicall += "&minprice=" + pFrom;
        }

        if (pTo.trim().length() > 0) {
            apicall += "&maxprice=" + pTo;
        }

        if (sortBy.equals("Best Match")) apicall += "&sort=BestMatch";
        if (sortBy.equals("Price: highest first")) apicall += "&sort=CurrentPriceHighest";
        if (sortBy.equals("Price + Shipping: highest first"))
            apicall += "&sort=PricePlusShippingHighest";
        if (sortBy.equals("Price + Shipping: lowest first"))
            apicall += "&sort=PricePlusShippingLowest";

        jsonParse data = new jsonParse();

        Log.v("apicall", apicall);

        // transfer URL to onPostExecute
        data.execute(apicall);
    }


}
