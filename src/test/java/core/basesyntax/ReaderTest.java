package core.basesyntax;

import core.basesyntax.services.Reader;
import core.basesyntax.services.impl.ReaderImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ReaderTest {
    private static Reader reader;

    @BeforeAll
    public static void setUp() {
        reader = new ReaderImpl();
    }

    @Test
    public void reader_correctFile_Ok() {
        List<String> expected = new ArrayList<>();
        expected.add("b,banana,45");
        List<String> actual = reader.read("src/test/java/core/basesyntax/recources/inputFile.csv");
        Assert.assertEquals("Reader did not return expected data.",
                expected.toString(), actual.toString());
    }

    @Test
    public void reader_incorrectFile_NotOk() {
        Assert.assertThrows(RuntimeException.class, () -> {
            reader.read("src/test/java/core/basesyntax/recources/incorrectFile.csv");
        });
    }

    @Test
    public void reader_emptyFile_NotOk() {
        Assert.assertThrows(RuntimeException.class, () -> {
            reader.read("src/test/java/core/basesyntax/recources/emptyFile.csv");
        });
    }

    @Test
    public void reader_incorrectData_NotOk() {
        Assert.assertThrows(RuntimeException.class, () -> {
            reader.read("src/test/java/core/basesyntax/recources/incorrectData.csv");
        });
    }
}
