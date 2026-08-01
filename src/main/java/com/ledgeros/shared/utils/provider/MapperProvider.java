package com.ledgeros.shared.utils.provider;

import com.fasterxml.jackson.databind.ObjectMapper;

public class MapperProvider {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();

}
