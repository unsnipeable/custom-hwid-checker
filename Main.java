import javax.swing.JOptionPane;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static void main(String[] args) {
        byte[] hwid = generateHWID();
        String hwidHex = bytesToHex(hwid);

        JOptionPane.showMessageDialog(
                null,
                "Your encrypted hardware id: \n" + hwidHex,
                "HWID",
                JOptionPane.INFORMATION_MESSAGE
        );


        try (FileWriter writer = new FileWriter("hwid.log")) {
            writer.write(hwidHex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] generateHWID() {
        try {
            MessageDigest hash = MessageDigest.getInstance("MD5");

            String s = System.getProperty("os.name") +
                    System.getProperty("os.version") +
                    System.getProperty("os.arch") +
                    System.getenv("PROCESSOR_ARCHITECTURE") +
                    System.getenv("PROCESSOR_ARCHITEW6432") +
                    Runtime.getRuntime().availableProcessors() +
                    System.getenv("PROCESSOR_IDENTIFIER") +
                    System.getenv("NUMBER_OF_PROCESSORS");
            return hash.digest(s.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new Error("Algorithm wasn't found.", e);
        }
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
