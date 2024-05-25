package mod.acgaming.playerattributecommands;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.SharedMonsterAttributes;
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
        return "playerspeed";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return new TextComponentTranslation("command.playerspeed.usage").getUnformattedText();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new CommandException("command.playerspeed.invalid_usage");
        }

        EntityPlayer player = getPlayer(server, sender, args[0]);
        IAttributeInstance speedAttribute = player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        String action = args[1];
        double value = args.length == 3 ? parseDouble(args[2]) : 0;

        switch (action)
        {
            case "get":
                sender.sendMessage(new TextComponentTranslation("command.playerspeed.get", (float) speedAttribute.getBaseValue()));
                break;
            case "add":
                player.capabilities.walkSpeed = (float) (speedAttribute.getBaseValue() + value);
                sender.sendMessage(new TextComponentTranslation("command.playerspeed.add", value));
                break;
            case "set":
                player.capabilities.walkSpeed = (float) value;
                sender.sendMessage(new TextComponentTranslation("command.playerspeed.set", value));
                break;
            case "mult":
                player.capabilities.walkSpeed = (float) (speedAttribute.getBaseValue() * value);
                sender.sendMessage(new TextComponentTranslation("command.playerspeed.mult", value));
                break;
            default:
                throw new CommandException("command.playerspeed.unknown_action", action);
        }
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
            return getListOfStringsMatchingLastWord(args, "get", "add", "set", "mult");
        }
        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
