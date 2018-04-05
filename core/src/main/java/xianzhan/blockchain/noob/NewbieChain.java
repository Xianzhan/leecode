package xianzhan.blockchain.noob;

import java.util.ArrayList;

/**
 * @auther xianzhan
 * @sinese 2018-04-05
 * https://medium.com/programmers-blockchain/create-simple-blockchain-java-tutorial-from-scratch-6eeed3cb03fa
 */
public class NewbieChain {
    private static       ArrayList<Block> blockChain = new ArrayList<>();
    private static final int              DIFFICULTY = 5;

    public static void main(String[] args) {
        //add our blocks to the blockChain ArrayList:

        System.out.println("Trying to Mine block 1... ");
        addBlock(new Block("Hi im the first block", "0"));

        System.out.println("Trying to Mine block 2... ");
        addBlock(new Block("Yo im the second block", blockChain.get(blockChain.size() - 1).getHash()));

        System.out.println("Trying to Mine block 3... ");
        addBlock(new Block("Hey im the third block", blockChain.get(blockChain.size() - 1).getHash()));

        System.out.println("\nBlockChain is Valid: " + isChainValid());

        System.out.println("\nThe block chain: ");
        blockChain.forEach(System.out::println);
    }

    private static boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[DIFFICULTY]).replace('\0', '0');

        //loop through blockChain to check hashes:
        for (int i = 1; i < blockChain.size(); i++) {
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i - 1);
            //compare registered hash and calculated hash:
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //check if hash is solved
            if (!currentBlock.getHash().substring(0, DIFFICULTY).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }

        }
        return true;
    }

    private static void addBlock(Block newBlock) {
        newBlock.mineBlock(DIFFICULTY);
        blockChain.add(newBlock);
    }
}
