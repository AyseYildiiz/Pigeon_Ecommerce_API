package com.pigeondev.Pigeon.Ecommerce.service.interf;

import com.pigeondev.Pigeon.Ecommerce.dto.AddressDto;
import com.pigeondev.Pigeon.Ecommerce.dto.Response;

public interface AddressService {

    Response saveAndUpdateAddress(AddressDto addressDto);
}
