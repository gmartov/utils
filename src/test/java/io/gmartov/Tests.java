package io.gmartov;

import com.jayway.jsonpath.Configuration;

public class Tests {

//    private static final Configuration configuration = Configuration.builder()
//            .jsonProvider(new JacksonJsonNodeJsonProvider())
//            .mappingProvider(new JacksonMappingProvider())
//            .build();

    private static final Configuration configuration = Configuration.defaultConfiguration();

    private final String complexJson = "{\n" +
            "    \"key01\": [\n" +
            "        {\n" +
            "            \"key11\": 11\n" +
            "        },\n" +
            "        {\n" +
            "            \"key12\": 12\n" +
            "        }\n" +
            "    ],\n" +
            "    \"key00\": {\n" +
            "        \"key12\": \"\",\n" +
            "        \"key11\": null\n" +
            "    }\n" +
            "}";



}
