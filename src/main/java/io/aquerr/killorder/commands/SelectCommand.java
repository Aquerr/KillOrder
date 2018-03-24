package io.aquerr.killorder.commands;

import io.aquerr.killorder.KillOrder;
import io.aquerr.killorder.PluginInfo;
import io.aquerr.killorder.entities.*;
import io.aquerr.killorder.managers.OrderManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class SelectCommand implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource source, CommandContext context)
    {
        //It should be possible to select a player from the console :)

        Optional<Player> optionalPlayer = context.getOne(Text.of("player"));
        Optional<OrderRewardType> optionalRewardType = context.getOne(Text.of("rewardtype"));
        Optional<Integer> optionalReward = context.getOne(Text.of("reward"));

        if (!optionalPlayer.isPresent())
        {
            source.sendMessage(Text.of(PluginInfo.PluginPrefix, TextColors.RED, "You need to provide a player name!"));
        }

        if (!optionalRewardType.isPresent())
        {
            source.sendMessage(Text.of(PluginInfo.PluginPrefix, TextColors.RED, "You need to provide a reward type!"));
        }

        if (!optionalReward.isPresent())
        {
            source.sendMessage(Text.of(PluginInfo.PluginPrefix, TextColors.RED, "You need to provide a reward!"));
        }

        if (source instanceof Player)
        {
            Player player = ((Player) source);

//            Inventory orderGUI = Inventory.builder().of(InventoryArchetypes.CHEST).property(InventoryTitle.PROPERTY_NAME,
//                    InventoryTitle.of(Text.of("Orders"))).build(KillOrder.getInstance());
//
//            GridInventory gridInventory = orderGUI.query(GridInventory.class);
//
//            gridInventory.set(1, 1, ItemStack.builder().itemType(ItemTypes.GLASS_PANE).quantity(1).build());
//            gridInventory.set(6, 1, ItemStack.builder().itemType(ItemTypes.GLASS_PANE).quantity(1).build());
//
//            player.openInventory(orderGUI);

            OrderReward orderReward = null;


            //TODO: Add item reward
            switch (optionalRewardType.get())
            {
                case MONEY:
                    orderReward = new MoneyReward(optionalReward.get());
                    break;
                case POWER:
                    orderReward = new PowerReward(optionalReward.get());
                    break;
            }

            Order order = new Order(player.getUniqueId(), optionalPlayer.get().getUniqueId(), orderReward);

            if (!OrderManager.getOrderList().contains(order))
            {
                OrderManager.addOrder(order);
            }
        }

        return CommandResult.success();
    }
}
