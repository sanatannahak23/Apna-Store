package com.apnaStore.product_catalog_service.clients;

import com.apnaStore.product_catalog_service.config.FeignMultipartSupportConfig;
import com.apnaStore.product_catalog_service.dto.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "COMMON-SERVICE",
        path = "/api/s3-service",
        configuration = FeignMultipartSupportConfig.class)
public interface CommonClient {

    @PostMapping(value = "/upload/{productKey}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse uploadFile(
            @PathVariable("productKey") String productKey,
            @RequestPart("file") MultipartFile file
    );

    @GetMapping("/download")
    byte[] downloadFile(@RequestParam String key);

    @DeleteMapping("/delete")
    ApiResponse deleteFile(@RequestParam String key);

}
