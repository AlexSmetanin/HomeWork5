package lecture5.tests;

import lecture5.BaseTest;
import lecture5.model.ProductData;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PlaceOrderTest extends BaseTest {

    @DataProvider
    public Object[][] getCustomerData() {
        return new Object[][]{
                {"Сметанин", "Александр", "AlexSmetanin72@gmail.com", "ул.Шевченко 123 кв.54", "18000", "Черкассы"},
        };
    }

    @Test
    public void checkSiteVersion() {
        // TODO open main page and validate website version
        actions.checkSiteVersion(isMobileTesting);
    }


    @Test(dataProvider = "getCustomerData")
    public void createNewOrder(String firstname, String lastname, String email, String address1, String postcode, String city) {
        // TODO implement order creation test

        actions.openRandomProduct();        // open random product

        ProductData product = actions.getOpenedProductInfo();    // save product parameters

        actions.addProductToCard();     // add product to Cart and validate product information in the Cart

        actions.orderCreation(firstname, lastname, email, address1, postcode, city);    // proceed to order creation, fill required information

        actions.validateOrderSummary(product);  // place new order and validate order summary

        actions.chkUpdInStockValue(product);    // check updated In Stock value
    }

}
