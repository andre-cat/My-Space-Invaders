package space.invaders;
// Create a public abstract class called Objeto without any attributes or methods.
public abstract class Objeto {
    private int x;
    private int y;
    private int vel_x;
    private int vel_y;
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public int getVel_x() {
        return vel_x;
    }
    public void setVel_x(int vel_x) {
        this.vel_x = vel_x;
    }
    public int getVel_y() {
        return vel_y;
    }
    public void setVel_y(int vel_y) {
        this.vel_y = vel_y;
    }
    //Generate constructor with all the attributes of the class.
    public Objeto(int x, int y, int vel_x, int vel_y) {
        this.x = x;
        this.y = y;
        this.vel_x = vel_x;
        this.vel_y = vel_y;
    }
    // Create a public abstract method called mover.
    public abstract void mover();
    // Create a public abstract method called pintar.
    public abstract void pintar();
}
