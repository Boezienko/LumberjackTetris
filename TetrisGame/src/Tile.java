public abstract class Tile implements ITile {
    int[] position;
    boolean active;

    Tile(){
        position = new int[3];
        active = true;
    }

    void setPosition(int[] newPos){
        if(newPos.length == 3){
            this.position = newPos;
        }
    }

    public int[] getPosition(){
        return position;
    }

    public boolean getActiveStatus(){
        return active;
    }
}
