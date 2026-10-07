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
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;

import java.util.ArrayList;
import java.util.List;

public class NetworkNodesSaveData extends SavedData {
    public static final Codec<NetworkNodesSaveData> CODEC = NodeNetwork.PackedNode.CODEC.listOf()
            .xmap(NetworkNodesSaveData::new, networkNodesSaveData -> networkNodesSaveData.networks);
    public static final SavedDataType<NetworkNodesSaveData> SAVE_DATA_ID = new SavedDataType<>(
            Constants.id("network_nodes"),
            NetworkNodesSaveData::new,
            CODEC
    );
    private final List<NodeNetwork.PackedNode> networks = new ArrayList<>();

    NetworkNodesSaveData() {

    }

    NetworkNodesSaveData(List<NodeNetwork.PackedNode> networks) {
        updateNetworks(networks);
    }

    public void updateNetworks(List<NodeNetwork.PackedNode> networks) {
        this.networks.addAll(networks);
        setDirty();
    }

    static List<NodeNetwork.PackedNode> get(MinecraftServer server) {
        return server.getDataStorage().computeIfAbsent(SAVE_DATA_ID).networks;
    }
}
