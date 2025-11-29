package core.basesyntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import core.basesyntax.services.Reader;
import core.basesyntax.services.impl.ReaderImpl;
import java.util.ArrayList;
import java.util.List;
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
        List<String> actual = reader.read("src/test/java/recources/inputFile.csv");
        assertEquals("Reader did not return expected data.",
                expected.toString(), actual.toString());
    }

    @Test
    public void reader_incorrectFile_NotOk() {
        assertThrows(RuntimeException.class, () -> {
            reader.read("src/test/recources/incorrectFile.csv");
        });
    }

    @Test
    public void reader_emptyFile_NotOk() {
        assertThrows(RuntimeException.class, () -> {
            reader.read("src/test/recources/emptyFile.csv");
        });
    }

    @Test
    public void reader_incorrectData_NotOk() {
        assertThrows(RuntimeException.class, () -> {
            reader.read("src/test/incorrectData.csv");
        });
    }
}
