package org.stockMarket;

import org.stockMarket.service.OrderService;
import org.stockMarket.strategy.FifoMatchingStrategy;
import org.stockMarket.strategy.ProRataMatchingStrategy;

public class Main {
    public static void main(String[] args) {
        // ---------- FIFO walkthrough ----------
        OrderService fifoExchange = newExchange();
        fifoExchange.addSellOrder("S1", 1, "INFY", 10, 100);
        fifoExchange.addSellOrder("S2", 2, "INFY", 5, 99);
        fifoExchange.addSellOrder("S3", 3, "INFY", 20, 100);

        check("buy fills cheapest level first, then oldest at 100",
                fifoExchange.addBuyOrder("B1", 4, "INFY", 12, 100), "[B1/S2 5 @ 99, B1/S1 7 @ 100]");
        check("last trade price", fifoExchange.getLastTradePrice("INFY"), "100");
        check("buy below best sell rests",
                fifoExchange.addBuyOrder("B2", 5, "INFY", 4, 98), "[]");
        check("sell trades at resting buy's price, remainder rests",
                fifoExchange.addSellOrder("S4", 6, "INFY", 6, 97), "[B2/S4 4 @ 98]");
        check("sell book", fifoExchange.getOpenSellOrders("INFY"),
                "[S4 SELL 2 @ 97, S1 SELL 3 @ 100, S3 SELL 20 @ 100]");
        check("buy book empty", fifoExchange.getOpenBuyOrders("INFY"), "[]");
        check("price moved down", fifoExchange.getLastTradePrice("INFY"), "98");

        // ---------- Trade price is the resting order's price ----------
        OrderService exchange = newExchange();
        exchange.addSellOrder("S1", 1, "INFY", 10, 90);
        check("resting sell @ 90, buy @ 100 -> trades at 90",
                exchange.addBuyOrder("B1", 2, "INFY", 10, 100), "[B1/S1 10 @ 90]");
        exchange.addBuyOrder("B2", 2, "INFY", 10, 100);
        check("resting buy @ 100, sell @ 90 -> trades at 100",
                exchange.addSellOrder("S2", 1, "INFY", 10, 90), "[B2/S2 10 @ 100]");

        // ---------- Big buyer, small supply ----------
        exchange = newExchange();
        exchange.addSellOrder("S1", 1, "INFY", 10, 99);
        exchange.addSellOrder("S2", 1, "INFY", 20, 100);
        exchange.addSellOrder("S3", 1, "INFY", 50, 105);
        check("buyer stops at its price limit",
                exchange.addBuyOrder("B1", 2, "INFY", 100, 100), "[B1/S1 10 @ 99, B1/S2 20 @ 100]");
        check("remainder rests on buy side", exchange.getOpenBuyOrders("INFY"), "[B1 BUY 70 @ 100]");
        check("next seller fills the waiting buyer",
                exchange.addSellOrder("S4", 3, "INFY", 30, 95), "[B1/S4 30 @ 100]");

        // ---------- Cancel ----------
        exchange = newExchange();
        exchange.addSellOrder("S1", 1, "INFY", 10, 100);
        exchange.addSellOrder("S2", 1, "INFY", 10, 100);
        check("cancel open order", exchange.cancelOrder("S1"), "true");
        check("cancel twice", exchange.cancelOrder("S1"), "false");
        check("cancelled order is skipped",
                exchange.addBuyOrder("B1", 2, "INFY", 5, 100), "[B1/S2 5 @ 100]");
        exchange.addBuyOrder("B2", 2, "INFY", 5, 100);
        check("cancel filled order", exchange.cancelOrder("S2"), "false");

        // ---------- Separate stocks ----------
        exchange = newExchange();
        exchange.addSellOrder("S1", 1, "INFY", 10, 100);
        check("TCS buy doesn't touch INFY sells",
                exchange.addBuyOrder("B1", 2, "TCS", 10, 100), "[]");

        // ---------- Pro-rata ----------
        exchange = newExchange();
        exchange.setMatchingStrategy(new ProRataMatchingStrategy());
        exchange.addSellOrder("S1", 1, "INFY", 30, 100);
        exchange.addSellOrder("S2", 2, "INFY", 10, 100);
        check("pro-rata split 7 + 2, leftover 1 to oldest",
                exchange.addBuyOrder("B1", 3, "INFY", 10, 100), "[B1/S1 8 @ 100, B1/S2 2 @ 100]");
        check("pro-rata book", exchange.getOpenSellOrders("INFY"), "[S1 SELL 22 @ 100, S2 SELL 8 @ 100]");
        check("pro-rata fills whole level, then moves on",
                exchange.addBuyOrder("B2", 3, "INFY", 40, 100), "[B2/S1 22 @ 100, B2/S2 8 @ 100]");
        check("remainder rests", exchange.getOpenBuyOrders("INFY"), "[B2 BUY 10 @ 100]");
    }

    private static OrderService newExchange() {
        OrderService exchange = new OrderService(new FifoMatchingStrategy());
        exchange.addStock("INFY", "Infosys", 100);
        exchange.addStock("TCS", "Tata Consultancy Services", 3500);
        return exchange;
    }

    private static void check(String scenario, Object actual, String expected) {
        String result = String.valueOf(actual).equals(expected) ? "PASS" : "FAIL";
        System.out.println(result + "  " + scenario + "  ->  " + actual
                + (result.equals("FAIL") ? "   (expected " + expected + ")" : ""));
    }
}
