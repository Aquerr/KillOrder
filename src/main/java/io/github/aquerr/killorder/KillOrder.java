package io.github.aquerr.killorder;

import com.google.inject.Inject;
import io.github.aquerr.killorder.commands.HelpCommand;
import io.github.aquerr.killorder.commands.ListCommand;
import io.github.aquerr.killorder.commands.SelectCommand;
import io.github.aquerr.killorder.entities.Order;
import io.github.aquerr.killorder.entities.OrderRewardType;
import io.github.aquerr.killorder.listeners.InventoryClickListener;
import io.github.aquerr.killorder.managers.OrderManager;
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
, dependencies = {@Dependency(id="eaglefactions", optional = true, version = "0.15.0")})
public class KillOrder
{
    private static KillOrder INSTANCE;

    private final Map<List<String>, CommandSpec> subcommands = new HashMap<>();
    private final List<Order> orders = new ArrayList<>();


    private OrderManager orderManager;

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    public static KillOrder getInstance()
    {
        return INSTANCE;
    }

    @Listener
    public void onServerStart(GameInitializationEvent event)
    {
        INSTANCE = this;

        Sponge.getServer().getConsole().sendMessage(Text.of(TextColors.YELLOW, "Kill Order is loading..."));

        // Add method for setting up storage

        // Setup managers
        setupManager();

        // Setup commands and listeners
        registerCommands();
        registerListeners();
    }

    private void registerCommands()
    {
        //Select Command
        subcommands.put(Collections.singletonList("select"), CommandSpec.builder()
                .description(Text.of("Selects a player for an order"))
                .arguments(GenericArguments.optional(GenericArguments.player(Text.of("player"))),
                        GenericArguments.optional(GenericArguments.enumValue(Text.of("rewardtype"), OrderRewardType.class)),
                        GenericArguments.optional(GenericArguments.integer(Text.of("reward"))))
                .executor(new SelectCommand(this.orderManager))
                .build());

        //List Command
        subcommands.put(Collections.singletonList("list"), CommandSpec.builder()
                .description(Text.of("Shows list of orders"))
                .executor(new ListCommand(this.orderManager))
                .build());

        //Build all commands
        CommandSpec commandKillOrder = CommandSpec.builder ()
                .description (Text.of ("Help Command"))
                .executor (new HelpCommand())
                .children (subcommands)
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
        this.orderManager = new OrderManager("HOCON", getConfigDir());
    }

    public Logger getLogger()
    {
        return this.logger;
    }

    public Path getConfigDir()
    {
        return this.configDir;
    }

    public List<Order> getOrderList()
    {
        return this.orders;
    }

    public Map<List<String>, CommandSpec> getSubcommands()
    {
        return this.subcommands;
    }

    public OrderManager getOrderManager()
    {
        return this.orderManager;
    }
}
