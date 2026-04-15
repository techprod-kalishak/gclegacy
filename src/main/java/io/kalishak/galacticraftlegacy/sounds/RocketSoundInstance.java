/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.sounds;

import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.AbstractAutoRocket;
import io.kalishak.galacticraftlegacy.world.entity.vehicle.rocket.AbstractSpaceShip;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;

public class RocketSoundInstance extends AbstractTickableSoundInstance {
    public static final int TRANSITION_STARTHEIGHT = 200;
    private final AbstractAutoRocket autoRocket;
    private boolean ignition;

    RocketSoundInstance(AbstractAutoRocket autoRocket) {
        super(GalacticraftSounds.SHUTTLE.get(), SoundSource.AMBIENT, autoRocket.getRandom());
        this.autoRocket = autoRocket;
        this.volume = 0.00001F;
        this.pitch = 0.0F;
        this.attenuation = Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
        updateSoundPosition();
    }

    public static RocketSoundInstance create(AbstractAutoRocket autoRocket) {
        return new RocketSoundInstance(autoRocket);
    }

    @Override
    public void tick() {
        if (this.autoRocket.isAlive()) {
            AbstractSpaceShip.LaunchPhase launchPhase = this.autoRocket.getLaunchPhase();

            if (launchPhase == AbstractSpaceShip.LaunchPhase.IGNITED) {
                if (!this.ignition) {
                    this.pitch = 0.0F;
                    this.ignition = true;
                }

                if (this.autoRocket.getTimeUntilLaunch() < this.autoRocket.getPreLaunchDelay()) {
                    if (this.pitch < 1.0F) {
                        this.pitch += 0.0025F;
                    } else if (this.pitch > 1.0F) {
                        this.pitch = 1.0F;
                    }
                }
            } else {
                this.pitch = 1.0F;
            }

            if (launchPhase == AbstractSpaceShip.LaunchPhase.LAUNCHED || launchPhase == AbstractSpaceShip.LaunchPhase.LANDING) {
                if (this.autoRocket.getY() > 1000) {
                    this.volume = 0.0F;

                    if (launchPhase != AbstractSpaceShip.LaunchPhase.LANDING) {
                        stop();
                    }
                } else if (this.autoRocket.getY() > TRANSITION_STARTHEIGHT) {
                    this.volume = 1.0F - (float) ((this.autoRocket.getY() - TRANSITION_STARTHEIGHT) / (1000.0F - TRANSITION_STARTHEIGHT));
                } else {
                    this.volume = 1.0F;
                }
            }

            updateSoundPosition();
        } else {
            stop();
        }

    }

    public void stopRocketSound() {
        stop();
    }

    public void updateSoundPosition() {
        this.x = this.autoRocket.getX();
        this.y = this.autoRocket.getY();
        this.z = this.autoRocket.getZ();
    }
}
