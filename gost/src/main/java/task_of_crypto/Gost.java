package task_of_crypto;

import java.math.BigInteger;

public class Gost {
    private final static int QUANTITY_OF_ROUNDS = 32;
    private final static int SIZE_OF_CHAR = 8;
    private final static int SIZE_OF_BLOCK = 64;
    private final static int SIZE_OF_KEY = 256;
    private final static int QUANTITY_OF_KEY = 8;

    private int countOfBlockInCurrentTxt;
    public String[] currentTextBlocks;
    public String[] currentKeyBlocks;

    public final static int[][] SUBSTITUTION_BLOCK =
            {
                    {4, 10, 9, 2, 13, 8, 0, 14, 6, 11, 1, 12, 7, 15, 5, 3},
                    {14, 11, 4, 12, 6, 13, 15, 10, 2, 3, 8, 1, 0, 7, 5, 9},
                    {5, 8, 1, 13, 10, 3, 4, 2, 14, 15, 12, 7, 6, 0, 9, 11},
                    {7, 13, 10, 1, 0, 8, 9, 15, 14, 4, 6, 12, 11, 2, 5, 3},
                    {6, 12, 7, 1, 5, 15, 13, 8, 4, 10, 9, 14, 0, 3, 11, 2},
                    {4, 11, 10, 0, 7, 2, 1, 13, 3, 6, 8, 5, 9, 12, 15, 14},
                    {13, 11, 4, 1, 3, 15, 5, 9, 0, 10, 14, 7, 6, 8, 2, 12},
                    {1, 15, 13, 0, 5, 7, 10, 4, 9, 2, 3, 14, 6, 11, 8, 12}
            };

    public void cutKeyIntoBlocks(String key) {
        currentKeyBlocks = new String[8];
        for (int i = 0; i < QUANTITY_OF_KEY; i++) {
            currentKeyBlocks[i] = key.substring(i * (SIZE_OF_KEY / QUANTITY_OF_KEY),
                    i * (SIZE_OF_KEY / QUANTITY_OF_KEY) + (SIZE_OF_KEY / QUANTITY_OF_KEY));
        }
    }

    public String stringToRightLength(String text) {
        StringBuilder textBuilder = new StringBuilder(text);
        while ((textBuilder.length() * SIZE_OF_CHAR) % SIZE_OF_BLOCK != 0) {
            textBuilder.append("#");
        }
        text = textBuilder.toString();
        return text;
    }

    public void cutStringIntoBlocks(String text) {
        countOfBlockInCurrentTxt = (text.length() * SIZE_OF_CHAR) / SIZE_OF_BLOCK;
        currentTextBlocks = new String[countOfBlockInCurrentTxt];
        int lengthOfBlock = text.length() / countOfBlockInCurrentTxt;
        for (int i = 0; i < countOfBlockInCurrentTxt; i++) {
            currentTextBlocks[i] = text.substring(i * lengthOfBlock, i * lengthOfBlock + lengthOfBlock);
            currentTextBlocks[i] = stringToBinaryFormat(currentTextBlocks[i]);
        }
    }

    public String stringToBinaryFormat(String text) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int ascii = (int) text.charAt(i);
            result.append(convertToBinaryStringWithCustomLength(ascii, 8));
        }
        return result.toString();
    }

    public String convertToBinaryStringWithCustomLength(long value, int length) {
        StringBuilder result = new StringBuilder(Long.toBinaryString(value));
        while (result.length() != length) {
            result.insert(0, "0");
        }
        return result.toString();
    }

    public String toCorrectKeyWord(String key) {
        int lengthOfKey = SIZE_OF_KEY / SIZE_OF_CHAR;
        StringBuilder result = new StringBuilder(key);
        if (key.length() > lengthOfKey) {
            result.delete(lengthOfKey, key.length());
        } else {
            while (result.length() != lengthOfKey) {
                result.append("*");
            }
        }
        return result.toString();
    }

    public String xor(String str1, String str2) {
        StringBuilder result = new StringBuilder();
        if (str1.length() != str2.length()) {
            System.out.println("Something went wrong!");
        } else {
            for (int i = 0; i < str1.length(); i++) {
                if (str1.charAt(i) == str2.charAt(i))
                    result.append("0");
                else
                    result.append("1");
            }
        }
        return result.toString();
    }

    public String sumMod2In32(String str1, String str2) {
        BigInteger value1 = new BigInteger(str1, 2);
        BigInteger value2 = new BigInteger(str2, 2);
        BigInteger mod = new BigInteger(Long.toBinaryString((long) Math.pow(2, 32)), 2);
        BigInteger sumResult = value1.add(value2);
        if (sumResult.compareTo(mod) >= 0) {
            return convertToBinaryStringWithCustomLength(sumResult.subtract(mod).longValue(), 32);
        } else {
            return convertToBinaryStringWithCustomLength(sumResult.longValue(), 32);
        }

    }

    public String substitutionInTheSBlock(String block) {
        String[] blocks = new String[8];
        StringBuilder result = new StringBuilder();
        int quantityBlocks = blocks.length;
        for (int i = 0; i < quantityBlocks; i++) {
            blocks[i] = block.substring(i * 4, i * 4 + 4);
        }
        for (int i = 0; i < quantityBlocks; i++) {
            BigInteger valueInDec = new BigInteger(blocks[i], 2);
            int column = Integer.valueOf(valueInDec.toString());
            String replacement = convertToBinaryStringWithCustomLength(SUBSTITUTION_BLOCK[i][column], 4);
            result.append(replacement);
        }
        return result.toString();
    }

    public String stringFromBinaryToNormalFormat(String binaryStr) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < binaryStr.length(); i++) {
            String charBinary = binaryStr.substring(0, SIZE_OF_CHAR);
            binaryStr = binaryStr.substring(SIZE_OF_CHAR);
            BigInteger asciiBig = new BigInteger(charBinary, 2);
            int ascii = Integer.valueOf(asciiBig.toString());
            result.append((char) ascii);
        }
        return result.toString();
    }

    public String cyclicShift(String s1) {
        StringBuilder result = new StringBuilder(s1);
        for (int i = 0; i < 11; i++) {
            result.append(result.charAt(0));
            result.delete(0, 1);
        }
        return result.toString();
    }


    String encryption(String text, String key) {
        String R, L;
        String buf;
        StringBuilder result = new StringBuilder();
        text = stringToRightLength(text);

        cutStringIntoBlocks(text);
        key = toCorrectKeyWord(key);

        key = stringToBinaryFormat(key);
        cutKeyIntoBlocks(key);

        for (int j = 0; j < countOfBlockInCurrentTxt; j++) {
            R = currentTextBlocks[j].substring(0, 32);
            L = currentTextBlocks[j].substring(32, 64);
            for (int i = 0; i < QUANTITY_OF_ROUNDS - 1; i++) {
                buf = R;
                if (i >= 24)
                    R = encryptionOneRound(R, L, currentKeyBlocks[7 - i % 8]);
                else
                    R = encryptionOneRound(R, L, currentKeyBlocks[i % 8]);
                L = buf;
            }
            L = encryptionOneRound(R, L, currentKeyBlocks[0]);

            result.append(R).append(L);
        }
        return stringFromBinaryToNormalFormat(result.toString());
    }

    String encryptionOneRound(String R, String L, String key) {
        String temp = sumMod2In32(R, key);
        temp = substitutionInTheSBlock(temp);
        temp = cyclicShift(temp);
        temp = xor(L, temp);
        return temp;
    }

    String decryption(String text, String key) {
        String R, L;
        String buf;
        StringBuilder result = new StringBuilder();
        text = stringToRightLength(text);

        cutStringIntoBlocks(text);
        key = toCorrectKeyWord(key);

        key = stringToBinaryFormat(key);
        cutKeyIntoBlocks(key);
        for (int j = 0; j < countOfBlockInCurrentTxt; j++) {
            R = currentTextBlocks[j].substring(0, 32);
            L = currentTextBlocks[j].substring(32, 64);
            for (int i = 0; i < QUANTITY_OF_ROUNDS - 1; i++) {
                buf = R;
                if (i <= 7)
                    R = encryptionOneRound(R, L, currentKeyBlocks[i]);
                else
                    R = encryptionOneRound(R, L, currentKeyBlocks[7 - i % 8]);
                L = buf;
            }
            L = encryptionOneRound(R, L, currentKeyBlocks[0]);
            result.append(R).append(L);
        }
        return stringFromBinaryToNormalFormat(result.toString());
    }


}
