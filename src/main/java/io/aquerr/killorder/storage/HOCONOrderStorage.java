package io.aquerr.killorder.storage;

import com.google.common.reflect.TypeToken;
import io.aquerr.killorder.entities.*;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class HOCONOrderStorage implements IStorage
{
    ConfigurationLoader<CommentedConfigurationNode> _configurationLoader;
    CommentedConfigurationNode _confignNode;

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
        try
        {
            int availableIndex = getLastAvailableOrderIndex();

            _confignNode.getNode(new Object[]{"orders", String.valueOf(availableIndex), "ordered-by-player"}).setValue(order.getOrdererByPlayerUUID().toString());
            _confignNode.getNode(new Object[]{"orders", String.valueOf(availableIndex), "ordered-player"}).setValue(order.getOrderedPlayerUUID().toString());
            _confignNode.getNode(new Object[]{"orders", String.valueOf(availableIndex), "reward", "type"}).setValue(order.getOrderReward().getOrderRewardType().name());

            if (order.getOrderReward().getOrderRewardType() == OrderRewardType.ITEM)
            {
                _confignNode.getNode(new Object[]{"orders", String.valueOf(availableIndex), "reward", "item"}).setValue(((ItemReward)order.getOrderReward()).getItem());
            }
            else if(order.getOrderReward().getOrderRewardType() == OrderRewardType.MONEY)
            {
                _confignNode.getNode(new Object[]{"orders", String.valueOf(availableIndex), "reward", "money"}).setValue(((MoneyReward)order.getOrderReward()).getMoney());
            }
            else if(order.getOrderReward().getOrderRewardType() == OrderRewardType.POWER)
            {
                _confignNode.getNode(new Object[]{"orders", String.valueOf(availableIndex), "reward", "power"}).setValue(((PowerReward)order.getOrderReward()).getPowerAmount());
            }

            return saveChanges();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
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
        //_confignNode.getNode(new Object[]{"orders"}).getList()

        return null;
    }

    @Override
    public int getLastAvailableOrderIndex()
    {
        Set<Object> objectSet = _confignNode.getNode("orders").getChildrenMap().keySet();

        int index = 1;

        for(;;)
        {
            if (objectSet.contains(index))
            {
                index++;
                continue;
            }
            else
            {
                return index;
            }
        }
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

    private boolean saveChanges()
    {
        try
        {
            _configurationLoader.save(_confignNode);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
