package io.aquerr.killorder.entities;

public class PowerReward extends OrderReward
{
    int powerAmount;

    public PowerReward(int powerAmount)
    {
        super(OrderRewardType.POWER);
        this.powerAmount = powerAmount;
    }

    public int getPowerAmount()
    {
        return powerAmount;
    }
}
