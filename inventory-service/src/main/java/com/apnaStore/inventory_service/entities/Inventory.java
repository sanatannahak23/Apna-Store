package com.apnaStore.inventory_service.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "inventories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory extends Auditable {

    @Id
    private String id;

    @Field("product_id")
    private Long productId;

    @DBRef
    @Field("warehouse")
    private Warehouse warehouse;

    private Long stock;
}
