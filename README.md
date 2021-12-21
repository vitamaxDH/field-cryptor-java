# Field-Cryptor-Java

- Field-Cryptor is to help developers encrypt / decrypt designated field values conveniently
- Supported cryptographic algorithms are listed below
  - AES/CBC/NoPadding (128)
  -  AES/CBC/PKCS5Padding (128)
  -  AES/ECB/NoPadding (128)
  -  AES/ECB/PKCS5Padding (128)
  -  `[TBD]` ~~DES/CBC/NoPadding (56)~~ 
  -  `[TBD]` ~~DES/CBC/PKCS5Padding (56)~~ 
  -  `[TBD]` ~~DES/ECB/NoPadding (56)~~ 
  -  `[TBD]` ~~DES/ECB/PKCS5Padding (56)~~ 
  -  `[TBD]` ~~DESede/CBC/NoPadding (168)~~ 
  -  `[TBD]` ~~DESede/CBC/PKCS5Padding (168)~~ 
  -  `[TBD]` ~~DESede/ECB/NoPadding (168)~~ 
  -  `[TBD]` ~~DESede/ECB/PKCS5Padding (168)~~ 
  -  `[TBD]` ~~RSA/ECB/PKCS1Padding (1024, 2048)~~ 
  -  `[TBD]` ~~RSA/ECB/OAEPWithSHA-1AndMGF1Padding (1024, 2048)~~ 
  -  `[TBD]` ~~RSA/ECB/OAEPWithSHA-256AndMGF1Padding (1024, 2048)~~ 

by [Oracle Cipher API](https://docs.oracle.com/javase/8/docs/api/javax/crypto/Cipher.html)


v0.0.1

<br>

## Examples

1. Put `@FieldCrypto` annotation on fields you want to encrypt/decrypt.
```java
import com.max.fieldcryptor.annot.FieldCrypto;

public class Person {

    @FieldCrypto
    String name;
    String address;
    int age;
}
```

2. Add crypto properties to create a cipher
```java
String cipherName = "Field-Cryptor";
CryptographicAlgorithm algorithm = CryptographicAlgorithm.AES_CBC_PKCS5_PADDING;
String key = "sampleAESKeyForDemoItIsEasyToUse";
String iv = "sampleCBCinitVec";

CipherFactory.addCipher(cipherKeyName, algorithm, key, iv, true);
```



3. Get the cipher from FieldCryptorFactory and encrypt / decrypt
```java
AbstractCipher cipher = CipherFactory.getCipher(cipherKeyName);
Person person = new Person("VitaMax", "Suwon", 50);

Person encryptedObj = FieldCryptor.encryptFields(cipher, person, Person::new);
Person decryptedObj = FieldCryptor.decryptFields(cipher, encryptedObj, Person::new);
```

4. Check the result
```java
System.out.println("encryptedObj = " + encryptedObj);
System.out.println("decryptedObj = " + decryptedObj);
```

>encryptedObj = Person{name='ocWec2FvMBH7ytkZaQTbgw==', address='Suwon', age=50}
> 
>decryptedObj = Person{name='VitaMax', address='Suwon', age=50}
