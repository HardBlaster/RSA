package hu.unideb;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Map.Entry;

public class Main {

    public static void main(String[] args) {
        BigInteger p = Utils.randomPrime(2048);
        BigInteger q = Utils.randomPrime(2048);

        BigInteger N = p.multiply(q);
        BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));

        BigInteger e = minRelativePrime(phiN);
        BigInteger d = privateKey(phiN, e);

        String message = "Hello World!";
        System.out.println("Message: " + message);

        BigInteger encrypted = encrypt(message, e, N);
        System.out.println("Encrypted: " + encrypted);

        String decrypted = decrypt(encrypted, d, N);
        System.out.println("Decrypted: " + decrypted);

        String decryptedCRT = decrypt(p ,q, d, encrypted);
        System.out.println("Decrypted with CRT: " + decryptedCRT);

    }

    public static BigInteger encrypt(String message, BigInteger e, BigInteger N) {
        BigInteger msg = new BigInteger(message.getBytes(StandardCharsets.US_ASCII));

        return Utils.quickExponent(msg, e, N);
    }

    public static String decrypt(BigInteger encrypted, BigInteger d, BigInteger N) {
        return new String(Utils.quickExponent(encrypted, d, N).toByteArray());
    }

    public static String decrypt(BigInteger p, BigInteger q, BigInteger d, BigInteger encrypted) {
        return new String(Utils.chineseRemainderTheorem(p, q, d, encrypted).toByteArray());
    }

    public static BigInteger minRelativePrime(BigInteger phiN) {
        for(int i = 3; ; i+=2) {
            if(Utils.euclideanAlgorithm(BigInteger.valueOf(i), phiN).equals(BigInteger.ONE)) {
                return BigInteger.valueOf(i);
            }
        }
    }

    public static BigInteger privateKey(BigInteger phiN, BigInteger e) {
        Entry<BigInteger, BigInteger> xy = Utils.extendedEuclideanAlgorithm(phiN, e);

        return xy.getValue().signum() == -1 ? xy.getValue().add(phiN) : xy.getValue();
    }

}

