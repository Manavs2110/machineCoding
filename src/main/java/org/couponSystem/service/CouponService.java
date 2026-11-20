package org.couponSystem.service;

import org.couponSystem.model.Cart;
import org.couponSystem.model.Coupon;
import org.couponSystem.model.DiscountType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CouponService {

    public boolean isApplicable(Coupon coupon, Cart cart, LocalDate today) {
        if (today.isAfter(coupon.getExpiryDate())) return false;
        if (cart.getTotal() < coupon.getMinOrderValue()) return false;
        if (coupon.getRestaurantId() != null && !coupon.getRestaurantId().equals(cart.getRestaurantId())) return false;
        return true;
    }

    public long calculateDiscount(Coupon coupon, Cart cart) {
        long discount;
        if (coupon.getDiscountType() == DiscountType.FLAT) {
            discount = coupon.getDiscountValue();
        } else {
            discount = cart.getTotal() * coupon.getDiscountValue() / 100;
            discount = Math.min(discount, coupon.getMaxDiscount());
        }
        return Math.min(discount, cart.getTotal());   // final price never goes below 0
    }

    public List<Coupon> getApplicableCoupons(Cart cart, List<Coupon> coupons, LocalDate today) {
        List<Coupon> applicableCoupons = new ArrayList<>();
        for (Coupon coupon : coupons) {
            if (isApplicable(coupon, cart, today)) {
                applicableCoupons.add(coupon);
            }
        }
        return applicableCoupons;
    }

    public Coupon getBestCoupon(Cart cart, List<Coupon> coupons, LocalDate today) {
        Coupon bestCoupon = null;
        long bestDiscount = 0;
        for (Coupon coupon : getApplicableCoupons(cart, coupons, today)) {
            long discount = calculateDiscount(coupon, cart);
            if (discount > bestDiscount) {
                bestDiscount = discount;
                bestCoupon = coupon;
            }
        }
        return bestCoupon;
    }
}
