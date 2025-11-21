package core.basesyntax;

import core.basesyntax.model.FruitTransaction;
import core.basesyntax.services.ShopService;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ShopServiceTest {
    private static ShopService shopService;
    private static List<FruitTransaction> soloListFruit;
    private static List<FruitTransaction> multiplyListFruits;

    @BeforeAll
    public static void setUp() {
        soloListFruit = new ArrayList<>();
        multiplyListFruits = new ArrayList<>();

        Map<FruitTransaction.Operation, OperationHandler> operationHandlers = new HashMap<>();
        operationHandlers.put(FruitTransaction.Operation.BALANCE, new BalanceOperation());
        operationHandlers.put(FruitTransaction.Operation.PURCHASE, new PurchaseOperation());
        operationHandlers.put(FruitTransaction.Operation.RETURN, new ReturnOperation());
        operationHandlers.put(FruitTransaction.Operation.SUPPLY, new SupplyOperation());
        OperationStrategy operationStrategy = new OperationStrategyImpl(operationHandlers);
        shopService = new ShopServiceImpl(operationStrategy);
    }

    @BeforeEach
    public void setUpEach() {
        soloListFruit.clear();
        multiplyListFruits.clear();
    }

    @Test
    void shopService_oneDatacorrect_Ok() {
        Map<String, Integer> expected = new HashMap<>();
        expected.put("banana", 45);
        soloListFruit.add(new FruitTransaction("b", "banana", 45));
        Map<String, Integer> actual = shopService.process(soloListFruit);
        Assert.assertEquals(1, actual.size());
        Assert.assertEquals(actual, expected);
    }

    @Test
    void shopService_multiplyDataCorrect_Ok() {
        Map<String, Integer> expected = new HashMap<>();
        expected.put("banana", 70);
        multiplyListFruits.add(new FruitTransaction("b", "banana", 45));
        multiplyListFruits.add(new FruitTransaction("p", "banana", 15));
        multiplyListFruits.add(new FruitTransaction("s", "banana", 25));
        multiplyListFruits.add(new FruitTransaction("r", "banana", 15));
        Map<String, Integer> actual = shopService.process(multiplyListFruits);
        Assert.assertEquals(1, actual.size());
        Assert.assertEquals(actual, expected);
    }

    @Test
    void shopService_NegativeBalance_NotOk() {
        soloListFruit.add(new FruitTransaction("b", "banana", -15));
        Assert.assertThrows(RuntimeException.class, () -> {
            shopService.process(soloListFruit);
        });
    }

    @Test
    void shopService_NegativePurchase_NotOk() {
        multiplyListFruits.add(new FruitTransaction("b", "banana", 15));
        multiplyListFruits.add(new FruitTransaction("p", "banana", 20));
        Assert.assertThrows(RuntimeException.class, () -> {
            shopService.process(multiplyListFruits);
        });
    }

    @Test
    void shopService_NegativeSupply_NotOk() {
        soloListFruit.add(new FruitTransaction("s", "banana", -15));
        Assert.assertThrows(RuntimeException.class, () -> {
            shopService.process(soloListFruit);
        });
    }

    @Test
    void shopService_NegativeReturn_NotOk() {
        soloListFruit.add(new FruitTransaction("r", "banana", -15));
        Assert.assertThrows(RuntimeException.class, () -> {
            shopService.process(soloListFruit);
        });
    }
}
