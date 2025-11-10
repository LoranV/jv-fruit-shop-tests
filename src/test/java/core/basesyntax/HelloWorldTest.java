package core.basesyntax;

import core.basesyntax.model.FruitTransaction;
import core.basesyntax.services.Reader;
import core.basesyntax.services.ReportGenerator;
import core.basesyntax.services.ShopService;
import core.basesyntax.services.Writer;
import core.basesyntax.services.converter.DataConverter;
import core.basesyntax.services.converter.DataConverterImpl;
import core.basesyntax.services.impl.ReaderImpl;
import core.basesyntax.services.impl.ReportGeneratorImpl;
import core.basesyntax.services.impl.ShopServiceImpl;
import core.basesyntax.services.impl.WriterImpl;
import core.basesyntax.strategy.OperationStrategy;
import core.basesyntax.strategy.OperationStrategyImpl;
import core.basesyntax.strategy.operations.BalanceOperation;
import core.basesyntax.strategy.operations.OperationHandler;
import core.basesyntax.strategy.operations.PurchaseOperation;
import core.basesyntax.strategy.operations.ReturnOperation;
import core.basesyntax.strategy.operations.SupplyOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HelloWorldTest {
    private static Reader reader;
    private static DataConverter dataConverter;
    private static ShopService shopService;
    private static List<FruitTransaction> soloListFruit;
    private static List<FruitTransaction> multiplyListFruits;
    private static ReportGenerator reportGenerator;
    private static Writer fileWriter;

    @BeforeAll
    public static void setUp() {
        reader = new ReaderImpl();
        dataConverter = new DataConverterImpl();
        soloListFruit = new ArrayList<>();
        soloListFruit.add(new FruitTransaction("b", "banana", 45));
        multiplyListFruits = new ArrayList<>();

        Map<FruitTransaction.Operation, OperationHandler> operationHandlers = new HashMap<>();
        operationHandlers.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        operationHandlers.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperation());
        operationHandlers.put(FruitTransaction.Operation.RETURN, new ReturnOperation());
        operationHandlers.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        OperationStrategy operationStrategy = new OperationStrategyImpl(operationHandlers);
        shopService = new ShopServiceImpl(operationStrategy);

        reportGenerator = new ReportGeneratorImpl();
        fileWriter = new WriterImpl();
    }

    @BeforeEach
    public void setUpEach() {
        soloListFruit.clear();
        soloListFruit.add(new FruitTransaction("b", "banana", 45));
    }

    @Test
    public void reader_correctFile_Ok() {
        List<String> expected = new ArrayList<>();
        expected.add("b,banana,45");
        List<String> actual = reader.read("src/test/java/core/basesyntax/recources/inputFile.csv");
        Assert.assertEquals("Readed data is wrong.", expected.toString(), actual.toString());
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

    @Test
    public void converter_correctData_Ok() {
        List<String> data = reader.read("src/test/java/core/basesyntax/recources/inputFile.csv");
        List<FruitTransaction> actual = dataConverter.convertToTransaction(data);
        List<FruitTransaction> expected = soloListFruit;
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

    @Test
    void shopService_oneDatacorrect_Ok() {
        Map<String, Integer> expected = new HashMap<>();
        expected.put("banana", 45);
        Map<String, Integer> actual = shopService.process(soloListFruit);
        Assert.assertEquals(1, actual.size());
        Assert.assertEquals(actual, expected);
    }

    @Test
    void shopService_multiplyDataCorrect_Ok() {
        multiplyListFruits.clear();
        multiplyListFruits.add(new FruitTransaction("b", "banana", 45));
        multiplyListFruits.add(new FruitTransaction("p", "banana", 15));
        multiplyListFruits.add(new FruitTransaction("s", "banana", 25));
        multiplyListFruits.add(new FruitTransaction("r", "banana", 15));
        Map<String, Integer> expected = new HashMap<>();
        expected.put("banana", 70);
        Map<String, Integer> actual = shopService.process(multiplyListFruits);
        Assert.assertEquals(1, actual.size());
        Assert.assertEquals(actual, expected);
    }

    @Test
    void shopService_NegativeBalance_NotOk() {
        soloListFruit.clear();
        soloListFruit.add(new FruitTransaction("b", "banana", -15));
        Assert.assertThrows(RuntimeException.class, () -> {
            shopService.process(soloListFruit);
        });
    }

    @Test
    void shopService_NegativePurchase_NotOk() {
        multiplyListFruits.clear();
        multiplyListFruits.add(new FruitTransaction("b", "banana", 15));
        multiplyListFruits.add(new FruitTransaction("p", "banana", 20));
        Assert.assertThrows(RuntimeException.class, () -> {
            shopService.process(multiplyListFruits);
        });
    }

    @Test
    void shopService_NegativeSupply_NotOk() {
        soloListFruit.clear();
        soloListFruit.add(new FruitTransaction("s", "banana", -15));
        Assert.assertThrows(RuntimeException.class, () -> {
            shopService.process(soloListFruit);
        });
    }

    @Test
    void shopService_NegativeReturn_NotOk() {
        soloListFruit.clear();
        soloListFruit.add(new FruitTransaction("r", "banana", -15));
        Assert.assertThrows(RuntimeException.class, () -> {
            shopService.process(soloListFruit);
        });
    }

    @Test
    void reportGenerator_CorrectOutput_Ok() {
        String expected = "fruit,quantity\nbanana,45\n";
        String actual = reportGenerator.getReport(shopService.process(soloListFruit));
        Assert.assertEquals(expected, actual);
    }

    @Test
    void writer_CorrectOutput_Ok() {
        soloListFruit.clear();
        soloListFruit.add(new FruitTransaction("b", "banana", 15));
        String reportToWrite = reportGenerator.getReport(shopService.process(soloListFruit));
        boolean actual = fileWriter.write(reportToWrite,
                "src/test/java/core/basesyntax/recources/outputActualFile.csv"
        );
        Assert.assertTrue(actual);
    }
}
