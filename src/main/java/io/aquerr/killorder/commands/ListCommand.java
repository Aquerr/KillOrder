package io.aquerr.killorder.commands;

import io.aquerr.killorder.PluginInfo;
import io.aquerr.killorder.entities.Order;
import io.aquerr.killorder.managers.OrderManager;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.profile.GameProfile;
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

    @Override
    public CommandResult execute(CommandSource source, CommandContext context) throws CommandException
    {
        List<Text> helpList = new ArrayList<>();

        for(Order order : OrderManager.getOrderList())
        {
            String orderedPlayerName = null;
            if (GameProfile.of(order.getOrderedPlayerUUID()).getName().isPresent())
            {
                orderedPlayerName = GameProfile.of(order.getOrderedPlayerUUID()).getName().get();
            }

            String orderingPlayerName = null;
            if (GameProfile.of(order.getOrdererdByPlayerUUID()).getName().isPresent())
            {
                orderingPlayerName = GameProfile.of(order.getOrdererdByPlayerUUID()).getName().get();
            }

            if (order.isAccepted())
            {
                Text.Builder orderDesc = Text.builder();

                orderDesc.append(Text.of(TextColors.GREEN, "Ordered by: " + orderingPlayerName + "\n"));
                orderDesc.append(Text.of("Reward: " + order.getOrderReward().getOrderRewardType().name()));

                Text.Builder orderText = Text.builder();

                orderText.append(Text.of(TextColors.RED, order.getOrderId()  + "." + " " + "Kill " + orderedPlayerName));
                orderText.onHover(TextActions.showText(orderDesc.build()));

                helpList.add(orderText.build());
            }
            else
            {
                Text.Builder orderDesc = Text.builder();

                orderDesc.append(Text.of(TextColors.GREEN, "Ordered by: " + orderingPlayerName + "\n"));
                orderDesc.append(Text.of("Reward: " + order.getOrderReward().getOrderRewardType().name()));

                Text.Builder orderText = Text.builder();

                orderText.append(Text.of(TextColors.GREEN, order.getOrderId()  + "." + " " + "Kill " + orderedPlayerName));
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
        PaginationList.Builder paginationBuilder = paginationService.builder().title(Text.of(TextColors.GOLD, "Orders list" + PluginInfo.Version)).padding(Text.of(TextColors.DARK_GREEN, "-")).contents(helpList).linesPerPage(10);
        paginationBuilder.sendTo(source);

        return CommandResult.success();
    }

    private Consumer<CommandSource> acceptOrder(int orderId, Player player)
    {
        return accept ->
        {
            //Because the text can exists in the chat after being accepted then we need to make this check...
            if (OrderManager.getOrderList().stream().anyMatch(x-> x.getOrderId() == orderId && x.isAccepted()))
            {
                player.sendMessage(Text.of(PluginInfo.PluginPrefix, TextColors.RED, "This order is already taken!"));
            }
            else
            {
                player.sendMessage(Text.of(PluginInfo.PluginPrefix, TextColors.GREEN, "You accepted the order " + orderId + "!"));
                OrderManager.acceptOrder(orderId, player.getUniqueId());
            }
        };
    }
}
