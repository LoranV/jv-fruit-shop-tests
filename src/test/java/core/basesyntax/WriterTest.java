package core.basesyntax;

import core.basesyntax.model.FruitTransaction;
import core.basesyntax.services.Reader;
import core.basesyntax.services.ReportGenerator;
import core.basesyntax.services.ShopService;
import core.basesyntax.services.Writer;
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
import org.junit.jupiter.api.Test;

public class WriterTest {
    private static ReportGenerator reportGenerator;
    private static ShopService shopService;
    private static List<FruitTransaction> soloListFruit;
    private static Writer fileWriter;
    private static Reader fileReader;

    @BeforeAll
    public static void setUp() {
        reportGenerator = new ReportGeneratorImpl();
        soloListFruit = new ArrayList<>();
        soloListFruit.add(new FruitTransaction("b", "banana", 15));
        fileWriter = new WriterImpl();
        fileReader = new ReaderImpl();

        Map<FruitTransaction.Operation, OperationHandler> operationHandlers = new HashMap<>();
        operationHandlers.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        operationHandlers.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperation());
        operationHandlers.put(FruitTransaction.Operation.RETURN, new ReturnOperation());
        operationHandlers.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        OperationStrategy operationStrategy = new OperationStrategyImpl(operationHandlers);
        shopService = new ShopServiceImpl(operationStrategy);
    }

    @Test
    void writer_CorrectOutput_Ok() {
        List<String> expected = new ArrayList<>();
        expected.add("banana,15");
        String reportToWrite = reportGenerator.getReport(shopService.process(soloListFruit));
        boolean isWritten = fileWriter.write(reportToWrite,
                "src/test/java/core/basesyntax/recources/outputActualFile.csv"
        );
        List<String> actual = fileReader.read(
                "src/test/java/core/basesyntax/recources/outputActualFile.csv");
        Assert.assertEquals(expected, actual);
        Assert.assertTrue(isWritten);
    }
}
