package org.couponSystem.model;

import java.time.LocalDate;

public class Coupon {
    String code;
    DiscountType discountType;
    long discountValue;     // rupees for FLAT, percent for PERCENTAGE
    long maxDiscount;       // cap for PERCENTAGE coupons
    long minOrderValue;
    String restaurantId;    // null means it works on every restaurant (platform coupon)
    LocalDate expiryDate;

    public Coupon(String code, DiscountType discountType, long discountValue, long maxDiscount,
                  long minOrderValue, String restaurantId, LocalDate expiryDate) {
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.maxDiscount = maxDiscount;
        this.minOrderValue = minOrderValue;
        this.restaurantId = restaurantId;
        this.expiryDate = expiryDate;
    }

    public String getCode() {
        return code;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public long getDiscountValue() {
        return discountValue;
    }

    public long getMaxDiscount() {
        return maxDiscount;
    }

    public long getMinOrderValue() {
        return minOrderValue;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    @Override
    public String toString() {
        return code;
    }
}
