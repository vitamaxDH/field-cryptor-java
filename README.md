# Field-Cryptor-Java
___
- Field-Cryptor is to help developers encrypt / decrypt designated field values conveniently

v0.0.1

<br>

## Examples

---

1. Add crypto properties to create a cipher
```java
String cipherName = "Field-Cryptor";
String key = "sampleAESKeyForDemoItIsEasyToUse";
String iv = "sampleCBCinitVal";

FieldCryptorFactory.addAESSpec(name, key, iv, 128, true);
```


2. Put `@FieldCrypto` annotation on fields you want to encrypt/decrypt.
```java
import com.max.fieldcryptor.annot.FieldCrypto;

public class Person {

    @FieldCrypto
    String name;
    
    int age;
}
```

3. Get cipher from FieldCryptorFactory and encrypt / decrypt
```java
FieldCipher cipher = FieldCryptorFactory.getCipher(name);
Person person = new Person("VitaMax", 50);
Person encryptedObj = FieldCryptor.encrypt(person, cipher);
```

4. Check how the result
```java
System.out.println("encryptedObj = " + encryptedObj);
```

```
Person{name='hZATRdJUr7WqqnYdR9uKMA==', age=50}
```
