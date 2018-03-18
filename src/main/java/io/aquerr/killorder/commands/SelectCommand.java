package io.aquerr.killorder.commands;

import io.aquerr.killorder.KillOrder;
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

public class SelectCommand implements CommandExecutor
{
    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException
    {
        //It should be possible to select a player from the console :)

        if (source instanceof Player)
        {
            Player player = ((Player) source);

            Inventory orderGUI = Inventory.builder().of(InventoryArchetypes.CHEST).property(InventoryTitle.PROPERTY_NAME,
                    InventoryTitle.of(Text.of("Orders"))).build(KillOrder.getInstance());

            GridInventory gridInventory = orderGUI.query(GridInventory.class);

            gridInventory.set(1, 1, ItemStack.builder().itemType(ItemTypes.GLASS_PANE).quantity(1).build());
            gridInventory.set(6, 1, ItemStack.builder().itemType(ItemTypes.GLASS_PANE).quantity(1).build());

            player.openInventory(orderGUI);
        }

        return CommandResult.success();
    }
}
