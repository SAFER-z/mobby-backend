package com.safer.safer.routing.dto.address;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressResultDto {

    @JsonProperty("results")
    private Result result;

    public List<String> getAddressList() {
        return result.juso.stream()
                .map(Juso::getRoadAddr)
                .toList();
    }

    public String getErrorCode() {
        return result.common.errorCode;
    }

    public String getErrorMessage() {
        return result.common.errorMessage;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Result {
        private Common common;
        private List<Juso> juso;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Common {
        private String errorMessage;
        private String errorCode;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class Juso {
        private String roadAddr;
    }
}

