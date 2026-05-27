import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Chunker {
    public static String read(String inFile, int chunkSize) {
        byte[] buffer = new byte[chunkSize];
        byte padding = 0;
        StringBuilder output = new StringBuilder();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(inFile));
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                // Process the chunk (exactly 'bytesRead' bytes are valid)
                if (bytesRead < chunkSize) {
                    // Fill remaining buffer with padding byte
                    Arrays.fill(buffer, bytesRead, chunkSize, padding);
                }
                output.append(processChunk(buffer));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return output.toString();
    }
    public static String processChunk(byte[] buffer) {
        return Arrays.toString(buffer);
    }

    public static ArrayList<byte[]> read2(String inFile, int chunkSize) {
        byte[] buffer = new byte[chunkSize];
        byte padding = 0;
        ArrayList<byte[]> output = new ArrayList<>();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(inFile));
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                // Process the chunk (exactly 'bytesRead' bytes are valid)
                if (bytesRead < chunkSize) {
                    // Fill remaining buffer with padding byte
                    Arrays.fill(buffer, bytesRead, chunkSize, padding);
                }
                output.add(buffer);
                byte[] processedBytes = new byte[chunkSize + 1];
                processedBytes[0] = (byte) bytesRead;
                System.arraycopy(buffer, 0, processedBytes, 1, chunkSize);
                // return processedBytes
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return output;
    }
}
