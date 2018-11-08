public class Blade {

    double x;
    double y;
    double speed;
    double dx;
    double dy;
    double size = 10;

    public Blade(double x, double y, int speedModifier) {
        this.x = x;
        this.y = y;

        dx = Math.random() * 2 - 1;
        dy = Math.random() * 2 - 1;

        speed = speedModifier + (speedModifier * Math.random() / 10);
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
        double newX = x + (dx * speed * deltaUpdate);
        double newY = y + (dy * speed * deltaUpdate);
        if(newX < leftBound){
            x = leftBound;
            dx *= -1;
        } else if(newX + size > rightBound){
            x = rightBound - size;
            dx *= -1;
        } else {
            x = newX;
        }

        if(newY < upperBound){
            y = upperBound;
            dy *= -1;
        } else if(newY + size > lowerBound){
            y = lowerBound - size;
            dy *= -1;
        } else {
            y = newY;
        }
    }

}
