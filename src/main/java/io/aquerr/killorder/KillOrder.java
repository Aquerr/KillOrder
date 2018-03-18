package io.aquerr.killorder;

import com.google.inject.Inject;
import io.aquerr.killorder.commands.HelpCommand;
import io.aquerr.killorder.entities.MoneyReward;
import io.aquerr.killorder.entities.Order;
import io.aquerr.killorder.entities.OrderReward;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.*;

/**
 * Created by Aquerr on 2018-03-16.
 */

@Plugin(id = PluginInfo.Id, name = PluginInfo.Name, description = PluginInfo.Description, version = PluginInfo.Version, url = PluginInfo.Url, authors = {PluginInfo.Authors})
public class KillOrder
{
    private static KillOrder _killOrder;

    Map<List<String>, CommandSpec> _subcommands;
    private List<Order> _orderList;

    @Inject
    private Logger _logger;

    @Listener
    public void onServerStart(GameInitializationEvent event)
    {
        Sponge.getServer().getConsole().sendMessage(Text.of(TextColors.YELLOW, "Kill Order is loading..."));

        _orderList = new ArrayList<>();

        initCommands();
    }

    private void initCommands()
    {
        _subcommands = new HashMap<>();

        //Build all commands
        CommandSpec commandKillOrder = CommandSpec.builder ()
                .description (Text.of ("Help Command"))
                .executor (new HelpCommand())
                .children (_subcommands)
                .build ();

        Sponge.getCommandManager().register(this, commandKillOrder, "killorder", "ko");
    }

    public static KillOrder getInstance()
    {
        if (_killOrder == null)
        {
            _killOrder = new KillOrder();
            return _killOrder;
        }

        return _killOrder;
    }

    public Logger getLogger()
    {
        return _logger;
    }

    public List<Order> getOrderList()
    {
        return _orderList;
    }

    public Map<List<String>, CommandSpec> getSubcommands()
    {
        return _subcommands;
    }
}
