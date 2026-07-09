package io.kalishak.galacticraftlegacy.network.handler.server;

import io.kalishak.galacticraftlegacy.attachment.GalacticraftAttachments;
import io.kalishak.galacticraftlegacy.attachment.entity.PlayerSpaceData;
import io.kalishak.galacticraftlegacy.network.payload.ToggleSensorGlassesPayload;
import io.kalishak.galacticraftlegacy.references.GalacticraftComponents;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ToggleSensorGlassesServerHandler {
    public static void handleServer(ToggleSensorGlassesPayload payload, IPayloadContext cxt) {
        cxt.enqueueWork(() -> {
            Player player = cxt.player();
            PlayerSpaceData data = player.getData(GalacticraftAttachments.PLAYER_SPACE_DATA);

            data.toggleSensorGlasses(data.isSensorGlassesActivated());

        }).exceptionally(e -> GalacticraftComponents.networkFailureMessage(cxt::disconnect, e));
    }
}
