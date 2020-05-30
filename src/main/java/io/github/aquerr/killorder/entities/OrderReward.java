package io.github.aquerr.killorder.entities;

public abstract class OrderReward
{
    OrderRewardType orderRewardType;

    public OrderReward(OrderRewardType orderRewardType)
    {
        this.orderRewardType = orderRewardType;
    }

    public OrderRewardType getOrderRewardType()
    {
        return orderRewardType;
    }
}
