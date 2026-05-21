package io.kalishak.galacticraftlegacy.transfer.node;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.Constants;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.List;

public class NetworkNodesSaveData extends SavedData {
    public static final SavedDataType<NetworkNodesSaveData> SAVE_DATA_ID = new SavedDataType<>(
            Constants.id("network_nodes"),
            NetworkNodesSaveData::new,
            NetworkNodesSaveData::codec
    );
    private final List<FluidNodeNetwork.Packed> networks = new ArrayList<>();

    NetworkNodesSaveData(ServerLevel serverLevel) {

    }

    NetworkNodesSaveData(ServerLevel serverLevel, List<FluidNodeNetwork.Packed> networks) {
        updateNetworks(networks);
    }

    public void updateNetworks(List<FluidNodeNetwork.Packed> networks) {
        this.networks.addAll(networks);
        setDirty();
    }

    static Codec<NetworkNodesSaveData> codec(ServerLevel serverLevel) {
        return FluidNodeNetwork.Packed.CODEC
                .listOf()
                .xmap(networks -> new NetworkNodesSaveData(serverLevel, networks), saveData -> saveData.networks);
    }

    static List<FluidNodeNetwork.Packed> get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(SAVE_DATA_ID).networks;
    }
}
