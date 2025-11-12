package Enemies;

import Builders.FrameBuilder;
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
import java.awt.Composite;
import java.awt.Graphics2D;
import java.util.HashMap;

/*
 * FrostDragon — patrols the frost region, chases, lunges, and plays a death→smoke sequence.
 * Sprite sheet: 360x360, 5x5 grid, each cell 72x72 (last cell bottom-right is filler; we skip it).
 *
 * Rows (left→right):
 *  0: Flying/idle (5 frames)             -> FLY_*
 *  1: Attack wind-up / lunge posture     -> ATTACK_WINDUP_*
 *  2: Attack follow-through / recover    -> ATTACK_LOOP_*
 *  3: Death (dark→explosion)             -> DEATH
 *  4: Smoke/dissipate (first 4 frames)   -> SMOKE
 */
public class FrostDragon extends NPC {

    // per-frame tile size in the sheet
    private static final int TILE_W = 72;
    private static final int TILE_H = 72;
    // scale the whole dragon if you want it bigger
    private static final int SCALE   = 1;

    // movement speeds
    private static final float PATROL_SPEED = 0.9f;
    private static final float CHASE_SPEED  = 1.6f;

    // detection / combat windows
    private static final float CHASE_RANGE   = 260f;
    private static final float GIVE_UP_RANGE = 420f;
    private static final float ATTACK_RANGE  = 110f;

    // attack tuning
    private static final int   ATTACK_DAMAGE      = 12;
    private static final long  ATTACK_COOLDOWN_MS = 1200;
    private static final long  ATTACK_DURATION_MS = 500;

    // simple state machine
    private enum State { PATROL, CHASE, ATTACK, DEAD }
    private State state = State.PATROL;

    // patrol circle
    private float patrolCx, patrolCy;
    private float patrolTx, patrolTy;
    private static final float PATROL_RADIUS = 140f;

    // facing + attack bookkeeping
    private Direction facing = Direction.RIGHT;
    private boolean isAttacking = false;
    private long attackStart = 0;
    private long lastAttack  = -5000;
    private boolean dealtThisSwing = false;
    private Direction attackFacing = null;

    // health + death timing
    private int  hp = 80;
    private boolean dead = false;
    private long deathTime = 0;
    private static final long DEATH_LINGER_MS = 1400; // fade window

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
    }

    // core loop
    @Override
    public void update(Player player) {
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

    // attack flow
    private void beginAttack(Player player, long now) {
        state = State.ATTACK;
        isAttacking = true;
        attackStart = now;
        dealtThisSwing = false;

        facing = (player.getX() > getX()) ? Direction.RIGHT : Direction.LEFT;
        attackFacing = facing;

        setCurrentAnimationName(facing == Direction.RIGHT ? "ATTACK_WINDUP_RIGHT" : "ATTACK_WINDUP_LEFT");
    }

    private void updateAttack(Player player, long now) {
        long elapsed = now - attackStart;

        // swap to follow-through mid-attack
        if (elapsed >= ATTACK_DURATION_MS / 2) {
            setCurrentAnimationName(attackFacing == Direction.RIGHT ? "ATTACK_LOOP_RIGHT" : "ATTACK_LOOP_LEFT");
        }

        // do one damage check mid-swing
        if (!dealtThisSwing && elapsed >= ATTACK_DURATION_MS / 2) {
            tryDealDamage(player);
        }

        if (elapsed >= ATTACK_DURATION_MS) {
            isAttacking = false;
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

    // movement
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

    // damage + death
    public void takeDamage(int amount) {
        if (dead) return;
        hp -= amount;
        if (hp <= 0) die();
    }

    private void die() {
        dead = true;
        state = State.DEAD;
        deathTime = System.currentTimeMillis();
        setCurrentAnimationName("DEATH");
        // If you want SMOKE to play automatically after DEATH loops once:
        // You can check hasAnimationLooped() and then setCurrentAnimationName("SMOKE")
        // if your engine exposes that in AnimatedSprite/NPC. Ping me and I’ll wire it.
    }

    public boolean isDead() { return dead; }

    public boolean shouldRemove() {
        if (!dead) return false;
        long t = System.currentTimeMillis() - deathTime;
        return t >= DEATH_LINGER_MS;
    }

    @Override
    public void draw(GraphicsHandler graphicsHandler) {
        if (!dead) {
            super.draw(graphicsHandler);
            return;
        }

        long t = System.currentTimeMillis() - deathTime;
        float fade = 1f - Math.min(1f, t / (float) DEATH_LINGER_MS);

        Graphics2D g2d = graphicsHandler.getGraphics();
        Composite old = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Math.max(0f, fade)));
        super.draw(graphicsHandler);
        g2d.setComposite(old);
    }

    // animations
    @Override
    public HashMap<String, Frame[]> loadAnimations(SpriteSheet sheet) {
        HashMap<String, Frame[]> map = new HashMap<>();

        // hitbox bounds inside each frame (tweak if needed)
        final int bx = 20, by = 16, bw = 32, bh = 40;

        // Row 0 — fly/idle
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

        // Row 1 — attack wind-up / lunge posture
        Frame[] windR = new Frame[] {
            f(sheet,1,0,5,bx,by,bw,bh,SCALE,false),
            f(sheet,1,1,5,bx,by,bw,bh,SCALE,false),
            f(sheet,1,2,5,bx,by,bw,bh,SCALE,false),
            f(sheet,1,3,5,bx,by,bw,bh,SCALE,false),
            f(sheet,1,4,5,bx,by,bw,bh,SCALE,false),
        };
        Frame[] windL = new Frame[] {
            f(sheet,1,0,5,bx,by,bw,bh,SCALE,true),
            f(sheet,1,1,5,bx,by,bw,bh,SCALE,true),
            f(sheet,1,2,5,bx,by,bw,bh,SCALE,true),
            f(sheet,1,3,5,bx,by,bw,bh,SCALE,true),
            f(sheet,1,4,5,bx,by,bw,bh,SCALE,true),
        };
        map.put("ATTACK_WINDUP_RIGHT", windR);
        map.put("ATTACK_WINDUP_LEFT",  windL);

        // Row 2 — attack follow-through / recover
        Frame[] atkR = new Frame[] {
            f(sheet,2,0,5,bx,by,bw,bh,SCALE,false),
            f(sheet,2,1,5,bx,by,bw,bh,SCALE,false),
            f(sheet,2,2,5,bx,by,bw,bh,SCALE,false),
            f(sheet,2,3,5,bx,by,bw,bh,SCALE,false),
            f(sheet,2,4,5,bx,by,bw,bh,SCALE,false),
        };
        Frame[] atkL = new Frame[] {
            f(sheet,2,0,5,bx,by,bw,bh,SCALE,true),
            f(sheet,2,1,5,bx,by,bw,bh,SCALE,true),
            f(sheet,2,2,5,bx,by,bw,bh,SCALE,true),
            f(sheet,2,3,5,bx,by,bw,bh,SCALE,true),
            f(sheet,2,4,5,bx,by,bw,bh,SCALE,true),
        };
        map.put("ATTACK_LOOP_RIGHT", atkR);
        map.put("ATTACK_LOOP_LEFT",  atkL);

        // Row 3 — death (dark→explosion)
        Frame[] death = new Frame[] {
            f(sheet,3,0,7,bx,by,bw,bh,SCALE,false),
            f(sheet,3,1,7,bx,by,bw,bh,SCALE,false),
            f(sheet,3,2,7,bx,by,bw,bh,SCALE,false),
            f(sheet,3,3,7,bx,by,bw,bh,SCALE,false),
            f(sheet,3,4,10,bx,by,bw,bh,SCALE,false),
        };
        map.put("DEATH", death);

        // Row 4 — smoke/dissipate (first 4 frames)
        Frame[] smoke = new Frame[] {
            f(sheet,4,0,10,bx,by,bw,bh,SCALE,false),
            f(sheet,4,1,10,bx,by,bw,bh,SCALE,false),
            f(sheet,4,2,10,bx,by,bw,bh,SCALE,false),
            f(sheet,4,3,12,bx,by,bw,bh,SCALE,false),
        };
        map.put("SMOKE", smoke);

        return map;
    }

    // helper to build frames cleanly (flip handled here)
    private Frame f(SpriteSheet s, int r, int c, int delay, int bx, int by, int bw, int bh, int scale, boolean flip) {
        FrameBuilder b = new FrameBuilder(s.getSprite(r, c), delay)
                .withScale(scale)
                .withBounds(bx, by, bw, bh);
        if (flip) b.withImageEffect(ImageEffect.FLIP_HORIZONTAL);
        return b.build();
    }
}
