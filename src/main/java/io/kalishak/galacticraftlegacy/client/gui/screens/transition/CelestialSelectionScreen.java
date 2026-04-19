/*
 * Copyright (c) 2026 Kalishak
 *
 * Licensed under the MIT license
 * See LICENSE file for more details
 */

package io.kalishak.galacticraftlegacy.client.gui.screens.transition;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.kalishak.galacticraftlegacy.Constants;
import io.kalishak.galacticraftlegacy.galaxies.CelestialObject;
import io.kalishak.galacticraftlegacy.galaxies.GalacticraftGalaxies;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

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
    protected float ticksSinceSelectionF = 0;
    protected float ticksSinceUnselectionF = -1;
    protected float ticksSinceMenuOpenF = 0;
    protected float ticksTotalF = 0;
    protected int animateGrandchildren = 0;
    protected Vec2 position = Vec2.ZERO;
    protected Map<CelestialObject, Vec3> planetPosMap = Maps.newHashMap();
    protected CelestialObject selectedBody;
    protected CelestialObject lastSelectedBody;
    protected static int BORDER_SIZE = 0;
    protected static int BORDER_EDGE_SIZE = 0;
    protected int canCreateOffset = 24;
    protected ViewType viewState = ViewType.PREVIEW;
    protected SelectionType selectionState = SelectionType.DEFAULT;
    protected int zoomTooltipPos = 0;
    protected CelestialObject selectedParent = GalacticraftGalaxies.SOL.get();
    protected final boolean mapMode;
    public List<CelestialObject> possibleBodies;
    //public Map<Integer, Map<String, StationDataGUI>> spaceStationMap = Maps.newHashMap();

    protected String selectedStationOwner = "";
    protected int spaceStationListOffset = 0;
    protected boolean renamingSpaceStation;
    protected String renamingString = "";
    protected Vec2 translation = Vec2.ZERO;
    protected boolean mouseDragging = false;
    protected int lastMovePosX = -1;
    protected int lastMovePosY = -1;
    protected boolean errorLogged = false;
    public boolean canCreateStations = false;
    protected List<CelestialObject> bodiesToRender = Lists.newArrayList();

    public CelestialSelectionScreen(boolean mapMode, List<CelestialObject> possibleBodies, boolean canCreateStations) {
        super(GameNarrator.NO_TITLE);
        this.mapMode = mapMode;
        this.possibleBodies = possibleBodies;
        this.canCreateStations = canCreateStations;
    }

    @Override
    protected void init() {
        super.init();

        //this.bodiesToRender.addAll(GalacticraftGalaxies.getRenderables());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        super.extractRenderState(graphics, mouseX, mouseY, a);
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
