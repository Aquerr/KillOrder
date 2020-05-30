package io.github.aquerr.killorder.entities;

import org.spongepowered.api.item.inventory.ItemStack;

public class ItemReward extends OrderReward
{
    private ItemStack item;

    public ItemReward(ItemStack itemStack)
    {
        super(OrderRewardType.ITEM);
        item = itemStack;
    }

    public ItemStack getItem()
    {
        return item;
    }
}
