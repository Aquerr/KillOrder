package io.aquerr.killorder;

import com.google.inject.Inject;
import io.aquerr.killorder.commands.HelpCommand;
import io.aquerr.killorder.commands.ListCommand;
import io.aquerr.killorder.commands.SelectCommand;
import io.aquerr.killorder.entities.MoneyReward;
import io.aquerr.killorder.entities.Order;
import io.aquerr.killorder.entities.OrderReward;
import io.aquerr.killorder.entities.OrderRewardType;
import io.aquerr.killorder.listeners.InventoryClickListener;
import io.aquerr.killorder.managers.OrderManager;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.nio.file.Path;
import java.util.*;

/**
 * Created by Aquerr on 2018-03-16.
 */

@Plugin(id = PluginInfo.Id, name = PluginInfo.Name, description = PluginInfo.Description, version = PluginInfo.Version, url = PluginInfo.Url, authors = {PluginInfo.Authors}
, dependencies = {@Dependency(id="eaglefactions", optional = true)})
public class KillOrder
{
    private static KillOrder _killOrder;

    private Map<List<String>, CommandSpec> _subcommands;
    private List<Order> _orderList;

    @Inject
    private Logger _logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path _configDir;

    @Listener
    public void onServerStart(GameInitializationEvent event)
    {
        Sponge.getServer().getConsole().sendMessage(Text.of(TextColors.YELLOW, "Kill Order is loading..."));

        _orderList = new ArrayList<>();

        initCommands();
        registerListeners();

        setupManager();
    }

    private void initCommands()
    {
        _killOrder = this;

        _subcommands = new HashMap<>();

        //Select Command
        _subcommands.put(Arrays.asList("select"), CommandSpec.builder()
                .description(Text.of("Selects a player for an order"))
                .arguments(GenericArguments.optional(GenericArguments.player(Text.of("player"))),
                        GenericArguments.optional(GenericArguments.enumValue(Text.of("rewardtype"), OrderRewardType.class)),
                        GenericArguments.optional(GenericArguments.integer(Text.of("reward"))))
                .executor(new SelectCommand())
                .build());

        //List Command
        _subcommands.put(Arrays.asList("list"), CommandSpec.builder()
                .description(Text.of("Shows list of orders"))
                .executor(new ListCommand())
                .build());

        //Build all commands
        CommandSpec commandKillOrder = CommandSpec.builder ()
                .description (Text.of ("Help Command"))
                .executor (new HelpCommand())
                .children (_subcommands)
                .build ();

        Sponge.getCommandManager().register(this, commandKillOrder, "killorder", "ko");
    }

    private void registerListeners()
    {
        Sponge.getEventManager().registerListeners(this, new InventoryClickListener());
    }

    private void setupManager()
    {
        //TODO: Create a config node for this so the user can manually choose which storage shall be used.
        OrderManager orderManager = new OrderManager("HOCON", getConfigDir());
    }

    public static KillOrder getInstance()
    {
        return _killOrder;
    }

    public Logger getLogger()
    {
        return _logger;
    }

    public Path getConfigDir()
    {
        return _configDir;
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
