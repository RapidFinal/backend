package io.muic.rapid.hackathon4.api.user.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

@Getter
public class ResponseLogin {

    private String message;
    private boolean login;
    private Integer status;

    public ResponseLogin(){
        this("You're not logged in", 401,false);
    }

    public ResponseLogin(String msg, Integer status ,boolean login){
        this.message = msg;
        this.login = login;
        this.status = status;
    }

    @Override
    public String toString(){
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}