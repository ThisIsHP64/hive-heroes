package Level;

import Engine.GamePanel;
import Engine.Key;
import Engine.KeyLocker;
import Engine.Keyboard;
import Game.GameState;
import GameObject.GameObject;
import GameObject.Rectangle;
import GameObject.SpriteSheet;
import SpriteImage.ResourceHUD;
import StaticClasses.BeeStats;
import StaticClasses.TeleportManager;
import Utils.Direction;

public abstract class Player extends GameObject {
    // values that affect player movement
    // these should be set in a subclass
    protected float walkSpeed = 0;

    // player stats

    protected int stamina = 0;

    protected int nectar = 0;

    protected int experience = 0;


    protected ResourceHUD resourceBars;

    protected int interactionRange = 1;
    protected Direction currentWalkingXDirection;
    protected Direction currentWalkingYDirection;
    protected Direction lastWalkingXDirection;
    protected Direction lastWalkingYDirection;

    // values used to handle player movement
    protected float moveAmountX, moveAmountY;
    protected float lastAmountMovedX, lastAmountMovedY;

    // values used to keep track of player's current state
    protected PlayerState playerState;
    protected PlayerState previousPlayerState;
    protected Direction facingDirection;
    protected Direction lastMovementDirection;

    // define keys
    protected KeyLocker keyLocker = new KeyLocker();
    protected Key MOVE_LEFT_KEY = Key.LEFT;
    protected Key MOVE_RIGHT_KEY = Key.RIGHT;
    protected Key MOVE_UP_KEY = Key.UP;
    protected Key MOVE_DOWN_KEY = Key.DOWN;
    protected Key INTERACT_KEY = Key.SPACE;
    protected Key SPRINT_KEY = Key.SHIFT;

    protected boolean isLocked = false;


    public Player(SpriteSheet spriteSheet, float x, float y, String startingAnimationName) {
        super(spriteSheet, x, y, startingAnimationName);
        facingDirection = Direction.RIGHT;
        playerState = PlayerState.STANDING;
        previousPlayerState = playerState;
        this.affectedByTriggers = true;
    }

    public void update() {
        if (!isLocked) {
            moveAmountX = 0;
            moveAmountY = 0;

            // if player is currently playing through level (has not won or lost)
            // update player's state and current actions, which includes things like
            // determining how much it should move each frame and if its walking or jumping
            do {
                previousPlayerState = playerState;
                handlePlayerState();
            } while (previousPlayerState != playerState);

            // move player with respect to map collisions based on how much player needs to
            // move this frame
            lastAmountMovedY = super.moveYHandleCollision(moveAmountY);
            lastAmountMovedX = super.moveXHandleCollision(moveAmountX);
        }

        handlePlayerAnimation();

        updateLockedKeys();

        // update player's animation
        super.update();
    }

    // based on player's current state, call appropriate player state handling
    // method
    protected void handlePlayerState() {
        switch (playerState) {
            case STANDING:
                playerStanding();
                break;
            case WALKING:
                playerWalking();
                break;
        }
    }

    // player STANDING state logic
    protected void playerStanding() {
        if (!keyLocker.isKeyLocked(INTERACT_KEY) && Keyboard.isKeyDown(INTERACT_KEY)) {
            keyLocker.lockKey(INTERACT_KEY);
            map.entityInteract(this);
        }

        // if a walk key is pressed, player enters WALKING state
        if (Keyboard.isKeyDown(MOVE_LEFT_KEY) || Keyboard.isKeyDown(MOVE_RIGHT_KEY) || Keyboard.isKeyDown(MOVE_UP_KEY)
                || Keyboard.isKeyDown(MOVE_DOWN_KEY)) {
            playerState = PlayerState.WALKING;
        }
    }

    // player WALKING state logic
    protected void playerWalking() {
        if (!keyLocker.isKeyLocked(INTERACT_KEY) && Keyboard.isKeyDown(INTERACT_KEY)) {
            keyLocker.lockKey(INTERACT_KEY);
            map.entityInteract(this);
            if (currentWalkingXDirection != Direction.NONE && currentWalkingYDirection != Direction.NONE) {
                moveAmountX *= 0.7071f;
                moveAmountY *= 0.7071f;
            }
        }
        
        // if walk left key is pressed, move player to the left
        if (Keyboard.isKeyDown(MOVE_LEFT_KEY)) {

            // if the shift key is pressed, the player's speed is increased
            if (Keyboard.isKeyDown(SPRINT_KEY)) {
                if (BeeStats.getStamina() > 0) {
                    moveAmountX -= BeeStats.getWalkSpeed() * 1f;
                }

                BeeStats.manageStamina();
            } 

            if ((TeleportManager.getCurrentGameState() == GameState.GRASSLEVEL || TeleportManager.getCurrentGameState() == GameState.SNOWLEVEL) 
                && (GamePanel.getisRaining()==true || GamePanel.getisWindActive()==true || GamePanel.getisSnowing()==true)) {

                moveAmountX -= BeeStats.getWalkSpeed() *0.5f;
                facingDirection = Direction.LEFT;
                currentWalkingXDirection = Direction.LEFT;
                lastWalkingXDirection = Direction.LEFT;
            } 

            else if (TeleportManager.getCurrentGameState() == GameState.VOLCANOLEVEL && GamePanel.getisRedRaining()==true) {
                if (BeeStats.hasTunic() == true){
                    moveAmountX -= BeeStats.getWalkSpeed();
                    facingDirection = Direction.LEFT;
                    currentWalkingXDirection = Direction.LEFT;
                    lastWalkingXDirection = Direction.LEFT;
                } else {
                    BeeStats.takeDamage(1);
                    moveAmountX -= BeeStats.getWalkSpeed();
                    facingDirection = Direction.LEFT;
                    currentWalkingXDirection = Direction.LEFT;
                    lastWalkingXDirection = Direction.LEFT;
                }
            } else {
                moveAmountX -= BeeStats.getWalkSpeed();
                facingDirection = Direction.LEFT;
                currentWalkingXDirection = Direction.LEFT;
                lastWalkingXDirection = Direction.LEFT;
            } 
        }

        // if walk right key is pressed, move player to the right
        else if (Keyboard.isKeyDown(MOVE_RIGHT_KEY)) {

            if (Keyboard.isKeyDown(SPRINT_KEY)) {

                if (BeeStats.getStamina() > 0) {
                    moveAmountX += BeeStats.getWalkSpeed() * 1f;
                }

                BeeStats.manageStamina();
            }

            if ((TeleportManager.getCurrentGameState() == GameState.GRASSLEVEL || TeleportManager.getCurrentGameState() == GameState.SNOWLEVEL) 
                    && (GamePanel.getisRaining()==true || GamePanel.getisWindActive()==true || GamePanel.getisSnowing()==true)) {
                moveAmountX += BeeStats.getWalkSpeed()*0.5f;
                facingDirection = Direction.RIGHT;
                currentWalkingXDirection = Direction.RIGHT;
                lastWalkingXDirection = Direction.RIGHT;
            } 
            else if (TeleportManager.getCurrentGameState() == GameState.SNOWLEVEL && GamePanel.getisSnowing()==true) {
                moveAmountX += BeeStats.getWalkSpeed()*0.5f;
                currentWalkingYDirection = Direction.RIGHT;
                lastWalkingYDirection = Direction.RIGHT; 
            }
            else if (TeleportManager.getCurrentGameState() == GameState.VOLCANOLEVEL && GamePanel.getisRedRaining()==true){
                if (BeeStats.hasTunic() == true){
                    moveAmountX += BeeStats.getWalkSpeed();
                    facingDirection = Direction.RIGHT;
                    currentWalkingXDirection = Direction.RIGHT;
                    lastWalkingXDirection = Direction.RIGHT;
                } else {
                    BeeStats.takeDamage(1);
                    moveAmountX += BeeStats.getWalkSpeed();
                    facingDirection = Direction.RIGHT;
                    currentWalkingXDirection = Direction.RIGHT;
                    lastWalkingXDirection = Direction.RIGHT;
                }
            
            } else {
                moveAmountX += BeeStats.getWalkSpeed();
                facingDirection = Direction.RIGHT;
                currentWalkingXDirection = Direction.RIGHT;
                lastWalkingXDirection = Direction.RIGHT;
            }
        
        } else {
            currentWalkingXDirection = Direction.NONE;
        }

        if (Keyboard.isKeyDown(MOVE_UP_KEY)) {

            if (Keyboard.isKeyDown(SPRINT_KEY)) {
                if (BeeStats.getStamina() > 0) {
                    moveAmountY -= BeeStats.getWalkSpeed() * 1f;
                }

                BeeStats.manageStamina();
            } 
            
            if ((TeleportManager.getCurrentGameState() == GameState.GRASSLEVEL || TeleportManager.getCurrentGameState() == GameState.SNOWLEVEL) 
                    && (GamePanel.getisRaining()==true || GamePanel.getisWindActive()==true || GamePanel.getisSnowing()==true)){
                
                moveAmountY -= BeeStats.getWalkSpeed()*0.5f;
                currentWalkingYDirection = Direction.UP;
                lastWalkingYDirection = Direction.UP;
                
            } else if (TeleportManager.getCurrentGameState() == GameState.VOLCANOLEVEL && GamePanel.getisRedRaining()==true){
                if (BeeStats.hasTunic() == true){
                    moveAmountY -= BeeStats.getWalkSpeed();
                    currentWalkingYDirection = Direction.UP;
                    lastWalkingYDirection = Direction.UP;
                } else {
                    BeeStats.takeDamage(1);
                    moveAmountY -= BeeStats.getWalkSpeed();
                    facingDirection = Direction.UP;
                    currentWalkingXDirection = Direction.UP;
                    lastWalkingXDirection = Direction.UP;
                }
            } else {
                moveAmountY -= BeeStats.getWalkSpeed();
                currentWalkingYDirection = Direction.UP;
                lastWalkingYDirection = Direction.UP;
            }
        
        } else if (Keyboard.isKeyDown(MOVE_DOWN_KEY)) {

            if (Keyboard.isKeyDown(SPRINT_KEY)) {
                if (BeeStats.getStamina() > 0) {
                    moveAmountY += BeeStats.getWalkSpeed() * 1f;
                }
                BeeStats.manageStamina();
            }

            if ((TeleportManager.getCurrentGameState() == GameState.GRASSLEVEL || TeleportManager.getCurrentGameState() == GameState.SNOWLEVEL)
                    && (GamePanel.getisRaining()==true || GamePanel.getisWindActive()==true || GamePanel.getisSnowing()==true)) {
                
                moveAmountY += BeeStats.getWalkSpeed()*0.5f;
                currentWalkingYDirection = Direction.DOWN;
                lastWalkingYDirection = Direction.DOWN;
                
            } else if (TeleportManager.getCurrentGameState() == GameState.VOLCANOLEVEL && GamePanel.getisRedRaining()==true){
                
                if (BeeStats.hasTunic() == true){
                    moveAmountY += BeeStats.getWalkSpeed();
                    facingDirection = Direction.DOWN;
                    currentWalkingXDirection = Direction.DOWN;
                    lastWalkingXDirection = Direction.DOWN;
                } else {
                    BeeStats.takeDamage(1);
                    moveAmountY += BeeStats.getWalkSpeed();
                    facingDirection = Direction.DOWN;
                    currentWalkingXDirection = Direction.DOWN;
                    lastWalkingXDirection = Direction.DOWN;
                }
             
            } else {
                moveAmountY += BeeStats.getWalkSpeed();
                currentWalkingYDirection = Direction.DOWN;
                lastWalkingYDirection = Direction.DOWN;
            }
        
        } else {
            currentWalkingYDirection = Direction.NONE;
        }

        if ((currentWalkingXDirection == Direction.RIGHT || currentWalkingXDirection == Direction.LEFT)
                && currentWalkingYDirection == Direction.NONE) {
            lastWalkingYDirection = Direction.NONE;
        }

        if ((currentWalkingYDirection == Direction.UP || currentWalkingYDirection == Direction.DOWN)
                && currentWalkingXDirection == Direction.NONE) {
            lastWalkingXDirection = Direction.NONE;
        }

        if (Keyboard.isKeyUp(MOVE_LEFT_KEY) && Keyboard.isKeyUp(MOVE_RIGHT_KEY) && Keyboard.isKeyUp(MOVE_UP_KEY)
                && Keyboard.isKeyUp(MOVE_DOWN_KEY)) {
            playerState = PlayerState.STANDING;
        }
    }

    protected void updateLockedKeys() {
        if (Keyboard.isKeyUp(INTERACT_KEY) && !isLocked) {
            keyLocker.unlockKey(INTERACT_KEY);
        }
    }

    // anything extra the player should do based on interactions can be handled here
    protected void handlePlayerAnimation() {
        if (playerState == PlayerState.STANDING) {
            // sets animation to a STAND animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";
        } else if (playerState == PlayerState.WALKING) {
            // sets animation to a WALK animation based on which way player is facing
            this.currentAnimationName = facingDirection == Direction.RIGHT ? "WALK_RIGHT" : "WALK_LEFT";
        }
    }

    @Override
    public void onEndCollisionCheckX(boolean hasCollided, Direction direction, GameObject entityCollidedWith) {}

    @Override
    public void onEndCollisionCheckY(boolean hasCollided, Direction direction, GameObject entityCollidedWith) {}

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public Direction getFacingDirection() {
        return facingDirection;
    }

    public void setFacingDirection(Direction facingDirection) {
        this.facingDirection = facingDirection;
    }

    public Rectangle getInteractionRange() {
        return new Rectangle(
                getBounds().getX1() - interactionRange,
                getBounds().getY1() - interactionRange,
                getBounds().getWidth() + (interactionRange * 2),
                getBounds().getHeight() + (interactionRange * 2));
    }

    public Key getInteractKey() {
        return INTERACT_KEY;
    }

    public Direction getCurrentWalkingXDirection() {
        return currentWalkingXDirection;
    }

    public Direction getCurrentWalkingYDirection() {
        return currentWalkingYDirection;
    }

    public Direction getLastWalkingXDirection() {
        return lastWalkingXDirection;
    }

    public Direction getLastWalkingYDirection() {
        return lastWalkingYDirection;
    }

    public void lock() {
        isLocked = true;
        playerState = PlayerState.STANDING;
        this.currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";
    }

    public void unlock() {
        isLocked = false;
        playerState = PlayerState.STANDING;
        this.currentAnimationName = facingDirection == Direction.RIGHT ? "STAND_RIGHT" : "STAND_LEFT";
    }

    // used by other files or scripts to force player to stand
    public void stand(Direction direction) {
        playerState = PlayerState.STANDING;
        facingDirection = direction;
        if (direction == Direction.RIGHT) {
            this.currentAnimationName = "STAND_RIGHT";
        } else if (direction == Direction.LEFT) {
            this.currentAnimationName = "STAND_LEFT";
        }
    }

    // used by other files or scripts to force player to walk
    public void walk(Direction direction, float speed) {
        playerState = PlayerState.WALKING;
        facingDirection = direction;
        if (direction == Direction.RIGHT) {
            this.currentAnimationName = "WALK_RIGHT";
        } else if (direction == Direction.LEFT) {
            this.currentAnimationName = "WALK_LEFT";
        }
        if (direction == Direction.UP) {
            moveY(-speed);
        } else if (direction == Direction.DOWN) {
            moveY(speed);
        } else if (direction == Direction.LEFT) {
            moveX(-speed);
        } else if (direction == Direction.RIGHT) {
            moveX(speed);
        }
    }

    // Uncomment this to have game draw player's bounds to make it easier to
    // visualize
    /*
     * public void draw(GraphicsHandler graphicsHandler) {
     * super.draw(graphicsHandler);
     * drawBounds(graphicsHandler, new Color(255, 0, 0, 100));
     * }
     */

    // Line 276 - 273 is implemented for the speed power
    public float getWalkSpeed() {
        return BeeStats.getWalkSpeed();
    }

    public void setWalkSpeed(float walkSpeed) {
        BeeStats.setWalkSpeed(walkSpeed);
    }
}
