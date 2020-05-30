package io.github.aquerr.killorder.managers;

import io.github.aquerr.killorder.entities.Order;
import io.github.aquerr.killorder.storage.HOCONOrderStorage;
import io.github.aquerr.killorder.storage.IStorage;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class OrderManager
{
    private IStorage orderStorage;

    public OrderManager(String storageType, Path configDir)
    {
        if (storageType.equals("HOCON"))
        {
            orderStorage = new HOCONOrderStorage(configDir);
        }
    }

    public boolean addOrder(Order order)
    {
        return this.orderStorage.addOrder(order);
    }

    public boolean removeOrder(int orderId)
    {
        return this.orderStorage.removeOrder(orderId);
    }

    public boolean acceptOrder(int orderId, UUID acceptedByPlayerUUID)
    {
        return this.orderStorage.acceptOrder(orderId, acceptedByPlayerUUID);
    }

    public List<Order> getOrderList()
    {
        return this.orderStorage.getOrders();
    }

    public int getLastAvailableIndex()
    {
        return this.orderStorage.getLastAvailableOrderIndex();
    }
}
