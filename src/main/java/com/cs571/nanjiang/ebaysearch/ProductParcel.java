package com.cs571.nanjiang.ebaysearch;

import android.os.Parcelable;
import android.os.Parcel;

import android.os.Parcelable;

/**
 * Created by slyar on 4/30/15.
 */
public class ProductParcel implements Parcelable {

    // Basic Info
    public String title = "";
    public String viewItemURL = "";
    public String galleryURL = "";
    public String pictureURLSuperSize = "";
    public String convertedCurrentPrice = "";
    public String shippingServiceCost = "";
    public String conditionDisplayName = "";
    public String listingType = "";
    public String location = "";
    public String categoryName = "";
    public String topRatedListing = "";

    // Seller Info
    public String sellerUserName = "";
    public String feedbackScore = "";
    public String positiveFeedbackPercent = "";
    public String feedbackRatingStar = "";
    public String topRatedSeller = "";
    public String sellerStoreName = "";
    public String sellerStoreURL = "";

    // Shipping Info
    public String shippingType = "";
    public String shipToLocations = "";
    public String expeditedShipping = "";
    public String oneDayShippingAvailable = "";
    public String returnsAccepted = "";
    public String handlingTime = "";

    public ProductParcel(Parcel parcel) {

        this.title = parcel.readString();
        this.viewItemURL = parcel.readString();
        this.galleryURL = parcel.readString();
        this.pictureURLSuperSize = parcel.readString();
        this.convertedCurrentPrice = parcel.readString();
        this.shippingServiceCost = parcel.readString();
        this.conditionDisplayName = parcel.readString();
        this.listingType = parcel.readString();
        this.location = parcel.readString();
        this.categoryName = parcel.readString();
        this.topRatedListing = parcel.readString();

        this.sellerUserName = parcel.readString();
        this.feedbackScore = parcel.readString();
        this.positiveFeedbackPercent = parcel.readString();
        this.feedbackRatingStar = parcel.readString();
        this.topRatedSeller = parcel.readString();
        this.sellerStoreName = parcel.readString();
        this.sellerStoreURL = parcel.readString();

        this.shippingType = parcel.readString();
        this.shipToLocations = parcel.readString();
        this.expeditedShipping = parcel.readString();
        this.oneDayShippingAvailable = parcel.readString();
        this.returnsAccepted = parcel.readString();
        this.handlingTime = parcel.readString();
    }

    public ProductParcel() {
        super();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(viewItemURL);
        parcel.writeString(galleryURL);
        parcel.writeString(pictureURLSuperSize);
        parcel.writeString(convertedCurrentPrice);
        parcel.writeString(shippingServiceCost);
        parcel.writeString(conditionDisplayName);
        parcel.writeString(listingType);
        parcel.writeString(location);
        parcel.writeString(categoryName);
        parcel.writeString(topRatedListing);

        parcel.writeString(sellerUserName);
        parcel.writeString(feedbackScore);
        parcel.writeString(positiveFeedbackPercent);
        parcel.writeString(feedbackRatingStar);
        parcel.writeString(topRatedSeller);
        parcel.writeString(sellerStoreName);
        parcel.writeString(sellerStoreURL);

        parcel.writeString(shippingType);
        parcel.writeString(shipToLocations);
        parcel.writeString(expeditedShipping);
        parcel.writeString(oneDayShippingAvailable);
        parcel.writeString(returnsAccepted);
        parcel.writeString(handlingTime);
    }

    public static final Parcelable.Creator<ProductParcel> CREATOR = new Parcelable.Creator<ProductParcel>() {

        @Override
        public ProductParcel createFromParcel(Parcel parcel) {
            return new ProductParcel(parcel);
        }

        public ProductParcel[] newArray(int size) {
            return new ProductParcel[size];
        }
    };
}
