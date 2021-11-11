import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ThreadLocalRandom;

public final class GenerateWords {

    public static void main(String[] args) throws IOException {
        int words = 100_000_000;
        int wordsPerLine = 1_000_000;
        int charsPerWord = 4;
        StringBuilder result = new StringBuilder(words * charsPerWord);
        for (int i = 0; i < words; i++) {
            if (i != 0 && i % wordsPerLine == 0) {
                result.append('\n');
            }
            for (int j = 0; j < charsPerWord; j++) {
                result.append((char) ThreadLocalRandom.current().nextInt(97, 123));
            }
            result.append(' ');
        }
        Files.writeString(Path.of("data/wordcount"), result);
    }
}
