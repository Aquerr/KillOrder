package io.aquerr.killorder.entities;

import io.aquerr.killorder.managers.OrderManager;

import java.util.UUID;

public final class Order
{
    private int orderId;
    private UUID orderedByPlayerUUID;
    private UUID orderedPlayerUUID;

    private OrderReward orderReward;

//    public Order(int orderId, UUID orderedByPlayerUUID, UUID orderedPlayerUUID, OrderReward orderReward)
//    {
//        this.orderId = orderId;
//        this.orderedByPlayerUUID = orderedByPlayerUUID;
//        this.orderedPlayerUUID = orderedPlayerUUID;
//        this.orderReward = orderReward;
//    }

    public Order(UUID ordererByPlayerUUID, UUID orderedPlayerUUID, OrderReward orderReward)
    {
        this.orderId = OrderManager.getLastAvailableIndex();
        this.orderedByPlayerUUID = ordererByPlayerUUID;
        this.orderedPlayerUUID = orderedPlayerUUID;
        this.orderReward = orderReward;
    }

    public UUID getOrderedPlayerUUID()
    {
        return orderedPlayerUUID;
    }

    public UUID getOrdererByPlayerUUID()
    {
        return orderedByPlayerUUID;
    }

    public OrderReward getOrderReward()
    {
        return orderReward;
    }


    // OrderBuilder
}
