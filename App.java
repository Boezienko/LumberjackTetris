import java.util.Random;

public class App {
    public static void main(String[] args) throws Exception {
        Random rand = new Random();

        Tile tile = generateTile(rand.nextInt(1, 8));

        System.out.println("Hello, World!");
    }

    public static Tile generateTile(int caseNum){
        // probably make the game have the factories as attributes
        I_TileFactory IFactory = new I_TileFactory();
        J_TileFactory JFactory = new J_TileFactory();
        L_TileFactory LFactory = new L_TileFactory();
        O_TileFactory OFactory = new O_TileFactory();
        S_TileFactory SFactory = new S_TileFactory();
        T_TileFactory TFactory = new T_TileFactory();
        Z_TileFactory ZFactory = new Z_TileFactory();

        switch(caseNum){
            case 1:
                return IFactory.createTile();
            case 2:
                return JFactory.createTile();
            case 3:
                return LFactory.createTile();
            case 4:
                return OFactory.createTile();
            case 5:
                return SFactory.createTile();
            case 6:
                return TFactory.createTile();
            case 7:
                return ZFactory.createTile();        
            default:
            throw new IllegalArgumentException("unknown case number"); 
        }
    }
}
