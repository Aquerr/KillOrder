package io.aquerr.killorder.storage;

import io.aquerr.killorder.entities.Order;

import java.util.List;
import java.util.UUID;

public interface IStorage
{
    boolean addOrder(Order order);

    boolean removeOrder(int orderId);

    List<Order> getOrders();

    int getLastAvailableOrderIndex();

    boolean acceptOrder(int orderId, UUID acceptedByPlayerUUID);
}
