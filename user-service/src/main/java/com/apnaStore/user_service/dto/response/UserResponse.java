package com.apnaStore.user_service.dto.response;

import com.apnaStore.user_service.entities.Auditable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserResponse extends Auditable {

    private Long id;

    private String userRef;

    private String name;

    private String email;

    private Long phoneNum;

    private String gender;

    private String role;

    private String status;

    private List<AddressResponse> addressResponse;

}
