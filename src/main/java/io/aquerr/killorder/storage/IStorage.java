package io.aquerr.killorder.storage;

import io.aquerr.killorder.entities.Order;

import java.util.List;

public interface IStorage
{
    boolean addOrder(Order order);

    boolean removeOrder(Order order);

    List<Order> getOrders();
}
