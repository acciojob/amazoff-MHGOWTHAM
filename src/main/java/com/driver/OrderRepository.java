package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    HashMap<String, Order> orderDb = new HashMap<>();
    HashMap<String, DeliveryPartner> partnerDb = new HashMap<>();
    HashMap<String, List<String>> pairDb = new HashMap<>();
    HashMap<String, String> assignedDb = new HashMap<>();

    public String addOrder(Order order) {
        orderDb.put(order.getId(), order);
        return "Added";
    }

    public String addPartner(String partnerId) {
        DeliveryPartner partner = new DeliveryPartner(partnerId);
        partnerDb.put(partnerId, partner);
        return "Added";
    }

    public String addOrderPartnerPair(String orderId, String partnerId) {
        List<String> list = pairDb.getOrDefault(partnerId, new ArrayList<>());
        list.add(orderId);
        pairDb.put(partnerId, list);
        assignedDb.put(orderId, partnerId);
        DeliveryPartner partner = partnerDb.get(partnerId);
        partner.setNumberOfOrders(list.size());
        return "Added";

    }

    public Order getOrderById(String orderId) {
        for (String s : orderDb.keySet()) {
            if (s.equals(orderId)) {
                return orderDb.get(s);
            }
        }
        return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        if (partnerDb.containsKey(partnerId)) {
            return partnerDb.get(partnerId);
        }
        return null;
    }

    public int getOrderCountByPartnerId(String partnerId) {
        int orders = pairDb.getOrDefault(partnerId, new ArrayList<>()).size();
        return orders;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        List<String> orders = pairDb.getOrDefault(partnerId, new ArrayList<>());
        return orders;
    }

    public List<String> getAllOrders() {
        List<String> orders = new ArrayList<>();
        for (String s : orderDb.keySet()) {
            orders.add(s);
        }
        return orders;
    }

    public int getCountOfUnassignedOrders() {
        // Count of orders that have not been assigned to any DeliveryPartner
        int countOfOrders = orderDb.size() - assignedDb.size();
        return countOfOrders;
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int countOfOrders = 0;
        List<String> list = pairDb.get(partnerId);
        int deliveryTime = Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3));
        for (String s : list) {
            Order order = orderDb.get(s);
            if (order.getDeliveryTime() > deliveryTime) {
                countOfOrders++;
            }
        }
        return countOfOrders;
    }

    public int getOrderCountAfterGivenTimeByPartnerId(String time, String partnerId) {
        int orderCount = 0;
        List<String> orderIds = pairDb.getOrDefault(partnerId, new ArrayList<>());
        int deliveryTime = Integer.parseInt(time.substring(0, 2)) * 60 + Integer.parseInt(time.substring(3));
        for (String orderId : orderIds) {
            Order order = orderDb.get(orderId);
            if (order.getDeliveryTime() > deliveryTime) {
                orderCount++;
            }
        }
        return orderCount;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        List<String> orderIds = pairDb.get(partnerId);
        if (orderIds == null) {
            return "";
        }

        int lastDeliveryTime = 0;
        for (String orderId : orderIds) {
            Order order = orderDb.get(orderId);
            lastDeliveryTime = Math.max(lastDeliveryTime, order.getDeliveryTime());
        }

        int hours = lastDeliveryTime / 60;
        int minutes = lastDeliveryTime % 60;

        return String.format("%02d:%02d", hours, minutes);
    }


    public String deletePartnerById(String partnerId) {
        partnerDb.remove(partnerId);

        List<String> orderIds = pairDb.remove(partnerId);
        if (orderIds == null) {
            return "Deleted";
        }

        for (String orderId : orderIds) {
            assignedDb.remove(orderId);
        }

        return "Deleted";
    }

    public String deleteOrderById(String orderId) {
        orderDb.remove(orderId);
        String partnerId = assignedDb.remove(orderId);
        List<String> orderIds = pairDb.get(partnerId);
        orderIds.remove(orderId);
        pairDb.put(partnerId, orderIds);
        return "Deleted";
    }

}
   /* public static Order getOrderById(String orderId) {
    }

    public String addOrder(Order order) {
    }

    public String addPartner(String partnerId) {
    }

    public DeliveryPartner getPartnerById(String partnerId) {
    }

    public int getOrderCountByPartnerId(String partnerId) {
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
    }

    public List<String> getAllOrders() {
    }

    public int getCountOfUnassignedOrders() {
    }

    public int getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
    }*/

