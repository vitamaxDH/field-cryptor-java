# Field-Cryptor-Java
- Field-Cryptor is to help developers encrypt / decrypt designated field values conveniently

v0.0.1

<br>

## Examples

1. Put `@FieldCrypto` annotation on fields you want to encrypt/decrypt.
```java
import com.max.fieldcryptor.annot.FieldCrypto;

public class Person {

    @FieldCrypto
    String name;
    
    int age;
}
```

2. Add crypto properties to create a cipher
```java
String cipherName = "Field-Cryptor";
String key = "sampleAESKeyForDemoItIsEasyToUse";
String iv = "sampleCBCinitVal";

FieldCryptorFactory.addAESSpec(name, key, iv, 128, true);
```



3. Get the cipher from FieldCryptorFactory and encrypt / decrypt
```java
FieldCipher cipher = FieldCryptorFactory.getCipher(name);
Person person = new Person("VitaMax", 50);
Person encryptedObj = FieldCryptor.encrypt(person, cipher);
```

4. Check the result
```java
System.out.println("encryptedObj = " + encryptedObj);
```

>Person{name='hZATRdJUr7WqqnYdR9uKMA==', age=50}
