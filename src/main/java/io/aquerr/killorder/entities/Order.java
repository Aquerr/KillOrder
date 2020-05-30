package io.aquerr.killorder.entities;

import io.aquerr.killorder.managers.OrderManager;

import javax.annotation.Nullable;
import java.util.UUID;

public final class Order
{
    private int orderId;
    private UUID orderedByPlayerUUID;
    private UUID orderedPlayerUUID;

    private boolean isAccepted;
    private UUID acceptedByPlayerUUID;

    private OrderReward orderReward;

    public Order(int orderId, UUID orderedByPlayerUUID, UUID orderedPlayerUUID, OrderReward orderReward, boolean isAccepted, @Nullable UUID acceptedByPlayerUUID)
    {
        this.orderId = orderId;
        this.orderedByPlayerUUID = orderedByPlayerUUID;
        this.orderedPlayerUUID = orderedPlayerUUID;
        this.orderReward = orderReward;
        this.isAccepted = isAccepted;
        this.acceptedByPlayerUUID = acceptedByPlayerUUID;
    }

    public Order(UUID orderedByPlayerUUID, UUID orderedPlayerUUID, OrderReward orderReward, boolean isAccepted, @Nullable UUID acceptedByPlayerUUID)
    {
        this.orderId = OrderManager.getLastAvailableIndex();
        this.orderedByPlayerUUID = orderedByPlayerUUID;
        this.orderedPlayerUUID = orderedPlayerUUID;
        this.orderReward = orderReward;
        this.isAccepted = isAccepted;
        this.acceptedByPlayerUUID = acceptedByPlayerUUID;
    }

    public UUID getOrderedPlayerUUID()
    {
        return orderedPlayerUUID;
    }

    public UUID getOrderedByPlayerUUID()
    {
        return orderedByPlayerUUID;
    }

    public OrderReward getOrderReward()
    {
        return orderReward;
    }

    public int getOrderId()
    {
        return orderId;
    }

    public boolean isAccepted()
    {
        return isAccepted;
    }

    public @Nullable UUID getAcceptedByPlayerUUID()
    {
        return acceptedByPlayerUUID;
    }

    // OrderBuilder
}
