package core.basesyntax;

import core.basesyntax.model.FruitTransaction;
import core.basesyntax.services.Reader;
import core.basesyntax.services.converter.DataConverter;
import core.basesyntax.services.converter.DataConverterImpl;
import core.basesyntax.services.impl.ReaderImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
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
        List<String> data = reader.read("src/test/java/core/basesyntax/recources/inputFile.csv");
        List<FruitTransaction> actual = dataConverter.convertToTransaction(data);
        Assert.assertEquals(1, actual.size());
        Assert.assertEquals(actual.get(0), expected.get(0));
    }

    @Test
    public void converter_incorrectData_NotOk() {
        List<String> data = reader.read(
                "src/test/java/core/basesyntax/recources/incorrectDataForConvert.csv");
        Assert.assertThrows(RuntimeException.class, () -> {
            dataConverter.convertToTransaction(data);
        });
    }
}
