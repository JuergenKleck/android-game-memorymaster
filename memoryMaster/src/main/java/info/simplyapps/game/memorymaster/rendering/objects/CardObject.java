package info.simplyapps.game.memorymaster.rendering.objects;


public class CardObject {

    public boolean flipped;
    public boolean found;
    public int type;

    public int tx;
    public int ty;

    public int gReference;

    public int id;

    public CardObject() {
        id = this.hashCode();
    }

}
