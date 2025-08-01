package com.apnaStore.inventory_service.messages;

public interface ExceptionMessages {

    String USER_ALREADY_EXIST = "User Already Exisy";

    String PRODUCT_CATEGORY_ALREADY_EXIST = "Product Category Already Exist";

    String INVALID_USERNAME_OR_PASSWORD = "Invalid User Name Or Password";

    String INVENTORY_ALREADY_PRESENT = "Inventory Already Present ";

    String USER_NOT_FOUND = "User Not Found";

    String PRODUCT_NOT_FOUND = "Product Not Found";

    String INSUFFICIENT_STOCK = "Insufficient stock";

    String REFRESH_TOKEN_EXPIRED = "Refresh Token Already Expired ,Please Sign In Again.";

    String INVALID_TOKEN = "Invalid Token";

    String INVALID_CART_ID = "Invalid Cart Id";

    String INVALID_CART_ITEM = "Invalid Cart Item";

    String QUANTITY_CAN_NOT_BE_ZERO = "Quantity Can Not Be Zero";

    String INVALID_ADDRESS_TYPE = "Invalid Address Type";

    String DATA_NOT_FOUND = "Data Not Found";

    String PRODUCT_CATEGORY_NOT_FOUND = "Product Category Not Found";

    String ADDRESS_NOT_FOUND = "Address Not Fouond";

    String CART_IS_EMPTY = "Cart Is Empty";

    String TRANSACTION_FAILED = "Transaction Failed";

    String INVALID_WAREHOUSE = "Invalid Warehouse";
    
    String PRODUCT_SERVICE_UNAVAILABLE = "Product service is unavailable. Try again later.";
}
