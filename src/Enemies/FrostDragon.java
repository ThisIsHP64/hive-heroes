package Enemies;

import Builders.FrameBuilder;
import Effects.FloatingText;
import Engine.GraphicsHandler;
import Engine.ImageLoader;
import GameObject.Frame;
import GameObject.ImageEffect;
import GameObject.SpriteSheet;
import Level.NPC;
import Level.Player;
import Utils.Direction;
import Utils.Point;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;

/*
 * FrostDragon — wings-tucked nose-dive with contact damage.
 * - Dive animation locked to tucked frames (no flap)
 * - Strong nose-dive path
 * - CONTACT DAMAGE: dive strike box ahead of nose; damages on intersection (once per dive)
 * - Floating damage numbers + scaled bee_attack1 hit spark on hit
 */
public class FrostDragon extends NPC {

    private static final int TILE_W = 72;
    private static final int TILE_H = 72;
    private static final int SCALE   = 1;

    // movement + combat
    private static final float PATROL_SPEED = 0.9f;
    private static final float CHASE_SPEED  = 1.6f;

    // --- Horde mode (same idea as crab/goblin) ---
    private boolean hordeMode = false;
    private float hordeSpeedMult = 1.0f;

    private float currentChaseSpeed() {
        return hordeMode ? CHASE_SPEED * hordeSpeedMult : CHASE_SPEED;
    }

    private float currentPatrolSpeed() {
        return hordeMode ? PATROL_SPEED * hordeSpeedMult : PATROL_SPEED;
    }

    // called by horde/volcano logic
    public void setHordeAggression(float speedMult, boolean on) {
        this.hordeMode = on;
        this.hordeSpeedMult = (speedMult <= 0f) ? 1.0f : speedMult;
    }

    private static final float CHASE_RANGE   = 260f;
    private static final float GIVE_UP_RANGE = 420f;
    private static final float ATTACK_RANGE  = 110f;

    // BALANCED ATTACK DAMAGE
    private static final int   ATTACK_DAMAGE      = 18;
    private static final long  ATTACK_COOLDOWN_MS = 1200;

    // very obvious dive
    private static final long  ATTACK_DURATION_MS = 720;
    private static final long  WINDUP_MS          = 60;

    // dive feel
    private static final float DIVE_EXTRA_DOWN = 1.35f;
    private static final float DIVE_SPEED_MULT = 1.85f;

    // dive contact hitbox (ahead of the nose)
    private static final int DIVE_NOSE_REACH = 36; // distance from torso center
    private static final int DIVE_NOSE_W     = 28; // width
    private static final int DIVE_NOSE_H     = 24; // height
    private static final int DIVE_NOSE_YOFF  = 6;  // vertical offset

    private enum State { PATROL, CHASE, ATTACK, DEAD }
    private State state = State.PATROL;

    // patrol
    private float patrolCx, patrolCy;
    private float patrolTx, patrolTy;
    private static final float PATROL_RADIUS = 140f;

    // facing + attack
    private Direction facing = Direction.RIGHT;
    private boolean isAttacking = false;
    private boolean inDivePhase = false;
    private long attackStart = 0;
    private long lastAttack  = -5000;
    private boolean dealtThisSwing = false;
    private Direction attackFacing = null;

    // health + death (BALANCED HP)
    private int  hp = 140;
    private boolean dead = false;
    private long deathTime = 0;
    private static final long DEATH_LINGER_MS = 1400;

    // floating text
    private final ArrayList<FloatingText> floatingTexts = new ArrayList<>();

    // --- Hit FX (bee_attack1) ---
    private boolean showAttackFx = false;
    private long attackFxStartTime = 0;
    private static final long ATTACK_FX_DURATION_MS = 450;

    private SpriteSheet hitFxSheet = null;
    private static final int HIT_FX_W = 32;
    private static final int HIT_FX_H = 32;
    private static final int HIT_FX_FRAME_DELAY = 3;
    private static final float HIT_FX_SCALE = 1.35f;

    public FrostDragon(Point spawn) {
        super(
            0,
            spawn.x,
            spawn.y,
            new SpriteSheet(ImageLoader.load("frost_dragon.png"), TILE_W, TILE_H, 0),
            "FLY_RIGHT"
        );
        this.patrolCx = spawn.x;
        this.patrolCy = spawn.y;
        setRandomPatrolTarget();

        try {
            hitFxSheet = new SpriteSheet(ImageLoader.load("bee_attack1.png"), HIT_FX_W, HIT_FX_H, 0);
        } catch (Exception ignored) {
            hitFxSheet = null;
        }
    }

    // ---------- core loop ----------
    @Override
    public void update(Player player) {
        floatingTexts.removeIf(t -> { t.update(); return t.isDead(); });

        if (showAttackFx && (System.currentTimeMillis() - attackFxStartTime) >= ATTACK_FX_DURATION_MS) {
            showAttackFx = false;
        }

        if (dead) {
            super.update(player);
            return;
        }
        super.update(player);
    }

    @Override
    public void performAction(Player player) {
        if (dead) return;

        float dist = distanceTo(player);
        long now = System.currentTimeMillis();

        switch (state) {
            case PATROL:
                patrol();
                if (dist < CHASE_RANGE) state = State.CHASE;
                break;

            case CHASE:
                if (dist < ATTACK_RANGE && (now - lastAttack) > ATTACK_COOLDOWN_MS) {
                    beginAttack(player, now);
                } else {
                    chase(player);
                }
                if (!hordeMode && dist > GIVE_UP_RANGE) {
                    state = State.PATROL;
                    setRandomPatrolTarget();
                }
                break;

            case ATTACK:
                updateAttack(player, now);
                break;

            case DEAD:
                break;
        }
    }

    // ---------- attack flow ----------
    private void beginAttack(Player player, long now) {
        state = State.ATTACK;
        isAttacking = true;
        inDivePhase = false;
        attackStart = now;
        dealtThisSwing = false;

        facing = (player.getX() > getX()) ? Direction.RIGHT : Direction.LEFT;
        attackFacing = facing;

        setCurrentAnimationName(facing == Direction.RIGHT ? "ATTACK_WINDUP_RIGHT" : "ATTACK_WINDUP_LEFT");

        float nudge = 1.2f;
        moveXHandleCollision((facing == Direction.RIGHT ? nudge : -nudge));
    }

    private void updateAttack(Player player, long now) {
        long elapsed = now - attackStart;

        if (!inDivePhase && elapsed >= WINDUP_MS) {
            inDivePhase = true;
        }
        if (inDivePhase) {
            setCurrentAnimationName(attackFacing == Direction.RIGHT ? "ATTACK_DIVE_RIGHT" : "ATTACK_DIVE_LEFT");
        }

        // motion: lead + gravity ramp
        float lead = 32f;
        float targetX = player.getX() + (attackFacing == Direction.RIGHT ? lead : -lead);
        float targetY = player.getY() + 20f;

        float dx = targetX - getX();
        float dy = targetY - getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        float t = Math.min(1f, elapsed / (float) ATTACK_DURATION_MS);
        float gravityBoost = DIVE_EXTRA_DOWN * (0.8f + 0.4f * t);

        if (dist > 0.1f) {
            float dirX = dx / dist;
            float dirY = dy / dist;

            dirY += gravityBoost;
            float norm = (float) Math.sqrt(dirX * dirX + dirY * dirY);
            dirX /= norm;
            dirY /= norm;

            float speed = currentChaseSpeed() * (DIVE_SPEED_MULT * (0.85f + 0.35f * t));
            moveXHandleCollision(dirX * speed);
            moveYHandleCollision(dirY * speed);
        }

        // --- CONTACT DAMAGE: strike box vs bee AWT bounds (constructed directly) ---
        if (!dealtThisSwing && inDivePhase) {
            java.awt.Rectangle strike = getDiveStrikeBox();
            java.awt.Rectangle beeBounds = new java.awt.Rectangle(
                (int) player.getX(),
                (int) player.getY(),
                player.getWidth(),
                player.getHeight()
            );

            if (strike != null && beeBounds.intersects(strike)) {
                if (player instanceof Players.Bee) {
                    Players.Bee bee = (Players.Bee) player;
                    bee.applyDamage(ATTACK_DAMAGE);
                    dealtThisSwing = true;
                }
            }
        }

        // fallback distance check (kept from earlier logic)
        if (!dealtThisSwing && elapsed >= ATTACK_DURATION_MS * 0.45f) {
            tryDealDamageDistanceFallback(player);
        }

        if (inDivePhase && elapsed == WINDUP_MS && map != null && map.getCamera() != null) {
            map.getCamera().shake();
        }

        if (elapsed >= ATTACK_DURATION_MS) {
            isAttacking = false;
            inDivePhase = false;
            lastAttack = now;
            state = State.CHASE;
            attackFacing = null;
        }
    }

    // Nose strike box projected from torso hitbox — returns java.awt.Rectangle
    private java.awt.Rectangle getDiveStrikeBox() {
        java.awt.Rectangle torso = getHitbox();
        if (torso == null) return null;

        int cx = (int) Math.round(torso.getCenterX());
        int cy = (int) Math.round(torso.getCenterY()) + DIVE_NOSE_YOFF;

        int w = DIVE_NOSE_W;
        int h = DIVE_NOSE_H;

        int x;
        if (attackFacing == Direction.LEFT) {
            x = cx - DIVE_NOSE_REACH - w;
        } else {
            x = cx + DIVE_NOSE_REACH;
        }
        int y = cy - (h / 2);

        return new java.awt.Rectangle(x, y, w, h);
    }

    // fallback distance check
    private void tryDealDamageDistanceFallback(Player player) {
        float cx = getX() + (TILE_W * SCALE) / 2f;
        float cy = getY() + (TILE_H * SCALE) / 2f;

        float px = player.getX() + 32f;
        float py = player.getY() + 32f;

        float dx = px - cx;
        float dy = py - cy;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        boolean inFront = (attackFacing == Direction.RIGHT && dx > 0) ||
                          (attackFacing == Direction.LEFT  && dx < 0);

        if (dist < 96f && inFront) {
            if (player instanceof Players.Bee) {
                Players.Bee bee = (Players.Bee) player;
                bee.applyDamage(ATTACK_DAMAGE);
                dealtThisSwing = true;
            }
        }
    }

    // ---------- movement ----------
    private void chase(Player player) {
        float dx = player.getX() - getX();
        float dy = player.getY() - getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist > 0.1f) {
            float mx = (dx / dist) * currentChaseSpeed();
            float my = (dy / dist) * currentChaseSpeed();
            moveXHandleCollision(mx);
            moveYHandleCollision(my);
            if (Math.abs(dx) > 6) facing = (dx > 0) ? Direction.RIGHT : Direction.LEFT;
        }

        setCurrentAnimationName(facing == Direction.RIGHT ? "FLY_RIGHT" : "FLY_LEFT");
    }

    private void patrol() {
        float dx = patrolTx - getX();
        float dy = patrolTy - getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        if (dist < 24f) setRandomPatrolTarget();
        if (dist > 0.1f) {
            float mx = (dx / dist) * currentPatrolSpeed();
            float my = (dy / dist) * currentPatrolSpeed();
            moveXHandleCollision(mx);
            moveYHandleCollision(my);
            facing = (dx > 0) ? Direction.RIGHT : Direction.LEFT;
        }

        setCurrentAnimationName(facing == Direction.RIGHT ? "FLY_RIGHT" : "FLY_LEFT");
    }

    private void setRandomPatrolTarget() {
        double ang = Math.random() * Math.PI * 2;
        float r = (float) (Math.random() * PATROL_RADIUS);
        patrolTx = patrolCx + (float) (Math.cos(ang) * r);
        patrolTy = patrolCy + (float) (Math.sin(ang) * r);
    }

    private float distanceTo(Player p) {
        float dx = p.getX() - getX();
        float dy = p.getY() - getY();
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    // ---------- damage + death ----------
    public void takeDamage(int amount) {
        if (dead) return;
        hp -= amount;

        float textX = getX() + (TILE_W * SCALE) / 2f;
        float textY = getY();
        floatingTexts.add(new FloatingText(textX, textY, "-" + amount, Color.RED));

        showAttackFx = true;
        attackFxStartTime = System.currentTimeMillis();

        if (hp <= 0) die();
    }

    private void die() {
        dead = true;
        state = State.DEAD;
        deathTime = System.currentTimeMillis();
        setCurrentAnimationName("DEATH");
    }

    public boolean isDead() { return dead; }

    public boolean shouldRemove() {
        if (!dead) return false;
        long t = System.currentTimeMillis() - deathTime;
        return t >= DEATH_LINGER_MS;
    }

    // used by horde / cleanup code that expects canBeRemoved()
    public boolean canBeRemoved() {
        return shouldRemove();
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // dragon (fade if dead)
        if (!dead) {
            super.draw(graphicsHandler);
        } else {
            long t = System.currentTimeMillis() - deathTime;
            float fade = 1f - Math.min(1f, t / (float) DEATH_LINGER_MS);

            Graphics2D g2d = graphicsHandler.getGraphics();
            Composite old = g2d.getComposite();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, fade)));
            super.draw(graphicsHandler);
            g2d.setComposite(old);
        }

        // bee_attack1 hit spark (scaled)
        if (showAttackFx && hitFxSheet != null && map != null && map.getCamera() != null) {
            long elapsed = System.currentTimeMillis() - attackFxStartTime;

            int fxCols = Math.max(1, hitFxSheet.getSheetWidth() / HIT_FX_W);
            int frame = (int) Math.min(fxCols - 1, (elapsed / (HIT_FX_FRAME_DELAY * 16)) % fxCols);

            java.awt.Rectangle hb = getHitbox();
            float fxCenterX = (float) hb.getCenterX();
            float fxCenterY = (float) hb.getCenterY();

            float camX = map.getCamera().getX();
            float camY = map.getCamera().getY();

            int drawW = Math.round(HIT_FX_W * HIT_FX_SCALE);
            int drawH = Math.round(HIT_FX_H * HIT_FX_SCALE);

            int drawX = Math.round(fxCenterX - camX - drawW / 2f);
            int drawY = Math.round(fxCenterY - camY - drawH / 2f);

            java.awt.image.BufferedImage fxImg = hitFxSheet.getSprite(0, frame);
            if (facing == Direction.LEFT) {
                graphicsHandler.drawImage(fxImg, drawX + drawW, drawY, -drawW, drawH);
            } else {
                graphicsHandler.drawImage(fxImg, drawX, drawY, drawW, drawH);
            }
        }

        // floating numbers
        if (map != null && map.getCamera() != null) {
            float camX = map.getCamera().getX();
            float camY = map.getCamera().getY();
            for (FloatingText text : new ArrayList<>(floatingTexts)) {
                text.draw(graphicsHandler, camX, camY);
            }
        }

        // (Optional debug) draw dive strike box:
        // java.awt.Rectangle s = getDiveStrikeBox();
        // if (s != null) {
        //     int sx = s.x - (int) map.getCamera().getX();
        //     int sy = s.y - (int) map.getCamera().getY();
        //     graphicsHandler.drawFilledRectangle(sx, sy, s.width, s.height, new java.awt.Color(0,255,0,80));
        // }
    }

    // ---------- torso hitbox (AWT rectangle) ----------
    public java.awt.Rectangle getHitbox() {
        int w = 32 * SCALE;  // torso width
        int h = 40 * SCALE;  // torso height
        int offsetX = 20 * SCALE;
        int offsetY = 16 * SCALE;

        return new java.awt.Rectangle(
            (int) getX() + offsetX,
            (int) getY() + offsetY,
            w, h
        );
    }

    // ---------- attack FX hooks ----------
    public boolean isShowingAttackFx() {
        return !dead && showAttackFx && (System.currentTimeMillis() - attackFxStartTime) < ATTACK_FX_DURATION_MS;
    }

    public Direction getFacing() {
        return facing;
    }

    // ---------- floating text (public method for level screens) ----------
    public void drawFloatingTexts(GraphicsHandler graphicsHandler, float cameraX, float cameraY) {
        for (FloatingText text : new ArrayList<>(floatingTexts)) {
            text.draw(graphicsHandler, cameraX, cameraY);
        }
    }

    // ---------- animations ----------
    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet sheet) {
        HashMap<String, Frame[]> map = new HashMap<>();
        final int bx = 20, by = 16, bw = 32, bh = 40;

        // Row 0 — FLY
        Frame[] flyR = new Frame[] {
            f(sheet,0,0,8,bx,by,bw,bh,SCALE,false),
            f(sheet,0,1,8,bx,by,bw,bh,SCALE,false),
            f(sheet,0,2,8,bx,by,bw,bh,SCALE,false),
            f(sheet,0,3,8,bx,by,bw,bh,SCALE,false),
            f(sheet,0,4,8,bx,by,bw,bh,SCALE,false),
        };
        Frame[] flyL = new Frame[] {
            f(sheet,0,0,8,bx,by,bw,bh,SCALE,true),
            f(sheet,0,1,8,bx,by,bw,bh,SCALE,true),
            f(sheet,0,2,8,bx,by,bw,bh,SCALE,true),
            f(sheet,0,3,8,bx,by,bw,bh,SCALE,true),
            f(sheet,0,4,8,bx,by,bw,bh,SCALE,true),
        };
        map.put("FLY_RIGHT", flyR);
        map.put("FLY_LEFT",  flyL);

        // Row 1 — WINDUP
        Frame[] windR = new Frame[] {
            f(sheet,1,0,4,bx,by,bw,bh,SCALE,false),
            f(sheet,1,1,4,bx,by,bw,bh,SCALE,false),
            f(sheet,1,2,4,bx,by,bw,bh,SCALE,false),
            f(sheet,1,3,4,bx,by,bw,bh,SCALE,false),
            f(sheet,1,4,4,bx,by,bw,bh,SCALE,false),
        };
        Frame[] windL = new Frame[] {
            f(sheet,1,0,4,bx,by,bw,bh,SCALE,true),
            f(sheet,1,1,4,bx,by,bw,bh,SCALE,true),
            f(sheet,1,2,4,bx,by,bw,bh,SCALE,true),
            f(sheet,1,3,4,bx,by,bw,bh,SCALE,true),
            f(sheet,1,4,4,bx,by,bw,bh,SCALE,true),
        };
        map.put("ATTACK_WINDUP_RIGHT", windR);
        map.put("ATTACK_WINDUP_LEFT",  windL);

        // Row 2 — DIVE (wings tucked, head-first)
        final int DIVE_ROW = 2;
        final int DIVE_COL_A = 4;
        final int DIVE_COL_B = 4;

        Frame[] diveR = new Frame[] {
            f(sheet, DIVE_ROW, DIVE_COL_A, 6, bx,by,bw,bh,SCALE,false),
            f(sheet, DIVE_ROW, DIVE_COL_B, 6, bx,by,bw,bh,SCALE,false),
        };
        Frame[] diveL = new Frame[] {
            f(sheet, DIVE_ROW, DIVE_COL_A, 6, bx,by,bw,bh,SCALE,true),
            f(sheet, DIVE_ROW, DIVE_COL_B, 6, bx,by,bw,bh,SCALE,true),
        };
        map.put("ATTACK_DIVE_RIGHT", diveR);
        map.put("ATTACK_DIVE_LEFT",  diveL);

        // Row 3 — DEATH
        Frame[] death = new Frame[] {
            f(sheet,3,0,7,bx,by,bw,bh,SCALE,false),
            f(sheet,3,1,7,bx,by,bw,bh,SCALE,false),
            f(sheet,3,2,7,bx,by,bw,bh,SCALE,false),
            f(sheet,3,3,7,bx,by,bw,bh,SCALE,false),
            f(sheet,3,4,10,bx,by,bw,bh,SCALE,false),
        };
        map.put("DEATH", death);

        // Row 4 — SMOKE (first 4)
        Frame[] smoke = new Frame[] {
            f(sheet,4,0,10,bx,by,bw,bh,SCALE,false),
            f(sheet,4,1,10,bx,by,bw,bh,SCALE,false),
            f(sheet,4,2,10,bx,by,bw,bh,SCALE,false),
            f(sheet,4,3,12,bx,by,bw,bh,SCALE,false),
        };
        map.put("SMOKE", smoke);

        return map;
    }

    private Frame f(SpriteSheet s, int r, int c, int delay, int bx, int by, int bw, int bh, int scale, boolean flip) {
        FrameBuilder b = new FrameBuilder(s.getSprite(r, c), delay)
                .withScale(scale)
                .withBounds(bx, by, bw, bh);
        if (flip) b.withImageEffect(ImageEffect.FLIP_HORIZONTAL);
        return b.build();
    }
}
