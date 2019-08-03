import com.nixxcode.jvmbrotli.common.BrotliLoader;
import com.nixxcode.jvmbrotli.dec.BrotliInputStream;
import com.nixxcode.jvmbrotli.enc.BrotliOutputStream;
import com.nixxcode.jvmbrotli.enc.Encoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

    private static String sourceFilePath = "src/main/java/Main.java";
    private static String encodedPath = "src/encoded.br";
    private static String decodedPath = "src/decoded.txt";

    public static void main(String[] args) throws Exception {
        BrotliLoader.loadBrotli();
        encode();
        decode();
    }

    private static void encode() throws IOException {
        // Init file input and output
        FileInputStream inFile = new FileInputStream(sourceFilePath);
        FileOutputStream outFile = new FileOutputStream(encodedPath);

        // If being used to compress streams in real-time, I do not advise a quality setting above 4 due to performance
        Encoder.Parameters params = new Encoder.Parameters().setQuality(4);

        // Initialize compressor by binding it to our file output stream
        BrotliOutputStream brotliOutputStream = new BrotliOutputStream(outFile, params);

        int read = inFile.read();
        while (read > -1) { // -1 means EOF
            brotliOutputStream.write(read);
            read = inFile.read();
        }

        // It's important to close the BrotliOutputStream object. This also closes the underlying FileOutputStream
        brotliOutputStream.close();
        inFile.close();
        System.out.println("Created new file '" + encodedPath + "'");
    }

    private static void decode() throws Exception {
        // Init file input and output
        FileInputStream inFile = new FileInputStream(encodedPath);
        FileOutputStream outFile = new FileOutputStream(decodedPath);

        // Initialize decompressor by binding it to our file input stream
        BrotliInputStream brotliInputStream = new BrotliInputStream(inFile);

        int read = brotliInputStream.read();
        while (read > -1) { // -1 means EOF
            outFile.write(read);
            read = brotliInputStream.read();
        }

        // It's important to close the BrotliInputStream object. This also closes the underlying FileInputStream
        brotliInputStream.close();
        outFile.close();
        System.out.println("Created new file '" + decodedPath + "'");
    }

}

