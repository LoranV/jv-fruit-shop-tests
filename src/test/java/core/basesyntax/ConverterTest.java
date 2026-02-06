package core.basesyntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import core.basesyntax.model.FruitTransaction;
import core.basesyntax.services.Reader;
import core.basesyntax.services.converter.DataConverter;
import core.basesyntax.services.converter.DataConverterImpl;
import core.basesyntax.services.impl.ReaderImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ConverterTest {
    private static DataConverter dataConverter;
    private static Reader reader;

    @BeforeAll
    public static void setUp() {
        dataConverter = new DataConverterImpl();
        reader = new ReaderImpl();
    }

    @Test
    public void converter_correctData_Ok() {
        List<FruitTransaction> expected = new ArrayList<>();
        expected.add(new FruitTransaction("b", "banana", 45));
        List<String> data = reader.read("src/test/recources/inputFile.csv");
        List<FruitTransaction> actual = dataConverter.convertToTransaction(data);
        assertEquals(1, actual.size());
        assertEquals(actual.get(0), expected.get(0));
    }

    @Test
    public void converter_incorrectData_NotOk() {
        List<String> data = reader.read(
                "src/test/recources/incorrectDataForConvert.csv");
        assertThrows(RuntimeException.class, () -> {
            dataConverter.convertToTransaction(data);
        });
    }
}
