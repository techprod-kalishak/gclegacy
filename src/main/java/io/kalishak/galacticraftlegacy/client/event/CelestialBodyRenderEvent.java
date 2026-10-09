package io.kalishak.galacticraftlegacy.client.event;

import io.kalishak.galacticraftlegacy.galaxies.celestial.CelestialBodyInstance;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.Event;
import org.joml.Vector3f;

public abstract class CelestialBodyRenderEvent extends Event {
    private final CelestialBodyInstance celestialBody;

    public CelestialBodyRenderEvent(CelestialBodyInstance celestialBody) {
        this.celestialBody = celestialBody;
    }

    public CelestialBodyInstance getCelestialBody() {
        return this.celestialBody;
    }

    public boolean isCanceled() {
        return false;
    }

    public static abstract class RenderRing extends CelestialBodyRenderEvent {
        public RenderRing(CelestialBodyInstance celestialBody) {
            super(celestialBody);
        }

        public static class Pre extends RenderRing {
            public final Vector3f parentOffset;

            public Pre(CelestialBodyInstance celestialBody, Vector3f parentOffset) {
                super(celestialBody);
                this.parentOffset = parentOffset;
            }

            public Vector3f getParentOffset() {
                return this.parentOffset;
            }
        }

        public static class Post extends RenderRing {
            public Post(CelestialBodyInstance celestialBody) {
                super(celestialBody);
            }
        }
    }

    public static class Pre extends CelestialBodyRenderEvent {
        private Identifier celestialBodyTexture;
        private int spriteSize;

        public Pre(CelestialBodyInstance celestialBody, Identifier celestialBodyTexture, int spriteSize) {
            super(celestialBody);
            this.celestialBodyTexture = celestialBodyTexture;
            this.spriteSize = spriteSize;
        }

        public Identifier getCelestialBodyTexture() {
            return this.celestialBodyTexture;
        }

        public void setCelestialBodyTexture(Identifier celestialBodyTexture) {
            this.celestialBodyTexture = celestialBodyTexture;
        }

        public int getSpriteSize() {
            return this.spriteSize;
        }

        public void setSpriteSize(int spriteSize) {
            this.spriteSize = spriteSize;
        }
    }

    public static class Post extends CelestialBodyRenderEvent {
        public Post(CelestialBodyInstance celestialBody) {
            super(celestialBody);
        }
    }
}
