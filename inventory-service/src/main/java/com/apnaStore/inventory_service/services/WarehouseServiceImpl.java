package com.apnaStore.inventory_service.services;

import com.apnaStore.inventory_service.dto.request.WarehouseRequest;
import com.apnaStore.inventory_service.dto.response.WarehouseResponse;
import com.apnaStore.inventory_service.entities.Warehouse;
import com.apnaStore.inventory_service.exception.DataNotFoundException;
import com.apnaStore.inventory_service.helper.EntityToResponse;
import com.apnaStore.inventory_service.helper.RequestToEntity;
import com.apnaStore.inventory_service.messages.ExceptionMessages;
import com.apnaStore.inventory_service.repository.WarehouseRepository;
import com.apnaStore.inventory_service.services.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Override
    public void deleteWarehouse(String id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_WAREHOUSE));
        warehouseRepository.delete(warehouse);
    }

    @Override
    public WarehouseResponse updateWarehouse(String id, WarehouseRequest warehouseRequest) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_WAREHOUSE));

        boolean check = false;
        if (warehouseRequest.getLocation() != null && !warehouseRequest.getLocation().equals(warehouse.getLocation())) {
            warehouse.setLocation(warehouseRequest.getLocation());
            check = true;
        }

        if (warehouseRequest.getName() != null && !warehouseRequest.getName().equals(warehouse.getName())) {
            warehouse.setName(warehouseRequest.getName());
            check = true;
        }

        if (check) warehouse = warehouseRepository.save(warehouse);
        return EntityToResponse.warehouseToResponse(warehouse);
    }

    @Override
    public WarehouseResponse getById(String id) {
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException(ExceptionMessages.INVALID_WAREHOUSE));
        return EntityToResponse.warehouseToResponse(warehouse);
    }

    @Override
    public List<WarehouseResponse> getAll(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        List<Warehouse> content = warehouseRepository.findAll(pageable).getContent();
        if (content.isEmpty()) throw new DataNotFoundException(ExceptionMessages.DATA_NOT_FOUND);
        return content
                .stream()
                .map(EntityToResponse::warehouseToResponse)
                .toList();
    }

    @Override
    public WarehouseResponse create(WarehouseRequest warehouseRequest) {
        warehouseRepository.findByName(warehouseRequest.getName())
                .ifPresent(ex -> {
                    throw new DataNotFoundException(ExceptionMessages.INVALID_WAREHOUSE);
                });
        Warehouse warehouse = RequestToEntity.requestToWarehouse(warehouseRequest);
        Warehouse save = warehouseRepository.save(warehouse);
        return EntityToResponse.warehouseToResponse(save);
    }
}
