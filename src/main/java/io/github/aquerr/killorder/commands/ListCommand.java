package io.github.aquerr.killorder.commands;

import io.github.aquerr.killorder.PluginInfo;
import io.github.aquerr.killorder.entities.ItemReward;
import io.github.aquerr.killorder.entities.MoneyReward;
import io.github.aquerr.killorder.entities.Order;
import io.github.aquerr.killorder.entities.PowerReward;
import io.github.aquerr.killorder.managers.OrderManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.service.pagination.PaginationService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ListCommand implements CommandExecutor
{
    private final OrderManager orderManager;

    public ListCommand(final OrderManager orderManager)
    {
        this.orderManager = orderManager;
    }


    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException
    {
        List<Text> helpList = new ArrayList<>();

        for(Order order : this.orderManager.getOrderList())
        {
            String orderedPlayerName = null;
            if (Sponge.getServer().getPlayer(order.getOrderedPlayerUUID()).isPresent())
            {
                orderedPlayerName = Sponge.getServer().getPlayer(order.getOrderedPlayerUUID()).get().getName();
            }

            String orderingPlayerName = null;
            if (Sponge.getServer().getPlayer(order.getOrderedByPlayerUUID()).isPresent())
            {
                orderingPlayerName = Sponge.getServer().getPlayer(order.getOrderedByPlayerUUID()).get().getName();
            }

            if (order.isAccepted())
            {
                Text.Builder orderDesc = Text.builder();

                orderDesc.append(Text.of(TextColors.GREEN, "Ordered by: " + orderingPlayerName + "\n"));
                orderDesc.append(Text.of("Reward: " + order.getOrderReward().getOrderRewardType().name()));

                Text.Builder orderText = Text.builder();

                orderText.append(Text.of(TextColors.RED, order.getOrderId()  + "." + " " + "Kill " + orderedPlayerName + " - Ordered by " + orderingPlayerName));
                orderText.onHover(TextActions.showText(orderDesc.build()));

                helpList.add(orderText.build());
            }
            else
            {
                Text.Builder orderDesc = Text.builder();

                orderDesc.append(Text.of(TextColors.GREEN, "Ordered by: " + orderingPlayerName + "\n"));
                orderDesc.append(Text.of("Reward: " + order.getOrderReward().getOrderRewardType().name() + "\n"));

                if (order.getOrderReward() instanceof MoneyReward)
                {
                    orderDesc.append(Text.of("Money: " + ((MoneyReward)order.getOrderReward()).getMoney()));
                }
                else if (order.getOrderReward() instanceof PowerReward)
                {
                    orderDesc.append(Text.of("Power: " + ((PowerReward)order.getOrderReward()).getPowerAmount()));
                }
                else if (order.getOrderReward() instanceof ItemReward)
                {
                    orderDesc.append(Text.of("Item: " + ((ItemReward)order.getOrderReward()).getItem()));
                }

                Text.Builder orderText = Text.builder();

                orderText.append(Text.of(TextColors.YELLOW, order.getOrderId()  + "." + " " + "Kill " + orderedPlayerName + " - ordered by " + orderingPlayerName));
                orderText.onHover(TextActions.showText(orderDesc.build()));

                if (source instanceof Player)
                {
                    Player player = (Player)source;
                    orderText.onClick(TextActions.executeCallback(acceptOrder(order.getOrderId(), player)));
                }

                helpList.add(orderText.build());
            }
        }

        PaginationService paginationService = Sponge.getServiceManager().provide(PaginationService.class).get();
        PaginationList.Builder paginationBuilder = paginationService.builder().title(Text.of(TextColors.GOLD, "Orders List")).padding(Text.of(TextColors.DARK_GREEN, "-")).contents(helpList).linesPerPage(10);
        paginationBuilder.sendTo(source);

        return CommandResult.success();
    }

    private Consumer<CommandSource> acceptOrder(int orderId, Player player)
    {
        return accept ->
        {
            //Because the text can exists in the chat after being accepted then we need to make this check...
            if (this.orderManager.getOrderList().stream().anyMatch(x-> x.getOrderId() == orderId && x.isAccepted()))
            {
                player.sendMessage(Text.of(PluginInfo.PluginPrefix, TextColors.RED, "This order is already taken!"));
            }
            else
            {
                player.sendMessage(Text.of(PluginInfo.PluginPrefix, TextColors.GREEN, "You accepted order " + orderId + "!"));
                this.orderManager.acceptOrder(orderId, player.getUniqueId());
            }
        };
    }
}
