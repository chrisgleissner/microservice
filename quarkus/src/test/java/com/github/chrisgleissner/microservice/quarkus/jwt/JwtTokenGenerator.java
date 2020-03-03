package com.github.chrisgleissner.microservice.quarkus.jwt;

import io.smallrye.jwt.build.Jwt;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Scanner;

import static java.lang.System.currentTimeMillis;
import static org.eclipse.microprofile.jwt.Claims.auth_time;

public class JwtTokenGenerator {
    private static final String CLAIMS_FILE_PATH = "/jwtClaims.json";
    private static final String PRIVATE_KEY_PEM_PATH = "/privateKey.pem";

    public static String createJwtToken(String claimsFilePath, long lifetimeInSeconds) {
        long currentTimeInSecs = (int) (currentTimeMillis() / 1000);
        return Jwt.claims(claimsFilePath)
                .issuedAt(currentTimeInSecs)
                .claim(auth_time.name(), currentTimeInSecs)
                .expiresAt(currentTimeInSecs + lifetimeInSeconds)
                .jws()
                .signatureKeyId(PRIVATE_KEY_PEM_PATH)
                .sign(readPrivateKey(PRIVATE_KEY_PEM_PATH));
    }

    public static void main(String[] args) {
        System.out.println(createJwtToken(CLAIMS_FILE_PATH, 300));
    }

    private static PrivateKey readPrivateKey(final String privateKeyPemFilePath) {
        Scanner scanner = new Scanner(JwtTokenGenerator.class.getResourceAsStream(privateKeyPemFilePath)).useDelimiter("\\A");
        String privateKeyPem = scanner.hasNext() ? scanner.next() : "";
        byte[] pemBytes = Base64.getDecoder().decode(privateKeyPem
                .replaceAll("-----BEGIN (.*)-----", "")
                .replaceAll("-----END (.*)----", "")
                .replaceAll("\r\n", "")
                .replaceAll("\n", "")
                .trim());
        try {
            return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(pemBytes));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}