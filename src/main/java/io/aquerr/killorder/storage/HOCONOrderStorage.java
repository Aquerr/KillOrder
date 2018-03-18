package io.aquerr.killorder.storage;

import io.aquerr.killorder.entities.Order;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class HOCONOrderStorage implements IStorage
{
    ConfigurationLoader _configurationLoader;
    ConfigurationNode _confignNode;

    public HOCONOrderStorage(Path configDir)
    {
        try
        {
            if (!Files.exists(configDir))
            {
                Files.createDirectory(configDir);
            }

            Path filePath = configDir.resolve("orders.conf");

            if (!Files.exists(filePath))
            {
                Files.createFile(filePath);
            }

            _configurationLoader = HoconConfigurationLoader.builder().setPath(filePath).build();
            load();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    @Override
    public boolean addOrder(Order order)
    {
        return false;
    }

    @Override
    public boolean removeOrder(Order order)
    {
        return false;
    }

    @Override
    public List<Order> getOrders()
    {
        return null;
    }

    private void load()
    {
        try
        {
            _confignNode = _configurationLoader.load();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }
}
