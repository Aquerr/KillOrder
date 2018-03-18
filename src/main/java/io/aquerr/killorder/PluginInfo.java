package io.aquerr.killorder;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.inject.Singleton;

/**
 * Created by Aquerr on 2018-02-09.
 */

@Singleton
public abstract class PluginInfo
{
    public static final String Id = "killorder";
    public static final String Name = "Kill Order";
    public static final String Version = "1.0.0";
    public static final String Description = "A plugin for making kill orders on a specific players.";
    public static final String Url = "https://github.com/Aquerr/KillOrder";
    public static final Text PluginPrefix = Text.of(TextColors.GOLD, "[KO] ");
    public static final String Authors = "Aquerr";
}
