/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.galaxy;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import io.kalishak.galacticraftlegacy.client.event.CelestialBodyRenderEvent;
import io.kalishak.galacticraftlegacy.config.ClientConfig;
import io.kalishak.galacticraftlegacy.config.ServerConfig;
import io.kalishak.galacticraftlegacy.data.GalacticraftTags;
import io.kalishak.galacticraftlegacy.galaxies.BuiltinGalaxies;
import io.kalishak.galacticraftlegacy.galaxies.celestial.CelestialBodyInstance;
import io.kalishak.galacticraftlegacy.galaxies.celestial.Star;
import io.kalishak.galacticraftlegacy.galaxies.celestial.WorldCelestials;
import io.kalishak.galacticraftlegacy.network.payload.CreateSpaceStationPayload;
import io.kalishak.galacticraftlegacy.network.payload.PlayerTransitionPayload;
import io.kalishak.galacticraftlegacy.references.Constants;
import io.kalishak.galacticraftlegacy.registry.GalacticraftRegistries;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.SavedDataStorage;
import net.minecraft.world.phys.Vec2;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.common.NeoForge;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CelestialSelectionScreen extends Screen {
    protected static final int MAX_SPACE_STATION_NAME_LENGTH = 32;
    protected float zoom = 0.0F;
    protected float planetZoom = 0.0F;
    protected boolean doneZooming = false;
    protected float preSelectZoom = 0.0F;
    protected Vec2 preSelectPosition = Vec2.ZERO;
    protected static final Identifier BACKGROUND = Constants.id("textures/gui/celestial_selection.png");
    protected static final Identifier BACKGROUND_ALT = Constants.id("textures/gui/celestial_selection_alt.png");
    protected float ticksSinceSelection = 0;
    protected float ticksSinceUnselection = -1;
    protected float ticksSinceMenuOpen = 0;
    protected float ticksTotal = 0;
    protected int animateGrandchildren = 0;
    protected Vec2 position = Vec2.ZERO;
    protected Map<CelestialBodyInstance, Vector3f> planetPosMap = Maps.newHashMap();
    protected CelestialBodyInstance selectedBody;
    protected CelestialBodyInstance lastSelectedBody;
    protected static float BORDER_SIZE = 0;
    protected static float BORDER_EDGE_SIZE = 0;
    protected int canCreateOffset = 24;
    protected ViewType viewState = ViewType.PREVIEW;
    protected SelectionType selectionState = SelectionType.DEFAULT;
    protected int zoomTooltipPos = 0;
    protected CelestialBodyInstance selectedParent;
    protected final boolean canPlayerLeave;
    public List<CelestialBodyInstance> possibleBodies;
    public Map<CelestialBodyInstance, Map<String, Holder<DimensionType>>> spaceStationMap = Maps.newHashMap();

    protected String selectedStationOwner = "";
    protected int spaceStationListOffset = 0;
    protected boolean renamingSpaceStation;
    protected String renamingString = "";
    protected Vec2 translation = Vec2.ZERO;
    protected boolean mouseDragging = false;
    protected int lastMovePosX = -1;
    protected int lastMovePosY = -1;
    protected boolean errorLogged = false;
    public boolean canCreateStations;
    protected List<CelestialBodyInstance> bodiesToRender = Lists.newArrayList();
    private final WorldCelestials worldCelestials;
    private final RegistryAccess registryAccess;

    public CelestialSelectionScreen(WorldCelestials worldCelestials, RegistryAccess registryAccess, boolean canPlayerLeave, List<CelestialBodyInstance> possibleBodies, boolean canCreateStations) {
        super(GameNarrator.NO_TITLE);
        this.worldCelestials = worldCelestials;
        this.registryAccess = registryAccess;
        this.canPlayerLeave = canPlayerLeave;
        this.possibleBodies = possibleBodies;
        this.canCreateStations = canCreateStations;
        this.selectedParent = worldCelestials.get(this.registryAccess.getOrThrow(BuiltinGalaxies.SOL));
    }

    public static CelestialSelectionScreen create(RegistryAccess registryAccess, SavedDataStorage savedDataStorage, boolean canPlayerLeave, boolean canCreateStations) {
        WorldCelestials worldCelestials = savedDataStorage.computeIfAbsent(WorldCelestials.ID);
        List<CelestialBodyInstance> celestialBodies = registryAccess.lookupOrThrow(GalacticraftRegistries.Keys.CELESTIAL).registryKeySet().stream().map(registryAccess::getOrThrow).map(worldCelestials::get).toList();
        return new CelestialSelectionScreen(worldCelestials, registryAccess, canPlayerLeave, celestialBodies, canCreateStations);
    }

    @Override
    protected void init() {
        super.init();

        CelestialSelectionScreen.BORDER_SIZE = this.width / 65.0F;
        CelestialSelectionScreen.BORDER_EDGE_SIZE = CelestialSelectionScreen.BORDER_SIZE / 4.0F;

        MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();

        if (server != null) {
            WorldCelestials worldCelestials = server.getDataStorage().computeIfAbsent(WorldCelestials.ID);
            this.registryAccess.lookupOrThrow(GalacticraftRegistries.Keys.CELESTIAL)
                    .registryKeySet()
                    .stream()
                    .map(this.registryAccess::getOrThrow)
                    .forEach(body -> this.bodiesToRender.add(worldCelestials.get(body)));
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }

    protected @Nullable Holder<DimensionType> getSatelliteParentDimensionKey(CelestialBodyInstance celestial) {
        if (celestial.isSatellite()) {
            CelestialBodyInstance parent = celestial.parent(this.worldCelestials);

            if (!parent.isEmpty()) {
                return parent.typeHolder().value().targetDimension().orElse(null);
            }
        }

        return null;
    }

    protected Component getGrandParentName() {
        return getRecursiveNameAsString(this.selectedBody);
    }

    protected Component getDirectParentName() {
        CelestialBodyInstance parent = this.selectedBody.parent(this.worldCelestials);

        if (!parent.isEmpty()) {
            return parent.getDisplayName();
        }

        return Component.empty();
    }

    protected float getScale(CelestialBodyInstance celestialBody) {
        float typeScale = celestialBody.is(GalacticraftTags.Celestials.PLANETS) ? 25.0F : 1.0F / 5.0F;

        return 3.0F * celestialBody.typeHolder().value().relativeDistanceFromCenter().unscaled() * typeScale;
    }

    protected Vec2 lerpVec(Vec2 first, Vec2 second, float value) {
        return new Vec2(first.x + value * (second.x - first.x), first.y + value * (second.y - first.y));
    }

    protected float getAdvancedZoom() {
        if (this.ticksTotal < 30.0F) {
            float scale = Math.clamp(this.ticksTotal / 30.0F, 0.0F, 1.0F);

            return Mth.lerp(-0.75F, 0.0F, (float) Math.pow(scale, 0.5F));
        }

        if (this.selectedBody == null || this.selectionState != SelectionType.ZOOMED) {
            if (!this.doneZooming) {
                float unselectScale = Mth.lerp(this.zoom, this.preSelectZoom, Math.clamp(this.ticksSinceUnselection / 100.0F, 0.0F, 1.0F));

                if (unselectScale <= this.preSelectZoom + 0.05F) {
                    this.zoom = this.preSelectZoom;
                    this.ticksSinceUnselection = -1.0F;
                    this.doneZooming = true;
                }

                return unselectScale;
            }

            return this.zoom;
        }

        if (!this.doneZooming) {
            float f = Mth.lerp(this.zoom, 12, Math.clamp((this.ticksSinceSelection - 20.0F) / 40.0F, 0.0F, 1.0F));

            if (f >= 11.95F) {
                this.doneZooming = true;
            }

            return f;
        }

        return 12.0F + this.planetZoom;
    }

    protected Vec2 getAdvancedTranslation(RegistryAccess access, float partialTicks) {
        if (this.selectedBody == null) {
            if (this.ticksSinceUnselection > 0) {
                float f0 = Math.clamp(this.ticksSinceUnselection / 100.0F, 0.0F, 1.0F);

                if (f0 >= 0.999999F) {
                    this.ticksSinceUnselection = 0;
                }

                return lerpVec(this.position, this.preSelectPosition, f0);
            }

            return new Vec2(this.position.x + translation.x, this.position.y + translation.y);
        }

        if (!isZoomed()) {
            CelestialBodyInstance parent = this.selectedBody.parent(this.worldCelestials);

            if (!parent.isEmpty()) {
                Vector3f posVec = getCelestialBodyPosition(parent);

                return new Vec2(posVec.x, posVec.y);
            }

            return new Vec2(this.position.x + translation.x, this.position.y + translation.y);
        }

        Vec2 pos = this.position;

        if (this.lastSelectedBody != null) {
            Vector3f pos3 = getCelestialBodyPosition(this.lastSelectedBody);
            pos = new Vec2(pos3.x, pos3.y);
        }

        Vector3f posVec = getCelestialBodyPosition(this.selectedBody);

        return lerpVec(pos, new Vec2(posVec.x, posVec.y), Math.clamp((this.ticksSinceSelection - 18) / 7.5F, 0.0F, 1.0F));
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (this.canPlayerLeave) {
            if (event.isEscape() && this.shouldCloseOnEsc()) {
                this.onClose();
                return true;
            }
        }

        if (event.key() == InputConstants.MOUSE_BUTTON_LEFT) {
            if (this.selectedBody != null) {
                unselectCelestialBody();
            }
        }

        if (this.renamingSpaceStation) {
//            if (event.key() == InputConstants.KEY_BACKSPACE) {
//                if (this.renamingString != null && !this.renamingString.isEmpty()) {
//                    String toBeParsed = this.renamingString.substring(0, this.renamingString.length() - 1);
//
//                    if (this.isValid(toBeParsed))
//                    {
//                        this.renamingString = toBeParsed;
//                    } else
//                    {
//                        this.renamingString = "";
//                    }
//                }
//            } else if (event.key() == InputConstants.KEY_S) {
//                String pastestring = Screen.getClipboardString();
//
//                if (pastestring == null)
//                {
//                    pastestring = "";
//                }
//
//                if (this.isValid(this.renamingString + pastestring))
//                {
//                    this.renamingString = this.renamingString + pastestring;
//                    this.renamingString = this.renamingString.substring(0, Math.min(String.valueOf(this.renamingString).length(), MAX_SPACE_STATION_NAME_LENGTH));
//                }
//            } else if (this.isValid(this.renamingString + keyChar))
//            {
//                this.renamingString = this.renamingString + keyChar;
//                this.renamingString = this.renamingString.substring(0, Math.min(this.renamingString.length(), MAX_SPACE_STATION_NAME_LENGTH));
//            }

            return true;
        }

        if (event.key() == InputConstants.KEY_RETURN) {
            teleportToSelectedBody();

            return true;
        }

        if (event.key() == InputConstants.KEY_LEFT) {
            this.translation = this.translation.add(-2.0F);
            return true;
        }

        if (event.key() == InputConstants.KEY_RIGHT) {
            this.translation = this.translation.add(2.0F);
            return true;
        }

        if (event.key() == InputConstants.KEY_UP) {
            this.translation = this.translation.add(new Vec2(2.0F, -2.0F));
            return true;
        }

        if (event.key() == InputConstants.KEY_DOWN) {
            this.translation = this.translation.add(new Vec2(-2.0F, 2.0F));
            return true;
        }

        return false;
    }

    protected boolean canCreateSpaceStation(CelestialBodyInstance atBody) {
        if (!this.canPlayerLeave || ServerConfig.DISABLE_SPACE_STATIONS.get() || !this.canCreateStations) {
            return false;
        }

        if (!atBody.isReachable() || (this.possibleBodies != null && !this.possibleBodies.contains(atBody))) {
            return false;
        }

        boolean foundRecipe = atBody.typeHolder().value().recipe().isPresent();

        if (!foundRecipe) {
            return false;
        }

        return false; // we dont support that yet
    }

    protected void unselectCelestialBody() {
        this.selectionState = SelectionType.DEFAULT;
        this.ticksSinceUnselection = 0;
        this.lastSelectedBody = this.selectedBody;
        this.selectedBody = null;
        this.doneZooming = false;
        this.selectedStationOwner = "";
        this.animateGrandchildren = 0;
    }

    protected void teleportToSelectedBody() {
        if (this.selectedBody == null) {
            return;
        }

        if (this.selectedBody.isReachable() && this.possibleBodies.contains(this.selectedBody)) {
            ClientPacketDistributor.sendToServer(new PlayerTransitionPayload(this.selectedBody));
        }
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double dx, double dy) {
        if (this.mouseDragging && this.lastMovePosX != -1 && event.button() == InputConstants.MOUSE_BUTTON_LEFT)
        {
            double deltaX = event.x() - this.lastMovePosX;
            double deltaY = event.y() - this.lastMovePosY;
            float scollMultiplier = -Math.abs(this.zoom);

            if (this.zoom == -1.0F) {
                scollMultiplier = -1.5F;
            }

            if (this.zoom >= -0.25F && this.zoom <= 0.15F) {
                scollMultiplier = -0.2F;
            }

            if (this.zoom >= 0.15F) {
                scollMultiplier = -0.15F;
            }

            float newX = (float) (this.translation.x + (deltaX - deltaY) * scollMultiplier * (ClientConfig.INVERT_SCROLL.get() ? -1.0F : 1.0F) * ClientConfig.MOUSE_SENSITIVITY.get() * 0.2F);
            float newY = (float) (this.translation.y + (deltaY + deltaX) * scollMultiplier * (ClientConfig.INVERT_SCROLL.get() ? -1.0F : 1.0F) * ClientConfig.MOUSE_SENSITIVITY.get() * 0.2F);

            this.translation = new Vec2(newX, newY);
        }

        this.lastMovePosX = (int) event.x();
        this.lastMovePosY = (int) event.y();

        return super.mouseDragged(event, dx, dy);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        super.mouseReleased(event);

        this.mouseDragging = false;
        this.lastMovePosX = -1;
        this.lastMovePosY = -1;

        return false;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        super.mouseClicked(event, doubleClick);

        assert getMinecraft().player != null;

        boolean clickHandled = false;
        boolean isSatelliteSelected = this.selectedBody.isSatellite();

        final float LHS = BORDER_SIZE + BORDER_EDGE_SIZE;
        final float RHS = this.width - LHS;
        final float TOP = LHS;

        if (this.selectedBody != null && event.x() > LHS && event.x() < LHS + 88 && event.y() > TOP && event.y() < TOP + 13) {
            unselectCelestialBody();
            return false;
        }

        if (!this.canPlayerLeave) {
            if (event.x() >= RHS - 95 && event.x()< RHS && event.y() > TOP + 181 + this.canCreateOffset && event.y() < TOP + 182 + 12 + this.canCreateOffset) {
                if (this.selectedBody != null) {
                    var recipe = this.selectedBody.typeHolder().value().recipe().orElse(null);

                    if (recipe != null && canCreateSpaceStation(this.selectedBody)) {

                        if (recipe.value().matches(getMinecraft().player, false) || getMinecraft().player.isCreative()) {
                            ClientPacketDistributor.sendToServer(new CreateSpaceStationPayload(recipe));

                            if (!isZoomed()) {
                                this.selectionState = SelectionType.ZOOMED;
                                this.preSelectZoom = this.zoom;
                                this.preSelectPosition = this.position;
                                this.ticksSinceSelection = 0;
                                this.doneZooming = false;
                            }

                            return true;
                        }

                        clickHandled = true;
                    }
                }
            }
        }

        if (event.x() > RHS - 88 && event.x() < RHS && event.y() > TOP && event.y() < TOP + 13) {
            if (this.canPlayerLeave) {
                getMinecraft().gui.setScreen(null);
                clickHandled = true;
            }

            if (this.selectedBody != null && !this.canPlayerLeave) {
                if (isSatelliteSelected || !this.selectedStationOwner.isEmpty()) {
                    teleportToSelectedBody();
                }

                clickHandled = true;
            }
        }

        // Need unscaled mouse coords
        int mouseX = (int) event.x();
        int mouseY = (int) event.y() * -1 + getMinecraft().gui.screen().height - 1;

        if (isSatelliteSelected) {
            if (this.renamingSpaceStation) {
                if (event.x() >= this.width / 2.0F - 90 && event.x() <= this.width / 2.0F + 90 && event.y() >= this.height / 2.0F - 38 && event.y() <= this.height / 2.0F + 38) {

                    if (event.x() >= width / 2.0F - 90 + 17 && event.x() <= width / 2.0F - 90 + 17 + 72 && event.y() >= this.height / 2.0F - 38 + 59 && event.y() <= this.height / 2.0F - 38 + 59 + 12) {
                        String playerName = getMinecraft().player.getScoreboardName();

//                        ResourceKey<Celestial> spacestationID = this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).get(playerName).getStationDimensionID();
//                        if (spacestationID == null) {
//                            spacestationID = this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).get(playerName.toLowerCase()).getStationDimensionID();
//                        }
//
//                        if (spacestationID != null) {
//                            this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).get(strName).setStationName(this.renamingString);
//                            GalacticraftCore.packetPipeline.sendToServer(new PacketSimple(EnumSimplePacket.S_RENAME_SPACE_STATION, GCCoreUtil.getDimensionID(this.mc.world), new Object[] {this.renamingString, spacestationID}));
//                        }

                        this.renamingSpaceStation = false;
                    }
                    // Cancel
                    if (event.x() >= this.width / 2.0F && event.x() <= this.width / 2.0F + 72 && event.y() >= this.height / 2.0F - 38 + 59 && event.y() <= this.height / 2.0F - 38 + 59 + 12) {
                        this.renamingSpaceStation = false;
                    }

                    clickHandled = true;
                }
            } else {
                //drawTexturedModalRect(width / 2 - 47, TOP, 94, 11, 0, 414, 188, 22, false, false);

                if (event.x() >= this.width / 2.0F - 47 && event.x() <= this.width / 2.0F - 47 + 94 && event.y() >= TOP && event.y() <= TOP + 11) {
                    if (this.selectedStationOwner.isEmpty() && this.selectedStationOwner.equalsIgnoreCase(getMinecraft().player.getScoreboardName())) {
                        this.renamingSpaceStation = true;
                        this.renamingString = null;
                        clickHandled = true;
                    }
                }


                int stationListSize = 1;//this.spaceStationMap.get(getSatelliteParentID(selectedSatellite)).size();
                int max = Math.min((this.height / 2) / 14, stationListSize);

                float xPos;
                float yPos;

                // Up button
                xPos = RHS - 85;
                yPos = TOP + 45;

                if (event.x() >= xPos && event.x() <= xPos + 61 && event.y() >= yPos && event.y() <= yPos + 4) {
                    if (this.spaceStationListOffset > 0) {
                        this.spaceStationListOffset--;
                    }

                    clickHandled = true;
                }

                // Down button
                xPos = RHS - 85;
                yPos = TOP + 49 + max * 14;

                if (event.x() >= xPos && event.x() <= xPos + 61 && event.y() >= yPos && event.y() <= yPos + 4) {
                    if (max + spaceStationListOffset < stationListSize) {
                        this.spaceStationListOffset++;
                    }

                    clickHandled = true;
                }

                Iterator<Map.Entry<String, Holder<DimensionType>>> it = this.spaceStationMap.get(getSatelliteParentDimensionKey(this.selectedBody)).entrySet().iterator();
                int i = 0;
                int j = 0;

                while (it.hasNext() && i < max) {
                    Map.Entry<String, Holder<DimensionType>> e = it.next();

                    if (j >= this.spaceStationListOffset) {
                        int xOffset = 0;

                        if (e.getKey().equalsIgnoreCase(this.selectedStationOwner)) {
                            xOffset -= 5;
                        }

                        xPos = RHS - 95 + xOffset;
                        yPos = TOP + 50 + i * 14;

                        if (event.x() >= xPos && event.x() <= xPos + 93 && event.y() >= yPos && event.y() <= yPos + 12) {
                            this.selectedStationOwner = e.getKey();
                            clickHandled = true;
                        }
                        i++;
                    }
                    j++;
                }
            }
        }

        float xPos = LHS + 2;
        float yPos = TOP + 10;

        boolean planetZoomedMoon = isZoomed() && this.selectedParent.is(GalacticraftTags.Celestials.PLANETS);

        // Top yellow button e.g. Sol
        if (event.x() >= xPos && event.x() <= xPos + 93 && event.x() >= yPos && event.x() <= yPos + 12) {
            if (this.selectedBody == null) {
                this.preSelectZoom = this.zoom;
                this.preSelectPosition = this.position;
            }

            SelectionType selectionCountOld = this.selectionState;

            if (isSelected()) {
                unselectCelestialBody();
            }

            if (selectionCountOld == SelectionType.ZOOMED) {
                this.selectionState = SelectionType.SELECTED;
            }

            this.selectedBody = this.selectedParent;
            this.ticksSinceSelection = 0;
            this.selectionState = SelectionType.values()[this.selectionState.ordinal() + 1];

            if (isZoomed() && !planetZoomedMoon) {
                this.ticksSinceMenuOpen = 0;
            }

            clickHandled = true;
        }

        yPos += 22;

        // First blue button - normally the Selected Body (but it's the parent
        // planet if this is a moon)
        if (event.x() >= xPos && event.x() <= xPos + 93 && event.y() >= yPos && event.y() <= yPos + 12) {

            if (planetZoomedMoon) {
                if (this.selectedBody == null) {
                    this.preSelectZoom = this.zoom;
                    this.preSelectPosition = this.position;
                }

                SelectionType selectionCountOld = this.selectionState;

                if (isSelected()) {
                    unselectCelestialBody();
                }

                if (selectionCountOld == SelectionType.ZOOMED) {
                    this.selectionState = SelectionType.SELECTED;
                }

                this.selectedBody = this.selectedParent;
                this.ticksSinceSelection = 0;
                this.selectionState = SelectionType.values()[this.selectionState.ordinal() + 1];
            }

            clickHandled = true;
        }

        if (!clickHandled) {
            Collection<CelestialBodyInstance> children = CelestialBodyInstance.getChildren(this.registryAccess, isZoomed() && !(this.selectedParent.is(GalacticraftTags.Celestials.PLANETS)) ? this.selectedBody : this.selectedParent, this.worldCelestials);

            yPos = TOP + 50;

            for (CelestialBodyInstance child : children) {
                clickHandled = testClicked(child, child.is(this.selectedBody.typeHolder()) ? 5 : 0, yPos, event.x(), event.y(), false);
                yPos += 14;

                if (!clickHandled && !isZoomed() && child.is(this.selectedBody.typeHolder())) {
                    Collection<CelestialBodyInstance> grandchildren = CelestialBodyInstance.getChildren(this.registryAccess, child, this.worldCelestials);
                    int gOffset = 0;

                    for (CelestialBodyInstance grandchild : grandchildren) {
                        if (gOffset + 14 > this.animateGrandchildren) {
                            break;
                        }

                        clickHandled = testClicked(grandchild, 10, yPos, event.x(), event.y(), true);
                        yPos += 14;
                        gOffset += 14;

                        if (clickHandled) break;
                    }

                    yPos += this.animateGrandchildren - gOffset;
                }

                if (clickHandled) break;
            }
        }

        if (!clickHandled) {
            for (Map.Entry<CelestialBodyInstance, Vector3f> e : this.planetPosMap.entrySet()) {
                CelestialBodyInstance bodyClicked = e.getKey();
                CelestialBodyInstance parent = bodyClicked.parent(this.worldCelestials);

                if (this.selectedBody == null && !parent.isEmpty()) {
                    continue;
                }

                double iconSize = e.getValue().z; // Z value holds size on-screen

                if (mouseX >= e.getValue().x - iconSize && mouseX <= e.getValue().x + iconSize && mouseY >= e.getValue().y - iconSize && mouseY <= e.getValue().y + iconSize) {
                    if (this.selectedBody != bodyClicked || !isZoomed()) {
                        if (isSelected() && this.selectedBody != bodyClicked) {

                            if (parent.isEmpty() || (!parent.is(bodyClicked.typeHolder()))) {
                                this.unselectCelestialBody();
                            } else if (isZoomed()) {
                                this.selectionState = SelectionType.SELECTED;
                            }
                        }

                        if (bodyClicked != this.selectedBody) {
                            this.lastSelectedBody = this.selectedBody;
                            this.animateGrandchildren = 0;

                            if (parent.isEmpty() || (!parent.is(bodyClicked.typeHolder()))) {
                                // Only unzoom if the new selected body is not
                                // the child of the previously selected body
                                this.selectionState = SelectionType.DEFAULT;
                            }
                        } else {
                            this.doneZooming = false;
                            this.planetZoom = 0.0F;
                        }

                        this.selectedBody = bodyClicked;
                        this.ticksSinceSelection = 0;
                        this.selectionState = SelectionType.values()[this.selectionState.ordinal() + 1];

                        if (!parent.isEmpty()) {
                            this.selectionState = SelectionType.ZOOMED;
                        }

                        if (isZoomed()) {
                            this.ticksSinceMenuOpen = 0;
                        }


                        if (this.selectedBody.isSatellite() && this.spaceStationMap.get(getSatelliteParentDimensionKey(this.selectedBody)).size() == 1) {
                            Iterator<Map.Entry<String, Holder<DimensionType>>> it = this.spaceStationMap.get(this.getSatelliteParentDimensionKey(this.selectedBody)).entrySet().iterator();
                            this.selectedStationOwner = it.next().getKey();
                        }

                        clickHandled = true;
                        break;
                    }
                }
            }
        }

        if (!clickHandled) {
            if (this.selectedBody != null) {
                this.unselectCelestialBody();
                this.planetZoom = 0.0F;
            }

            this.mouseDragging = true;
        }

        CelestialBodyInstance selectedParent = this.selectedParent;
        if (this.selectedBody != null) {
            CelestialBodyInstance nextParent = this.selectedParent.parent(this.worldCelestials);
            if (!nextParent.isEmpty()) {
                selectedParent = nextParent;
            }
        } else {
            selectedParent = this.worldCelestials.get(this.registryAccess.getOrThrow(BuiltinGalaxies.SOL));
        }

        if (this.selectedParent != selectedParent) {
            this.selectedParent = selectedParent;
        }

        return true;
    }

    protected boolean testClicked(CelestialBodyInstance body, int xOffset, float yPos, double x, double y, boolean grandchild) {
        float xPos = BORDER_SIZE + BORDER_EDGE_SIZE + 2 + xOffset;
        if (x >= xPos && x <= xPos + 93 && y >= yPos && y <= yPos + 12) {
            if (!this.selectedBody.is(body) || !this.isZoomed()) {
                if (this.selectedBody == null) {
                    this.preSelectZoom = this.zoom;
                    this.preSelectPosition = this.position;
                }

                SelectionType selectionCountOld = this.selectionState;

                if (selectionCountOld == SelectionType.ZOOMED) {
                    this.selectionState = SelectionType.SELECTED;
                }

                this.doneZooming = false;
                this.planetZoom = 0.0F;

                if (body != this.selectedBody) {
                    this.lastSelectedBody = this.selectedBody;
                    this.selectionState = SelectionType.SELECTED;
                } else {
                    this.selectionState = SelectionType.values()[this.selectionState.ordinal() + 1];
                }

                this.selectedBody = body;
                this.ticksSinceSelection = 0;

                if (grandchild)
                    this.selectionState = SelectionType.ZOOMED;

                if (this.isZoomed()) {
                    this.ticksSinceMenuOpen = 0;
                }

                this.animateGrandchildren = 0;
                return true;
            }
        }
        return false;
    }

    protected Vector3f getCelestialBodyPosition(@Nullable CelestialBodyInstance celestialBody) {
        if (celestialBody == null || celestialBody.isEmpty())
            return new Vector3f(0.0F, 0.0F, 0.0F);
        if (celestialBody.typeHolder().value() instanceof Star star) {
            return star.position().toVector3f();
        }

        boolean isPlanet = celestialBody.is(GalacticraftTags.Celestials.PLANETS);
        float timeScale = isPlanet ? 200.0F : 2.0F;
        float distanceFromCenter = getScale(celestialBody);
        Vector3f cBodyPos = new Vector3f((float) Math.sin(ticksTotal / (timeScale * celestialBody.typeHolder().value().relativeOrbitTime()) + celestialBody.typeHolder().value().phaseShift()) * distanceFromCenter, (float) Math.cos(ticksTotal / (timeScale * celestialBody.typeHolder().value().relativeOrbitTime()) + celestialBody.typeHolder().value().phaseShift()) * distanceFromCenter, 0);

        if (isPlanet) {
            Vector3f parentVec = this.planetPosMap.get(CelestialBodyInstance.getStar(celestialBody, this.worldCelestials));
            return cBodyPos.add(parentVec);
        }

        if (CelestialBodyInstance.isLesserBody(this.registryAccess, celestialBody, this.worldCelestials)) {
            Vector3f parentVec = this.planetPosMap.get(celestialBody.parent(this.worldCelestials));
            return cBodyPos.add(parentVec);
        }

        return cBodyPos;
    }

    public void drawTexturedModalRect(PoseStack poseStack, GuiGraphicsExtractor guiGraphics, SubmitNodeCollector submitNodeCollector, float x, float y, float width, float height, float u, float v, float uWidth, float vHeight, boolean invertX, boolean invertY, float texSizeX, float texSizeY) {

        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lines(), (_, buffer) -> {
            float texModX = 1F / texSizeX;
            float texModY = 1F / texSizeY;
            float height0 = invertY ? 0 : vHeight;
            float height1 = invertY ? vHeight : 0;
            float width0 = invertX ? uWidth : 0;
            float width1 = invertX ? 0 : uWidth;
            buffer.addVertexWith2DPose(guiGraphics.pose(), x, y + height).setUv((u + width0) * texModX, (v + height0) * texModY);
            buffer.addVertexWith2DPose(guiGraphics.pose(), x + width, y + height).setUv((u + width1) * texModX, (v + height0) * texModY);
            buffer.addVertexWith2DPose(guiGraphics.pose(), x + width, y).setUv((u + width1) * texModX, (v + height1) * texModY);
            buffer.addVertexWith2DPose(guiGraphics.pose(), x, y).setUv((u + width0) * texModX, (v + height1) * texModY);
        });
    }

    public void drawCircles(GuiGraphicsExtractor guiGraphics) {
        int count = 0;

        final float theta = (float) (2 * Math.PI / 90);
        final float cos = (float) Math.cos(theta);
        final float sin = (float) Math.sin(theta);

        guiGraphics.pose().pushMatrix();
        for (CelestialBodyInstance body : this.bodiesToRender) {
            Vector3f systemOffset;
            if (CelestialBodyInstance.isLesserBody(this.registryAccess, body, this.worldCelestials)) {
                systemOffset = getCelestialBodyPosition(body.parent(this.worldCelestials));
            } else {
                systemOffset = getCelestialBodyPosition(CelestialBodyInstance.getStar(body, this.worldCelestials));
            }

            float x = getScale(body);
            float y = 0;

            float alpha = getAlpha(body);

            if (alpha > 0.0F) {
//                switch (count % 2) {
//                    case 0 -> GL11.glColor4f(0.0F / 1.4F, 0.6F / 1.4F, 1.0F / 1.4F, alpha / 1.4F);
//                    case 1 -> GL11.glColor4f(0.3F / 1.4F, 0.8F / 1.4F, 1.0F / 1.4F, alpha / 1.4F);
//                }

                CelestialBodyRenderEvent.RenderRing.Pre preEvent = new CelestialBodyRenderEvent.RenderRing.Pre(body, systemOffset);
                NeoForge.EVENT_BUS.post(preEvent);

                if (!preEvent.isCanceled()) {
                    guiGraphics.pose().transform(systemOffset);

                    float temp;
                    for (int i = 0; i < 90; i++) {
                        guiGraphics.pose().transformDirection(new Vector2f(x, y));

                        temp = x;
                        x = cos * x - sin * y;
                        y = sin * temp + cos * y;
                    }

                    guiGraphics.pose().transform(systemOffset.negate());

                    count++;
                }

                CelestialBodyRenderEvent.RenderRing.Post postEvent = new CelestialBodyRenderEvent.RenderRing.Post(body);
                NeoForge.EVENT_BUS.post(postEvent);
            }
        }

        guiGraphics.pose().popMatrix();
    }

    public float getAlpha(CelestialBodyInstance body) {
        float alpha = 1.0F;

        if (CelestialBodyInstance.isLesserBody(this.registryAccess, body, this.worldCelestials)) {
            boolean selected = body == this.selectedBody || (isParentSelected(body) && this.selectionState != SelectionType.SELECTED);
            boolean ready = this.lastSelectedBody != null || this.ticksSinceSelection > 35;
            boolean isSibling = CelestialBodyInstance.getSiblings(this.registryAccess, this.selectedBody, this.worldCelestials).contains(body);
            boolean isPossible = !body.isSatellite() || (this.possibleBodies != null && this.possibleBodies.contains(body));

            if ((!selected && !isSibling) || !isPossible) {
                alpha = 0.0F;
            } else if (isZoomed() && ((!selected || !ready) && !isSibling)) {
                alpha = Math.clamp((this.ticksSinceSelection - 30) / 15.0F, 0.0F, 1.0F);
            }
        } else {
            boolean isSelected = this.selectedBody == body;
            boolean isChildSelected = CelestialBodyInstance.isLesserBody(this.registryAccess, this.selectedBody, this.worldCelestials);
            boolean isOwnChildSelected = isChildSelected && isParentSelected(body);

            if (!isSelected && !isOwnChildSelected && (this.isZoomed() || isChildSelected)) {
                if (this.lastSelectedBody != null || CelestialBodyInstance.isLesserBody(this.registryAccess, this.selectedBody, this.worldCelestials)) {
                    alpha = 0.0F;
                } else {
                    alpha = 1.0F - Math.min(this.ticksSinceSelection / 25.0F, 1.0F);
                }
            }
        }

        return alpha;
    }

    protected boolean isZoomed() {
        return this.selectionState == SelectionType.ZOOMED;
    }

    protected boolean isSelected() {
        return this.selectionState != SelectionType.DEFAULT;
    }

    private Component getRecursiveNameAsString(CelestialBodyInstance celestialBody) {
        CelestialBodyInstance parent = celestialBody.parent(this.worldCelestials);

        if (parent.isEmpty()) {
            return celestialBody.getDisplayName();
        }

        return getRecursiveNameAsString(parent);
    }

    public boolean isParentSelected(CelestialBodyInstance celestial) {
        CelestialBodyInstance parent = celestial.parent(this.worldCelestials);

        if (parent.isEmpty()) return false;

        return parent.is(this.selectedBody);
    }

    protected enum ViewType {
        PREVIEW,
        PROFILE
    }

    protected enum SelectionType {
        DEFAULT,
        SELECTED,
        ZOOMED
    }
}
