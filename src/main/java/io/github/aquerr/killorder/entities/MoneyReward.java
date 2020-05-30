package io.github.aquerr.killorder.entities;

public class MoneyReward extends OrderReward
{
    private int money;

    public MoneyReward(int money)
    {
        super(OrderRewardType.MONEY);
        this.money = money;
    }

    public int getMoney()
    {
        return money;
    }
}
