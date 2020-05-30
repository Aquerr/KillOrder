package io.aquerr.killorder.managers;

import io.aquerr.killorder.entities.Order;
import io.aquerr.killorder.storage.HOCONOrderStorage;
import io.aquerr.killorder.storage.IStorage;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public class OrderManager
{
    private static IStorage _orderStorage;

    public OrderManager(String storageType, Path configDir)
    {
        if (storageType.equals("HOCON"))
        {
            _orderStorage = new HOCONOrderStorage(configDir);
        }
    }

    public static boolean addOrder(Order order)
    {
        return _orderStorage.addOrder(order);
    }

    public static boolean removeOrder(int orderId)
    {
        return _orderStorage.removeOrder(orderId);
    }

    public static boolean acceptOrder(int orderId, UUID acceptedByPlayerUUID)
    {
     return _orderStorage.acceptOrder(orderId, acceptedByPlayerUUID);
    }

    public static List<Order> getOrderList()
    {
        return _orderStorage.getOrders();
    }

    public static int getLastAvailableIndex()
    {
        return _orderStorage.getLastAvailableOrderIndex();
    }
}
