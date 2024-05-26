package mod.acgaming.playerattributecommands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class PACCommand extends CommandBase
{
    @Override
    public String getName()
    {
        return "playerattribute";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return new TextComponentTranslation("command.playerattribute.usage").getUnformattedText();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 3)
        {
            throw new CommandException("command.playerattribute.invalid_usage");
        }

        EntityPlayer player = getPlayer(server, sender, args[0]);
        IAttributeInstance attributeInstance = player.getAttributeMap().getAttributeInstanceByName(args[1]);

        if (attributeInstance == null)
        {
            throw new CommandException("command.playerattribute.invalid_attribute", args[1]);
        }

        String action = args[2];
        double value = args.length == 4 ? parseDouble(args[3]) : 0;

        switch (action)
        {
            case "get":
                sender.sendMessage(new TextComponentTranslation("command.playerattribute.get", attributeInstance.getAttribute().getName(), attributeInstance.getBaseValue()));
                break;
            case "add":
                attributeInstance.setBaseValue(attributeInstance.getBaseValue() + value);
                updatePlayerCapabilities(player, attributeInstance);
                sender.sendMessage(new TextComponentTranslation("command.playerattribute.add", attributeInstance.getAttribute().getName(), value, attributeInstance.getBaseValue()));
                break;
            case "set":
                attributeInstance.setBaseValue(value);
                updatePlayerCapabilities(player, attributeInstance);
                sender.sendMessage(new TextComponentTranslation("command.playerattribute.set", attributeInstance.getAttribute().getName(), value));
                break;
            case "mult":
                attributeInstance.setBaseValue(attributeInstance.getBaseValue() * value);
                updatePlayerCapabilities(player, attributeInstance);
                sender.sendMessage(new TextComponentTranslation("command.playerattribute.mult", attributeInstance.getAttribute().getName(), value, attributeInstance.getBaseValue()));
                break;
            default:
                throw new CommandException("command.playerattribute.unknown_action", action);
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        }
        else if (args.length == 2)
        {
            EntityPlayer player;
            try
            {
                player = getPlayer(server, sender, args[0]);
            }
            catch (CommandException e)
            {
                return new ArrayList<>();
            }

            AbstractAttributeMap attributeMap = player.getAttributeMap();
            Collection<IAttributeInstance> attributes = attributeMap.getAllAttributes();
            List<String> attributeNames = new ArrayList<>();
            for (IAttributeInstance attribute : attributes)
            {
                attributeNames.add(attribute.getAttribute().getName());
            }
            return getListOfStringsMatchingLastWord(args, attributeNames.toArray(new String[0]));
        }
        else if (args.length == 3)
        {
            return getListOfStringsMatchingLastWord(args, "get", "add", "set", "mult");
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    private void updatePlayerCapabilities(EntityPlayer player, IAttributeInstance attributeInstance)
    {
        if ("generic.movementSpeed".equals(attributeInstance.getAttribute().getName()))
        {
            player.capabilities.walkSpeed = (float) attributeInstance.getBaseValue();
        }
    }
}
