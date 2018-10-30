public class Player {

    double x;
    double y;
    double speed;
    double dx;
    double dy;
    PlayerDirection direction;

    public Player() {
        x = 0;
        y = 0;
        speed = 1;
        dx = 0;
        dy = 0;
        direction = PlayerDirection.down;
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

    public void move(double deltaUpdate){
        if(direction == PlayerDirection.up){
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
        x += dx * speed * deltaUpdate;
        y += dy * speed * deltaUpdate;
    }

}
