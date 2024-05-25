package mod.acgaming.playerattributecommands;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod.EventBusSubscriber
@Mod(modid = PAC.MOD_ID, name = PAC.NAME, version = PAC.VERSION, acceptedMinecraftVersions = PAC.ACCEPTED_VERSIONS)
public class PAC
{
    public static final String MOD_ID = Tags.MOD_ID;
    public static final String NAME = "Player Attribute Commands";
    public static final String VERSION = Tags.VERSION;
    public static final String ACCEPTED_VERSIONS = "[1.12.2]";

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new PACCommand());
    }
}
