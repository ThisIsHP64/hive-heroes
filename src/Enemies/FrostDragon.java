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
 * FrostDragon — patrols, chases, tiny wind-up, then a big NOSE-DIVE (wings tucked).
 * - Dive animation uses wings-tucked frames only (no flap)
 * - Strong dive motion with downward bias + speed ramp
 * - Torso hitbox for Bee melee/projectile collisions
 * - Spawns floating damage numbers and draws them automatically
 * - Shows the "bee_attack1" hit spark ON the enemy when damaged (like Goblin), scaled up
 *
 * Sprite sheet: frost_dragon.png (5x5 cells @ 72x72, gutter 0)
 * Rows:
 *   0: FLY
 *   1: ATTACK_WINDUP
 *   2: ATTACK_DIVE (we lock to the most tucked columns)
 *   3: DEATH
 *   4: SMOKE (first 4 cells)
 */
public class FrostDragon extends NPC {

    private static final int TILE_W = 72;
    private static final int TILE_H = 72;
    private static final int SCALE   = 1;

    // movement + combat
    private static final float PATROL_SPEED = 0.9f;
    private static final float CHASE_SPEED  = 1.6f;

    private static final float CHASE_RANGE   = 260f;
    private static final float GIVE_UP_RANGE = 420f;
    private static final float ATTACK_RANGE  = 110f;

    private static final int   ATTACK_DAMAGE      = 12;
    private static final long  ATTACK_COOLDOWN_MS = 1200;

    // very obvious dive
    private static final long  ATTACK_DURATION_MS = 720;  // long enough to see it
    private static final long  WINDUP_MS          = 60;   // snap into dive quickly

    // dive feel
    private static final float DIVE_EXTRA_DOWN = 1.35f;   // strong downward bias
    private static final float DIVE_SPEED_MULT = 1.85f;   // faster while diving

    private enum State { PATROL, CHASE, ATTACK, DEAD }
    private State state = State.PATROL;

    // patrol
    private float patrolCx, patrolCy;
    private float patrolTx, patrolTy;
    private static final float PATROL_RADIUS = 140f;

    // facing + attack
    private Direction facing = Direction.RIGHT;
    private boolean isAttacking = false;
    private boolean inDivePhase = false;   // when true we keep DIVE anim asserted
    private long attackStart = 0;
    private long lastAttack  = -5000;
    private boolean dealtThisSwing = false;
    private Direction attackFacing = null;

    // health + death
    private int  hp = 80;
    private boolean dead = false;
    private long deathTime = 0;
    private static final long DEATH_LINGER_MS = 1400;

    // floating text
    private final ArrayList<FloatingText> floatingTexts = new ArrayList<>();

    // --- Hit FX window (so we can draw "bee_attack1" on impact) ---
    private boolean showAttackFx = false;
    private long attackFxStartTime = 0;
    private static final long ATTACK_FX_DURATION_MS = 450;

    // bee_attack1 sprite (32x32 frames, single row)
    private SpriteSheet hitFxSheet = null;
    private static final int HIT_FX_W = 32;
    private static final int HIT_FX_H = 32;
    private static final int HIT_FX_FRAME_DELAY = 3; // ticks per FX frame
    private static final float HIT_FX_SCALE = 1.35f; // SCALE UP the spark (~35% bigger)

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

        // Try to load bee_attack1 spark; fail-safe if missing
        try {
            hitFxSheet = new SpriteSheet(ImageLoader.load("bee_attack1.png"), HIT_FX_W, HIT_FX_H, 0);
        } catch (Exception ignored) {
            hitFxSheet = null;
        }
    }

    // ---------- core loop ----------
    @Override
    public void update(Player player) {
        // update floating numbers
        floatingTexts.removeIf(t -> { t.update(); return t.isDead(); });

        // timeout hit FX
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
                if (dist > GIVE_UP_RANGE) {
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

        // face the bee
        facing = (player.getX() > getX()) ? Direction.RIGHT : Direction.LEFT;
        attackFacing = facing;

        // micro telegraph
        setCurrentAnimationName(facing == Direction.RIGHT ? "ATTACK_WINDUP_RIGHT" : "ATTACK_WINDUP_LEFT");

        // tiny forward nudge for a snappy commit
        float nudge = 1.2f;
        moveXHandleCollision((facing == Direction.RIGHT ? nudge : -nudge));
    }

    private void updateAttack(Player player, long now) {
        long elapsed = now - attackStart;

        // lock into dive ASAP and re-assert the DIVE anim each frame (prevents wing cycles)
        if (!inDivePhase && elapsed >= WINDUP_MS) {
            inDivePhase = true;
        }
        if (inDivePhase) {
            setCurrentAnimationName(attackFacing == Direction.RIGHT ? "ATTACK_DIVE_RIGHT" : "ATTACK_DIVE_LEFT");
        }

        // BIG, CLEAR MOTION: lead + gravity ramp
        float lead = 32f; // lead horizontally
        float targetX = player.getX() + (attackFacing == Direction.RIGHT ? lead : -lead);
        float targetY = player.getY() + 20f; // bias below player for a true nose-dive

        float dx = targetX - getX();
        float dy = targetY - getY();
        float dist = (float) Math.sqrt(dx * dx + dy * dy);

        float t = Math.min(1f, elapsed / (float) ATTACK_DURATION_MS);
        float gravityBoost = DIVE_EXTRA_DOWN * (0.8f + 0.4f * t);

        if (dist > 0.1f) {
            float dirX = dx / dist;
            float dirY = dy / dist;

            dirY += gravityBoost; // heavy downward push
            float norm = (float) Math.sqrt(dirX * dirX + dirY * dirY);
            dirX /= norm;
            dirY /= norm;

            float speed = CHASE_SPEED * (DIVE_SPEED_MULT * (0.85f + 0.35f * t));
            moveXHandleCollision(dirX * speed);
            moveYHandleCollision(dirY * speed);
        }

        // damage once mid-swing
        if (!dealtThisSwing && elapsed >= ATTACK_DURATION_MS * 0.45f) {
            tryDealDamage(player);
        }

        // optional micro-shake at the moment dive commits
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

    private void tryDealDamage(Player player) {
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
            float mx = (dx / dist) * CHASE_SPEED;
            float my = (dy / dist) * CHASE_SPEED;
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
            float mx = (dx / dist) * PATROL_SPEED;
            float my = (dy / dist) * PATROL_SPEED;
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

        // floating text
        float textX = getX() + (TILE_W * SCALE) / 2f;
        float textY = getY();
        floatingTexts.add(new FloatingText(textX, textY, "-" + amount, Color.RED));

        // open the hit FX window (lets the hit spark animate)
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

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        // draw the dragon (with fade if dead)
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

        // draw bee_attack1 hit FX on top while the window is open (scaled up)
        if (showAttackFx && hitFxSheet != null && map != null && map.getCamera() != null) {
            long elapsed = System.currentTimeMillis() - attackFxStartTime;

            // derive frame count defensively
            int fxCols = Math.max(1, hitFxSheet.getSheetWidth() / HIT_FX_W);
            int frame = (int) Math.min(fxCols - 1, (elapsed / (HIT_FX_FRAME_DELAY * 16)) % fxCols);

            // FX position: center around the dragon's torso (hitbox center)
            java.awt.Rectangle hb = getHitbox();
            float fxCenterX = (float) hb.getCenterX();
            float fxCenterY = (float) hb.getCenterY();

            // camera offset
            float camX = map.getCamera().getX();
            float camY = map.getCamera().getY();

            // scaled draw size
            int drawW = Math.round(HIT_FX_W * HIT_FX_SCALE);
            int drawH = Math.round(HIT_FX_H * HIT_FX_SCALE);

            // center the scaled sprite
            int drawX = Math.round(fxCenterX - camX - drawW / 2f);
            int drawY = Math.round(fxCenterY - camY - drawH / 2f);

            // choose frame and mirror if facing LEFT
            java.awt.image.BufferedImage fxImg = hitFxSheet.getSprite(0, frame);
            if (facing == Direction.LEFT) {
                graphicsHandler.drawImage(fxImg, drawX + drawW, drawY, -drawW, drawH);
            } else {
                graphicsHandler.drawImage(fxImg, drawX, drawY, drawW, drawH);
            }
        }

        // draw floating numbers relative to camera
        if (map != null && map.getCamera() != null) {
            float camX = map.getCamera().getX();
            float camY = map.getCamera().getY();
            for (FloatingText text : new ArrayList<>(floatingTexts)) {
                text.draw(graphicsHandler, camX, camY);
            }
        }
    }

    // ---------- hitbox ----------
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

    // ---------- attack FX hooks (mirrors Goblin so shared systems can poll if needed) ----------
    public boolean isShowingAttackFx() {
        return !dead && showAttackFx && (System.currentTimeMillis() - attackFxStartTime) < ATTACK_FX_DURATION_MS;
    }

    public Direction getFacing() {
        return facing;
    }

    // ---------- animations ----------
    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet sheet) {
        HashMap<String, Frame[]> map = new HashMap<>();
        final int bx = 20, by = 16, bw = 32, bh = 40; // bounds inside each frame

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

        // Row 2 — DIVE (wings tucked, head-first) — repeat one frame to eliminate any flap
        final int DIVE_ROW = 2;
        final int DIVE_COL_A = 4; // most tucked pose on your sheet
        final int DIVE_COL_B = 4; // repeat same frame (no animation)

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
