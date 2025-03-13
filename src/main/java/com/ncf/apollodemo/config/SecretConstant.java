package com.ncf.apollodemo.config;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.checkerframework.checker.units.qual.A;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SecretConstant {

    /**
     * 非对称加密 私钥 公钥私钥可以自己用代码生成，也可以在线生成，例如：https://tool.ip138.com/rsa/
     */
    public static final String PRIVATE_KEY = "MIIBOAIBAAJAflJQPKoKO9gz9Op2XINkHXVIyonzt/HClZRVf+2MyF4OLGckiBLM\n" +
            "rLq6jN/U4JlgIxCIni3zOsJdhIIF1D6fQwIDAQABAkAsKpeHPmSpm+Q+o6OSoRXl\n" +
            "/tXeivE9xTelmOF0AxiQDWRu1XWKAmjR2kKBgN/B9NlhBjW5+p4PW30UI7uCyKUR\n" +
            "AiEAw8ba06ZG7CZyXZVD8MhFx0ztg0kIE+ZLOqGpjSpKC70CIQClLfTWxpTbF4gb\n" +
            "lxDOJL5G1XiXcM516MUz6q3udTiG/wIgZ0v929yI4ULr5urB/UJ+Zsj1LOcUxwMk\n" +
            "wFvaDSy6AvUCIAehu/JAcpg82hkMPcaIhBIZwtycZa2k95eSfD7MQ7RZAiA+Y8yI\n" +
            "MkG4asXqoh2jryn40ih2q/GnXoCwdPXUa9E4MA==";

    /**
     * 非对称加密 公钥
     */
    public static final String PUBLIC_KEY = "MFswDQYJKoZIhvcNAQEBBQADSgAwRwJAflJQPKoKO9gz9Op2XINkHXVIyonzt/HClZRVf+2MyF4OLGckiBLMrLq6jN/U4JlgIxCIni3zOsJdhIIF1D6fQwIDAQAB";
    public RSA getRSA(){
        RSA rsa = new RSA(SecretConstant.PRIVATE_KEY, SecretConstant.PUBLIC_KEY);
        return rsa;
    }
}


//    /**
//     * 测试一下
//     * @param args
//     */
//    public static void main(String[] args) {
//        String un = "admin";
//        String pw = "admin@123456";
//        // 用户名密码解密
//        RSA rsa = new RSA(SecretConstant.LoginSecret.PRIVATE_KEY, SecretConstant.LoginSecret.PUBLIC_KEY);
//        String un_jm = rsa.encryptBase64(un,KeyType.PublicKey);
//        System.out.println("公钥加密后："+un_jm);
//        // 前端传过来的用户名密码是通过公钥加密的
//        String userName_plaintext = rsa.decryptStr(un_jm, KeyType.PrivateKey);
//        System.out.println("私钥解密后："+userName_plaintext);
//        // 加密后的密文
//        String temp = "djCaCjhwVRc29vNHEIUqoGkn0azDjGdjHHV+zetw8GcKmJ8u2/VgAX54G/zzpcrBrpkR+SmS7QPkpSz5s05OLA==";
//        System.out.println("私钥解密后："+rsa.decryptStr(temp, KeyType.PrivateKey));
//
//    }
//}