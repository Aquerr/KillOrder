package io.github.aquerr.killorder.listeners;

import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.text.Text;

public class InventoryClickListener
{
    @Listener
    public void onInventoryClick(ClickInventoryEvent event)
    {
        Inventory inventory = event.getTargetInventory().root();

        if(inventory.getInventoryProperty(InventoryTitle.class).isPresent())
        {

        }

//        if (container.root().getProperty.equals("Orders"))
//        {
//            event.setCancelled(true);
//        }
    }
}
