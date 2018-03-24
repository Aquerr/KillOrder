package io.aquerr.killorder.listeners;

import io.aquerr.killorder.entities.ItemReward;
import io.aquerr.killorder.entities.MoneyReward;
import io.aquerr.killorder.entities.Order;
import io.aquerr.killorder.entities.PowerReward;
import io.aquerr.killorder.managers.OrderManager;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.source.EntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class PlayerKillListener
{
    @Listener
    public void onPlayerKill(DamageEntityEvent event, @Getter("getTargetEntity") Player damagedPlayer)
    {
        if (event.willCauseDeath())
        {
            if (event.getCause().root() instanceof EntityDamageSource)
            {
                EntityDamageSource entityDamageSource = (EntityDamageSource) event.getCause().root();

                if (entityDamageSource.getSource() instanceof Player)
                {
                    List<Order> orderList = OrderManager.getOrderList().stream().filter(x -> x.getOrderedPlayerUUID().equals(damagedPlayer.getUniqueId()) && x.isAccepted()).collect(Collectors.toList());
                    if (!orderList.isEmpty())
                    {
                        Player attacker = (Player) entityDamageSource.getSource();

                        if (orderList.stream().anyMatch(x -> x.getAcceptedByPlayerUUID().equals(attacker.getUniqueId())))
                        {
                            for (Order order : orderList)
                            {
                                //Reward the killer
                                if (order.getOrderReward() instanceof MoneyReward)
                                {
                                    MoneyReward moneyReward = ((MoneyReward) order.getOrderReward());

                                    //TODO: Give player money
                                }
                                else if (order.getOrderReward() instanceof PowerReward)
                                {
                                    PowerReward powerReward = ((PowerReward) order.getOrderReward());

                                    //TODO: Give player power
                                }
                                else if (order.getOrderReward() instanceof ItemReward)
                                {
                                    ItemReward itemReward = ((ItemReward) order.getOrderReward());

                                    //TODO: Give player item
                                    attacker.getInventory().offer(itemReward.getItem());
                                }

                                OrderManager.removeOrder(order.getOrderId());
                            }
                        }
                    }
                }
            }
        }
    }
}
