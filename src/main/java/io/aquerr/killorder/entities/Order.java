package io.aquerr.killorder.entities;

import java.util.UUID;

public final class Order
{
    private UUID ordererByPlayerUUID;
    private UUID orderedPlayerUUID;

    private OrderReward orderReward;

    public Order(UUID ordererByPlayerUUID, UUID orderedPlayerUUID, OrderReward orderReward)
    {
        this.ordererByPlayerUUID = ordererByPlayerUUID;
        this.orderedPlayerUUID = orderedPlayerUUID;
        this.orderReward = orderReward;
    }

    public UUID getOrderedPlayerUUID()
    {
        return orderedPlayerUUID;
    }

    public UUID getOrdererByPlayerUUID()
    {
        return ordererByPlayerUUID;
    }

    public OrderReward getOrderReward()
    {
        return orderReward;
    }


    // OrderBuilder
}
