package com.cs571.nanjiang.ebaysearch;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import org.w3c.dom.Text;


public class DetailActivity extends ActionBarActivity {

    private LinearLayout tab1, tab2, tab3;
    private Button basic, seller, shipping, buynow;
    private ImageButton fbshare;

    private CallbackManager callbackFB;
    private ShareDialog shareFB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_details);
        Intent intent = getIntent();
        if (intent != null) {
            final ProductParcel item = intent.getParcelableExtra("item");
            getItemDetails(item);

            // get 3 tabs
            FrameLayout tabs = (FrameLayout) findViewById(R.id.frameLayout);
            tab1 = (LinearLayout) tabs.findViewWithTag(String.valueOf(1));
            tab2 = (LinearLayout) tabs.findViewWithTag(String.valueOf(2));
            tab3 = (LinearLayout) tabs.findViewWithTag(String.valueOf(3));

            // default set tab to 1
            setTab(1);

            // get Buttons
            basic = (Button) findViewById(R.id.detail_btn_basicinfo);
            seller = (Button) findViewById(R.id.detail_btn_seller);
            shipping = (Button) findViewById(R.id.detail_btn_shipping);
            buynow = (Button) findViewById(R.id.detail_btn_buy);
            fbshare = (ImageButton) findViewById(R.id.detail_btn_facebook);

//            basic.setBackgroundColor(Color.parseColor("#c0c0c0"));
//            seller.setBackgroundColor(Color.parseColor("#c0c0c0"));
//            shipping.setBackgroundColor(Color.parseColor("#c0c0c0"));

            basic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTab(1);
                    basic.setBackgroundColor(Color.parseColor("#66b2ff"));
                    seller.setBackgroundColor(Color.parseColor("#c0c0c0"));
                    shipping.setBackgroundColor(Color.parseColor("#c0c0c0"));
                }
            });

            seller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTab(2);
                    basic.setBackgroundColor(Color.parseColor("#c0c0c0"));
                    seller.setBackgroundColor(Color.parseColor("#66b2ff"));
                    shipping.setBackgroundColor(Color.parseColor("#c0c0c0"));
                }
            });

            shipping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setTab(3);
                    basic.setBackgroundColor(Color.parseColor("#c0c0c0"));
                    seller.setBackgroundColor(Color.parseColor("#c0c0c0"));
                    shipping.setBackgroundColor(Color.parseColor("#66b2ff"));

                }
            });

            buynow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newPage = new Intent(Intent.ACTION_VIEW);
                    newPage.setData(Uri.parse(item.viewItemURL));
                    startActivity(newPage);

                }
            });


            //Facebook
            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackFB = CallbackManager.Factory.create();
            shareFB = new ShareDialog(this);

            shareFB.registerCallback(callbackFB, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    if (result.getPostId() != null) {
                        Toast.makeText(DetailActivity.this, "Posted Story, ID:" + result.getPostId(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(DetailActivity.this, "Post Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getApplicationContext(), "Post Cancelled", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException e) {
                    Toast.makeText(getApplicationContext(), "Post Failed", Toast.LENGTH_SHORT).show();
                }
            });

            fbshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (shareFB.canShow(ShareLinkContent.class)) {
                        ShareLinkContent content = new ShareLinkContent.Builder()
                                .setContentTitle(item.title)
                                .setImageUrl(Uri.parse(item.galleryURL))
                                .setContentUrl(Uri.parse(item.viewItemURL))
                                .setContentDescription("Price: " + item.convertedCurrentPrice + item.shippingServiceCost + ", Location: " + item.location)
                                .build();
                        shareFB.show(content);
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackFB.onActivityResult(requestCode, resultCode, data);
    }

    public void setTab(int tab) {

        this.tab1.setVisibility(View.INVISIBLE);
        this.tab2.setVisibility(View.INVISIBLE);
        this.tab3.setVisibility(View.INVISIBLE);

        switch (tab) {
            case 1:
                this.tab1.setVisibility(View.VISIBLE);
                break;
            case 2:
                this.tab2.setVisibility(View.VISIBLE);
                break;
            case 3:
                this.tab3.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }

    }

    private void getItemDetails(ProductParcel item) {

        // pic
        ImageView detail_image = (ImageView) findViewById(R.id.detail_image);
        new getIMG(detail_image).execute(item.pictureURLSuperSize);

        // title
        TextView detail_title = (TextView) findViewById(R.id.detail_title);
        detail_title.setText(item.title);

        // Price
        TextView detail_price = (TextView) findViewById(R.id.detail_price);
        detail_price.setText(item.convertedCurrentPrice + " " + item.shippingServiceCost);

        // location
        TextView detail_location = (TextView) findViewById(R.id.detail_location);
        detail_location.setText(item.location);

        // top listed
        ImageView detail_btn_topping = (ImageView) findViewById(R.id.detail_btn_topping);
        if (item.topRatedListing.equals("true")) {
            detail_btn_topping.setVisibility(View.VISIBLE);
        } else {
            detail_btn_topping.setVisibility(View.INVISIBLE);
        }

        // Basic Info

        TextView detail_categoryName = (TextView) findViewById(R.id.categoryName);
        detail_categoryName.setText(item.categoryName);

        TextView detail_condition = (TextView) findViewById(R.id.condition);
        detail_condition.setText(item.conditionDisplayName);

        TextView detail_buyingFormat = (TextView) findViewById(R.id.buyingFormat);
        detail_buyingFormat.setText(item.listingType);

        // Seller Info
        TextView detail_username = (TextView) findViewById(R.id.username);
        detail_username.setText(item.sellerUserName);

        TextView detail_feedback = (TextView) findViewById(R.id.feedback);
        detail_feedback.setText(item.feedbackScore);

        TextView detail_positive = (TextView) findViewById(R.id.positive);
        detail_positive.setText(item.positiveFeedbackPercent);

        TextView detail_feedbackrating = (TextView) findViewById(R.id.feedbackrating);
        detail_feedbackrating.setText(item.feedbackRatingStar);

        ImageView detail_toprated = (ImageView) findViewById(R.id.toprated);
        if (item.topRatedListing.equals("true")) {
            detail_toprated.setImageResource(R.drawable.yes);
        } else {
            detail_toprated.setImageResource(R.drawable.no);
        }

        TextView detail_storename = (TextView) findViewById(R.id.storename);
        detail_storename.setText(item.sellerStoreName);

        // Shipping Info

        TextView detail_shippingtype = (TextView) findViewById(R.id.shippingtype);
        detail_shippingtype.setText(item.shippingType);

        TextView detail_handlingtime = (TextView) findViewById(R.id.handlingtime);
        detail_handlingtime.setText(item.handlingTime);

        TextView detail_shippinglocation = (TextView) findViewById(R.id.shippinglocation);
        detail_shippinglocation.setText(item.shipToLocations);

        ImageView detail_expeditedshipping = (ImageView) findViewById(R.id.expeditedshipping);
        if (item.expeditedShipping.equals("true")) {
            detail_expeditedshipping.setImageResource(R.drawable.yes);
        } else {
            detail_expeditedshipping.setImageResource(R.drawable.no);
        }

        ImageView detail_onedayshipping = (ImageView) findViewById(R.id.onedayshipping);
        if (item.oneDayShippingAvailable.equals("true")) {
            detail_onedayshipping.setImageResource(R.drawable.yes);
        } else {
            detail_onedayshipping.setImageResource(R.drawable.no);
        }

        ImageView detail_returnaccepted = (ImageView) findViewById(R.id.returnaccepted);
        if (item.returnsAccepted.equals("true")) {
            detail_returnaccepted.setImageResource(R.drawable.yes);
        } else {
            detail_returnaccepted.setImageResource(R.drawable.no);
        }


    }


}
