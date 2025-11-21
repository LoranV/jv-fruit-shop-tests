package core.basesyntax;

import core.basesyntax.model.FruitTransaction;
import core.basesyntax.services.ReportGenerator;
import core.basesyntax.services.ShopService;
import core.basesyntax.services.impl.ReportGeneratorImpl;
import core.basesyntax.services.impl.ShopServiceImpl;
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

public class ReportGeneratorTest {
    private static ReportGenerator reportGenerator;
    private static ShopService shopService;
    private static List<FruitTransaction> soloListFruit;

    @BeforeAll
    public static void setUp() {
        reportGenerator = new ReportGeneratorImpl();
        soloListFruit = new ArrayList<>();
        soloListFruit.add(new FruitTransaction("b", "banana", 45));

        Map<FruitTransaction.Operation, OperationHandler> operationHandlers = new HashMap<>();
        operationHandlers.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        operationHandlers.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperation());
        operationHandlers.put(FruitTransaction.Operation.RETURN, new ReturnOperation());
        operationHandlers.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        OperationStrategy operationStrategy = new OperationStrategyImpl(operationHandlers);
        shopService = new ShopServiceImpl(operationStrategy);
    }

    @Test
    void reportGenerator_CorrectOutput_Ok() {
        String expected = "fruit,quantity\nbanana,45\n";
        String actual = reportGenerator.getReport(shopService.process(soloListFruit));
        Assert.assertEquals(expected, actual);
    }
}
