/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.transfer.node;

import com.mojang.serialization.Codec;
import io.kalishak.galacticraftlegacy.references.Constants;
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
            NetworkNodesSaveData::packCodec
    );
    private final List<NodeNetwork.PackedNode> networks = new ArrayList<>();

    NetworkNodesSaveData(ServerLevel serverLevel) {

    }

    NetworkNodesSaveData(ServerLevel serverLevel, List<NodeNetwork.PackedNode> networks) {
        updateNetworks(networks);
    }

    static Codec<NetworkNodesSaveData> packCodec(ServerLevel serverLevel) {
        return NodeNetwork.PackedNode.CODEC
                .listOf()
                .xmap(packedNodes -> new NetworkNodesSaveData(serverLevel, packedNodes), networkNodesSaveData -> networkNodesSaveData.networks);
    }

    public void updateNetworks(List<NodeNetwork.PackedNode> networks) {
        this.networks.addAll(networks);
        setDirty();
    }

    static List<NodeNetwork.PackedNode> get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(SAVE_DATA_ID).networks;
    }
}
