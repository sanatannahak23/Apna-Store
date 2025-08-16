package com.apnaStore.inventory_service.dto.response;

import com.apnaStore.inventory_service.entities.Auditable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WarehouseResponse extends Auditable {

    private String id;

    private String name;

    private String location;
}
