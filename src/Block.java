import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Block {
    private final String name;
    private List<Place> assignedPlaces = new ArrayList<>();
    private static List<Block> allExistingBlocks = new ArrayList<>();

    public Block(String name) {
        this.name = name;
    }

    public Place assignPlace(Place place) {
        Block.addPlace(this, place);
        return place;
    }

    public void assignPlace(List<Place> places) {
        Block.addPlace(this, places);
    }

    public static void showBlockOptions(Estate selEstate) {
        System.out.println("\nSelect block in " + selEstate.getName() + ":");
        System.out.println("a - assign block");
        if (selEstate.blocksCount() > 0) {
            System.out.println("r - remove block");
            System.out.println("------------------");
            System.out.println("Existing block:");
            System.out.print(selEstate.showBlocks());
        } else
            System.out.println("No places available yet");
    }

    public static Block selBlockOption(String choice, Estate selEstate) {
        Block selBlock;
        if (choice.equals("a")) {
            selBlock = Block.createBlock();
            selEstate.addBlock(selBlock);
            Block.addBlockToExisting(selBlock);
            return selBlock;
        } else if (choice.equals("r") && Estate.allExistingEstates.size() > 0) {
            selBlock = Block.findExistingBlock();
            if (selBlock != null)
                selBlock.removeBlock(selBlock);
            else
                System.out.println("Entered block is not exists");
        } else
            return selEstate.getBlock(Integer.parseInt(choice));
        return null;
    }

    private static Block findExistingBlock() {
        String blockName = Block.enterBlockName();
        for (Block block : Block.allExistingBlocks)
            if (block.name.equals(blockName))
                return block;
        return null;
    }

    private static Block createBlock() {
        String name = Block.enterBlockName();
        return new Block(name);
    }

    private static String enterBlockName() {
        System.out.print("Enter estate name: ");
        return Main.getScan().next();
    }

    public static void addPlace(Block block, Place place) {
        Place.addToAllExistingPlaces(place);
        block.assignedPlaces.add(place);
    }

    public static void addPlace(Block block, List<Place> places) {
        Place.addToAllExistingPlaces(places);
        block.assignedPlaces.addAll(places);
    }

    private static List<String> enterEstateName() {
        System.out.print("Enter estate name: ");
        String name = Main.getScan().next();

        return Arrays.asList(name);
    }

    public static Block readyBlock() {
        return new Block("Block2115");
    }

    public static void addBlockToExisting(Block block) {
        Block.allExistingBlocks.add(block);
    }

    public static void addBlockToExisting(List<Block> blocks) {
        Block.allExistingBlocks.addAll(blocks);
    }

    public String getName() {
        return name;
    }

    public Place getPlace(int index) {
        return this.assignedPlaces.get(index);
    }

    public int placesSize() {
        return this.assignedPlaces.size();
    }

    public static int allExistingBlocksSize() {
        return Block.allExistingBlocks.size();
    }

    public void removePlace(Place place) {
        this.assignedPlaces.remove(place);
    }

    public void removeBlock(Block block) {
        Block.allExistingBlocks.remove(block);
    }

    public Place getAssignedPlaces(int index) {
        return this.assignedPlaces.get(index);
    }

}
