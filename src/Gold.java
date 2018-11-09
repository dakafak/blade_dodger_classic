import java.awt.*;

public class Gold {

    double x;
    double y;
    double size = 8;
    Rectangle goldBounds;

    public Gold(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Rectangle getGoldBounds(){
        if(goldBounds == null){
            goldBounds = new Rectangle();
        }

        goldBounds.setBounds((int)x, (int)y, (int)size, (int)size);
        return goldBounds;
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

}
