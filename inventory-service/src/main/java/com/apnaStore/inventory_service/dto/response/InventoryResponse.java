package com.apnaStore.inventory_service.dto.response;

import com.apnaStore.inventory_service.entities.Auditable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse extends Auditable {

    private String id;

    private Long productId;

    private String warehouseName;

    private Long stock;
}
