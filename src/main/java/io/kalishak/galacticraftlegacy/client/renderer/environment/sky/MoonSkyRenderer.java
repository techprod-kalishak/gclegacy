/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.renderer.environment.sky;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import io.kalishak.galacticraftlegacy.references.Constants;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.client.CustomSkyboxRenderer;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public class MoonSkyRenderer implements CustomSkyboxRenderer {
    public static final Identifier ID = Constants.id("moon");

    private final RenderSystem.AutoStorageIndexBuffer quadIndices;
    private GpuBuffer starsBuffer;
    private static int starIndexCount = 1500;

    public MoonSkyRenderer() {
        this.quadIndices = RenderSystem.getSequentialBuffer(PrimitiveTopology.QUADS);
    }

    private void init() {
        this.starsBuffer = buildStars();
    }

    private GpuBuffer buildStars() {
        RandomSource random = RandomSource.createThreadLocalInstance(10842L);
        float starDistance = 100.0F;

        GpuBuffer var19;
        try (ByteBufferBuilder byteBufferBuilder = ByteBufferBuilder.exactlySized(DefaultVertexFormat.POSITION.getVertexSize() * 1500 * 4)) {
            BufferBuilder bufferBuilder = new BufferBuilder(byteBufferBuilder, PrimitiveTopology.QUADS, DefaultVertexFormat.POSITION);

            for(int i = 0; i < 1500; ++i) {
                float x = random.nextFloat() * 2.0F - 1.0F;
                float y = random.nextFloat() * 2.0F - 1.0F;
                float z = random.nextFloat() * 2.0F - 1.0F;
                float starSize = 0.15F + random.nextFloat() * 0.1F;
                float lengthSq = Mth.lengthSquared(x, y, z);
                if (!(lengthSq <= 0.010000001F) && !(lengthSq >= 1.0F)) {
                    Vector3f starCenter = (new Vector3f(x, y, z)).normalize(100.0F);
                    float zRot = (float)(random.nextDouble() * (double)(float)Math.PI * (double)2.0F);
                    Matrix3f rotation = (new Matrix3f()).rotateTowards((new Vector3f(starCenter)).negate(), new Vector3f(0.0F, 1.0F, 0.0F)).rotateZ(-zRot);
                    bufferBuilder.addVertex((new Vector3f(starSize, -starSize, 0.0F)).mul(rotation).add(starCenter));
                    bufferBuilder.addVertex((new Vector3f(starSize, starSize, 0.0F)).mul(rotation).add(starCenter));
                    bufferBuilder.addVertex((new Vector3f(-starSize, starSize, 0.0F)).mul(rotation).add(starCenter));
                    bufferBuilder.addVertex((new Vector3f(-starSize, -starSize, 0.0F)).mul(rotation).add(starCenter));
                }
            }

            try (MeshData mesh = bufferBuilder.buildOrThrow()) {
                MoonSkyRenderer.starIndexCount = mesh.drawState().indexCount();
                var19 = RenderSystem.getDevice().createBuffer(() -> "Stars vertex buffer", 40, mesh.vertexBuffer());
            }
        }

        return var19;
    }
}
