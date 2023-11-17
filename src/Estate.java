import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Estate {
    private final String name;
    static List<Estate> allExistingEstates = new ArrayList<>();
    private List<Block> blocks = new ArrayList<>();
    private static List<Block> allExistingBlocks = new ArrayList<>();

    public Estate(String name) {
        this.name = name;
    }

    public void addBlock(Block block) {
        Estate.allExistingBlocks.add(block);
        this.blocks.add(block);
    }

    public static void showEstateOptions(Developer dev) {
        System.out.println("\nSelect estate:");
        System.out.println("a - add estate");
        if (dev.estatesSize() > 0) {
            System.out.println("r - remove estate");
            System.out.println("------------------");
            System.out.println("Existing estates:");
            System.out.print(dev.showEstates());
        } else
            System.out.println("No places available yet");
    }

    public static Estate selEstateOption(String choice, Developer dev) {
        Estate selEstate;
        if (choice.equals("a")) {
            selEstate = Estate.createEstate();
            dev.addEstate(selEstate);
            return selEstate;
        } else if (choice.equals("r") && Estate.allExistingEstates.size() > 0) {
            selEstate = Estate.findExistingEstate();
            Estate.existingEstateResult(dev, selEstate);
        } else
            return dev.getEstate(Integer.parseInt(choice));
        return selEstate;
    }

    public String showBlocks() {
        String blocksList = "";
        for (int i = 0; i < this.blocksCount(); i++)
            blocksList += i + " - " + this.getBlock(i).getName() + "\n";
        return blocksList;
    }

    private static void existingEstateResult(Developer dev, Estate selEstate) {
        if (selEstate != null) {
            dev.removeEstate(selEstate);
            System.out.println("Successfully removed the estate");
        } else
            System.out.println("Failed to removed the estate");
    }

    static Estate findExistingEstate() {
        List<String> estateData = Estate.enterBlockName();
        for (Estate estate : Estate.allExistingEstates)
            if (estate.name.equals(estateData.get(0)))
                return estate;
        System.out.println("Entered estate is not exists");
        return null;
    }

    private static Estate createEstate() {
        List<String> placeData = Estate.enterBlockName();
        return new Estate(placeData.get(0));
    }

    private static List<String> enterBlockName() {
        System.out.print("Enter estate name: ");
        String name = Main.getScan().next();

        return Arrays.asList(name);
    }

    public static void addEstateToExisting(Estate estate) {
        Estate.allExistingEstates.add(estate);
    }

    public static void addEstateToExisting(List<Estate> estates) {
        Estate.allExistingEstates.addAll(estates);
    }

    public static void removeEstateToExisting(Estate estate) {
        Estate.allExistingEstates.remove(estate);
    }

    public static Estate readyEstate() {
        return new Estate("Żoliborz");
    }

    public String getName() {
        return this.name;
    }

    public Block getBlock(int index) {
        return this.blocks.get(index);
    }

    public int blocksCount() {
        return this.blocks.size();
    }

}
