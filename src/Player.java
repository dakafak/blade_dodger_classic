import java.awt.*;

public class Player {

    double x;
    double y;
    double dx;
    double dy;

    double speed = .5;
    float speedBonusBalance = .015f;

    double size = 6;

    boolean boosting;
    long boostingTime;
    long boostingLength = 250L;
    int boostStaminaCost = 60;

    float stamina = 100;

    Rectangle playerBounds;
    PlayerDirection direction;
    PlayerDirection lastMovementDirection;

    public Player() {
        x = 0;
        y = 0;
        dx = 0;
        dy = 0;
        direction = PlayerDirection.down;
        lastMovementDirection = PlayerDirection.right;
    }

    public Rectangle getPlayerBounds(){
        if(playerBounds == null){
            playerBounds = new Rectangle();
        }

        playerBounds.setBounds((int)x, (int)y, (int)(size * .5), (int)size);//TODO add variables for width so it does not need to be calculated every time
        return playerBounds;
    }

    public Image getMovementImage(GameImages images){
        if(lastMovementDirection == PlayerDirection.right){
            return images.playerRight;
        } else {
            return images.playerLeft;
        }
    }

    public void setDirection(PlayerDirection direction){
        this.direction = direction;
    }

    public PlayerDirection getDirection() {
        return direction;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public double getSize(){
        return size;
    }

    public void move(double deltaUpdate, int leftBound, int rightBound, int upperBound, int lowerBound, int currentSpeedBonus){
        if(direction == null){
            dx = 0;
            dy = 0;
        } else if(direction == PlayerDirection.up){
            dy = -1;
            dx = 0;
        } else if(direction == PlayerDirection.down){
            dy = 1;
            dx = 0;
        } if(direction == PlayerDirection.left){
            dy = 0;
            dx = -1;
        } if(direction == PlayerDirection.right){
            dy = 0;
            dx = 1;
        } if(direction == PlayerDirection.upleft){
            dy = -.707;
            dx = -.707;
        } if(direction == PlayerDirection.upright){
            dy = -.707;
            dx = .707;
        } if(direction == PlayerDirection.downleft){
            dy = .707;
            dx = -.707;
        } if(direction == PlayerDirection.downright){
            dy = .707;
            dx = .707;
        }

        if(dx > 0){
            lastMovementDirection = PlayerDirection.right;
        } else if(dx < 0){
            lastMovementDirection = PlayerDirection.left;
        }

        double newX = x + (dx * (speed * (isBoosting() ? 3 : 1) + ((1 + currentSpeedBonus) * speedBonusBalance )) * deltaUpdate);
        double newY = y + (dy * (speed * (isBoosting() ? 3 : 1) + ((1 + currentSpeedBonus) * speedBonusBalance )) * deltaUpdate);
        if(newX < leftBound){
            x = leftBound;
        } else if(newX + size > rightBound){
            x = rightBound - size;
        } else {
            x = newX;
        }

        if(newY < upperBound){
            y = upperBound;
        } else if(newY + size > lowerBound){
            y = lowerBound - size;
        } else {
            y = newY;
        }
    }

	public boolean isBoosting() {
		return boosting;
	}

	private void setBoosting(boolean boosting) {
		this.boosting = boosting;
	}

	public void tryBoostingPlayer(Long time){
		if(!isBoosting() && stamina > boostStaminaCost){
			setBoosting(true);
			stamina -= boostStaminaCost;
			boostingTime = time;
		}
	}

	public void checkBoostResetAndStamina(Long time, double deltaUpdate, int boostingLevel){
    	if((time - boostingTime) > boostingLength){
    		setBoosting(false);
		}

		if(stamina < 100){
    		stamina += deltaUpdate * .1 * (1 + boostingLevel);
    		if(stamina > 100){
    			stamina = 100;
			}
		}
	}

	public float getStamina() {
		return stamina;
	}

	public void setStamina(float stamina) {
		this.stamina = stamina;
	}
}
