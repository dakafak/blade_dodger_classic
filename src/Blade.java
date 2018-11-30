import java.awt.*;

public class Blade {

    double x;
    double y;
    double speed;
    double dx;
    double dy;
    double size = 10;
    Rectangle bladeBounds;

    public Blade(double x, double y, int level, int difficultyModifier) {
        this.x = x;
        this.y = y;

        dx = Math.random() * 2 - 1;
        dy = Math.random() * 2 - 1;

        double speedModifier = .5 + (level * .05) + (difficultyModifier * .25);
        speed = speedModifier;
    }

    public Rectangle getBladeBounds(){
        if(bladeBounds == null){
            bladeBounds = new Rectangle();
        }

        bladeBounds.setBounds((int)x, (int)y, (int)(size * .75), (int)(size * .75));//TODO add variables so it does not need to be calculated every time
        return bladeBounds;
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
