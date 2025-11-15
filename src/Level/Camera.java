package Level;

import Engine.GraphicsHandler;
import Engine.ScreenManager;
import GameObject.GameObject;
import GameObject.Rectangle;

import java.awt.*;
import java.util.ArrayList;

public class Camera extends Rectangle {

    private Map map;

    private int tileWidth, tileHeight;

    private int leftoverSpaceX, leftoverSpaceY;

    private ArrayList<EnhancedMapTile> activeEnhancedMapTiles = new ArrayList<>();
    private ArrayList<NPC> activeNPCs = new ArrayList<>();
    private ArrayList<Trigger> activeTriggers = new ArrayList<>();

    private final int UPDATE_OFF_SCREEN_RANGE = 4;

    // screen shake when bee gets hit
    private float shakeOffsetX = 0;
    private float shakeOffsetY = 0;
    private long shakeStartTime = 0;
    private static final long SHAKE_DURATION_MS = 300;
    private static final float SHAKE_INTENSITY = 8f;

    // horde shake - more intense
    private static final long HORDE_SHAKE_DURATION_MS = 1200;
    private static final float HORDE_SHAKE_INTENSITY = 20f;
    private boolean isHordeShake = false;

    public Camera(int startX, int startY, int tileWidth, int tileHeight, Map map) {
        super(startX, startY, ScreenManager.getScreenWidth() / tileWidth, ScreenManager.getScreenHeight() / tileHeight);
        this.map = map;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.leftoverSpaceX = ScreenManager.getScreenWidth() % tileWidth;
        this.leftoverSpaceY = ScreenManager.getScreenHeight() % tileHeight;
    }

    public Point getTileIndexByCameraPosition() {
        int xIndex = Math.round(getX()) / tileWidth;
        int yIndex = Math.round(getY()) / tileHeight;
        return new Point(xIndex, yIndex);
    }

    public void update(Player player) {
        updateShake();
        updateMapTiles();
        updateMapEntities(player);
        updateScripts();
    }

    private void updateShake() {
        long currentTime = System.currentTimeMillis();
        long elapsed = currentTime - shakeStartTime;
        
        long duration = isHordeShake ? HORDE_SHAKE_DURATION_MS : SHAKE_DURATION_MS;
        float intensity = isHordeShake ? HORDE_SHAKE_INTENSITY : SHAKE_INTENSITY;
        
        if (elapsed < duration) {
            float progress = (float) elapsed / duration;
            float currentIntensity = intensity * (1f - progress);
            
            shakeOffsetX = (float) (Math.random() * currentIntensity * 2 - currentIntensity);
            shakeOffsetY = (float) (Math.random() * currentIntensity * 2 - currentIntensity);
        } else {
            shakeOffsetX = 0;
            shakeOffsetY = 0;
            isHordeShake = false;
        }
    }

    public void shake() {
        isHordeShake = false;
        shakeStartTime = System.currentTimeMillis();
    }

    public void hordeShake() {
        isHordeShake = true;
        shakeStartTime = System.currentTimeMillis();
    }

    private void updateMapTiles() {
        for (MapTile tile : map.getAnimatedMapTiles()) {
            tile.update();
        }
    }

    public void updateMapEntities(Player player) {
        activeEnhancedMapTiles = loadActiveEnhancedMapTiles();
        activeNPCs = loadActiveNPCs();
        activeTriggers = loadActiveTriggers();

        for (EnhancedMapTile enhancedMapTile : activeEnhancedMapTiles) {
            enhancedMapTile.update(player);
        }

        for (NPC npc : activeNPCs) {
            npc.update(player);
        }
    }

    private void updateScripts() {
        if (map.getActiveScript() != null) {
            map.getActiveScript().update();
        }
    }

    private ArrayList<EnhancedMapTile> loadActiveEnhancedMapTiles() {
        ArrayList<EnhancedMapTile> activeEnhancedMapTiles = new ArrayList<>();
        for (int i = map.getEnhancedMapTiles().size() - 1; i >= 0; i--) {
            EnhancedMapTile enhancedMapTile = map.getEnhancedMapTiles().get(i);

            if (isMapEntityActive(enhancedMapTile)) {
                activeEnhancedMapTiles.add(enhancedMapTile);
                if (enhancedMapTile.mapEntityStatus == MapEntityStatus.INACTIVE) {
                    enhancedMapTile.setMapEntityStatus(MapEntityStatus.ACTIVE);
                }
            } else if (enhancedMapTile.getMapEntityStatus() == MapEntityStatus.ACTIVE) {
                enhancedMapTile.setMapEntityStatus(MapEntityStatus.INACTIVE);
            } else if (enhancedMapTile.getMapEntityStatus() == MapEntityStatus.REMOVED) {
                map.getEnhancedMapTiles().remove(i);
            }
        }
        return activeEnhancedMapTiles;
    }

    private ArrayList<NPC> loadActiveNPCs() {
        ArrayList<NPC> activeNPCs = new ArrayList<>();
        for (int i = map.getNPCs().size() - 1; i >= 0; i--) {
            NPC npc = map.getNPCs().get(i);

            if (isMapEntityActive(npc)) {
                activeNPCs.add(npc);
                if (npc.mapEntityStatus == MapEntityStatus.INACTIVE) {
                    npc.setMapEntityStatus(MapEntityStatus.ACTIVE);
                }
            } else if (npc.getMapEntityStatus() == MapEntityStatus.ACTIVE) {
                npc.setMapEntityStatus(MapEntityStatus.INACTIVE);
            } else if (npc.getMapEntityStatus() == MapEntityStatus.REMOVED) {
                map.getNPCs().remove(i);
            }
        }
        return activeNPCs;
    }

    private ArrayList<Trigger> loadActiveTriggers() {
        ArrayList<Trigger> activeTriggers = new ArrayList<>();
        for (int i = map.getTriggers().size() - 1; i >= 0; i--) {
            Trigger trigger = map.getTriggers().get(i);

            if (isMapEntityActive(trigger)) {
                activeTriggers.add(trigger);
                if (trigger.mapEntityStatus == MapEntityStatus.INACTIVE) {
                    trigger.setMapEntityStatus(MapEntityStatus.ACTIVE);
                }
            } else if (trigger.getMapEntityStatus() == MapEntityStatus.ACTIVE) {
                trigger.setMapEntityStatus(MapEntityStatus.INACTIVE);
            } else if (trigger.getMapEntityStatus() == MapEntityStatus.REMOVED) {
                map.getTriggers().remove(i);
            }
        }
        return activeTriggers;
    }

    private boolean isMapEntityActive(MapEntity mapEntity) {
        return mapEntity.getMapEntityStatus() != MapEntityStatus.REMOVED && !mapEntity.isHidden() && mapEntity.exists() && (mapEntity.isUpdateOffScreen() || containsUpdate(mapEntity));
    }

    public void draw(GraphicsHandler graphicsHandler) {
        graphicsHandler.getGraphics().translate((int) shakeOffsetX, (int) shakeOffsetY);
        
        drawMapTilesBottomLayer(graphicsHandler);
        drawMapTilesTopLayer(graphicsHandler);
        
        graphicsHandler.getGraphics().translate(-(int) shakeOffsetX, -(int) shakeOffsetY);
    }

    public void draw(Player player, GraphicsHandler graphicsHandler) {
        graphicsHandler.getGraphics().translate((int) shakeOffsetX, (int) shakeOffsetY);
        
        drawMapTilesBottomLayer(graphicsHandler);
        drawMapEntities(player, graphicsHandler);
        drawMapTilesTopLayer(graphicsHandler);
        
        graphicsHandler.getGraphics().translate(-(int) shakeOffsetX, -(int) shakeOffsetY);
    }

    public void drawMapTilesBottomLayer(GraphicsHandler graphicsHandler) {
        Point tileIndex = getTileIndexByCameraPosition();
        for (int i = tileIndex.y - 1; i <= tileIndex.y + height + 1; i++) {
            for (int j = tileIndex.x - 1; j <= tileIndex.x + width + 1; j++) {
                MapTile tile = map.getMapTile(j, i);
                if (tile != null) {
                    tile.drawBottomLayer(graphicsHandler);
                }
            }
        }

        for (EnhancedMapTile enhancedMapTile : activeEnhancedMapTiles) {
            if (containsDraw(enhancedMapTile)) {
                enhancedMapTile.drawBottomLayer(graphicsHandler);
            }
        }
    }

    public void drawMapTilesTopLayer(GraphicsHandler graphicsHandler) {
        Point tileIndex = getTileIndexByCameraPosition();
        for (int i = tileIndex.y - 1; i <= tileIndex.y + height + 1; i++) {
            for (int j = tileIndex.x - 1; j <= tileIndex.x + width + 1; j++) {
                MapTile tile = map.getMapTile(j, i);
                if (tile != null && tile.getTopLayer() != null) {
                    tile.drawTopLayer(graphicsHandler);
                }
            }
        }

        for (EnhancedMapTile enhancedMapTile : activeEnhancedMapTiles) {
            if (containsDraw(enhancedMapTile) && enhancedMapTile.getTopLayer() != null) {
                enhancedMapTile.drawTopLayer(graphicsHandler);
            }
        }
    }

    public void drawMapEntities(Player player, GraphicsHandler graphicsHandler) {
        ArrayList<NPC> drawNpcsAfterPlayer = new ArrayList<>();

        for (NPC npc : activeNPCs) {
            if (containsDraw(npc)) {
                if (npc.getBounds().getY() < player.getBounds().getY1()  + (player.getBounds().getHeight() / 2f)) {
                    npc.draw(graphicsHandler);
                }
                else {
                    drawNpcsAfterPlayer.add(npc);
                }
            }
        }

        player.draw(graphicsHandler);

        for (NPC npc : drawNpcsAfterPlayer) {
            npc.draw(graphicsHandler);
        }
    }

    public boolean containsUpdate(GameObject gameObject) {
        return getX1() - (tileWidth * UPDATE_OFF_SCREEN_RANGE) < gameObject.getX() + gameObject.getWidth() &&
                getEndBoundX() + (tileWidth * UPDATE_OFF_SCREEN_RANGE) > gameObject.getX() &&
                getY1() - (tileHeight * UPDATE_OFF_SCREEN_RANGE) <  gameObject.getY() + gameObject.getHeight()
                && getEndBoundY() + (tileHeight * UPDATE_OFF_SCREEN_RANGE) > gameObject.getY();
    }

    public boolean containsDraw(GameObject gameObject) {
        return getX1() - tileWidth < gameObject.getX() + gameObject.getWidth() && getEndBoundX() + tileWidth > gameObject.getX() &&
                getY1() - tileHeight <  gameObject.getY() + gameObject.getHeight() && getEndBoundY() + tileHeight >  gameObject.getY();
    }

    public ArrayList<EnhancedMapTile> getActiveEnhancedMapTiles() {
        return activeEnhancedMapTiles;
    }

    public ArrayList<NPC> getActiveNPCs() {
        return activeNPCs;
    }

    public ArrayList<Trigger> getActiveTriggers() {
        return activeTriggers;
    }

    public float getEndBoundX() {
        return x + (width * tileWidth) + leftoverSpaceX;
    }

    public float getEndBoundY() {
        return y + (height * tileHeight) + leftoverSpaceY;
    }

    public boolean isAtTopOfMap() {
        return this.getY() <= 0;
    }

    public boolean isAtBottomOfMap() {
        return this.getEndBoundY() >= map.getEndBoundY();
    }
}