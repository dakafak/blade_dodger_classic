import java.awt.*;

public class Player {

    double x;
    double y;
    double speed;
    double dx;
    double dy;
    double size = 6;
    Rectangle playerBounds;
    PlayerDirection direction;
    PlayerDirection lastMovementDirection;

    public Player() {
        x = 0;
        y = 0;
        speed = .4;//TODO Reduce player start speed
        dx = 0;
        dy = 0;
        direction = PlayerDirection.down;
        lastMovementDirection = PlayerDirection.right;
    }

    public Rectangle getPlayerBounds(){
        if(playerBounds == null){
            playerBounds = new Rectangle();
        }

        playerBounds.setBounds((int)x, (int)y, (int)size, (int)size);
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

    public void move(double deltaUpdate, int leftBound, int rightBound, int upperBound, int lowerBound){
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

        double newX = x + (dx * speed * deltaUpdate);
        double newY = y + (dy * speed * deltaUpdate);
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

}
