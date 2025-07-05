package com.johnbryce.coupon_system_final.security;

import com.johnbryce.coupon_system_final.exceptions.CouponSystemException;
import com.johnbryce.coupon_system_final.exceptions.generic.GenericException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class TokenManager {

    private final Map<UUID, TokenInformation> tokens;

    public UUID addToken(TokenInformation tokenInformation) throws CouponSystemException {
        if(tokenInformation==null){
            throw new CouponSystemException
                    (GenericException.TOKEN_CREATION_GOT_WRONG);
        }
        UUID newToken = UUID.randomUUID();
        /*If the user already got a new token with his email - it's will get removed from 'tokens'
        which means if we log in from pc, and we already connected from mobile - we'll get logged out from
        one of it
         */
        removeToken(tokenInformation);
        while(tokens.containsKey(newToken)) {
            newToken = UUID.randomUUID();
        }
        tokens.put(newToken,tokenInformation);
        return newToken;
    }

    //Made for one connection - if we connected via pc we're logged out
    public void removeToken(TokenInformation tokenInformation) {
        tokens.entrySet().removeIf(entry ->
                entry.getValue().getEmail().equalsIgnoreCase(tokenInformation.getEmail()));
    }

    //For authorize actions - Using on controller instead of id of a user
    public Integer getUserIdFromToken(UUID token, ClientType clientType) throws CouponSystemException {
        if(token==null || !tokens.containsKey(token)){
            throw new CouponSystemException
                    (GenericException.INVALID_TOKEN);
        }
        TokenInformation tokenInformation = tokens.get(token);

            if(tokenInformation.getClientType()!= clientType){
                throw new CouponSystemException
                        (GenericException.UNAUTHORIZED_ACTION);
        }

        /*Here after we checked user is valid - We're extending his token for more 24 hours.
        Which means while user using the system we're giving it more time -
        otherwise - we throw user out*/
        tokenInformation.setExpireTime(LocalDateTime.now().plusMinutes(30));
        return tokenInformation.getId();
    }

    public UUID tokenFromEmail(String email) throws CouponSystemException {
        for (Map.Entry<UUID, TokenInformation> entry : tokens.entrySet()) {
            if (entry.getValue().getEmail().equalsIgnoreCase(email)) {
                return entry.getKey();
            }
        }
        //If we didn't find the token - we throw exception
            throw new CouponSystemException
                    (GenericException.INVALID_TOKEN);
    }

    @Scheduled(cron = "0 0/30 * * * *")
    public void removeAllExpiredTokens() throws CouponSystemException {
        tokens.entrySet().removeIf(entry ->
                entry.getValue().getExpireTime().isBefore(LocalDateTime.now()));
    }



}