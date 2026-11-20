package org.couponSystem;

import org.couponSystem.model.Cart;
import org.couponSystem.model.CartItem;
import org.couponSystem.model.Coupon;
import org.couponSystem.model.DiscountType;
import org.couponSystem.service.CouponService;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String args[]){
        LocalDate today = LocalDate.of(2025, 3, 10);

        Cart cart = new Cart("u-1", "dominos");
        cart.addItem(new CartItem("Pizza", 300, 2));
        cart.addItem(new CartItem("Coke", 60, 1));          // total = 660

        List<Coupon> coupons = List.of(
                new Coupon("FLAT100",  DiscountType.FLAT,       100, 0,   500, null,      today.plusDays(5)),
                new Coupon("PCT20",    DiscountType.PERCENTAGE,  20, 150, 300, null,      today.plusDays(5)),
                new Coupon("DOMINOS50",DiscountType.PERCENTAGE,  50, 200, 0,   "dominos", today.plusDays(5)),
                new Coupon("KFC30",    DiscountType.PERCENTAGE,  30, 100, 0,   "kfc",     today.plusDays(5)),   // wrong restaurant
                new Coupon("BIG1000",  DiscountType.FLAT,       300, 0,  1000, null,      today.plusDays(5)),   // min order not met
                new Coupon("OLD10",    DiscountType.FLAT,        10, 0,     0, null,      today.minusDays(1))   // expired
        );

        CouponService couponService = new CouponService();
        System.out.println("Cart total: " + cart.getTotal());

        List<Coupon> applicableCoupons = couponService.getApplicableCoupons(cart, coupons, today);
        System.out.println("Applicable: " + applicableCoupons);      // [FLAT100, PCT20, DOMINOS50]
        for (Coupon coupon : applicableCoupons) {
            System.out.println(coupon + " saves " + couponService.calculateDiscount(coupon, cart));
        }
        System.out.println("Best: " + couponService.getBestCoupon(cart, coupons, today));   // DOMINOS50 (200)
    }
}
