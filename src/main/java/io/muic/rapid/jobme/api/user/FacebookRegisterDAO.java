package io.muic.rapid.jobme.api.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor(onConstructor=@__({@JsonCreator}))
@ToString
public class FacebookRegisterDAO {
    private final String facebookId;
    private final String firstname;
    private final String lastname;
    private final String photoURL;
}
