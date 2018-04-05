package xianzhan.blockchain.noob;

import xianzhan.core.util.CryptoUtils;

/**
 * @auther xianzhan
 * @sinese 2018-04-05
 */
public class Block {

    private String hash;
    private String previousHash;
    private String data;
    private long   timestamp;
    private int    nonce;

    public Block(String data, String previousHash) {
        this.data = data;
        this.previousHash = previousHash;

        this.timestamp = System.currentTimeMillis();
        // Making sure we do this after we set the other values.
        this.hash = calculateHash();
    }

    /*
     * Calculate new hash based on blocks contents
     */
    String calculateHash() {
        return CryptoUtils.encodeSha256(previousHash +
                                                Long.toString(timestamp) +
                                                Integer.toString(nonce) +
                                                data);
    }

    void mineBlock(int difficulty) {
        String target = getDifficultyString(difficulty);
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Mined!!! : " + hash);
    }

    private String getDifficultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    @Override
    public String toString() {
        return "Block{\n" +
                "\t\"hash\": '" + hash + "\',\n" +
                "\t\"previousHash\": '" + previousHash + "\',\n" +
                "\t\"data\": '" + data + "\',\n" +
                "\t\"timestamp\": " + timestamp + ",\n" +
                "\t\"nonce\": " + nonce + ",\n" +
                '}';
    }

    String getHash() {
        return hash;
    }

    String getPreviousHash() {
        return previousHash;
    }
}
