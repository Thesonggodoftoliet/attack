package com.littlepants.attack.attackweb.config;

import com.littlepants.attack.attackweb.util.RsaUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

@Data
@ConfigurationProperties("rsa.key")
public class RsaKeyProperties {
    private String publicKeyPath;
    private String privateKeyPath;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    @PostConstruct
    public void createKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        this.publicKey = RsaUtils.getPublicKey(publicKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(privateKeyPath);
    }
}
