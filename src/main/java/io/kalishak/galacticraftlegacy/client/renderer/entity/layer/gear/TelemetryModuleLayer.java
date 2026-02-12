package io.kalishak.galacticraftlegacy.client.renderer.entity.layer.gear;

import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.client.renderer.entity.state.GearRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.model.obj.ObjModel;

public class TelemetryModuleLayer<S extends LivingEntityRenderState, M extends EntityModel<S>> extends GearEquipmentLayer<S, M> {
    public static final Identifier TEXTURES = Constants.texture("model/telemetry_module.png");

    public TelemetryModuleLayer(RenderLayerParent<S, M> renderer, EquipmentLayerRenderer layerRenderer, EquipmentAssetManager equipmentAssetManager) {
        super(renderer, null, layerRenderer, equipmentAssetManager);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState, float yRot, float xRot) {
        if (getDataFromContext(renderState, GearRenderState.HAS_TELEMETRY, false)) {

        }
    }
}
