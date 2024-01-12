package net.greeta.stock.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Oauth2TokenDto {

    @JsonProperty("access_token")
    private String accessToken;
}
