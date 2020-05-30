package io.aquerr.killorder.storage;

import com.google.common.reflect.TypeToken;
import io.aquerr.killorder.entities.*;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.item.inventory.ItemStack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
            _confignNode.getNode(new Object[]{"orders", String.valueOf(order.getOrderId()), "ordered-by-player"}).setValue(order.getOrderedByPlayerUUID().toString());
            _confignNode.getNode(new Object[]{"orders", String.valueOf(order.getOrderId()), "ordered-player"}).setValue(order.getOrderedPlayerUUID().toString());

            _confignNode.getNode(new Object[]{"orders", String.valueOf(order.getOrderId()), "is-accepted"}).setValue(order.isAccepted());
            _confignNode.getNode(new Object[]{"orders", String.valueOf(order.getOrderId()), "accepted-by-player"}).setValue(order.getAcceptedByPlayerUUID());

            _confignNode.getNode(new Object[]{"orders", String.valueOf(order.getOrderId()), "reward", "type"}).setValue(order.getOrderReward().getOrderRewardType().name());

            if (order.getOrderReward().getOrderRewardType() == OrderRewardType.ITEM)
            {
                _confignNode.getNode(new Object[]{"orders", String.valueOf(order.getOrderId()), "reward", "item"}).setValue(((ItemReward)order.getOrderReward()).getItem());
            }
            else if(order.getOrderReward().getOrderRewardType() == OrderRewardType.MONEY)
            {
                _confignNode.getNode(new Object[]{"orders", String.valueOf(order.getOrderId()), "reward", "money"}).setValue(((MoneyReward)order.getOrderReward()).getMoney());
            }
            else if(order.getOrderReward().getOrderRewardType() == OrderRewardType.POWER)
            {
                _confignNode.getNode(new Object[]{"orders", String.valueOf(order.getOrderId()), "reward", "power"}).setValue(((PowerReward)order.getOrderReward()).getPowerAmount());
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
    public boolean removeOrder(int orderId)
    {
        _confignNode.getNode("orders").removeChild(orderId);

        return saveChanges();
    }

    @Override
    public List<Order> getOrders()
    {
        Set<Object> objectSet = _confignNode.getNode("orders").getChildrenMap().keySet();

        List<Order> orderList = new ArrayList<>();

        for (Object objectId : objectSet)
        {
            int orderId = Integer.parseInt(objectId.toString());
            UUID orderedByPlayer = UUID.fromString(_confignNode.getNode("orders", objectId, "ordered-by-player").getString());
            UUID orderedPlayer = UUID.fromString(_confignNode.getNode("orders", objectId, "ordered-player").getString());

            boolean isAccepted = _confignNode.getNode(new Object[]{"orders", objectId, "is-accepted"}).getBoolean();
            UUID acceptedByPlayerUUID = null;

            if (_confignNode.getNode("orders", objectId, "accepted-by-player").getString() != null)
            {
                acceptedByPlayerUUID = UUID.fromString(_confignNode.getNode("orders", objectId, "accepted-by-player").getString());
            }

            OrderRewardType rewardType = OrderRewardType.valueOf(_confignNode.getNode("orders", objectId, "reward", "type").getString());

            Order order;

            switch (rewardType)
            {
                case POWER:
                    int power = _confignNode.getNode("orders", objectId, "reward", "power").getInt();
                    order = new Order(orderId, orderedByPlayer, orderedPlayer, new PowerReward(power), isAccepted, acceptedByPlayerUUID);
                    orderList.add(order);
                    break;
                case MONEY:
                    int money = _confignNode.getNode("orders", objectId, "reward", "money").getInt();
                    order = new Order(orderId, orderedByPlayer, orderedPlayer, new MoneyReward(money), isAccepted, acceptedByPlayerUUID);
                    orderList.add(order);
                    break;
                case ITEM:
                    try
                    {
                        ItemStack itemStack = _confignNode.getNode("orders", objectId, "reward", "item").getValue(TypeToken.of(ItemStack.class));
                        order = new Order(orderId, orderedByPlayer, orderedPlayer, new ItemReward(itemStack), isAccepted, acceptedByPlayerUUID);
                        orderList.add(order);
                        break;
                    }
                    catch (ObjectMappingException e)
                    {
                        e.printStackTrace();
                    }
            }
        }

        return orderList;
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

    @Override
    public boolean acceptOrder(int orderId, UUID acceptedByPlayerUUID)
    {
        _confignNode.getNode("orders", String.valueOf(orderId), "accepted-by-player").setValue(acceptedByPlayerUUID.toString());
        _confignNode.getNode("orders", String.valueOf(orderId), "is-accepted").setValue(true);

        return saveChanges();
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
